init the files and cluster 

./tigerbeetle format --cluster=0 --replica=0 --replica-count=1 0_0.tigerbeetle

./tigerbeetle start --addresses=3000 0_0.tigerbeetle


# run mvn package to build the tigerbeetle-0.0.1-SNAPSHOT.jar
mvn package

cd target 

# start the app with, ie 
/usr/lib/jvm/java-17-amazon-corretto/bin/java -jar tigerbeetle-0.0.1-SNAPSHOT.jar


# make large file of customer accounts and create them 100K accounts

for ((i=999998000001; i<=999998100000; i++)); do echo "$i $(uuidgen)"; done > numbers_and_uuids.txt

cat numbers_and_uuids.txt | awk '{printf "{\"accountId\":\""$1"\",\"customerId\":\""$2"\",\"accountCode\":\"1000_BANK\",\"ledger\":\"840_CURRENCY_USD\",\"accountName\":\"customer "$1"\",\"accountFlagList\": [\"HISTORY\"]}"} {printf "%s\0", $0}' | xargs --null -P 20 -I {}  curl --location "http://localhost:8080/accounts" --header "Content-Type: application/json" --data "{}"


merchants
for ((i=111111000001; i<=111111010000; i++)); do echo "$i $(uuidgen)"; done > merchants.txt

cat merchants.txt | awk '{printf "{\"accountId\":\""$1"\",\"customerId\":\""$2"\",\"accountCode\":\"1000_BANK\",\"ledger\":\"840_CURRENCY_USD\",\"accountName\":\"merchant "$1"\",\"accountFlagList\": [\"HISTORY\"]}"} {printf "%s\0", $0}' | xargs --null -P 20 -I {}  curl --location "http://localhost:8080/accounts" --header "Content-Type: application/json" --data "{}"
