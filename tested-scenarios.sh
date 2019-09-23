echo | openssl s_client -showcerts -servername ${RHSSO_URI} -connect ${RHSSO_URI}:443 2>/dev/null | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > self-signed-cert.pem
# Validate the connection first! must return HTTP/1.1 200 OK
curl -v https://${RHSSO_URI}/auth/realms/master --cacert self-signed-cert.pem

oc exec ${THREESCALE_ZYNC_QUE_POD} cat /etc/pki/tls/cert.pem > zync-que.pem -n ${THREESCALE_NAMESPACE}





SUCESS
https://3scale:82a6e752-1976-46e6-91c4-c48dd3a1f4e1@sso73.apps.arekkusu.io/auth/realms/3scale-api
https://sso73.apps.arekkusu.io/auth/realms/3scale-api/protocol/openid-connect/token
847d10c1 4519d24a04b3d9093aa9129aa7711d9d
john 12345
> https://supplier-api-production.apps.arekkusu.io:443/actuator/health

FAIL
https://3scale:82a6e752-1976-46e6-91c4-c48dd3a1f4e1@sso73.apps.arekkusu.io/auth/realms/3scale-api
http://authorization-server.apps.arekkusu.io/auth/realms/3scale-api/protocol/openid-connect/token
847d10c1 4519d24a04b3d9093aa9129aa7711d9d
john 12345
> https://supplier-api-production.apps.arekkusu.io:443/actuator/health



SUCESS
http://3scale:82a6e752-1976-46e6-91c4-c48dd3a1f4e1@authorization-server.apps.arekkusu.io/auth/realms/3scale-api
http://authorization-server.apps.arekkusu.io/auth/realms/3scale-api/protocol/openid-connect/token
847d10c1 4519d24a04b3d9093aa9129aa7711d9d
john 12345
> https://supplier-api-production.apps.arekkusu.io:443/actuator/health

???
http://3scale:82a6e752-1976-46e6-91c4-c48dd3a1f4e1@authorization-server.apps.arekkusu.io/auth/realms/3scale-api
http://authorization-server.apps.arekkusu.io/auth/realms/3scale-api/protocol/openid-connect/token
847d10c1 4519d24a04b3d9093aa9129aa7711d9d
john 12345
> https://auth-integration-api-production.apps.arekkusu.io/api/v1/supplier/status



[20/Sep/2019:22:16:02 +0000] supplier-api.microservices-security.svc.cluster.local:8080 10.128.0.16:35472 "GET /actuator/health HTTP/1.1" 200 25 (0.100) 0




https://sso73.apps.arekkusu.io/auth/realms/3scale-api/protocol/openid-connect/certs


# openssl x509 -in <(openssl s_client -connect auth.example.com:443 -prexit 2>/dev/null) -out ~/auth.example.com.crt
# keytool -importcert -file ~/auth.example.com.crt -alias keycloak -keystore ${JDK_HOME}/jre/lib/security/cacerts -storepass changeit


# local machine
mkdir temp
openssl x509 -in <(openssl s_client -connect sso73.apps.arekkusu.io:443 -prexit 2>/dev/null) -out temp/sso73.apps.arekkusu.io.crt
API_NAMESPACE=microservices-security
API_POD=$(oc get pods --selector deploymentconfig=auth-integration-api -n ${API_NAMESPACE} | { read line1 ; read line2 ; echo "$line2" ; } | awk '{print $1;}')
oc rsync temp ${API_POD}:/home/jboss
oc exec ${API_POD} "keytool -importcert -file home/jboss/temp/sso73.apps.arekkusu.io.crt -alias keycloak -keystore ${JDK_HOME}/jre/lib/security/cacerts -storepass changeit"


oc exec ${API_POD} "keytool -import -trustcacerts -keystore /etc/pki/ca-trust/extracted/java/cacerts -storepass changeit -noprompt -alias keycloak -file /home/jboss/temp/sso73.apps.arekkusu.io.crt"




Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,app_id,app_key

Authorization, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, app_id, app_key






https://auth-integration-api-microservices-security.apps.arekkusu.io
https://auth-integration-api-production.apps.arekkusu.io




https://auth-integration-api-microservices-security.apps.arekkusu.io/



https://better-coding.com/how-to-add-ssl-certificate-into-java-cacerts-file-and-jks-keystore/



awk 'split_after == 1 {n++;split_after=0}/-----END CERTIFICATE-----/ {split_after=1}{print > "cert" n ".pem"}' < self-signed-cert.pem


awk 'split_after==1{n++;split_after=0} /-----END CERTIFICATE-----/ {split_after=1} {print > "cert" n ".pem"}' < self-signed-cert.pem



openssl x509 -in <(openssl s_client -connect sso73.apps.arekkusu.io:443 -prexit 2>/dev/null) -out demo.crt

