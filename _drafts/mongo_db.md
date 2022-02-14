

docker network create mongoCluster
docker volume create mongo1
docker volume create mongo2
docker volume create mongo3

docker kill $(docker ps -q)

docker run -d --rm -p 27017:27017 --name mongo1 --mount source=mongo1,target=/data/db --network mongoCluster mongo mongod --replSet myReplicaSet --bind_ip localhost,mongo1

docker run -d --rm -p 27018:27017 --name mongo2 --network mongoCluster mongo:5 mongod --replSet myReplicaSet --bind_ip localhost,mongo2

docker run -d --rm -p 27019:27017 --name mongo3 --network mongoCluster mongo:5 mongod --replSet myReplicaSet --bind_ip localhost,mongo3

docker exec -it mongo1 mongosh --eval "rs.status()"

docker exec -it mongo1 mongosh --eval "rs.initiate({
 _id: \"myReplicaSet\",
 members: [
   {_id: 0, host: \"mongo1\"},
   {_id: 1, host: \"mongo2\"},
   {_id: 2, host: \"mongo3\"}
 ]
})"

docker exec -it mongo1 mongo

db.Canard.insert({"nom":"Scrouge"})
db.Canard.insert({"nom":"Donald"})
db.Canard.insert({"nom":"Della"})
db.Canard.insert({"nom":"Huey"})
db.Canard.insert({"nom":"Louis"})
db.Canard.insert({"nom":"Dewey"})
db.Canard.find()

docker kill mongo1

docker exec -it mongo2 mongo
db.Canard.find()

docker kill $(docker ps -q)

docker run -d --rm -p 27017:27017 --name mongo1 --mount source=mongo1,target=/data/db --network mongoCluster mongo mongod --replSet myReplicaSet --bind_ip localhost,mongo1

docker run -d --rm -p 27018:27017 --name mongo2 --network mongoCluster mongo:5 mongod --replSet myReplicaSet --bind_ip localhost,mongo2
docker run -d --rm -p 27019:27017 --name mongo3 --network mongoCluster mongo:5 mongod --replSet myReplicaSet --bind_ip localhost,mongo3

docker exec -it mongo1 mongosh --eval "rs.initiate()"

$ docker run --network _drafts_default -e ME_CONFIG_MONGODB_SERVER=mongo1 -p 8081:8081 mongo-express