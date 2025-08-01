# Dockerfile using pre-built JAR (no Maven wrapper dependency)
# Build your JAR first: mvn clean package -DskipTests
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
