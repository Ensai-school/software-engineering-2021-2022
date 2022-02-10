#!/bin/sh
docker-compose up -d
# configure our config servers replica set
docker exec -it mongo1 mongosh --eval "rs.initiate({
 _id: \"myReplicaSet\",
 members: [
   {_id: 0, host: \"mongo1\", priority: 3},
   {_id: 1, host: \"mongo2\", priority: 1},
   {_id: 2, host: \"mongo3\", priority: 1}
 ]
})"

docker run --name mongo-express --network _drafts_default -e ME_CONFIG_MONGODB_SERVER=mongo1 -p 8081:8081 mongo-express