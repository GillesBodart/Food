cd logs
del *
cd ..
mvn package
java -jar target/Food-1.0.jar server config.yml 2> logs\error.log