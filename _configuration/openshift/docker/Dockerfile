FROM registry.access.redhat.com/fuse7/fuse-java-openshift:1.4

USER 185

COPY self-signed/sso73.crt /tmp/

USER root

RUN keytool -import -trustcacerts -keystore /etc/pki/ca-trust/extracted/java/cacerts -storepass changeit -noprompt -alias keycloak -file /tmp/sso73.crt && \
    rm /tmp/sso73.crt

USER 185

CMD [ "/usr/local/s2i/run" ]
