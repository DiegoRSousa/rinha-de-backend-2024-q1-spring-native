FROM ubuntu:latest
WORKDIR /app
COPY /target/rinha-de-backend-2024-q1-spring-native rinha
ENTRYPOINT ["/app/rinha"]