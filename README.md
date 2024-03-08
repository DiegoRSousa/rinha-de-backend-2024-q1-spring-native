# Implementação com spring-native para a Rinha de [Backend - 2024/Q1](https://github.com/zanfranceschi/rinha-de-backend-2024-q1)

## Inicializando a aplicação

docker compose up -d
./mvnw spring-boot run

Testando a aplicação 
curl http://localhost:9999/clientes/2/extrato


## Gerando a imagem nativa
./mvnw -Pnative native:compile

Executando a imagem nativa

./target/rinha-de-backend-2024-q1-spring-native 
