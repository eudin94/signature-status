<h1>Bem-vindo a API de status de assinatura</h1>

<h2>1. Lista de portas utilizadas que precisarão estar disponíveis:</h2>

- *5672*
- *15672*
- *3210*
- *8095*

<h2>2. Abra um terminal bash na raíz do projeto e rode o script adequado ao seu sistema operacional:</h2>

<h3>Windows:</h3>
```
bash start-win.sh
```

<h3>Mac:</h3>
```
bash start-mac.sh
```

<h3>Ubuntu:</h3>
```
bash start-ubuntu.sh
```

<h2>3. Aguarde alguns momentos. O docker irá iniciar a aplicação quando o RabbitMQ estiver pronto e a chamada para a API
só ira rodar quando a rota estiver disponível.</h2>
<h6>Observação: *Testei a aplicação em um desktop com hdd e o RabbitMQ levou cerca de 40 minutos para ficar pronto.
No caso de ocorrer o mesmo em outras máquinas eu coloquei um elevado número de retries para o healthcheck do serviço.
Se o mesmo erro ocorrer e as retries não foram suficientes, a aplicação pode ser executada pela branch develop quando o
serviço de mensageria estiver pronto.*</h6>

<h2>4. Seu navegador abrirá o swagger na rota em que podemos listar o histórico de eventos salvos das transações. Será
possível filtrar por tipo de evento ou id e ajustar a paginação.</h2>

<h2>5. Para fins de teste, mantive rotas de acesso disponíveis para criar assinaturas novas ou atualizar assinaturas
existentes.</h2>

<h2>Informações Úteis:</h2>

- __Link do Swagger:__ [http://localhost:8095/api/signature-status/swagger-ui/](http://localhost:8095/api/signature-status/swagger-ui/)
  (*Por ser um link local pode ser necessário copiar e colar*)
- Na branch master deixei apenas os arquivos necessários para rodar a aplicação de forma automatizada
- Na branch develop podemos encontrar a aplicação desenvolvida por inteiro, foi a partir desta branch que gerei a imagem
no [DockerHub](https://hub.docker.com/repository/docker/eduardomc94/signature-status)
- O arquivo docker-compose de develop não conta com a imagem da própria aplicação, possui apenas Postgres e RabbitMQ
- A diferença entre os scripts dos diferentes sistemas operacionais é apenas para abrir o navegador automaticamente