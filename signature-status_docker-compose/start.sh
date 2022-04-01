docker-compose up -d --build
response=$(curl -X GET "http://localhost:8095/api/signature-status/swagger-ui/index.html?configUrl=/api/signature-status/v3/api-docs/swagger-config&filter=true" --write-out '%{http_code}' --silent --output /dev/null servername)
while [ "$response" != "200000" ];
do
  sleep 10
  response=$(curl -X GET "http://localhost:8095/api/signature-status/swagger-ui/index.html?configUrl=/api/signature-status/v3/api-docs/swagger-config&filter=true" --write-out '%{http_code}' --silent --output /dev/null servername)
done
curl -X POST "localhost:8095/api/signature-status/message" -H  "accept: */*" -H  "Content-Type: multipart/form-data" -F "file=@notifications.csv;type=text/csv"
start http://localhost:8095/api/signature-status/swagger-ui/index.html?configUrl=/api/signature-status/v3/api-docs/swagger-config#/Event%20Histories/findAll_2