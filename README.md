# 📈 Projeto Finance - API de Gestão de Investimentos

API RESTful desenvolvida em **Java 21 com Spring Boot** para o gerenciamento de investidores, carteiras de investimentos, ativos financeiros, registro de transações (compra/venda) e acompanhamento de cotações em tempo real/históricas através da integração com a **HG Brasil Finance API**.

---

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3.x (Spring Web, Spring Data JPA)
* **Banco de Dados:** MySQL 8.0 (compatível também com PostgreSQL via JPA/Hibernate)
* **Documentação da API:** Springdoc OpenAPI / Swagger UI
* **Integração Externa:** HG Brasil Finance API
* **Gerenciador de Dependências:** Maven (com Maven Wrapper incluído)

---

## 📂 Estrutura do Projeto

O projeto segue a arquitetura em camadas e está organizado no pacote `dev2.projeto_semestre`:

```text
back-finance/projeto-finance/
├── pom.xml
└── src/main/
    ├── java/dev2/projeto_semestre/
    │   ├── controller/       # Endpoints REST (AtivoFinanceiro, Carteira, Investidor, Transacao)
    │   ├── dto/              # Objetos de Transferência de Dados (Request, Response e Resumo)
    │   ├── exceptions/       # Exceções personalizadas e GlobalExceptionHandler
    │   ├── model/            # Entidades JPA (AtivoFinanceiro, Carteira, CotacaoHistorica, Investidor, Transacao)
    │   ├── repository/       # Interfaces de acesso ao banco de dados (Spring Data JPA)
    │   ├── service/          # Regras de negócio e integração externa (HgBrasilApiService)
    │   └── ProjetoSemestreApplication.java
    └── resources/
        └── application.properties
```

---

## ⚙️ Pré-requisitos

* **JDK 21** instalado.
* **MySQL 8.0** instalado localmente ou rodando via **Docker**.
* Uma chave de API válida da **[HG Brasil Finance](https://hgbrasil.com/status/finance)**.

---

## 🔒 Configuração de Segurança e Variáveis de Ambiente

Por segurança, nenhuma credencial fica salva diretamente no código-fonte. Antes de executar a aplicação, configure as variáveis de ambiente exigidas pelo `application.properties`:

| Variável | Descrição | Exemplo |
| :--- | :--- | :--- |
| `DB_PASSWORD` | Senha do usuário do banco de dados MySQL | `minha_senha_segura` |
| `HG_API_KEY` | Chave de autenticação da API HG Brasil Finance | `sua_chave_hg_brasil` |

*(Opcional)* Caso utilize um banco MySQL hospedado em nuvem ou com usuário/URL personalizados, você também pode parametrizar `DB_URL` e `DB_USERNAME`.

---

## 🗄️ Subindo o Banco de Dados (MySQL via Docker)

Caso não tenha o MySQL instalado localmente, você pode subir um container isolado e seguro em `localhost:3306` com o comando:

```bash
docker run --name mysql-finance \
  -e MYSQL_ROOT_PASSWORD=sua_senha_aqui \
  -e MYSQL_DATABASE=db_finance \
  -p 3306:3306 \
  -d mysql:8.0
```

> **Nota:** Como o projeto utiliza `spring.jpa.hibernate.ddl-auto=update`, todas as tabelas (`tb_investidor`, `tb_carteira`, `tb_ativo_financeiro`, `tb_transacao` e `tb_cotacao_historica`) serão criadas automaticamente na primeira execução.

---

## ▶️ Como Executar o Projeto

1. **Clone o repositório e acesse a pasta do projeto Maven:**
   ```bash
   git clone https://github.com/bragatto-tec/finance-back.git
   cd finance-back/back-finance/projeto-finance
   ```

2. **Exporte as variáveis de ambiente no terminal:**

   * **Linux / macOS / WSL / Git Bash:**
     ```bash
     export DB_PASSWORD="sua_senha_aqui"
     export HG_API_KEY="sua_chave_hg_brasil"
     ```

   * **Windows (PowerShell):**
     ```powershell
     $env:DB_PASSWORD="sua_senha_aqui"
     $env:HG_API_KEY="sua_chave_hg_brasil"
     ```

3. **Inicie a aplicação com o Maven Wrapper:**

   * **Linux / macOS / WSL:**
     ```bash
     ./mvnw spring-boot:run
     ```

   * **Windows:**
     ```cmd
     mvnw.cmd spring-boot:run
     ```

4. **Acesse a API e a Documentação Interativa:**
   * **Base URL:** `http://localhost:8080`
   * **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`

---

## 🛠️ Endpoints Principais

### 👤 Investidores (`/api/investidores`)
* `POST /api/investidores` — Cadastra um novo investidor.
* `GET /api/investidores` — Lista todos os investidores cadastrados.
* `GET /api/investidores/{id}` — Busca um investidor pelo ID.
* `PUT /api/investidores/{id}` — Atualiza os dados de um investidor.
* `DELETE /api/investidores/{id}` — Remove um investidor.

### 💼 Carteiras (`/api/carteiras`)
* `POST /api/carteiras` — Cria uma nova carteira vinculada a um investidor.
* `GET /api/carteiras` — Lista todas as carteiras.
* `GET /api/carteiras/{id}` — Consulta os detalhes de uma carteira específica.
* `GET /api/carteiras/{id}/resumo` — Retorna o resumo consolidado da carteira (patrimônio, rentabilidade e posição por ativo).
* `DELETE /api/carteiras/{id}` — Remove uma carteira.

### 📊 Ativos Financeiros (`/api/ativos`)
* `POST /api/ativos` — Cadastra um ativo (ex.: `PETR4`, `VALE3`) buscando a cotação atualizada via HG Brasil.
* `GET /api/ativos` — Lista todos os ativos financeiros cadastrados.
* `GET /api/ativos/{id}` — Consulta um ativo e seu histórico de cotações.

### 💸 Transações (`/api/transacoes`)
* `POST /api/transacoes` — Registra uma operação de compra ou venda em uma carteira.
* `GET /api/transacoes` — Lista todas as transações registradas.
* `GET /api/transacoes/carteira/{carteiraId}` — Lista o histórico de transações de uma carteira específica.

---

## 🛡️ Tratamento de Erros

A API conta com um `GlobalExceptionHandler` centralizado que padroniza as respostas de erro HTTP:
* **`400 Bad Request` (`BadRequestException`):** Dados inválidos ou regras de negócio violadas (ex.: saldo/quantidade insuficiente para venda).
* **`404 Not Found` (`NotFoundException`):** Recurso (investidor, carteira ou ativo) não encontrado.
* **`502 Bad Gateway / Service Error` (`ExternalServiceException`):** Falha de comunicação ou ticker inexistente na API externa da HG Brasil.

---

## 🧪 Testes

Para executar a suíte de testes automatizados:

```bash
./mvnw test
```
