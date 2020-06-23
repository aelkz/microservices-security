### Installing a self-signed certificate into JVM CA truststore
Self-Signed certificates must be added to a custom FUSE image to avoid SSL handshake errors by Keycloak adapter.
https://medium.com/@siweheee/keycloak-a-real-scenario-from-development-to-production-ce57800e3ba9

```
# step1
echo | openssl s_client -showcerts -servername ${RHSSO_URI} -connect ${RHSSO_URI}:443 2>/dev/null | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > self-signed-cert.pem
# Validate the connection first! must return HTTP/1.1 200 OK
curl -v https://${RHSSO_URI}/auth/realms/master --cacert self-signed-cert.pem

# step2
openssl x509 -in <(openssl s_client -connect sso73.apps.<DOMAIN>:443 -prexit 2>/dev/null) -out temp/sso73.apps.<DOMAIN>.crt
keytool -import -trustcacerts -keystore /etc/pki/ca-trust/extracted/java/cacerts -storepass changeit -noprompt -alias keycloak -file /home/jboss/temp/sso73.apps.<DOMAIN>.crt

openssl pkcs12 -export -out pkcskeystore.pkcs12 -in self-signed-cert.pem -inkey privkey.pem
keytool -importkeystore -destkeystore keystore.jks -srckeystore pkcskeystore.pkcs12 -srcstoretype PKCS12
```

In the end, I had to acquire 2 files:
- The .pem file
- The .cert file

In order to avoid any trouble to import them into the container image, I had splited the .pem into Nth .crt files.

So, in my case, I've ended up with 3 files?
- cert1.crt (first certificate in .pem)
- cert2.crt (second certificate in .pem)
- sso73.crt (the third certificate extracted in step2)

<b>The `Dockerfile` explains better how I had to customize the image in order to allow the keycloak-admin-client to poke with the sso73 self-signed server.</b>

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

