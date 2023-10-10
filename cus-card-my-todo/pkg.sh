cd ./src/main/web && yarn && yarn build && cd ../../..
#cd ./src/main/mobile && yarn && yarn build && cd ../../..
mvn -o clean package
cp *.properties target
cd target
zip -r cus-card-cjxy-my-todo.zip *.jar *.properties