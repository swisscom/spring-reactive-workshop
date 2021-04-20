trap "exit" INT TERM ERR
trap "kill 0" EXIT

./gradlew bootRun -Pargs=--server.port=8080 &
./gradlew bootRun -Pargs=--server.port=8081 &
./gradlew bootRun -Pargs=--server.port=8082 &
./gradlew bootRun -Pargs=--server.port=8083 &

wait