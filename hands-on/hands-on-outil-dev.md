---
layout: page
title: "Hands-on : Outils pout le dev/data-engineer"
---

## Gérer un projet java en ligne de commande ☕📦

Le but de ces exercices est de vous montrer qu'il est possible d'écrire, et lancer un code java à la main, c'est dire sans IDE.
Cela s'applique à java, mais est vrai pour tous les autres langages.
Le principe n'est pas de vous dire qu'il ne faut pas utiliser un IDE, car il faut en utiliser un pour automatiser les tâches fastidieuses, mais que vous pouvez réaliser ses tâches par vous même si un jour vous n'avez pas d'IDE à disposition.

### 1. Compiler un code java à la main ☕

Pour rappel voici différentes instructions pour compiler/lancer du code java

 - Compilation du code : `javac [-cp external jars] [file1.java, file2.java...]`
 - Lancer du code compilé : `java [-cp external jars] [main file]`

1. Commencez par créer un fichier `App.java` avec le contenu suivant :
   1. Une `public class` App
   2. Une `public static void main(String[] args)` qui affiche un hello world
2. Compilez cette classe
3. Lancez là

### 2. Créer un fichier .jar à la main 📦

Les fichiers JAR (pour Java ARchive) sont des fichiers archives qui permettent d'archiver des fichiers dans un format spécifique à Java, ce qui les rend portable à condition d'avoir la bonne version de java sur le système cible.
Les fichiers JAR ne sont en fait rien d'autre que des fichiers zip renommés (essayez d'ouvrir un fichier JAR avec Winzip ou Winrar, vous verrez que cela fonctionne).
L'intérêt des archives JAR est aussi qu'elles peuvent être auto-exécutables, faisant office de .exe pour vos programmes Java sous Windows par exemple.
Il existe d'autre moyen de packager des fichiers java comme les *war* (Web application Archive) pour déployer son application sur un serveur d'application.

Pour rappel voici différentes instructions pour créer/exécuter un jar :
 - Création d'un jar : `jar cmf [path.to.MANIFEST] [file1.class, file2.class]`
 - Lancer un jar :  `java jar [path.to.jar]`

1. Créez une fichier `MANIFEST.MF` fixant la classe principale de votre jar
2. Créez votre jar
3. Lancez le

### 3. Créer à un projet maven 🐦

Le but de cet exercice est de vous faire utiliser un peu plus maven. Pour des personnes qui ont essentiellement développé de petites application cela peut sembler un peu lourd, mais dans le mode pro, avoir un outil qui centralise différentes information (version du langage, packaging, dépendances etc) limite le risque d'erreur.

Pour rappel voici quelques commandes maven de base

- Créer un projet maven vide  
    {% highlight bash %}
mvn -B archetype:generate   -DgroupId=com.mycompany.app 
                            -DartifactId=my-app 
                            -DarchetypeArtifactId=maven-archetype-quickstart 
                            -DarchetypeVersion=1.4{% endhighlight %}
- Exécuter une phase maven : `mvn phase_name` exemple `mvn install`, `mvn test`

1. Dans un dossier créez un projet maven, puis insérer le code de l'exercice précédent.
2. Il n'est plus nécessaire d'avoir le jar commons-collections4-4.4, il faudra ajouter cette dépendance dans le pom.xml. Changer également la version de java pour en utiliser une plus récente.
3. Packagez votre application avec la bonne étape maven
4. Lancez votre jar
5. Ajouter dans la partie `build` de votre pom.xml le plugin `maven-assembly-plugin` et configurez-le pour spécifier votre classe principale mais aussi ajouter à votre jar toutes les dépendances nécessaires.
6. Packagez votre application et testez-la

## Les tests 🟢🔴🟣

Tester est extrêmement important quand on développe un produit (donc pas seulement une application informatique).
Un test permet de s'assurer par un exemple que le produit fait ce qu'il doit faire.
Mais un test ne peut être parfait, et c'est leur multiplication qui permet d'assurer une bonne qualité au produit.

