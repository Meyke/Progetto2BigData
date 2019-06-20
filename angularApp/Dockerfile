# base image
FROM node:12.2.0

# install chrome for protractor tests
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list'
RUN apt-get update && apt-get install -yq google-chrome-stable

WORKDIR ./root/angularApp

EXPOSE 8000
EXPOSE 4200
# add app
COPY . /root/angularApp

RUN npm install && npm install -g @angular/cli@7.3.9

CMD ng serve --host 0.0.0.0
