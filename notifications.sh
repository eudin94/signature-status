curl -X POST "$SERVER_SPRING_URL$SERVER_SERVLET_CONTEXT_PATH/message" -H  "accept: */*" -H  "Content-Type: multipart/form-data" -F "file=@notifications.csv;type=text/csv"