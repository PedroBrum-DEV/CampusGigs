# CampusGigs

Marketplace de freelas entre alunos da faculdade — API REST em Spring Boot com autenticação JWT, regras de negócio por papel de usuário e banco de dados versionado com Flyway.

Projeto acadêmico desenvolvido para a disciplina de **Java Advanced**.

---

## Integrantes

| Nome                          | RM       |
|-------------------------------|----------|
| Arthur Brito da Silva          | RM 562085 |
| Luiz Felipe Flosi dos Santos   | RM 563197 |
| Pedro Henrique Brum Lopes      | RM 561780 |

---

## Sobre o projeto

O **CampusGigs** é uma plataforma onde alunos podem publicar freelas (pequenos trabalhos remunerados, como aulas particulares, design, digitação, revisão de código, etc.) e outros alunos podem se candidatar a essas vagas. O dono de cada freela decide quem contratar entre os candidatos.

O projeto reúne, em uma única aplicação, os principais conceitos trabalhados ao longo da disciplina:

- **Autenticação e segurança**: login com JWT, senhas criptografadas com BCrypt
- **Regras de negócio por papel de usuário**: o que um aluno pode fazer, o que só o dono do recurso pode fazer, e o que é exclusivo de administrador
- **Persistência e versionamento de banco**: JPA/Hibernate com PostgreSQL, schema controlado por migrations do Flyway
- **Infraestrutura em containers**: banco de dados sobe via Docker Compose

---

## Tecnologias utilizadas

| Camada          | Tecnologia                                    |
|-----------------|------------------------------------------------|
| Linguagem       | Java 21                                        |
| Framework       | Spring Boot 3.3                                |
| Segurança       | Spring Security + JWT (`jjwt`)                 |
| Persistência    | Spring Data JPA / Hibernate                    |
| Banco de dados  | PostgreSQL 16                                  |
| Versionamento de schema | Flyway                                 |
| Infraestrutura  | Docker Compose                                 |
| Build           | Maven                                          |
| Utilitários     | Lombok, Bean Validation                        |

---

## Arquitetura

```
Cliente (console HTML / Insomnia / Postman)
              │
              ▼
   Spring Boot REST API (porta 8080)
              │
   ┌──────────┼──────────┐
   │          │          │
Controller  Service   Security (JWT)
   │          │
   └────► Repository (Spring Data JPA)
              │
              ▼
        PostgreSQL (Docker)
```

A API segue uma arquitetura em camadas:

- **`controller`** — recebe as requisições HTTP e delega para os services; não contém regra de negócio
- **`service`** — concentra as regras de negócio (quem pode fazer o quê)
- **`repository`** — acesso a dados via Spring Data JPA
- **`model`** — entidades JPA mapeadas para as tabelas do banco
- **`dto`** — objetos de entrada e saída da API (nunca expomos as entidades diretamente)
- **`security`** — geração e validação de JWT, filtro de autenticação
- **`exception`** — exceções de domínio e tratamento global de erros

---

## Domínio

| Entidade | Descrição |
|----------|-----------|
| `User` | Um aluno (`STUDENT`) ou administrador (`ADMIN`) da plataforma |
| `Gig` | Um freela publicado por um aluno (título, descrição, categoria, preço, status) |
| `JobApplication` | A candidatura de um aluno a um freela |

---

## Regras de negócio

- Qualquer pessoa, logada ou não, pode **listar e visualizar** os freelas disponíveis
- Apenas usuários **autenticados** podem **publicar** um freela — o dono é sempre quem está logado
- Apenas o **dono do freela ou um administrador** pode **editar**, **encerrar** ou **excluir** um freela
- Apenas usuários **autenticados** podem **se candidatar** a um freela, respeitando as seguintes condições:
  - não é possível se candidatar ao próprio freela
  - não é possível se candidatar a um freela já encerrado (`CLOSED`)
  - não é possível se candidatar duas vezes ao mesmo freela
