# Desafio Full Stack — Notas

Sistema de cadastro e consulta de notas de monitoramento. O frontend é uma SPA que consome a API REST em `/api/v1`.

| Aplicação | Endereço local |
|-----------|----------------|
| Frontend  | http://localhost:5173 |
| Backend   | http://localhost:8080 |

## Arquitetura

```text
frontend (React)  --HTTP-->  backend (Spring Boot)  --JPA-->  MySQL
```

O frontend fala com o backend pela variável `VITE_API_URL` (`http://localhost:8080/api/v1/`). O backend libera CORS para `http://localhost:5173`.

### Backend

API REST em Java, organizada em camadas dentro de `backend/src/main/java/com/microblau/desafio/backend`:

- **controller** — `NoteController` expõe `/api/v1/notes`. Os contratos de entrada e saída são records (`CreateNoteDTO`, `UpdateNoteDTO`, `NoteDTO`), validados com `jakarta.validation`.
- **service** — `INoteService` / `NoteServiceImpl` concentram as regras (paginação, filtros e validação de intervalo de datas).
- **repository** — Spring Data JPA. A listagem filtra por site, equipamento e período.
- **model** — entidade `Note`, mapeada para a tabela `notes`.

O Flyway aplica a migração que cria a tabela. Na subida, se o banco estiver vazio, um seed lê `notes.csv` com OpenCSV e popula as notas. Erros de API passam por `RestExceptionHandler`: validação e data inválida retornam 400; nota inexistente retorna 404.

**Stack:** Java 21, Spring Boot 4.1.1 (Web, Data JPA, Validation), MySQL, Flyway, SpringDoc OpenAPI 3.1.0, OpenCSV, Maven.

### Frontend

SPA de uma tela, em `frontend/src`:

- **features/notes** — página de notas, cliente da API, hooks do TanStack Query, tabela, filtros e modais de criar, editar e excluir.
- **shared** — cliente HTTP com `fetch` e componentes de interface (botão, modal, select, paginação, sidebar).

A tela lista notas com paginação e permite filtrar por site, equipamento e período. A página exibida começa em 1; o cliente envia `page - 1` para a API, que usa a paginação do Spring (índice 0).

**Stack:** React 19, TypeScript, Vite, Tailwind CSS 4, TanStack Query, TanStack Table, Zod, Sonner, Lucide, pnpm.

## Endpoints

Base: `http://localhost:8080/api/v1/notes`

A listagem aceita os parâmetros de página do Spring: `page` (começa em 0), `size` e `sort`.

| Método | Caminho | Descrição |
|--------|---------|-----------|
| `GET` | `/api/v1/notes` | Lista paginada. Query opcional: `site`, `equipment`, `startDate`, `endDate` (ISO 8601). |
| `GET` | `/api/v1/notes/{id}` | Recupera uma nota. 200 ou 404. |
| `POST` | `/api/v1/notes` | Cria uma nota. 201. Corpo: `site`, `equipment`, `variable`, `author`, `message`. |
| `PUT` | `/api/v1/notes/{id}` | Atualiza uma nota. 200, 400 ou 404. Corpo: `site`, `equipment`, `variable`, `message`. |
| `DELETE` | `/api/v1/notes/{id}` | Remove uma nota. 204 ou 404. |

Documentação interativa (Swagger UI), com o backend em execução:

http://localhost:8080/swagger-ui/index.html#/

## Como executar

MySQL local com o schema `db_notes`. As credenciais ficam em `backend/config.env`: `SPRING_DATABASE_URL`, `SPRING_DATABASE_USERNAME` e `SPRING_DATABASE_PASSWORD`.

```bash
cd backend
./mvnw spring-boot:run
```

```bash
cd frontend
pnpm install
pnpm dev
```

O frontend lê `VITE_API_URL` do arquivo `.env` (valor local: `http://localhost:8080/api/v1/`).

## Desafios

Apesar do escopo do projeto ser pequeno, houveram algumas decisões de modelagem e arquitetura interessantes e desafiadoras. Listarei aqui abaixo alguns exemplos:

### Tempo

O primeiro desafio, foi por uma questão pessoal minha. O meu tempo foi um pouco limitado esta semana com a rotina do serviço atual e pelo fato da minha filhinha tendo ficado doente no início desta semana. Porém, a solução foi fazer o projeto todo nas madrugadas dos dias 08/10 e 09/10. Apesar de ser um desafio pessoal e não relacionado ao projeto, acho que vale a pena mencionar. 

### Uso de Enums?

O primeiro desafio foi decidir se usaria ou não Enums no meu projeto. Os campos `variable` e `Equipment` eram fortes candidatos (principalmente `variable`) por se repetirem bastante na tabela e pouca variedade. Isso também poderia indicar a necessidade de criação de uma entidade própria para que estes valores fossem "gerenciados" independente de suas notas. Porém, optei por seguir com campos como String por 2 motivos:
1 - Nada me garante que não há mais possibilidades para os 2 campos e que apenas não apareceram na seed inicial
2 - Hoje, a seed trabalha com os campos em String. Uma mudança dessa exigiria uma migração. É possível e viável, mas para um momento futuro do projeto

### Uso de recursos nativos do Java para leitura do CSV e mapeamento de entidade

Já tive algumas vezes a experiência de ler arquivos csv para mapeamento de entidades. Porém, depois de pensar com calma, escolhi mudar o uso para uma biblioteca chamada OpenCSV. O resultado foi muito bom e pude terminar minha implementação do script de seed depois da mudança.

### Uso de ícones

Apesar de os ícones fazerem parte do documento, tive alguns problemas para integrá-los e já estava na madrugada do dia 09/10. Para não perder tempo, fiz o uso da biblioteca lucide-react, já que estou mais acostumado. A ideia era terminar a implementação utilizando os ícones fornecidos. Porém, não tive tempo para esta correção.

### Uso de IA para a parte visual

Como podem ver, a parte visual do projeto, em termos de código, não ficou muito extenso, porém não tive o tempo para fazer esta parte "na mão". O bom é que toda a parte de integração com a API eu fiz 100%, o que me ajudou a ter prompts mais bem orientado para a IA. Além disso, se eu garanto que a integração API/FrontEnd está bem feita, qualquer outro problema que venha a aparecer, tenho a certeza de que é apenas visual.

### Uso de IA para escrita final dos testes

Os testes unitários feitos no Backend foram escritos uma parte por mim, e outra com auxílio da IA. Apesar de ser um processo simples, é muito repetitivo. E tempo é algo que eu não tive de sobra, portanto optei por finalizar a escrita com IA.

### Finalização das implementações extras

Infelizmente não tive tempo hábil para a implementação do Docker e nem do download da lista em algum formato. 
