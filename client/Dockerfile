FROM node:erbium-alpine3.9

COPY . /usr/kalah/client

#RUN apt-get update && apt-get upgrade -yyq &&  sudo apt install yarn --yyq

WORKDIR /usr/kalah/client

ENTRYPOINT ["yarn", "serve"]
