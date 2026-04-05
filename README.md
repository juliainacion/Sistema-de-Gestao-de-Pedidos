# API REST - Sistema de Gestão de Pedidos (E-commerce)

Projeto desenvolvido em Spring Boot para simular o backend de um e-commerce, aplicando:

- Programação Orientada a Objetos (POO)
- Arquitetura em 3 camadas (Controller -> Service -> Repository)
- JPA/Hibernate
- Regras de negócio reais
- Boas práticas de desenvolvimento

## Tecnologias

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Hibernate
- Bean Validation
- H2 Database
- OpenAPI/Swagger
- Maven

## Domínio

- Cliente
- Endereço
- Produto
- Pedido
- ItemPedido

## Regras implementadas

1. Não permitir clientes com emails duplicados.
2. Ao criar pedido, validar estoque e subtrair do produto.
3. O total do pedido é calculado internamente.
4. Apenas pedidos com status `CRIADO` podem ser cancelados.
5. Fluxo permitido de status: `CRIADO -> PAGO -> ENVIADO`.
   - Não pode pular etapa.
   - Não pode voltar status.

## Estrutura de pacotes

```text
controller/
service/
repository/
model/
dto/
mapper/
exception/
```

## Executar

```bash
mvn spring-boot:run
```

## URLs úteis

- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console

## Exemplos de uso

### Criar cliente
```http
POST /clientes
Content-Type: application/json

{
  "nome": "Maria Silva",
  "email": "maria@email.com"
}
```

### Criar endereço para cliente
```http
POST /clientes/1/enderecos
Content-Type: application/json

{
  "rua": "Rua 10",
  "cidade": "Goiânia",
  "cep": "74000-000"
}
```

### Criar produto
```http
POST /produtos
Content-Type: application/json

{
  "nome": "Notebook Gamer",
  "preco": 4500.00,
  "estoque": 10
}
```

### Criar pedido
```http
POST /pedidos
Content-Type: application/json

{
  "clienteId": 1,
  "enderecoId": 1,
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2
    }
  ]
}
```

### Atualizar status do pedido
```http
PATCH /pedidos/1/status
Content-Type: application/json

{
  "novoStatus": "PAGO"
}
```
