# TaskManagerCRUD

Trabalho de faculdade - Sistema CRUD usando Java + MongoDB.

Este projeto é um sistema de gerenciamento de tarefas (CRUD) desenvolvido em Java, utilizando o driver síncrono do MongoDB. Ele faz parte de um trabalho acadêmico e tem como objetivo aplicar conceitos de Programação Orientada a Objetos, persistência de dados e conexão com banco NoSQL.

---

## ⚙️ Tecnologias utilizadas

- Java 24  
- MongoDB (banco de dados local)  
- MongoDB Java Driver (mongodb-driver-sync v5.5.0)  
- Maven (gerenciador de dependências)  
- NetBeans 25 (ou IDE compatível)  

---

## 📁 Estrutura do Projeto

```
TaskManagerCRUD/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── mycompany/
│                   └── sistemacrud/
│                       └── ConexaoMongo.java  # Classe de conexão com MongoDB
├── pom.xml              # Arquivo Maven com as dependências do projeto
└── README.md            # Documentação do projeto
```

---

## 🛠️ Como rodar o projeto localmente

### 1. Pré-requisitos

- Java 24 instalado  
- MongoDB instalado e em execução local (padrão: `mongodb://localhost:27017`)  
- Git instalado  
- NetBeans (ou outra IDE que suporte Maven)  
- Conexão com a internet (para baixar dependências Maven)  

### 2. Clonar o repositório

```bash
git clone https://github.com/seu-usuario/TaskManagerCRUD.git
cd TaskManagerCRUD
```

### 3. Abrir o projeto no NetBeans

1. Vá em **File > New Project**  
2. Crie um Novo Projeto `Java with Maven `  
3. Clique em `Java Application ` 
4. Na pasta **Project Files**, clique no arquivo pom.xml
5. Dentro do pom.xml adicione essas linhas de codigo:

```xml
<groupId>com.mycompany</groupId>
<artifactId>TaskManagerCRUD</artifactId>
<version>1.0-SNAPSHOT</version>
<packaging>jar</packaging>

<properties>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  <maven.compiler.release>24</maven.compiler.release>
  <exec.mainClass>com.mycompany.taskmanagercrud.TaskManagerCRUD</exec.mainClass>
</properties>

<!-- Dependências -->
<dependencies>
  <!-- Driver síncrono do MongoDB -->
  <dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>5.5.0</version>
  </dependency>
</dependencies>

<!-- Plugin de compilação Java (opcional, mas recomendado) -->
<build>
  <plugins>
    <plugin>
      <artifactId>maven-compiler-plugin</artifactId>
      <version>3.10.1</version>
      <configuration>
        <release>24</release>
      </configuration>
    </plugin>
  </plugins>
</build>
```

Precisa ficar assim:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.mycompany</groupId>
  <artifactId>TaskManagerCRUD</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.release>24</maven.compiler.release>
    <exec.mainClass>com.mycompany.taskmanagercrud.TaskManagerCRUD</exec.mainClass>
  </properties>

  <!-- Dependências -->
  <dependencies>
    <!-- Driver síncrono do MongoDB -->
    <dependency>
      <groupId>org.mongodb</groupId>
      <artifactId>mongodb-driver-sync</artifactId>
      <version>5.5.0</version>
    </dependency>
  </dependencies>

  <!-- Plugin de compilação Java (opcional, mas recomendado) -->
  <build>
    <plugins>
      <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.10.1</version>
        <configuration>
          <release>24</release>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

### 4. Atualizar dependências Maven

No painel de projetos:

- Clique com o botão direito no projeto → **Reload POM** ou **Clean and Build**
- Isso irá baixar a dependência do MongoDB automaticamente

## ✅ Verificando se está tudo funcionando

Execute a classe `ConexaoMongo.java`. Se tudo estiver certo, você verá a mensagem no console:

```bash
✓ Conectado com sucesso ao MongoDB local!
```

Se der erro, verifique:

- Se o MongoDB está em execução  
- Se o `pom.xml` foi carregado corretamente  
- Se o Java e o Maven estão funcionando no ambiente  

---

## 👨‍💻 Próximos passos

- Implementar as operações de **Create, Read, Update e Delete**  
- Criar interface gráfica (opcional)  
- Salvar dados das tarefas no MongoDB  

---

## 📌 Observações

Este projeto é apenas para fins acadêmicos. Está em desenvolvimento e pode conter melhorias futuras.
