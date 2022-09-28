FROM openjdk:11 as build
ADD target/DeeSeeComics.jar DeeSeeComics.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "DeeSeeComics.jar"]