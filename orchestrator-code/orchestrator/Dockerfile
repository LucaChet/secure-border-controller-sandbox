FROM openjdk:22-jdk

# Copia il file JAR dell'applicazione nel container
COPY target/fluidos-security-orchestrator-1.0-SNAPSHOT-jar-with-dependencies.jar /app.jar

# Esponi la porta su cui il rest API server si mette in ascolto
EXPOSE 8080


# Copia la cartella contenente lo schema XSD nel container
COPY xsd/ /app/xsd

# Definisce l'entry point per l'esecuzione dell'applicazione
ENTRYPOINT ["java", "-jar", "/app.jar"]