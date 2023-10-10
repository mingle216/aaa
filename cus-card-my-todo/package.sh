#!/bin/bash
cd ./src/main/web && yarn && yarn build && cd ../../.. && cd ./src/main/mobile && yarn && yarn build && cd ../../.. &&  mvn clean install && /bin/cp ./target/cus-card-xi-dian-my-todo.jar .