# Usa uma imagem oficial do Maven com o JDK
FROM maven:3.9-eclipse-temurin-21

# Instala as bibliotecas gráficas essenciais (X11, GTK, GLX) para o JavaFX renderizar
RUN apt-get update && apt-get install -y \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libgl1 \
    libgtk-3-0 \
    && rm -rf /var/lib/apt/lists/*

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia os arquivos do projeto para dentro do container
COPY pom.xml .
COPY src ./src

# Compila o projeto previamente para baixar todas as dependências do Maven
RUN mvn clean compile

# Comando padrão ao iniciar o container: rodar o JavaFX
CMD ["mvn", "javafx:run"]