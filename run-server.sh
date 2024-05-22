sudo docker network create fira-network
sudo docker network connect fira-network fira-db
sudo docker build -t fira-app .
sudo docker run --name fira-app  --net fira-network -e MYSQL_HOST=fira-db -e MYSQL_USER=root -e MYSQL_PASSWORD=13122002 -e MYSQL_PORT=3309 fira-app