---
layout: page
title: "Hands-on : Spring Boot 🍃"
---
____
- [1. Initialiser un projet Spring Boot ✨](#1-initialiser-un-projet-spring-boot-)
- [2. Un premier hello world 🤖](#2-un-premier-hello-world-)
- [3. Mes premiers beans 🌱](#3-mes-premiers-beans-)
- [4. Mon premier endpoint 🎯](#4-mon-premier-endpoint-)
- [5. Un webservice CRUD 💣](#5-un-webservice-crud-)

____

## 1. Initialiser un projet Spring Boot ✨
1. Allez sur https://start.spring.io/ créez un projet spring boot.
2. Lancez le main voir que le code fonctionne.

## 2. Un premier hello world 🤖
1. Créez une classe `HelloWorldService` qui implémente l'interface `CommandLineRunner`, et surchargez la méthode à implémenter. Cette méthode retournera un simple "hello world".
2. Faites de cette classe un beans Spring en y ajoutant l'annotation correspondante.
3. Lancez votre application avec un `mvn spring-boot:run`
4. Que se passe-t-il ?

## 3. Mes premiers beans 🌱
1. Créez une classe BookService qui contient une liste de livre. La classe `BookService` est un bean spring.
2. Créez une classe `LibraryService`. La classe `LibraryService` est un bean spring avec un attribut nom.
3. Affichez les informations de vos beans.
4. Est-ce que vos beans sont des singletons ?

## 4. Mon premier endpoint 🎯
1. Allez sur https://start.spring.io/ et créez un projet spring boot.
2. Prenez les starter web, h2, postgres et JPA.
3. Créez les packages services, model, controller.
4. Créez une classe `Book` avec les attributs titre, auteur et éditeur.
5. Créez une classe `BookService` avec une méthode `getBooks` qui retourne une liste de livre. Vous pouvez la hardcoder pour le moment.
6. Créez un classe `BookController` qui expose un endpoints `getBooks` qui retourne une liste de livre.

## 5. Un webservice CRUD 💣
1. Transformer la classe `Book` en entity.
2. Créez une interface `BookReposiory` qui hérite de `CrudRepository`.
3. `Book` service implémente un CRUD.
4. `BookController` implémente les bons endpoints.
5. Utilisez une base h2 pour stockez vos données. Cela va se faire en ajoutant dans les propritées suivante dans le ficheir `application.properties`
{% highlight properties %}
database=h2
spring.datasource.schema=classpath*:schema.sql
spring.datasource.data=classpath*:data.sql
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
{% endhighlight %}
{:start="6"}
6. Créez deux fichiers dans le dossier ressources:
   1. `schema.sql` qui va contenir les requêtes de création de votre base
   2. `data.sql` qui va contenir des données de votre base
7. Lancez votre application et vérifiez que vous récupérez bien les données de votre base
8. Quand vous avez terminé transformer la base h2 en base postgres contenue dans un container.
9. Dockerisez votre application.