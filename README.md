# Reto de AWS

Este repositorio contiene el código fuente y la documentación del reto de AWS.

## Objetivo del Proyecto

El objetivo de este reto es construir y desplegar una aplicación en la nube de Amazon Web Services (AWS), aplicando dos arquitecturas diferentes:

1.  **Arquitectura Monolítica:** Se construye una API robusta con Spring Boot, se empaqueta con Docker y se despliega en un entorno de contenedores (ECS) con una base de datos relacional (RDS).
2.  **Arquitectura de Microservicios:** Se evoluciona la funcionalidad hacia un modelo serverless, utilizando funciones AWS Lambda con Node.js, una base de datos NoSQL (DynamoDB) y servicios de mensajería (SQS/SNS) para la comunicación asíncrona.

## Tecnologías Utilizadas

### Etapa 1: Monolito
* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3.x
* **Construcción:** Gradle
* **Base de Datos:** PostgreSQL (en AWS RDS)
* **Contenedores:** Docker
* **Despliegue AWS:** ECR, ECS, API Gateway
* **Seguridad:** AWS Cognito
* **Configuración:** AWS Parameter Store

### Etapa 2: Microservicios (Serverless)
* **Lenguaje:** Node.js
* **Entorno de ejecución:** AWS Lambda
* **Base de Datos:** AWS DynamoDB
* **Mensajería:** AWS SQS & AWS SNS
* **Infraestructura como Código:** AWS Serverless Framework / CloudFormation

## Estado del Proyecto

Actualmente en desarrollo: **Etapa 1 - Construcción del API Monolítica.**
