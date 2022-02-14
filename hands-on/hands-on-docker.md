---
layout: page
title: "Hands-on : docker"
---
___
- [Virtualisation/Conteneurisation](#virtualisationconteneurisation)
  - [Hands-on : découverte des namespaces linux](#hands-on--découverte-des-namespaces-linux)
- [Lancer des conteneurs](#lancer-des-conteneurs)
  - [Mon premier conteneur 🐳](#mon-premier-conteneur-)
  - [Les commandes de bases pour les conteneurs 📦](#les-commandes-de-bases-pour-les-conteneurs-)
  - [Plus de conteneurs](#plus-de-conteneurs)
  - [Conteneurs et volumes](#conteneurs-et-volumes)
    - [Pour exposer un site statique](#pour-exposer-un-site-statique)
    - [Pour pérenniser des données](#pour-pérenniser-des-données)
- [Créer des images](#créer-des-images)
  - [Une base postgres déjà initialisée 🐳🐘](#une-base-postgres-déjà-initialisée-)
  - [Docker et python 🐳🐍](#docker-et-python-)
  - [Docker et java 🐳☕](#docker-et-java-)
- [Docker-compose](#docker-compose)
  - [Mongo + Mongo Express](#mongo--mongo-express)
  - [Python + Redis](#python--redis)
  - [Python + PostgresSQL](#python--postgressql)


___

## Virtualisation/Conteneurisation

S'il semble naturel pour un utilisateur classique de faire tourner plusieurs applications sur un même ordinateur personnel, c'est beaucoup moins clair dans le cadre d'un SI (système d'information) industriel. En effet faire cohabiter plusieurs application sur une même machine pose des problèmes :
- Nécessité un même OS
- Problème de sécurité
- Limiter la consommation des services

Ainsi rapidement à émerge le concept de virtualisation, qui permet sur une même machine physique d'héberger plusieurs machines virtuelles.
Ces machines virtuelle cohabitent entre elles en pensant toutes quelle sont la seule machine installer sur le hardware disponible.
L'élément logiciel qui permet cela est appelé *hypervisor*, c'est lui qui permet cette isolation et aux OS virtualiser de communiquer avec les composants physiques.
Voici le diagramme d'une hyperviseur de type 2


![Architecture hyperviseur de type 2. Le hardware communique avec l'OS hôte de la machine sur lequel est installer un hyperviseur. Ensuite différents OS peuvent être lancer via l'hyperviseur. Ces OS pensent être de OS classiques, et se savent pas qu'ils sont tous installer sur la même machine](../lectures/img/virtualisation.png){: width="50%" .centered}


Si la virtualisation répond à des besoins d'isolation, de sécurité et permet à plusieurs OS de cohabiter sur un même équipement, elle pose un problème de consommation de ressource.
En effet un OS est gourmand, et pour faire des traitements simple, il n'est pas nécessaire d'avoir tous les outils qu'il met à disposition.
C'est dans cette idée qu'est né Docker. Docker permet de créer des conteneurs qui sont en quelques sortes des machines virtuelles allégées. Elles n'embarquent que le strict nécessaire, Docker s'occupant du reste.

![](../lectures/img/docker.png){: width="50%" .centered}

Mais Docker se base sur des technologies anciennes, mais a su les agréger et en faciliter l'utilisation.
L'une d'entre elle est celle des *namespace* ou espace de nommage, qui permet de créer des blocs de ressources qui seront gérés par Linux.
Ainsi on n'a plus une liste de processus unique, mais un système de poupées russes, chaque processus pouvant en cacher plusieurs.
Mais surtout à l'intérieur d'un processus on pourra réutiliser des identifiant déjà utilisé, cela évitant une unicité des identifiant.


### Hands-on : découverte des namespaces linux

>Cette manipulation nécessite une distribution linux, si vous n'en avait pas mais que vous avez docker :
>1. Ouvrez deux terminaux 
>2. Dans un des deux faites `docker run -it --privileged --name ub ubuntu`
>3. Dans le second `docker image exec -it ub bash`
>Voilà vous avez deux terminaux pointant sur le même ubuntu

1. Regardez les processus actifs dans les deux terminaux avec `ps -au`
2. Exécutez la commande `sudo unshare -f -p --mount-proc usr/bin/sh` dans un des deux terminaux
3. Regardez les processus actifs dans les deux terminaux avec `ps -au`

Que s'est-il passé ?


## Lancer des conteneurs

### Mon premier conteneur 🐳
Exécutez la commande `sudo docker container run hello-world`. Regardez le terminal et essayez de comprendre ce qu'il se passe.

### Les commandes de bases pour les conteneurs 📦
{% highlight bash %}
// Lancer un conteneur
sudo docker container run [-it] [-p port_local:port_container] [--name container name] [image_name]

// Voir les conteneurs
sudo docker container ps

// Stopper un conteneur
sudo docker stop [id/name]

// Lancer une commande dans un container
sudo docker exec [id/name] [commande]

// Rentrer dans un container
sudo docker exec -it [id/name] bash

// conteneur avec un volume
sudo docker container run [-v host/path:container/folder[:ro]] [image_name]
{% endhighlight %}

### Plus de conteneurs

1. Allez sur le site [docker hub](https://hub.docker.com/search?type=image), cherchez une image ubuntu et lancez là avec la commande `docker run --name ubuntu_container ubuntu`. Regardez la liste de vos conteneurs actif avec `docker container ps`. Votre conteneur apparaît-il ? Regardez la liste de tous les conteneurs gérés par docker avec `docker container ps -a`. Supprimez votre conteneur avec `docker container rm ubuntu_container`
2. Relancez votre conteneur avec la commande `docker run -it --rm --name ubuntu_container ubuntu bash`. Que ce passe-t-il ? Installer un shell python3 dans ce conteneur avec un `apt upgrade` et `apt install`.
3. Cherchez une image postgres, lancez-la et connectez vous à la base depuis l'outil de votre choix (shell, python, java, pgadmin etc)

### Conteneurs et volumes 

#### Pour exposer un site statique
1. Récupérez le code d'un site statique (soit à vous, soit sur [ici](../assets/code/dummy%20site.7z))
2. Lancez un conteneur nginx avec un volume qui pointe vers votre site
  1. Le dossier dans le conteneur est : `/usr/share/nginx/html`
  2. Donnez uniquement les droits en lecture
  3. Pensez à mapper le port 80 du conteneur
  4. Allez voir la page 127.0.0.1:80

#### Pour pérenniser des données
1. Créez un volume nommé géré par docker avec la commande docker `volume create mongo_volume`
2. Créez un conteneur mongodb qui utilise le volume que vous venez de définir pour stocker les données. Le dossier de stokage des données du conteneur mongodb est le dossier `/data/db`. Comme vous utilisez un volume géré par docker et pas un dossier de votre machine vous mettre comme adresse du dossier local le nom du volume.
3. Connectez vous à votre conteneur avec la commande `docker exec -it [nom conteneur] mongosh`
4. Ajoutez des données
{% highlight js %}
db.Canard.insert({"nom":"Scrouge"})
db.Canard.insert({"nom":"Donald"})
db.Canard.insert({"nom":"Della"})
db.Canard.insert({"nom":"Huey"})
db.Canard.insert({"nom":"Louis"})
db.Canard.insert({"nom":"Dewey"})
db.Canard.find()
{% endhighlight %}
5. Étreignez votre conteneur avec un `docker kill`
6. Relancer en conteneur en utilisant le même volume
7. Vérifiez que vous avez toujours vos données dans le nouveau conteneur


## Créer des images

### Une base postgres déjà initialisée 🐳🐘
1. Récupérez le code sql disponible sur [ici](../assets/code/init_db.7z))
2. Créez une image d'une base de données Postgres qui contiendra les données fournies. Pour cela vous allez devoir créer un fichier `dockerfile` (c'est son nom) qui part d'ume image postgres, et qui va copier les données dans le répertoire `/docker-entrypoint-initdb.d/`
3. Créez votre image avec `docker image build -t custom_postgres .`
3. Testez votre image avec `docker run -d -e POSTGRES_PASSWORD=azerty custom_postgres` et vérifier que vous avez bien vos données.

### Docker et python 🐳🐍
1. Récupérez le code python disponible sur [ici](../assets/code/tic-tac-python.7z))
2. Faites-le fonctionner en local
3. Mettez-le dans un conteneur docker
    1. Partez de l'image python:3-alpine
    2. Exécutez l'application au lancement du package avec CMD ou ENTRYPOINT
4. Testez votre image

Faites la même chose avec un code python à vous.

### Docker et java 🐳☕
1. Téléchargez l'application example de spring boot : https://github.com/spring-projects/spring-petclinic
2. Faites-la fonctionner en local
3. Mettez-la dans un conteneur docker
    1. Partez de l'image openjdk:8-jdk-alpine
    2. Packagez l'application via maven
    3. Copiez le jar
    4. Exécutez l'application au lancement du package avec CMD ou ENTRYPOINT
4. Testez votre image

## Docker-compose

### Mongo + Mongo Express
Reprenez l'exemple du cours et lancez via `docker-compose` un serveur mongo et mongo-express. Pour cela :
1. Créez un fichier `docker-compose.yml` avec un service mongo et un service mongo-express
2. Lancez vos conteneurs avec la commande `docker-compose up`
3. Connectez vous sur `localhost:8081` voir si vous avez l'application mongo-express 
4. Éteignez vos conteneurs avec un `docker-compose down`


### Python + Redis
Récupérez le dossier hitCount sur Moodle ou [ici](../assets/code/hitcount.7z). Dedans vous trouverez les fichiers :
- `dockerfile` qui permet de créer une image docker qui permet de faire fonctionner le code python
- `main.py` contenant le code python de l'application
- `requirements.txt` avec les modules python à installer
Créez une fichier `docker-compose.yml` dans le dossier récupéré qui permet de faire fonctionner l'application. Pour cela vous devez :
1. Définir un service que vous appellerez `flask`. Il devra exposer le port 5000, et sera construit (mon clef `build` dans le `docker-compose.yml`) à partir du `dockerfile` présent dans le dossier courant (`./`)
2. Définir un service que vous appellerez re`dis qui utilise l'image `redis:alpine`
3. Ajouter une dépendance au service `redis` à votre service `flask`
4. Lancer vos conteneurs avec un `docker-compose up`,  allez visiter la page `localhost:5000` et rafraîchissez la page.
5. Éteindre vos conteneurs avec un `docker-compose down`
6. Modifier votre `docker-compose.yml` pour ajouter un volume à la base redis. Ajouter à votre `docker-compose.yml` au même niveau que la clef `services` une clef `volumes` avec un élément `reddis_volume` (ajoutez des : pour respecter la syntaxe yml). Le volume doit pointer vers le répertoire `/data` du conteneur. Il vous faut ajouter un élément volumes à votre objet `reddis` : 
{% highlight yml %}
    volumes:
      - reddis_volume:/data
{% endhighlight %}
6. Lancer vos conteneurs avec un `docker-compose up`,  allez visiter la page `localhost:5000` et rafraîchissez la page.
7. Tuer et relancer vos conteneurs puis retourner sur la page `localhost:5000`

### Python + PostgresSQL
Récupérez le code WSPython sur Moodle ou [ici](../assets/code/WSPython.zip) .
1. Créez un fichier `dockerfile` dans le dossier ws qui contiendra le code python. Attention il faut :
   1. Partir de l'image `python:alpine`
   2. Installer des dépendances via pip
   3. Copier le code
   4. Exposer le port 80
  Vous pouvez vous inspirez du `dockerfile` du précédent exercice. 
2. Créez un fichier `dockerfile` dans le dossier sql qui permettra de créer une image d'une base postgreSQL contenant les données du fichier init_db.sql
3. Créez un fichier `docker-compose.yml` à la racine de l'application. Il permettra de lancer
   1. La base de données. Attention vous êtes obligé de passer une variable d'env `POSTGRES_PASSWORD`
   2. Le webservice python. Comme votre webservice doit se connecter à la base il faut lui passer les variables d'environnement suivantes :
     - `PASSWORD` avec comme valeur la même valeur que `POSTGRES_PASSWORD`
     - `HOST` prendra le nom du service que vous avez donné à la base postgres dans votre fichier docker-compose.yml
     - `PORT` sera `5432`
     - `DATABASE` sera égale à postgres si vous avez laisser la valeur `POSTGRES_DB` de la base par défaut
     - `USER` sera égale à postgres si vous avez laisser la valeur `POSTGRES_USER` de la base par défaut
