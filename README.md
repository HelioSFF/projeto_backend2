# projeto_backend2
Projeto de backend em spring.

# ClinMed API

API REST para gerenciamento de clínica médica desenvolvida com Java e Spring Boot.

O sistema permite o gerenciamento de usuários, médicos, pacientes, especialidades, consultas e prontuários, aplicando regras de negócio reais para agendamento e controle clínico.

Este projeto foi desenvolvido como atividade da disciplina **Tecnologia para Back-End Avançado**.

---

# Objetivo do Projeto

Projeto desenvolvido para prática de desenvolvimento backend com Spring Boot, aplicando conceitos de:

- arquitetura em camadas
- modelagem de domínio
- regras de negócio
- APIs REST
- persistência de dados
- documentação de APIs

# Tecnologias Utilizadas

- Java 21
- Spring Boot 4.0.6
- Spring Web
- Spring Data JPA
- H2 Database
- Springdoc OpenAPI (Swagger)
- Lombok
- Maven

---

# Funcionalidades

- Cadastro de usuários
- Cadastro de médicos e especialidades
- Cadastro de pacientes
- Agendamento e cancelamento de consultas
- Controle de status das consultas
- Registro de prontuários médicos
- Documentação automática com Swagger
- Banco H2 em memória para testes e desenvolvimento

---

# Executando o Projeto

## Pré-requisitos

- Java 21
- Maven 3.9+

## Clonar o repositório

```bash
git clone <url-do-repositorio>
```

## Executar a aplicação

```bash
mvn spring-boot:run
```

Após iniciar, a aplicação estará disponível em:

```txt
http://localhost:8080
```

---

# Documentação da API

Swagger UI:

```txt
http://localhost:8080/swagger-ui.html
```

---

# Console H2

Acesso ao banco:

```txt
http://localhost:8080/h2-console
```

## Configurações

```txt
JDBC URL: jdbc:h2:mem:clinmeddb
Usuário: sa
Senha:
```

---

# Endpoints

| Domínio | Endpoint Base |
|---|---|
| Usuários | `/api/usuarios` |
| Especialidades | `/api/especialidades` |
| Médicos | `/api/medicos` |
| Pacientes | `/api/pacientes` |
| Consultas | `/api/consultas` |
| Prontuários | `/api/prontuarios` |

---

# Regras de Negócio

- Consultas devem ser agendadas com no mínimo 30 minutos de antecedência
- A clínica funciona apenas entre 07h e 19h
- Não há funcionamento aos domingos
- Pacientes suspensos não podem agendar consultas
- Não é permitido conflito de horários para médicos ou pacientes

Fluxo de status da consulta:

```txt
AGENDADA → CONFIRMADA → REALIZADA / CANCELADA
```

- Cancelamentos exigem motivo e antecedência mínima de 24 horas
- Não é permitido excluir recursos com vínculos ativos

---

# Arquitetura

O projeto segue arquitetura em camadas:

```txt
controller → service → repository → entity
```

Cada domínio possui DTOs específicos para entrada e saída de dados:

- `RequestDTO`
- `ResponseDTO`

As entidades não são expostas diretamente pela API.

---

# Dados Iniciais

Ao iniciar a aplicação, os seguintes dados são carregados automaticamente:

## Especialidades

- Cardiologia
- Pediatria
- Oftalmologia

## Usuários

- 1 administrador
- 2 médicos
- 1 recepcionista

## Médicos

- 2 médicos vinculados aos usuários

## Pacientes

- 1 paciente ativo
- 1 paciente suspenso

---

