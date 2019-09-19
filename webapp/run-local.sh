#!/usr/bin/env bash

export PORT=4200
export AUTH_URL="https://sso73.apps.arekkusu.io/auth"
export AUTH_REALM="3scale-api"
export AUTH_CLIENT_ID="8f314bfa"
export KEYCLOAK="true"

export INTEGRATION_URI="https://auth-integration-api-production.apps.arekkusu.io"
export INTEGRATION_HEALTH_URI="https://auth-integration-metrics-api-microservices-security.apps.arekkusu.io"
export PRODUCT_PATH="/product"
export STOCK_PATH="/stock"
export SUPPLIER_PATH="/supplier"

npm run start
