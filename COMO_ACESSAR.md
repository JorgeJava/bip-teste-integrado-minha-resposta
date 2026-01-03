# 🌐 Como Acessar e Verificar a Aplicação no Navegador

## ✅ Status Atual
- ✅ **Backend**: Rodando na porta **8080**
- ✅ **Frontend**: Rodando na porta **4200**
- ✅ **API**: Funcionando corretamente (2 benefícios já cadastrados)
- ✅ **Banco de Dados**: H2 em memória configurado

---

## 📋 URLs para Acessar no Navegador

### 1. 🎨 **Frontend (Aplicação Principal)**
**URL:** http://localhost:4200

**O que você verá:**
- Interface completa de gerenciamento de benefícios
- Lista de benefícios em tabela
- Botões para criar, editar, deletar e transferir benefícios
- Formulários modais para operações

**Como verificar se está funcionando:**
- ✅ A página carrega sem erros
- ✅ Você vê uma tabela com 2 benefícios pré-cadastrados:
  - Benefício A (R$ 1.000,00)
  - Benefício B (R$ 500,00)
- ✅ Consegue clicar nos botões e ver modais

---

### 2. 📚 **Swagger UI (Documentação da API)**
**URL:** http://localhost:8080/swagger-ui.html

**O que você verá:**
- Interface interativa do Swagger
- Lista de todos os endpoints da API
- Possibilidade de testar endpoints diretamente no navegador
- Documentação completa de cada endpoint

**Como verificar se está funcionando:**
- ✅ A página do Swagger carrega
- ✅ Você vê uma lista de endpoints:
  - `GET /api/v1/beneficios` - Listar todos
  - `GET /api/v1/beneficios/ativos` - Listar ativos
  - `GET /api/v1/beneficios/{id}` - Buscar por ID
  - `POST /api/v1/beneficios` - Criar novo
  - `PUT /api/v1/beneficios/{id}` - Atualizar
  - `DELETE /api/v1/beneficios/{id}` - Deletar
  - `POST /api/v1/beneficios/transfer` - Transferir entre benefícios
- ✅ Consegue expandir e testar os endpoints (botão "Try it out")

---

### 3. 🔧 **API REST Diretamente**
**URL:** http://localhost:8080/api/v1/beneficios

**O que você verá:**
- JSON com a lista de benefícios
- Dados no formato:
```json
[
  {
    "id": 1,
    "nome": "Beneficio A",
    "descricao": "Descrição A",
    "valor": 1000.00,
    "ativo": true,
    "version": 0
  },
  {
    "id": 2,
    "nome": "Beneficio B",
    "descricao": "Descrição B",
    "valor": 500.00,
    "ativo": true,
    "version": 0
  }
]
```

**Como verificar se está funcionando:**
- ✅ JSON válido é retornado
- ✅ Você vê 2 benefícios no array
- ✅ Sem erros no console do navegador (F12)

---

### 4. 💾 **H2 Console (Banco de Dados)**
**URL:** http://localhost:8080/h2-console

**Configurações de conexão:**
- **JDBC URL:** `jdbc:h2:mem:beneficio_db`
- **Username:** `sa`
- **Password:** (deixe vazio)

**O que você verá:**
- Console do H2 Database
- Possibilidade de executar queries SQL
- Visualizar dados diretamente no banco

**Como verificar se está funcionando:**
- ✅ Consegue conectar com as credenciais acima
- ✅ Execute: `SELECT * FROM BENEFICIO;`
- ✅ Você verá os 2 registros da tabela

---

## 🔍 Checklist de Verificação Rápida

### Frontend (http://localhost:4200)
- [ ] Página carrega sem erros (sem tela branca)
- [ ] Vê a tabela com benefícios
- [ ] Botões estão funcionais
- [ ] Consegue abrir modais
- [ ] Não há erros no console (F12 → Console)

### Backend/Swagger (http://localhost:8080/swagger-ui.html)
- [ ] Página do Swagger carrega
- [ ] Vê lista de endpoints
- [ ] Consegue expandir endpoints
- [ ] Testa GET /api/v1/beneficios e vê os dados
- [ ] Não há erros no console

### API Direta (http://localhost:8080/api/v1/beneficios)
- [ ] Retorna JSON válido
- [ ] Contém 2 benefícios
- [ ] Estrutura JSON está correta

---

## 🚨 Problemas Comuns

### "Cannot GET /" ou página em branco
- **Frontend não iniciou:** Verifique se o processo está rodando na porta 4200
- **Solução:** Reinicie o frontend com `cd frontend && npm start`

### "Connection refused" ou erro de conexão
- **Backend não está rodando:** Verifique se está na porta 8080
- **Solução:** Reinicie o backend com `cd backend-module && mvn spring-boot:run`

### Swagger não carrega ou dá 404
- **Aguardar alguns segundos:** O backend pode estar ainda iniciando
- **Verificar URL:** Deve ser exatamente `http://localhost:8080/swagger-ui.html`

### Frontend não consegue conectar ao backend
- **Verificar CORS:** Já está configurado no backend
- **Verificar se backend está rodando:** Teste http://localhost:8080/api/v1/beneficios

---

## 📝 Comandos Úteis

**Ver processos rodando:**
```bash
# Backend na porta 8080
lsof -ti:8080

# Frontend na porta 4200
lsof -ti:4200
```

**Parar serviços:**
```bash
# Parar backend
lsof -ti:8080 | xargs kill

# Parar frontend
lsof -ti:4200 | xargs kill
```

**Reiniciar serviços:**
```bash
# Backend
cd backend-module
mvn spring-boot:run

# Frontend (em outro terminal)
cd frontend
npm start
```

---

## ✅ Tudo Funcionando?

Se todos os itens acima estão funcionando, sua aplicação está **100% operacional**! 🎉

