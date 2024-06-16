sudo docker network create fira-network
sudo docker network connect fira-network fira-db
sudo docker build -t fira-app .
sudo docker run --name fira-app  --net fira-network  fira-app