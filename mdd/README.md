# Monde de Dev

## Requirements

Ton run this project, you need to have the following tools installed on your computer :

* Java 21 : https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
* Maven : https://maven.apache.org/download.cgi
* Docker : https://www.docker.com/get-started/

* Sdkman (is optionnal if you already have the Java 21 version) : https://sdkman.io/install
  You can use sdkman to manage your java version, you can install java 21.0.6-jbr and use it by the following
  command :

``` bash
sdk install java 21.0.6-jbr
sdk use java 21.0.6-jbr
``` 

you can also find a file named .sdkmanrc to easily change version between projects by using the following command :

``` bash
sdk env
```

## INIT DB
command to init a new db in docker :

``` bash
docker run --name mdd -e POSTGRES_USER=pguser -e POSTGRES_PASSWORD=pgpass -e POSTGRES_DB=pgdb -p 5432:5432 -d postgres
```

to start db :

``` bash
docker start mdd
```

to stop db :

``` bash
docker stop mdd
```

to remove db :

``` bash
docker rm mdd
```

You dont need to populate DB as the app will sync the db with the entities at the start of the app by the synchonize
option set to true.

## Running the application

To run the application, you first need to install then run, you can use the following command :

``` bash
mvn install
```

Then you can run the application by using the following command :

``` bash
mvn spring-boot:run
```

## Seed
You will find a seed package with a MainSeed class that will populate the database with some data.
This will be automatically run at the start of the application if the initialisationMode is set to "always" od "embedded" in the application.properties file.
**This seed is for development purpose only and should not be used in production.**
It will create users, topics, posts, comments, and you can add others as features will be added.
Think about increment seed ids.

## Security
Spring Security is used to secure the application.
Authentication is done by JWT, you can find the configuration in the SecurityConfig class.
You can find JWt documentation here : https://jwt.io/introduction/
Users are linked to request to be accessible by the controllers.