- Apenas o **dono do freela ou um administrador** pode visualizar as candidaturas recebidas e decidir sobre elas (aceitar/rejeitar)
- O **próprio candidato** pode retirar sua candidatura enquanto ela estiver pendente; um **administrador** pode remover qualquer candidatura
- Apenas **administradores** podem listar todos os usuários da plataforma ou remover a conta de outro usuário

---

## Como executar o projeto

### Pré-requisitos

- Java 21
- Maven (ou o wrapper `./mvnw` incluído no projeto)
- Docker e Docker Compose

### 1. Subir o banco de dados

```bash
docker compose up -d
```

Isso inicia um container PostgreSQL em `localhost:5432`.

### 2. Executar a aplicação

```bash
./mvnw spring-boot:run
```

Ao subir, o Flyway aplica automaticamente as migrations localizadas em `src/main/resources/db/migration`:

- `V1__init_schema.sql` — cria as tabelas `users`, `gigs` e `applications`
- `V2__seed_data.sql` — popula o banco com usuários e freelas de exemplo

### 3. Usuários de teste (seed)

| Username        | Senha       | Papel     |
|------------------|-------------|-----------|
| `admin`          | `admin123`  | ADMIN     |
| `ana.souza`      | `aluno123`  | STUDENT   |
| `bruno.lima`     | `aluno123`  | STUDENT   |
| `carla.mendes`   | `aluno123`  | STUDENT   |

### 4. Testar a aplicação

Com a aplicação em execução, acesse:

```
http://localhost:8080
```

Um console de testes é servido diretamente pela aplicação, permitindo autenticar, criar freelas, se candidatar e testar todas as regras de negócio pelo navegador, sem necessidade de ferramentas externas.

Alternativamente, é possível usar o arquivo [`requests.http`](./requests.http) com a extensão **REST Client** (VS Code) ou importar as rotas em Insomnia/Postman.

---

## Endpoints

```
POST   /auth/register              Registra um novo aluno                          (público)
POST   /auth/login                 Autentica e retorna o token JWT                 (público)

GET    /gigs                       Lista todos os freelas                          (público)
GET    /gigs/{id}                  Detalha um freela                               (público)
POST   /gigs                       Publica um freela                               (autenticado)
PUT    /gigs/{id}                  Edita um freela                                 (dono ou admin)
PATCH  /gigs/{id}/close            Encerra um freela                               (dono ou admin)
DELETE /gigs/{id}                  Remove um freela                                (dono ou admin)

POST   /gigs/{id}/applications     Candidata-se a um freela                        (autenticado)
GET    /gigs/{id}/applications     Lista as candidaturas de um freela              (dono ou admin)
GET    /applications/me            Lista as próprias candidaturas                  (autenticado)
PATCH  /applications/{id}/status   Aceita ou rejeita uma candidatura               (dono ou admin)
DELETE /applications/{id}          Retira uma candidatura                          (candidato ou admin)

GET    /users/me                   Consulta o próprio perfil                       (autenticado)
GET    /users                      Lista todos os usuários                         (admin)
DELETE /users/{id}                 Remove um usuário                               (admin)
```

---

## Estrutura do projeto

```
campusgigs-api/
├── docker-compose.yml
├── pom.xml
├── requests.http
├── src/
│   ├── main/
│   │   ├── java/br/com/fiap/campusgigs/
│   │   │   ├── config/           # Configuração do Spring Security
│   │   │   ├── controller/       # Endpoints REST
│   │   │   ├── dto/              # Objetos de entrada e saída da API
│   │   │   ├── exception/        # Exceções de domínio e tratamento global de erros
│   │   │   ├── model/            # Entidades JPA
│   │   │   ├── repository/       # Repositórios Spring Data JPA
│   │   │   ├── security/         # Geração/validação de JWT
│   │   │   └── service/          # Regras de negócio
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/index.html # Console de testes servido pela própria aplicação
│   │       └── db/migration/     # Migrations do Flyway
│   └── test/
└── README.md
```

---

## Licença

Projeto de caráter acadêmico, sem fins comerciais.
