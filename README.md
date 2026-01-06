# Desafio Fullstack Integrado

Solução completa para gerenciamento de benefícios com arquitetura em camadas (DB, EJB, Backend Spring Boot, Frontend Angular).

## Sobre o Projeto

Sistema de gerenciamento de benefícios que permite operações CRUD e transferências entre benefícios, implementado seguindo os requisitos do desafio técnico.

## 🎯 Estrutura do Projeto

```
bip-teste-integrado/
├── db/                          # Scripts de banco de dados
│   ├── schema.sql              # Schema da tabela BENEFICIO
│   └── seed.sql                # Dados iniciais
├── ejb-module/                 # Módulo EJB com lógica de negócio
│   └── src/main/java/com/example/ejb/
│       ├── Beneficio.java      # Entidade JPA
│       └── BeneficioEjbService.java  # Serviço EJB com bug corrigido
├── backend-module/             # Backend Spring Boot
│   └── src/main/java/com/example/backend/
│       ├── entity/             # Entidades JPA
│       ├── repository/         # Repositórios Spring Data
│       ├── service/            # Serviços de negócio
│       ├── dto/                # Data Transfer Objects
│       ├── controller/         # Controllers REST
│       ├── config/             # Configurações (Swagger)
│       └── exception/          # Tratamento de exceções
├── frontend/                   # Aplicação Angular
│   └── src/app/
│       ├── models/             # Modelos TypeScript
│       ├── services/           # Serviços HTTP
│       └── beneficio-list/     # Componentes
└── docs/                       # Documentação
```

## Funcionalidades

- **Banco de Dados**: Schema e scripts de inicialização (schema.sql, seed.sql)
- **EJB**: Correção do bug no BeneficioEjbService com validações, locking e controle transacional
- **Backend**: API REST completa com CRUD e transferência entre benefícios
- **Frontend**: Interface Angular para gerenciamento de benefícios
- **Testes**: Testes unitários do serviço e controller
- **Documentação**: Swagger/OpenAPI configurado

### Endpoints da API

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/v1/beneficios` | Lista todos os benefícios |
| GET | `/api/v1/beneficios/ativos` | Lista benefícios ativos |
| GET | `/api/v1/beneficios/{id}` | Busca benefício por ID |
| POST | `/api/v1/beneficios` | Cria novo benefício |
| PUT | `/api/v1/beneficios/{id}` | Atualiza benefício |
| DELETE | `/api/v1/beneficios/{id}` | Deleta benefício |
| POST | `/api/v1/beneficios/transfer` | Transfere valor entre benefícios |

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+
- Node.js 18+ e npm
- Angular CLI (instalado globalmente: `npm install -g @angular/cli`)

### Passo 1: Compilar e iniciar o Backend

```bash
# Compilar o projeto (inclui módulo EJB)
cd backend-module
mvn clean install -DskipTests

# Iniciar o backend
mvn spring-boot:run
```

O backend estará disponível em `http://localhost:8080`

**Verificar se está funcionando:**
- API: `http://localhost:8080/api/v1/beneficios` (deve retornar JSON com 2 benefícios)
- Swagger: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:beneficio_db`
  - Username: `sa`
  - Password: (deixe vazio)

### Passo 2: Instalar dependências e iniciar o Frontend

Em um novo terminal:

```bash
cd frontend
npm install
ng serve
```

O frontend estará disponível em `http://localhost:4200`

### Passo 3: Executar Testes (Opcional)

```bash
# Testes do backend
cd backend-module
mvn test
```

**Nota:** Os testes podem falhar com Java 25 devido a incompatibilidade com Mockito. Use `-DskipTests` para compilar sem executar testes.

## Correção do Bug no EJB

O método `transfer` do `BeneficioEjbService` foi corrigido para:

- Validar dados de entrada (IDs, valores)
- Verificar saldo suficiente antes da transferência
- Usar locking pessimista (PESSIMISTIC_WRITE) para evitar condições de corrida
- Garantir controle transacional com rollback automático
- Validar benefícios ativos
- Prevenir transferências inválidas

Detalhes técnicos: ver `DOCUMENTACAO_DETALHADA.md`

## Arquitetura

A solução segue arquitetura em camadas:

1. **DB**: Scripts de schema e seed
2. **EJB**: Lógica de negócio transacional
3. **Backend**: API REST Spring Boot
4. **Frontend**: Interface Angular

## Testes

Testes unitários implementados para:
- Serviço de benefícios (BeneficioServiceTest)
- Controller REST (BeneficioControllerTest)
- Casos de sucesso e erro

Executar: `mvn test` no diretório `backend-module`

## Tecnologias

**Backend:** Java 17, Spring Boot 3.2.5, Spring Data JPA, H2 Database, Swagger  
**Frontend:** Angular 17, TypeScript, RxJS  
**EJB:** Jakarta EE 10, JPA/Hibernate

## Documentação Adicional

- [COMO_ACESSAR.md](COMO_ACESSAR.md) - Guia de acesso e uso da aplicação
- [DOCUMENTACAO_DETALHADA.md](DOCUMENTACAO_DETALHADA.md) - Documentação técnica completa
- [docs/README.md](docs/README.md) - Enunciado original do desafio