L'informatique dispose d'une facilité, elle peut reproduire des tests à l'infini à moindre coût.
Normalement vous avez déjà tous ressenti le besoin de tester votre code python juste pour vérifier s'il fonctionne.
Cela se fait souvent soit dans un terminal python ou dans une petite fonction dans un `main`.
Ces tests sont une première étape mais ils sont fastidieux à mettre en place car il faut les rejouer à la main, et souvent la vérification est visuelle (est-ce que c'est le bon résultat qui est affiché).
Mais quand une application demande de tester des centaines voir des milliers de fonction c'est impossible.
D'où l'importance d'avoir des tests rejouables à l'infini.

Il existe plusieurs frameworks de test est dans ce cours nous allons nous limiter à Junit5.
Vous trouverez la documentation officielle (et donc la réponse à toutes vos question) [ici](https://junit.org/junit5/docs/current/user-guide/). La convention d'un projet maven est de mettre les tests dans le répertoire `src/test/java`, de reproduire l'arborescence de vos packages et de suffixer vos classes de test par `Test`.

Voici quelques rappels de syntaxes
- Dépendance à Junit5
    {% highlight xml %}
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.4.0</version>
    <scope>test</scope>
</dependency>{% endhighlight %}
- Exemple de classe de test :
    {% highlight java %}
class StandardTests {
    @Test
    void dummyTest() {
        // GIVEN
        int integer1 = 1;
        int integer2 = 3
        // WHEN
        int sum = integer1+integer2
        // THEN
        assertEquals(4, sum)
    }
}{% endhighlight %} 

### Mes premiers tests unitaires 🟢🔴

1. Récupérer la branche `code-to-test` du dépôt git du cours
2. Ajouter une dépendance à `Junit5`
3. Codez différents tests et lancez les avec `mvn test`
4. Quand vous pensez avoir fait suffisamment de tests allez dans le dossier `target/site/jacoco` et ouvrez la page `index.html`. Regardez votre couverture de test et améliorez la si besoin

### Le mutation testing 🦠

Si les tests s'assurent de la qualité du code qui s'assurent de la qualité des tests ?
En effet il est facile de faire de faux tests, ou du moins des tests qui acceptent plus de cas que l'on ne le croit. Le *mutation testing* permet dans une certaines mesurer de tester nos tests.

Le principe du mutation testing est de voir si au moins un test échoue après avoir modifié légèrement le code.
Ces modifications sont infimes, un `+` devient un `-`, un `>`; devient un `>=`, un `true` devient `false` et on pour but si nos tests arrivent à nous prévenir d'un léger changement de comportement.
Si un test échoue, on dit que le mutant est tué, sinon il est en vie. Un mutant en vie nous alerte sur la qualité de nos tests.

Il existe en Java l'outil [Pitest](https://pitest.org/) qui permet de réaliser le mutation testing de notre application. Il suffit juste d'ajouter une dépendance à Pitest dans notre projet et de relancer nos tests.

{% highlight xml %}
<dependencies>
    <dependency>
        <groupId>org.pitest</groupId>
        <artifactId>pitest-parent</artifactId>
        <version>1.7.3</version>
        <type>pom</type>
    </dependency>       
</dependencies> 
{% endhighlight %} 

Ainsi qu'une nouvelle phase de build

{% highlight xml %}
<build>
    <plugins>
        <plugin>
            <groupId>org.pitest</groupId>
            <artifactId>pitest-maven</artifactId>
            <version>1.7.3</version>
            <executions>
                <execution>
                    <id>pit-report</id>
                    <!-- optional, this example attached the goal into mvn test phase -->
                    <phase>test</phase>
                    <goals>
                        <goal>mutationCoverage</goal>
                    </goals>
                </execution>
            </executions>
            <dependencies>
                <!-- for Juni5 -->
                <dependency>
                    <groupId>org.pitest</groupId>
                    <artifactId>pitest-junit5-plugin</artifactId>
                    <version>0.15</version>
                </dependency>
            </dependencies>
        </plugin>    
    </plugins>
</build>{% endhighlight %} 

### À vous de jouer

1. Ajoutez une dépendance à `Pitest` dans votre projet
2. Lancez les tests et regardez combien de mutant se sont échappés
3. Tuez tous les mutants