<h1>Bem-vindo a API de status de assinatura</h1>

<h2>1. Lista de portas utilizadas que precisarão estar disponíveis:</h2>

- *5672*
- *15672*
- *3210*
- *8095*

<h2>2. Abra o terminal na raíz do projeto e rode o script adequado ao seu sistema operacional:</h2>

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
<h6>Observação: *Testei a aplicação em um desktop com hdd e o RabbitMQ levou cerca de 30 minutos para ficar pronto.
No caso de ocorrer o mesmo em outras máquinas eu coloquei um elevado número de retries para o healthcheck do serviço.*</h6>

<h2>4. Seu navegador abrirá o swagger na rota em que podemos listar o histórico de eventos salvos das transações. Será
possível filtrar por tipo de evento ou id e ajustar a paginação.</h2>

<h2>5. Para fins de teste, mantive rotas de acesso disponíveis para criar assinaturas novas ou atualizar assinaturas
existentes.</h2>