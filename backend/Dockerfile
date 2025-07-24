FROM azul/zulu-openjdk-alpine:21-latest AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x gradlew
RUN ./gradlew build -x test

FROM azul/zulu-openjdk-alpine:21-jre-latest

WORKDIR /app

COPY --from=builder /app/build/libs/spotify-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9090

CMD ["java", "-jar", "app.jar"]