---
layout: page
title: "Hands-on : docker"
---
___
- [Virtualisation/Conteneurisation](#virtualisationconteneurisation)
  - [Hands-on : découverte des namespaces linux](#hands-on--découverte-des-namespaces-linux)
- [Lancer des containers](#lancer-des-containers)
  - [Mon premier container 🐳](#mon-premier-container-)
  - [Les commandes de bases pour les containers 📦](#les-commandes-de-bases-pour-les-containers-)
  - [Plus de containers](#plus-de-containers)
  - [Containers et volumes](#containers-et-volumes)
- [Créer des images](#créer-des-images)
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


## Lancer des containers

### Mon premier container 🐳
Exécutez la commande `sudo docker container run hello-world`. Regardez le terminal et essayer de comprendre ce qu'il se passe.

### Les commandes de bases pour les containers 📦
{% highlight bash %}
// Lancer un container
sudo docker container run [-it] [-p port_local:port_container] [--name container name] [image_name]

// Voir les containers
sudo docker container ps

// Stopper un container
sudo docker stop [id/name]

// Lancer une commande dans un container
sudo docker exec [id/name] [commande]

// Rentrer dans un container
sudo docker exec -it [id/name] bash

// Container avec un volume
sudo docker container run [-v host/path:container/folder[:ro]] [image_name]
{% endhighlight %}

### Plus de containers

1. Allez sur le site [docker hub](https://hub.docker.com/search?type=image) chercher une image ubuntu et lancez là. Lancez un shell python3 dans ce container.
2. Allez chercher une image postgres, lancez-la et connectez vous à la base depuis l'outil de votre choix (shell, python, java, pgadmin etc)

### Containers et volumes 

1. Récupérez le code d'un site statique (soit à vous, soit sur Moodle)
2. Lancez un container nginx avec un volume qui pointe vers votre site
  1. Le dossier dans le container est : /usr/share/nginx/html
  2. Donnez uniquement les droits en lecture
  3. Pensez à mapper le port 80 du container
  4. Allez voir la page 127.0.0.1:80

## Créer des images

### Docker et python 🐳🐍
1. Récupérez le code python disponible sur Moodle
2. Faites-le fonctionner en local
3. Mettez-le dans un container docker
    1. Partez de l'image python:3-alpine
    2. Exécutez l'application au lancement du package avec CMD ou ENTRYPOINT
4. Testez votre image

Faites la même chose avec un code python à vous.

### Docker et java 🐳☕
1. Téléchargez l'application example de spring boot : https://github.com/spring-projects/spring-petclinic
2. Faites-la fonctionner en local
3. Mettez-la dans un container docker
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
4. Éteignez vos containers avec un `docker-compose down`


### Python + Redis
Récupérez le code hitCount sur Moodle. Créez une fichier `docker-compose.yml` dans le dossier récupéré qui permet de faire fonctionner l'application. Pour cela vous devez :
1. Définir un service que vous appellerez `web`. Il devra exposer le port 5000, et sera construit (mon clef `build` dans le `docker-compose.yml`) à partir du `dockerfile` présent dans le dossier
2. Définir un service que vous appellerez reddis qui utilise l'image `redis:alpine`
3. Lancer vos conteneurs avec un `docker-compose up`,  allez visiter la page `localhost:5000` et rafraîchissez la page.
4. Éteindre vos containers avec un `docker-compose down`


### Python + PostgresSQL
Récupérez le code WSPython sur Moodle.
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
