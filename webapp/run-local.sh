#!/usr/bin/env bash

export PORT=4200
export AUTH_URL="https://secure-sso.apps.arekkusu.io/auth"
export KEYCLOAK="true"

export AUTH_INTEGRATION_URI="http://localhost:8080"
export PRODUCT_CONTEXT="https://eap-app-developer-ntier.192.168.42.119.nip.io/"
export STOCK_CONTEXT="http://springboot-app-developer-ntier.192.168.42.119.nip.io/"
export SUPPLIER_CONTEXT="http://springboot-app-developer-ntier.192.168.42.119.nip.io/"

npm run start
