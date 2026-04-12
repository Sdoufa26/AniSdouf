# ==========================================
# ÉTAPE 1 : BUILD (Compilation du projet)
# ==========================================
# On utilise l'image officielle Java 23
FROM eclipse-temurin:23-jdk AS build
WORKDIR /app

# On copie tout ton code source dans le conteneur
COPY . .

# On donne les droits d'exécution à ton fichier Maven local
RUN chmod +x mvnw

# On compile le projet pour créer le fichier .jar
RUN ./mvnw clean package -DskipTests

# ==========================================
# ÉTAPE 2 : RUN (Lancement du serveur)
# ==========================================
# On prend une image Java 23 plus légère pour faire tourner l'app
FROM eclipse-temurin:23-jre
WORKDIR /app

# On récupère le fichier .jar généré à l'étape 1
COPY --from=build /app/target/AniSdoufBack-0.0.1-SNAPSHOT.jar app.jar

# On expose le port 8080
EXPOSE 8080

# On lance l'application Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]