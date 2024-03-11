FROM node:20-bookworm-slim

WORKDIR /usr/src/app

COPY package.json .
COPY vite.config.js .
COPY .eslintrc.js .
COPY babel.config.js .

RUN ["npm", "install", "--legacy-peer-deps"]

COPY index.html .
COPY jsconfig.json .

#RUN npm install -g @vue/cli

EXPOSE 8001
LABEL authors="wld"

ENTRYPOINT ["npm", "run"]