FROM java:8
EXPOSE 8080
ADD /target/Spark.jar spark.jar
ENTRYPOINT ["java","-jar","spark.jar"]