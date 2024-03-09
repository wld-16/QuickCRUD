FROM node:20-alpine
COPY dev-entrypoint.sh .

WORKDIR /usr/src/app

COPY package.json .
COPY vite.config.js .
COPY .eslintrc.js .
COPY babel.config.js .

RUN ["npm", "install"]

COPY index.html .
COPY jsconfig.json .
COPY dev-entrypoint.sh .

#RUN npm install -g @vue/cli

EXPOSE 8001
LABEL authors="wld"

ENTRYPOINT ["npm", "run"]