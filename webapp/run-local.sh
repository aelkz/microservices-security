#!/usr/bin/env bash

export PORT=4200
export AUTH_URL="https://secure-sso.apps.arekkusu.io/auth"
export AUTH_REALM="3scale-api"
export AUTH_CLIENT_ID="nodejs-web"
export KEYCLOAK="false"

export INTEGRATION_URI="http://localhost:8080"
export INTEGRATION_HEALTH_URI="http://localhost:8081"
export PRODUCT_PATH="/product"
export STOCK_PATH="/stock"
export SUPPLIER_PATH="/supplier"

npm run start
