# Sistema de Gestão de Documentos (GED)

Sistema completo de gestão de documentos com upload, versionamento, busca e controle de acesso baseado em perfis.

## 📋 Índice

- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Execução](#instalação-e-execução)
- [Endpoints da API](#endpoints-da-api)
- [Testes](#testes)
- [Decisões Técnicas](#decisões-técnicas)
- [Estrutura do Projeto](#estrutura-do-projeto)

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.2.2** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados relacional
- **Flyway** - Migrations de banco de dados
- **JWT (jjwt 0.12.3)** - Tokens de autenticação
- **Lombok** - Redução de boilerplate
- **JUnit 5 + Mockito** - Testes unitários
- **Maven** - Gerenciamento de dependências

### Frontend
- **Angular 17** - Framework frontend
- **Angular Material** - Componentes UI
- **RxJS** - Programação reativa
- **TypeScript** - Type safety
- **SCSS** - Estilização

### DevOps
- **Docker** - Containerização
- **Docker Compose** - Orquestração de containers
- **GitHub Actions** - CI/CD
- **Nginx** - Servidor web para frontend

## Arquitetura

O projeto segue os princípios de **Clean Architecture** com separação clara de responsabilidades:

```
backend/
├── domain/              # Camada de Domínio (núcleo do negócio)
│   ├── model/          # Entidades de domínio
│   ├── repository/     # Interfaces de repositórios
│   ├── service/        # Interfaces de serviços
│   └── exception/      # Exceções de domínio
│
├── application/         # Camada de Aplicação (casos de uso)
│   ├── usecase/        # Casos de uso (lógica de negócio)
│   ├── dto/            # DTOs de entrada/saída
│   └── mapper/         # Mapeadores entre camadas
│
├── infrastructure/      # Camada de Infraestrutura (implementações)
│   ├── persistence/    # JPA entities e repositories
│   ├── security/       # JWT e configurações de segurança
│   ├── storage/        # Armazenamento de arquivos
│   └── config/         # Configurações do Spring
│
└── presentation/        # Camada de Apresentação (API REST)
    ├── controller/     # Controllers REST
    └── exception/      # Exception handlers
```

### Princípios Aplicados
- **SOLID** - Todos os princípios aplicados
- **DDD** - Domain-Driven Design
- **Dependency Inversion** - Camadas dependem de abstrações
- **Single Responsibility** - Cada classe com uma única responsabilidade
- **Clean Code** - Código limpo e sem comentários desnecessários

## 📦 Pré-requisitos

- **Docker** e **Docker Compose** instalados
- **Java 17** (apenas para desenvolvimento local sem Docker)
- **Maven 3.9+** (apenas para desenvolvimento local sem Docker)

## 🔧 Instalação e Execução

### Opção 1: Usando Docker Compose (Recomendado)

1. Clone o repositório:
```bash
git clone <repository-url>
cd UDS_MVP
```

2. Suba os containers:
```bash
docker compose up -d
```

3. Aguarde a aplicação iniciar (pode levar alguns minutos na primeira vez):
```bash
docker compose logs -f backend
```

4. A API estará disponível em: `http://localhost:8080`

5. Para parar os containers:
```bash
docker compose down
```

### Opção 2: Execução Local (Desenvolvimento)

1. Certifique-se de ter PostgreSQL rodando localmente:
```bash
createdb ged
```

2. Configure as variáveis de ambiente (ou use as padrões do application.yml):
```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/ged
export DATABASE_USERNAME=ged_user
export DATABASE_PASSWORD=ged_pass
```

3. Execute o backend:
```bash
cd backend
mvn spring-boot:run
```

## Autenticação

O sistema possui dois usuários pré-cadastrados:

| Username | Password | Role  |
|----------|----------|-------|
| admin    | admin    | ADMIN |
| user     | user     | USER  |

### Obter Token JWT

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
```

Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "role": "ADMIN"
}
```

Use o token nas requisições subsequentes:
```bash
curl -H "Authorization: Bearer {token}" http://localhost:8080/api/documents
```

## 📡 Endpoints da API

### Autenticação
- `POST /api/auth/login` - Autenticar usuário

### Documentos
- `POST /api/documents` - Criar documento
- `GET /api/documents` - Listar documentos (com paginação e filtros)
- `GET /api/documents/{id}` - Buscar documento por ID
- `PUT /api/documents/{id}` - Atualizar documento
- `PATCH /api/documents/{id}/publish` - Publicar documento
- `PATCH /api/documents/{id}/archive` - Arquivar documento
- `DELETE /api/documents/{id}` - Deletar documento (apenas ADMIN)

### Versões
- `POST /api/documents/{documentId}/versions` - Upload de nova versão
- `GET /api/documents/{documentId}/versions` - Listar versões
- `GET /api/documents/{documentId}/versions/{versionId}/download` - Download de versão

### Exemplos de Uso

**Criar documento:**
```bash
curl -X POST http://localhost:8080/api/documents \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Documento Teste",
    "description": "Descrição do documento",
    "tags": ["importante", "2024"]
  }'
```

**Listar documentos com filtros:**
```bash
curl "http://localhost:8080/api/documents?status=PUBLISHED&title=teste&page=0&size=10" \
  -H "Authorization: Bearer {token}"
```

**Upload de arquivo:**
```bash
curl -X POST http://localhost:8080/api/documents/1/versions \
  -H "Authorization: Bearer {token}" \
  -F "file=@documento.pdf"
```

## 🧪 Testes

### Executar todos os testes:
```bash
cd backend
mvn test
```

### Executar testes com relatório de cobertura:
```bash
mvn test jacoco:report
```

### Testes Implementados:
- ✅ `CreateDocumentUseCaseTest` - Criação de documentos
- ✅ `SearchDocumentsUseCaseTest` - Busca e filtros
- ✅ `UploadVersionUseCaseTest` - Upload de versões
- ✅ `AuthenticateUserUseCaseTest` - Autenticação

## 💡 Decisões Técnicas

### 1. Clean Architecture
Optei por Clean Architecture para garantir:
- **Testabilidade**: Camadas desacopladas facilitam testes unitários
- **Manutenibilidade**: Mudanças em uma camada não afetam outras
- **Escalabilidade**: Fácil adicionar novos casos de uso
- **Independência de frameworks**: Domínio puro sem dependências externas

### 2. Monolito Modular vs Microserviços
Escolhi **monolito modular** porque:
- Domínio coeso (GED) não justifica complexidade de microserviços
- Mais rápido para desenvolver e testar
- Transações ACID simplificadas
- Fácil de evoluir para microserviços se necessário (módulos já separados)

### 3. Versionamento de Arquivos
Implementação com:
- **Imutabilidade**: Cada upload cria nova versão
- **Flag `is_current`**: Marca versão ativa
- **Histórico completo**: Todas as versões mantidas
- **Storage local**: Simples para MVP, preparado para migrar para S3/MinIO

### 4. Segurança
- **JWT stateless**: Escalável, sem sessão no servidor
- **BCrypt**: Hash seguro de senhas
- **CORS configurado**: Apenas origens permitidas
- **Validação em camadas**: Bean Validation + validações de domínio

### 5. Banco de Dados
PostgreSQL escolhido por:
- **Arrays nativos**: Suporte a `tags[]`
- **Robustez**: Mais confiável que MySQL para este caso
- **ACID completo**: Transações consistentes
- **Performance**: Índices otimizados para buscas

### 6. Migrations
Flyway para:
- **Versionamento de schema**: Rastreável via Git
- **Idempotência**: Seguro executar múltiplas vezes
- **Seed data**: Usuários iniciais já criados

## 📁 Estrutura do Projeto

```
UDS_MVP/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/ged/
│   │   │   │   ├── domain/
│   │   │   │   ├── application/
│   │   │   │   ├── infrastructure/
│   │   │   │   └── presentation/
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── core/
│   │   │   ├── features/
│   │   │   └── shared/
│   │   ├── environments/
│   │   └── styles.scss
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── package.json
│   └── angular.json
├── docker-compose.yml
├── .github/
│   └── workflows/
│       └── ci.yml
└── README.md
```

## 🔄 CI/CD

O projeto possui pipeline GitHub Actions que:
1. ✅ Compila o código
2. ✅ Executa testes unitários
3. ✅ Gera artefato JAR
4. ✅ Valida build Docker
5. ✅ Valida docker-compose.yml

## 📊 Modelo de Dados

### Users
- `id` (PK)
- `username` (UNIQUE)
- `password` (BCrypt)
- `role` (ADMIN/USER)
- `created_at`

### Documents
- `id` (PK)
- `title`
- `description`
- `owner_id` (FK → users)
- `status` (DRAFT/PUBLISHED/ARCHIVED)
- `created_at`
- `updated_at`

### Document_Tags
- `document_id` (FK → documents)
- `tag`

### Document_Versions
- `id` (PK)
- `document_id` (FK → documents)
- `version_number` (UNIQUE com document_id)
- `file_key`
- `file_name`
- `file_size`
- `content_type`
- `uploaded_at`
- `uploaded_by` (FK → users)
- `is_current`

## 🚧 Limitações Conhecidas

1. **Storage local**: Produção deveria usar S3/MinIO
2. **Sem paginação em versões**: Lista todas as versões
3. **Sem soft delete**: DELETE é permanente (exceto ARCHIVED)
4. **Sem criação de documentos no frontend**: Apenas visualização e upload de versões

## 📝 Próximos Passos

- [ ] Adicionar testes de integração (backend)
- [ ] Adicionar testes unitários (frontend)
- [ ] Migrar storage para S3/MinIO
- [ ] Implementar soft delete
- [ ] Adicionar logs estruturados
- [ ] Implementar rate limiting
- [ ] Adicionar métricas (Prometheus)
- [ ] Implementar busca avançada com Elasticsearch

## 👤 Autor

Desenvolvido como teste técnico para vaga de Desenvolvedor Java Sênior (Fullstack).