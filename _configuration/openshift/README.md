### Installing a self-signed certificate into JVM CA truststore
Self-Signed certificates must be added to a custom FUSE image to avoid SSL handshake errors by Keycloak adapter.
https://medium.com/@siweheee/keycloak-a-real-scenario-from-development-to-production-ce57800e3ba9

openssl x509 -in <(openssl s_client -connect sso73.apps.<DOMAIN>:443 -prexit 2>/dev/null) -out temp/sso73.apps.<DOMAIN>.crt
keytool -import -trustcacerts -keystore /etc/pki/ca-trust/extracted/java/cacerts -storepass changeit -noprompt -alias keycloak -file /home/jboss/temp/sso73.apps.<DOMAIN>.crt

### Creating a custom FUSE docker image with the self-signed certificate

```
docker build -t "openshift/fuse-java-openshift-selfsigned" .

docker images | grep fuse

docker tag openshift/fuse-java-openshift-selfsigned:latest docker-registry.default.svc:5000/openshift/fuse-java-openshift-selfsigned:1.0
docker tag openshift/fuse-java-openshift-selfsigned:latest docker-registry.default.svc:5000/openshift/fuse-java-openshift-selfsigned:latest

docker images 

docker push docker-registry.default.svc:5000/openshift/fuse-java-openshift-selfsigned

# oc create is oracle-xe-11g -n openshift
```

