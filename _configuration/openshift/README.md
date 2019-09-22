Self-Signed certificates must be added to a custom FUSE image to avoid SSL handshake errors by Keycloak adapter.
https://medium.com/@siweheee/keycloak-a-real-scenario-from-development-to-production-ce57800e3ba9

openssl x509 -in <(openssl s_client -connect sso73.apps.<DOMAIN>:443 -prexit 2>/dev/null) -out temp/sso73.apps.<DOMAIN>.crt
keytool -import -trustcacerts -keystore /etc/pki/ca-trust/extracted/java/cacerts -storepass changeit -noprompt -alias keycloak -file /home/jboss/temp/sso73.apps.<DOMAIN>.crt
