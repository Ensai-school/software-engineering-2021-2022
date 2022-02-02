---
layout: page
title: "Hands-on : Spring Boot 🍃"
---
____
- [1. Initialiser un projet Spring Boot](#1-initialiser-un-projet-spring-boot)
- [2. Un premier hello world </h3>](#2-un-premier-hello-world-h3)
- [3. Mes premiers beans](#3-mes-premiers-beans)
- [4. Mon premier endpoint</h3>](#4-mon-premier-endpointh3)
- [5. un webservice CRUD](#5-un-webservice-crud)
____

## 1. Initialiser un projet Spring Boot
1. Allez sur https://start.spring.io/ créez un projet spring boot.
2. Lancez le main voir que le code fonctionne.

## 2. Un premier hello world </h3>
1. Créez une classe `HelloWorldService` qui implémente l'interface `CommandLineRunner`, et surchargez la méthode à implémenter pour afficher un hello world
2. Faites de cette classe un beans Spring
3. Lancez votre application
4. Que se passe-t-il ?

## 3. Mes premiers beans
1. Créez une classe BookService qui contient une liste de livre. La classe `BookService` est un bean spring.
2. Créez une classe `LibraryService`. La classe `LibraryService` est un bean spring avec un attribut nom.
3. Affichez les informations de vos beans.
4. Est-ce que vos beans sont des singletons ?

## 4. Mon premier endpoint</h3>
1. Allez sur https://start.spring.io/ et créez un projet spring boot.
2. Prenez les starter web, h2, postgres et JPA
3. Créez les packages services, model, controller
4. Créez une classe `Book` avec les attributs titre, auteur et éditeur
5. Créez une classe `BookService` avec une méthode `getBooks` qui retourne une liste de livre. Vous pouvez la hardcoder pour le moment
6. Créez un classe `LivreController` qui expose un endpoints `getBooks` qui retourne une liste de livre

## 5. un webservice CRUD
1. Transformer la classe Book en Entity
2. Créez une classe `BookReposiory` qui implémente `CrudRepository`
3. Book service implémente un CRUD
4. `BookController` implémente les bons endpoints
5. Utilisez une base h2 pour stockez vos données
6. Quand vous avez terminé transformer la base h2 en base postgres contenu dans un container
7. Dockerisez votre application