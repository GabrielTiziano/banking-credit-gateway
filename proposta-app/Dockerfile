# ---------- Estágio 1: build ----------
# Imagem com Maven + JDK 17 só para compilar. Não vai para a imagem final.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia só o pom primeiro e baixa as dependências.
# Assim o Docker cacheia esta camada e só rebaixa deps se o pom mudar.
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Agora copia o código e empacota (pulando testes no build da imagem).
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---------- Estágio 2: runtime ----------
# Só a JRE: imagem menor e com menor superfície de ataque.
FROM eclipse-temurin:17-jre
WORKDIR /app

# Traz apenas o .jar gerado no estágio de build.
COPY --from=build /app/target/*.jar app.jar

# proposta-app é a API/gateway web.
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
