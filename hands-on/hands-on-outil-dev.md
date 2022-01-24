---
layout: page
title: "Hands-on : Outils pout le dev/data-engineer"
---
____
- [Gérer un projet java en ligne de commande ☕📦](#gérer-un-projet-java-en-ligne-de-commande-)
  - [1. Compiler un code java à la main ☕](#1-compiler-un-code-java-à-la-main-)
  - [2. Créer un fichier .jar à la main 📦](#2-créer-un-fichier-jar-à-la-main-)
  - [3. Créer à un projet maven 🐦](#3-créer-à-un-projet-maven-)
- [Les tests 🟢🔴🟣](#les-tests-)
  - [Mes premiers tests unitaires 🟢🔴](#mes-premiers-tests-unitaires-)
  - [Le mutation testing 🦠](#le-mutation-testing-)
  - [À vous de jouer 🎮](#à-vous-de-jouer-)
- [Logging avec Log4J 📚](#logging-avec-log4j-)
  - [Mes premiers loggers en java ☕📚](#mes-premiers-loggers-en-java-)
  - [Logging en pyhon  avec *logging*🐍📚](#logging-en-pyhon--avec-logging)
  - [Mes premiers loggers en Python 🐍📚](#mes-premiers-loggers-en-python-)

____

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

### 3. Créer un wrapper maven 🐦📦

Il peut-être fastidieux d'installer maven sur certaines machines, comme par exemple un container docker. 
Pour nous faciliter la vie, maven nous permet de créer un *maven wrapper*, c'est à dire des fichiers qui vont contenir maven en version portable.
Ainsi on pourra utiliser maven sans avoir à l'installer.

Créer un *maven wrapper* est simple et demande simplement d'exécuter la commande `mvn wrapper:wrapper`
Cela ca créer deux fichiers, un mvnw.cmd pour windows et mvnw pour les autres OS.
À partir de la il suffit de faire `mvnw.cmd [phase]:[goal]` ou `mvnw [phase]:[goal]` pour utiliser maven sans avoir maven.

1. Créez un *maven wrapper* dans le dossier de l'application précédente
2. Utilisez le wrapper pour refaire les commandes précédentes

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

### À vous de jouer 🎮

1. Ajoutez une dépendance à `Pitest` dans votre projet
2. Lancez les tests et regardez combien de mutant se sont échappés
3. Tuez tous les mutants

## Logging avec Log4J 📚

Le *logging* (journalisation en français) consiste à émettre des messages suites à des événements que vie une application.
Ces messages peuvent être affichés dans un sortie standard (comme votre terminal) ou écrits dans un fichier, envoyés dans une base de données, par mail etc.

Le logging permet d'avoir un trace pour analyser à posteriori ce qu'il s'est passé.
C'est donc utile lors d'une phase de débogage, mais aussi pour analyser les performances d'une application.
L'analyse des logs ne sera pas abordée dans ce cours.

Il existe plusieurs bibliothèques pour logger en java, mais nous allons nous concentrer sur Log4j. L'architecture de Log4j repose sur :
- Les loggers qui vont rediriger les informations que l'on souhaite logger
- Les appenders qui spécifient la destination des logs
- Les layouts qui déterminent le format des messagers.
- Les niveaux de log qui qualifient l'importance de l'information (TRACE, DEBUG, INFO, WARN, ERROR, FATAL)

Log4J permet un une configuration par fichier, ce qui permet de souplesse supérieur à une configuration par code. Voici un exemple de fichier log4j2.xml. Pour être pris en compte, votre fichier doit être placé dans un dossier du classpath de votre application.

{% highlight xml %}
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
        <File name="MyFile" fileName="logs/app.log">
            <PatternLayout>
                <Pattern>%d %p %c{1.} [%t] %m%n</Pattern>
            </PatternLayout>
        </File>
    </Appenders>
    <Loggers>
        <Root level="trace">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="MyFile"/>
        </Root>
        <Logger name="custom" level="warn">
            <AppenderRef ref="Console"/>
        </Logger>
    </Loggers>
</Configuration>{% endhighlight %}

Et voici un exemple de code utilisant le fichier de configuration pour ses loggers.

{% highlight java %}
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App 
{
    private static Logger loggerRoot = LogManager.getRootLogger();
    private static Logger loggerCustom = LogManager.getLogger("custom");
    public static void main( String[] args )
    {
        loggerRoot.trace("msg de trace");
        loggerRoot.debug("msg de debogage");
        loggerRoot.info("msg d'info");
        loggerRoot.warn("msg de warn");
        loggerRoot.error("msg d'erreur");
        loggerRoot.fatal("msg fatal");

        loggerCustom.trace("msg de trace");
        loggerCustom.debug("msg de debogage");
        loggerCustom.info("msg d'info");
        loggerCustom.warn("msg de warn");
        loggerCustom.error("msg d'erreur");
        loggerCustom.fatal("msg fatal");
    }
}{% endhighlight %} 

### Mes premiers loggers en java ☕📚
1. Récupérez le code de la partie d'avant
2. Ajoutez différents loggers qui enregistrent les méthodes appelées et les paramètres passés. Vous devrez faire au minimum :
  1. Un logger qui log tout en console et dans un fichier
  2. Un logger qui log les messages WARN et plus dans un fichier

## Logging en python avec *logging*🐍📚

Le logging n'est pas propre à python et tous les langages dispose d'une utilitaire pour le faire.
Python dispose de [logging](https://docs.python.org/3/whatsnew/2.3.html#pep-282-the-logging-package) pour cela.
Le principe est le même, on a des loggers , handler (=appenders), formater (=layout), level. La configuration peut (doit) se faire un fichier externe pour une plus grande souplesse.
Par exemple voici un fichier yml de configuration :
{% highlight yaml %}
version: 1
formatters:
    simple:
    format: '%(asctime)s - %(name)s - %(levelname)s - %(message)s'
handlers:
    console:
    class: logging.StreamHandler
    level: DEBUG
    formatter: simple
    stream: ext://sys.stdout
loggers:
    simpleExample:
    level: DEBUG
    handlers: [console]
    propagate: no
root:
    level: DEBUG
    handlers: [console]{% endhighlight %} 

Et son utilisation dans un code python.

{% highlight python %}
import logging.config
import yaml

with open('./logging_conf.yml', 'r') as stream:
    config = yaml.load(stream, Loader=yaml.FullLoader)

logging.config.dictConfig(config)

# create logger
logger = logging.getLogger('simpleExample')

# 'application' code
logger.debug('debug message')
logger.info('info message')
logger.warning('warn message')
logger.error('error message')
logger.critical('critical message'){% endhighlight %} 

### Mes premiers loggers en Python 🐍📚
1. Prenez un de vos anciens code python
2. Ajoutez différents loggers qui enregistrent les méthodes appelées et les paramètres passés. Vous devrez faire au minimum :
  1. Un logger qui log tout en console et dans un fichier
  2. Un logger qui log les messages WARN et plus dans un fichier