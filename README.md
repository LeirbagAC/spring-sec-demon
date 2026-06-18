# Spring Boot Security - JWT Authentication Architecture (spring-sec-demon)

Este é um projeto laboratório desenvolvido em **Java 21** e **Spring Boot** focado na implementação detalhada de uma arquitetura de segurança stateless corporativa. O objetivo principal deste repositório é consolidar os fundamentos do controle de acesso, criptografia de dados confidenciais e autenticação baseada em tokens **JWT (JSON Web Tokens)**, eliminando o uso de sessões tradicionais de servidor.

---

## 🚀 Funcionalidades e Fluxos de Segurança

### 🔑 Autenticação Baseada em Token (JWT)
* **Emissão Estrita de Tokens:** No processo de autenticação via `/login`, o sistema verifica as credenciais informadas contra a base persistida através do `AuthenticationManager`. Se validadas, é gerado um token com tempo de expiração curto (3 minutos) assinado digitalmente com o algoritmo HMAC-SHA256.
* **Intercepção Personalizada (Custom Filter Layer):** Criação de um filtro customizado (`JwtFilter`) que estende `OncePerRequestFilter`. Ele atua capturando todas as chamadas recebidas pela aplicação, decodificando o header `Authorization: Bearer <token>` e extraindo os dados do usuário (*claims/subject*).
* **Reinjeção no Contexto do Framework:** Caso o token seja íntegro e autêntico, a identidade do usuário é acoplada dinamicamente ao `SecurityContextHolder` do próprio Spring Security. Isso permite que os filtros subsequentes reconheçam a autoridade do cliente sem a necessidade de reautenticação manual por credenciais brutas.

### 🔒 Persistência e Proteção de Dados
* **Hashes Criptográficos com BCrypt:** Registro de novos usuários (`/register`) que trata a senha via `BCryptPasswordEncoder` com custo de força estruturado em 12 ciclos, bloqueando ataques de força bruta no banco.
* **Custom UserDetailsService:** Integração nativa com o ecossistema JPA (`UserRepo`) para interceptar o login e carregar os privilégios do usuário a partir do modelo físico relacional.

---

## 🛠️ Tecnologias e Ferramentas Utilizadas

* **Linguagem:** Java 21
* **Framework Principal:** Spring Boot (v4.0.6)
* **Segurança:** Spring Security
* **Mecanismo JWT:** Java JWT - JJWT (Bibliotecas `jjwt-api`, `jjwt-impl` e `jjwt-jackson`)
* **ORM e Persistência:** Spring Data JPA / Hibernate
* **Banco de Dados:** MySQL
* **Utilitários:** Lombok & Maven Wrapper

---

## 🧠 Detalhes Importantes de Implementação

1. **Sessão Stateless:** A API foi configurada explicitamente com a política `SessionCreationPolicy.STATELESS`. Isso instrui o Spring Security a nunca gerar ou gerenciar um `HttpSession` em memória, forçando que cada requisição traga sua própria comprovação de identidade (o token).
2. **Injeção Estratégica do Filtro:** Utilizando o método `.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)`, garantimos que o token JWT seja processado e validado **antes** do filtro de autenticação padrão por formulário do Spring entrar em ação.
3. **Isolamento de Segredos:** A assinatura secreta do token foi centralizada de forma física na classe `JwtService` puramente para simplificação e documentação acadêmica do código. Em ambientes corporativos de produção, essa chave deve ser desacoplada do código-fonte e extraída para variáveis de ambiente protegidas do sistema operacional.

---

## 📋 Endpoints e Mapeamento de Rotas

| Método | Endpoint | Descrição | Controle de Acesso |
| :--- | :--- | :--- | :--- |
| `POST` | `/register` | Registra e gera o hash da senha de um usuário | 🔓 Público |
| `POST` | `/login` | Autentica credenciais e gera o Token JWT | 🔓 Público |
| `GET` | `/students` | Retorna lista mockada de estudantes | 🔒 Requer JWT Válido |
| `POST` | `/students` | Insere um novo estudante na memória | 🔒 Requer JWT Válido |
| `GET` | `/hello` | Retorna string de teste "Hello World" | 🔒 Requer JWT Válido |
| `GET` | `/csrf-token` | Expõe o token CSRF gerado em escopo de servlet | 🔒 Requer JWT Válido |

---

## 📦 Como Executar Localmente

### 1. Preparação da Base Relacional
Crie um schema no MySQL local. No seu arquivo `src/main/resources/application.properties`, insira as suas credenciais de acesso locais:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/Seu_Banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
