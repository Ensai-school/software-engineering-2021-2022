---
layout: page
title: "Hands-on : docker"
---
___
- [Copie des fichiers nécessaires au fonctionnement de maven](#copie-des-fichiers-nécessaires-au-fonctionnement-de-maven)
- [Installation des dépendances dans l'image du conteneur](#installation-des-dépendances-dans-limage-du-conteneur)
- [Copie du code de l'applicaton](#copie-du-code-de-lapplicaton)
- [Compilation du code en jar](#compilation-du-code-en-jar)
      - [Multi stage build](#multi-stage-build)
- [Copie des fichiers nécessaires au fonctionnement de maven](#copie-des-fichiers-nécessaires-au-fonctionnement-de-maven-1)
- [Installation des dépendances dans l'image du conteneur](#installation-des-dépendances-dans-limage-du-conteneur-1)
- [Copie du code de l'applicaton](#copie-du-code-de-lapplicaton-1)
- [Compilation du code en jar](#compilation-du-code-en-jar-1)
  - [Docker-compose](#docker-compose)
    - [Mongo + Mongo Express](#mongo--mongo-express)
    - [Python + Redis](#python--redis)
    - [Python + PostgresSQL](#python--postgressql)


___

## Virtualisation/Conteneurisation

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


### Plus de conteneurs

1. Par défaut un conteneur ubuntu exécute un shell bash à son lancement. Mais si on ne rentre pas dans le conteneur avec l'instruction `-it` dans le `docker run` le conteneur ferme le bash car il ne reçoit aucune commande. Et comme le processus d'entrée dans le conteneur se ferme le conteneur s'éteint.
2.  {% highlight bash %}
docker run -it --rm --name ubuntu_container ubuntu
apt update
apt install python3 -y
python3
{% endhighlight %}
3. {% highlight bash %}
docker run --rm --name postgresdb -e POSTGRES_PASSWORD=azerty -d postgres
docker exec -it postgresdb bash
su postgres
psql
{% endhighlight %}
### Conteneurs et volumes 

#### Pour exposer un site statique
Il faut utiliser la commande `-v` de `docker run` et lier le répertoire du site au répertoire `/usr/share/nginx/html` du conteneur (comme indiqué dans la doc officielle)
{% highlight bash %}
docker run -d --rm \
--name nginx \
-v /path/to/directory:/usr/share/nginx/html:ro \
-p 80:80 \
nginx
{% endhighlight %}

#### Pour pérenniser des données
{% highlight bash %}
docker volume create mongo_volume
docker run -d --rm -p 27017:27017 --name mongo -v mongo_volume:/data/db  mongo
docker exec -it mongo mongosh
docker kill mongo
docker run -d --rm -p 27017:27017 --name mongo -v mongo_volume:/data/db  mongo
{% endhighlight %}


## Créer des images

### Une base postgres déjà initialisée 🐳🐘
{% highlight dockerfile %}
FROM postgres
COPY init_db.sql /docker-entrypoint-initdb.d/
{% endhighlight %}


### Docker et python 🐳🐍
Le code affiche simplement tic et tac à intervals réguliers. La durée de l'interval est basée sur une variable d'environnement `TIMER` que l'on va définir dans l'image du conteneur. On pourra également la modifier au lancement des conteneurs.
{% highlight dockerfile %}
FROM python:3-alpine
ENV TIMER=1
COPY tic-tac-python.py main.py
ENTRYPOINT ["python", "main.py"]
{% endhighlight %}

{% highlight bash %}
cd folder/to/dockerfile
docker image build -t tic-tac .
docker container run tic-tac
{% endhighlight %}


### Docker et java 🐳☕
Il existe plusieurs façon de résoudre se problème. Voyons de la plus simple à la plus poussée :

#### L'application est déjà packagée
Si l'application est déjà packagée il suffit de copier le jar dans l'image du conteneur. Voici à quoi ressemblerait le docker file
 {% highlight dockerfile %}
FROM openjdk:16-alpine3.13
COPY target/spring-petclinic-2.6.0-SNAPSHOT.jar  spring-petclinic-2.6.0-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT  ["java", "-jar", "spring-petclinic-2.6.0-SNAPSHOT.jar"]
{% endhighlight %}
Le problème de cette solution est quelle demande un packagage manuel de l'application avant la construction de l'image. En cas d'oublie il y aura une désynchronisation entre l'image et le code de l'application.

#### On package l'image lors de la création de l'image
La seconde solution consiste à packager directement l'application lors de la création de l'image. Voici le dockerfile correspondant.
{% highlight dockerfile %}
FROM openjdk:16-alpine3.13
WORKDIR /app
# Copie des fichiers nécessaires au fonctionnement de maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Installation des dépendances dans l'image du conteneur
RUN ./mvnw dependency:go-offline 
# Copie du code de l'applicaton
COPY src ./src
# Compilation du code en jar
RUN ./mvnw package
EXPOSE 8080
ENTRYPOINT  ["java", "-jar", "app/target/spring-petclinic-2.6.0-SNAPSHOT.jar"]
{% endhighlight %}
Cette solution à l'avantage d'assurer que l'image dispose de la dernière version disponible du code. Mais l'image créé est un peu grosse car elle contient tous les fichiers source de l'application.

#### Multi stage build
La dernière solution est de faire un build multi stage. On commence par packager l'application et ensuite on met l'artifact dans le conteneur final.
{% highlight dockerfile %}
FROM openjdk:16-alpine3.13 as build
WORKDIR /app
# Copie des fichiers nécessaires au fonctionnement de maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Installation des dépendances dans l'image du conteneur
RUN ./mvnw dependency:go-offline 
# Copie du code de l'applicaton
COPY src ./src
# Compilation du code en jar
RUN ./mvnw package

FROM openjdk:16-alpine3.13
WORKDIR /app
COPY --from=build app/target/spring-petclinic-2.6.0-SNAPSHOT.jar  spring-petclinic-2.6.0-SNAPSHOT.jar
ENTRYPOINT  ["java", "-jar", "app/target/spring-petclinic-2.6.0-SNAPSHOT.jar"]
{% endhighlight %}
Cette solution permet d'avoir une image finale débarrassée de tout les fichiers inutiles à son fonctionnement.

## Docker-compose

### Mongo + Mongo Express
{% highlight yaml %}
version: "3.9"
services:
  mongodb:
    image: mongo
    container_name: mongo
    ports:
      - 27017:27017
    environment:
      - MONGO_INITDB_ROOT_USERNAME=hello
      - MONGO_INITDB_ROOT_PASSWORD=world
  mongo-express:
    image: mongo-express
    ports:
      - 8080:8081
    environment:
      ME_CONFIG_MONGODB_ADMINUSERNAME: hello 
      ME_CONFIG_MONGODB_ADMINPASSWORD: world 
      ME_CONFIG_MONGODB_SERVER: mongodb
    depends_on:
      - mongodb
{% endhighlight %}



### Python + Redis

{% highlight yaml %}
version: '3.9'
services:
  flask:
    image: flaskapp
    build: ./
    ports:
      - 5000:5000
    depends_on: 
      - redis
  redis:
    image: redis
    volumes:
      - redisdb:/data

volumes:
  redisdb:

{% endhighlight %}


### Python + PostgresSQL
{% highlight yaml %}
version: '3.9'
services:
  db:
    image: pokemon_postgres
    build: ./sql
    container_name: pokemon_db
    ports:
      - 5432:5432
    environment:
      - POSTGRES_USER=app
      - POSTGRES_PASSWORD=pokemon
      - POSTGRES_DB=app
    restart: on-failure
  ws:
    image: ws_pokemon
    build: ./ws
    container_name: ws_pokemon
    ports: 
      - 80:80
    environment:
      - PASSWORD=pokemon
      - HOST=db
      - PORT=5432
      - DATABASE=app
      - USER=app
    restart: on-failure
    depends_on :
      - db
{% endhighlight %}
