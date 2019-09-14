#!/usr/bin/env bash

export PORT=4200
export AUTH_URL="https://secure-sso.apps.arekkusu.io/auth/"
export KEYCLOAK="true"

export INTEGRATION="https://eap-app-developer-ntier.192.168.42.119.nip.io/"
export PRODUCT="https://eap-app-developer-ntier.192.168.42.119.nip.io/"
export STOCK="http://springboot-app-developer-ntier.192.168.42.119.nip.io/"
export SUPPLIER="http://springboot-app-developer-ntier.192.168.42.119.nip.io/"

export EAP="https://eap-app-developer-ntier.192.168.42.119.nip.io/"
export SPRINGBOOT="http://springboot-app-developer-ntier.192.168.42.119.nip.io/"

npm run start
