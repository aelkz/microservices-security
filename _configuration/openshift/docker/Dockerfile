FROM registry.access.redhat.com/fuse7/fuse-java-openshift:1.4

USER 185

COPY self-signed/sso73.crt /tmp/
COPY self-signed/cert1.crt /tmp/
COPY self-signed/cert2.crt /tmp/

USER root

RUN keytool -import -trustcacerts -keystore /etc/pki/ca-trust/extracted/java/cacerts -storepass changeit -noprompt -alias keycloak1 -file /tmp/sso73.crt
RUN keytool -importcert -file /tmp/cert1.crt -alias keycloak2 -keystore /etc/pki/ca-trust/extracted/java/cacerts -storepass changeit -noprompt
RUN keytool -importcert -file /tmp/cert2.crt -alias keycloak3 -keystore /etc/pki/ca-trust/extracted/java/cacerts -storepass changeit -noprompt
RUN rm /tmp/sso73.crt /tmp/cert1.crt /tmp/cert2.crt

USER 185

CMD [ "/usr/local/s2i/run" ]
