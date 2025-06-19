# Use Eclipse Temurin JDK 21
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia o JAR do build
COPY target/device-manager-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta da aplicação e da JVM debug
EXPOSE 8080 5005

# Inicia com o agente remoto para depuração
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]