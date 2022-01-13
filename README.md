# redes-ep2-typerace
Repositório para o EP2 de Redes de Computadores, EACH-USP - 2021/2

# Integrantes
* Carlos Eduardo Bertoni Pompermayer - 11795742
* Felipe Mateos Castro de Souza - 11796909
* Felipe Munhos Escobar - 11795620
* Gustavo Akashi - 11207821

## Regras

*Instrucoes:
 - Clique no botao "Novo Jogador" para abrir a
interface do usuario (Voce pode gerar quantas
quiser para testar);

 - Apos isso voce podera escolher um nome e um
servidor para se conectar (por padrao sera
ws://localhost:3004);

 - Qualquer jogador conectado ao servidor podera 
dar inicio a partida enviando a mensagem
 "start" (sem as aspas);

*Regras:
 - Assim que for dado sinal para iniciar a partida, 
sera iniciada uma contagem regressiva de
3 segundos e uma lista de palavras sera exibida na
parte superior da interface;

 - O usuario deve digitar corretamente as palavras
no menor tempo possivel, na ordem que
desejar;

 - Assim que todos os usuarios digitarem todas as
suas respectivas palavras, no mesmo compartimento
que se encontrava a lista aparecerao
os resultados da partida;



### Pré-requisitos
* JDK 11 ou maior (testado com a JDK11 OpenJDK)
* Gradle (incluso no repositório, não é necessário instalá-lo)

#### Rodando
Para rodar o servidor
```sh
./gradlew server:run
```

Para rodar um cliente
```sh
./gradlew client:run
```
