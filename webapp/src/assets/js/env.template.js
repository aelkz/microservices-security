// blank values are replaced at runtime by the set-config.js node script
(function(window) {
  window._env = window._env || {};

  window._env.url = '${AUTH_URL}';
  window._env.realm = 'microservices';
  window._env.clientId = 'nodejs-web';
  window._env.enabled = '${KEYCLOAK}';

  window._env.integration_uri = '${AUTH_INTEGRATION_URI}';
  window._env.stock_path = '${STOCK_PATH}';
  window._env.supplier_path = '${SUPPLIER_PATH}';
  window._env.product_path = '${PRODUCT_PATH}';

})(this);
