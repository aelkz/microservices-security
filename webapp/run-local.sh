#!/usr/bin/env bash

export PORT=4200
export AUTH_URL="https://secure-sso.apps.arekkusu.io/auth"
export KEYCLOAK="false"

export AUTH_INTEGRATION_URI="http://localhost:8080"
export PRODUCT_PATH="/product"
export STOCK_PATH="/stock"
export SUPPLIER_PATH="/supplier"

npm run start
