# Inventory System - PB TP4

Projeto desenvolvido como parte da disciplina **Engenharia Disciplinada de Software** no Instituto Infnet.

O sistema consiste em uma aplicação web para gerenciamento de produtos, construída com **Java 21**, **Spring Boot**, **Thymeleaf** e banco de dados **H2 em memória**. O projeto também inclui uma pipeline completa de **CI/CD** utilizando **GitHub Actions**, garantindo qualidade de código, testes automatizados e geração de artefatos.


## 📊 Objetivos do projeto

- Aplicar princípios de engenharia de software disciplinada
- Garantir qualidade através de testes automatizados
- Implementar CI/CD completo
- Produzir código limpo, modular e escalável
- Atingir alta cobertura de testes

---

## 🚀 Tecnologias utilizadas

- Java 21
- Spring Boot
- Maven Wrapper
- H2 Database (in-memory)
- Thymeleaf
- JUnit
- Mockito
- MockMvc
- JaCoCo (Code Coverage)
- PMD (Static Code Analysis)
- GitHub Actions (CI/CD)

---

## 📂 Funcionalidades

- CRUD completo de produtos
- Interface web com Thymeleaf
- Validação de regras de negócio
- Tratamento de exceções customizadas
- Testes automatizados com alta cobertura
- Pipeline CI/CD automatizada
- Geração de artefato .jar após build

---

## ▶️ Como executar o projeto

### Pré-requisitos

- JDK 21 instalado
- Git instalado

### Passos

Clone o repositório:

```bash
git clone https://github.com/leonardo-muniz/inventory-system-pb-tp4
cd inventory-system-pb-tp4
```

Execute a aplicação:

```bash
./mvnw spring-boot:run
```

Acesse no navegador:

```url
http://localhost:8080/products
```

---

## 🧪 Testes e Qualidade

O projeto segue princípios de Clean Code e Single Responsibility Principle (SRP).

Foram aplicadas práticas de refatoração orientadas por testes garantindo:

- 100% de cobertura no Controller
- Testes com MockMvc e Mockito
- Análise estática com PMD
- Validação automática via pipeline

Para rodar os testes localmente:
```bash
./mvnw clean verify
```

Relatório de cobertura disponível em:
```url
target/site/jacoco/index.html
```
