# README.md
# ENSA Investor Portal — Backend API

Backend API para o Portal do Investidor da **ENSA – Empresa Nacional de Seguros de Angola**.

## 🚀 Tecnologias

* **Java 21 / Spring Boot 3.2**
* **Security:** Spring Security + JWT (JJWT) + AES-256 (GCM)
* **Database:** Oracle 19c+ via Spring Data JPA / Hibernate
* **Migrations:** Flyway
* **Cache/Sessions:** Redis 7 via Spring Session e Spring Cache
* **Infrastructure:** Docker, Kubernetes (K8s), NGINX Load Balancer

## 📂 Estrutura e Camadas

A arquitetura baseia-se num **microserviço monolítico modular**, configurado para ser replicado em **Kubernetes**.

1. **Camada de Apresentação (REST Controller):**
   - Endpoints sob `/api/v1` documentados em `/swagger-ui.html`.
2. **Camada de Serviço:**
   - Lógica de negócio, validações e encriptação AES.
   - Comunicação assíncrona com SMTP para e-mails institucionais (Templates Thymeleaf).
3. **Camada de Prevenção/Segurança:**
   - Filtros JWT interceptam autenticação e concedem acesso baseado em *Roles* (`ADMIN`, `INVESTOR`, `MANAGER`).
4. **Camada de Dados (DB e Cache):**
   - **Oracle DB:** Dados sensíveis, perfis, KYC, transações.
   - **Redis:** Alojamento otimizado para *Sessions* dos utentes logados, e cache agressiva de `Static Data` (`/api/v1/static-data`) requerida constantemente pelo frontend (Províncias, Documentos, Moedas angolanas).

## 🐳 Instruções de Execução (Local DEV)

Execute o ambiente completo localmente usando Docker Compose:

```bash
cd docker
docker-compose up -d
```
Isto iniciará:
* Oracle XE Database
* Redis
* Spring Boot App (na porta 8080)

Após o arranque, aceda:
* API Docs: http://localhost:8080/api/v1/swagger-ui.html

## 📜 Static Data & Tabela Mestra Front-end

Foi implementada uma tabela robusta `static_data_definitions` que alimenta todos os *dropdowns* e traduções no frontend:
* *Exemplo GET:* `/api/v1/static-data/category/PROVINCES` retornará: Luanda, Bengo, Benguela, etc.
* O frontend não necessita adivinhar chaves geográficas ou categorias governamentais; é totalmente consultável e *cached* em Redis por 24 horas.
