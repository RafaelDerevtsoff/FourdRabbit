FROM openjdk:17-alpine

WORKDIR /app

COPY build/libs/rabbit-0.0.1-SNAPSHOT.jar /app/rabbit.jar

ENV POSTGRES_HOST=localhost RABBIT_HOST=rabbitmq

CMD ["java", "-jar", "rabbit.jar"]