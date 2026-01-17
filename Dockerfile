# Estágio de build
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar o pom.xml primeiro para aproveitar o cache do Docker
COPY pom.xml .

# Baixar as dependências
RUN mvn dependency:go-offline -B

# Copiar o código fonte
COPY src ./src

# Build da aplicação
RUN mvn clean package -DskipTests

# Estágio de runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar o jar do estágio de build
COPY --from=build /app/target/*.jar app.jar

# Expor a porta da aplicação
EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
