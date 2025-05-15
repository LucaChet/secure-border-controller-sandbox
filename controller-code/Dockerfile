FROM openjdk:22-jdk

# Copia il file JAR dell'applicazione nel container
COPY target/fluidos-security-orchestrator-1.0-SNAPSHOT-jar-with-dependencies.jar /app.jar

# Copia il file My_test2.xml nella directory dell'immagine
COPY testfile/consumer_MSPL_demo.xml /app/testfile/consumer_MSPL_demo.xml
COPY testfile/provider_MSPL_demo.xml /app/testfile/provider_MSPL_demo.xml
COPY testfile/My_test2.xml /app/testfile/My_test2.xml
COPY testfile/provider_MSPL_request.xml /app/testfile/provider_MSPL_request.xml

# Copia la cartella contenente lo schema XSD nel container
COPY xsd/ /app/xsd

RUN mkdir -p /app/network_policies

# Definisce l'entry point per l'esecuzione dell'applicazione
ENTRYPOINT ["java", "-jar", "/app.jar"]
