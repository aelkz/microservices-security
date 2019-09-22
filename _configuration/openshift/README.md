### Installing a self-signed certificate into JVM CA truststore
Self-Signed certificates must be added to a custom FUSE image to avoid SSL handshake errors by Keycloak adapter.
https://medium.com/@siweheee/keycloak-a-real-scenario-from-development-to-production-ce57800e3ba9

```
openssl x509 -in <(openssl s_client -connect sso73.apps.<DOMAIN>:443 -prexit 2>/dev/null) -out temp/sso73.apps.<DOMAIN>.crt
keytool -import -trustcacerts -keystore /etc/pki/ca-trust/extracted/java/cacerts -storepass changeit -noprompt -alias keycloak -file /home/jboss/temp/sso73.apps.<DOMAIN>.crt
```

openssl pkcs12 -export -out pkcskeystore.pkcs12 -in self-signed-cert.pem -inkey privkey.pem
keytool -importkeystore -destkeystore keystore.jks -srckeystore pkcskeystore.pkcs12 -srcstoretype PKCS12


### Creating a custom FUSE docker image with the self-signed certificate

Access your Openshift** instance and execute the following:
```
oc login -u admin -p <ADMIN-PASSWORD>

docker login -u openshift -p `oc whoami -t` docker-registry.default.svc:5000

docker build -t "openshift/fuse7-java-openshift-selfsigned" .

docker images | grep fuse
# docker rmi $(sudo docker images --filter "dangling=true" -q --no-trunc)

docker tag openshift/fuse7-java-openshift-selfsigned:latest docker-registry.default.svc:5000/openshift/fuse7-java-openshift-selfsigned:1.0
docker tag openshift/fuse7-java-openshift-selfsigned:latest docker-registry.default.svc:5000/openshift/fuse7-java-openshift-selfsigned:latest

docker images | grep fuse

docker push docker-registry.default.svc:5000/openshift/fuse7-java-openshift-selfsigned
```

