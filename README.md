# MICROSERVICES SECURITY
###### HOW TO SECURE APIs WITH RED HAT SINGLE SIGN-ON (KEYCLOAK), FUSE (CAMEL) AND 3SCALE.

![security](https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/03.png "Microservices Security")

<p align="center">

| Technology       | Version             |
| --------------- | -------------------- |
| [spring boot](https://spring.io/projects/spring-boot)     | 2.1.8.RELEASE        |
| [apache camel](https://camel.apache.org/)    | [7.4.0.fuse-740036-redhat-00002](https://www.redhat.com/en/technologies/jboss-middleware/fuse)<br>(w/ spring boot 1.5.22.RELEASE) |
| [3Scale](https://www.3scale.net)    |  [2.6](https://access.redhat.com/containers/#/product/RedHat3scaleApiManagement) |
| [Red Hat Single Sign-On](https://access.redhat.com/products/red-hat-single-sign-on)    | [7.3.3](https://access.redhat.com/containers/#/product/RedHatSingleSign-on)<br>(based on [keycloak 4.8](https://access.redhat.com/articles/2342881)) |

</p>

<b>TL;DR</b> This is a demonstration on how to protect APIs with Keycloak and 3Scale.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/04.png" title="Microservices Security" width="40%" height="40%" />
</p>

<b>WARNING</b>: This is a proof of concept. In production environments, there will be needed adittional configurations regarding scalability and security.

<b>The use-case scenario:</b>
The main proposal is achieve some concepts regarding security and microservices using a simple use-case scenario. A webapp is offered to promote easier understanding showing all use-case scenarios:


All APIs catalog is exposed bellow:

### `auth-integration-api` endpoints

:8081

| Method | URI | Description | Secured? |
| ------ | --- | ----------- | -------- | 
| GET    |/health | API actuator embedded health | false |
| GET    |/metrics | API actuator embedded metrics | false |

:8080

| Method | URI | Description | Secured? |
| ------ | --- | ----------- | -------- | 
| POST   |/api/v1/product  | Create new product | true |
| DELETE |/api/v1/product/{id}  | Delete product by Id | true |
| PUT   |/api/v1/product/{id}  | Update product by Id | true |
| GET   |/api/v1/product/{id}  | Retrieve product by Id | true |
| GET   |/api/v1/product  | Retrieve all products | true |
| GET   |/api/v1/product/status  | Check Product API health | true |
| GET   |/api/v1/supplier/status  | Check Supplier API health | true |
| GET   |/api/v1/stock/status  | Check Stock API health | true |
| GET   |/api/v1/supplier/update  | Call the Supplier Maintenance API | true |
| GET   |/api/v1/stock/update  | Call the Stock Maintenance API | true |

### `stock-api` endpoints

| Method | URI | Description | Secured? |
| ------ | --- | ----------- | -------- |
| GET    |/api/v1/sync | Stock Maintenance | false |

### `supplier-api` endpoints

| Method | URI | Description | Secured? |
| ------ | --- | ----------- | -------- |
| GET    |/api/v1/sync | Supplier Maintenance | true | 

### `product-api` endpoints

| Method | URI | Description | Secured? |
| ------ | --- | ----------- | -------- |
| GET    |/api/v1/products | Retrieve all products | true |
| GET    |/api/v1/product/{id} | Retrieve product by Id | true |
| POST   |/api/v1/product | Create new product | true |
| PUT    |/api/v1/product/{id} | Update product by Id | true |
| DELETE |/api/v1/product/{id} | Delete product by Id | true |

Each endpoint has it's own specifity, so in order to drive our test scenarios, I've ended up with 3 simple questions:

1. This API will be protected by an Integration Layer (FUSE)?
2. This API will be exposed as a unique service on 3Scale AMP?
3. This API will be managed by Keycloak having it's own client-id, groups and roles?

This lead me to draw this requirements matrix:

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/05.png" title="APIs Requirements" width="100%" height="100%" />
</p>

As we can see, each API has some differences, and we will strive to demonstrate each one in this tutorial.

### `SECURITY LAB: STEP 1 - PROJECT CREATION`

```sh
export PROJECT_NAMESPACE=microservices

# login into openshift platform
oc login https://master.<>.com:443 --token=<>

# create a new project
oc new-project microservices-security --description="microservices security" --display-name="microservices-security"
```

### `SECURITY LAB: STEP 2 - NEXUS SONATYPE DEPLOY`
In order to continue this lab, you must provide a Sonatype Nexus instance in the `microservices` namespace. The detailed instructions can be found in this [readme](https://github.com/aelkz/microservices-security/blob/master/README-NEXUS.md).

### `SECURITY LAB: STEP 3 - 3SCALE AMP DEPLOY`
In order to continue this lab, you must provision a 3Scale AMP into your Openshift Container Platform. Refer to the [documentation](https://access.redhat.com/documentation/en-us/red_hat_3scale_api_management/2.6) on how to install the 3Scale application.

### `SECURITY LAB: STEP 4 - RED HAT SINGLE SIGN-ON DEPLOY`
In order to continue this lab, you must provision RHSSO into your Openshift Container Platform. Refer to the [documentation](https://access.redhat.com/products/red-hat-single-sign-on) on how to install the RHSSO application.

### `SECURITY LAB: STEP 5 - NEXUS ENVIRONMENT SETUP`

```sh
export PROJECT_NAMESPACE=microservices-security

git clone https://github.com/aelkz/microservices-security.git

cd microservices-security/

# download maven settings.xml file
curl -o maven-settings-template.xml -s https://raw.githubusercontent.com/aelkz/microservices-security/master/_configuration/nexus/maven-settings-template.xml

# change mirror url using your nexus openshift route
export NEXUS_NAMESPACE=cicd-devtools
export MAVEN_URL=http://$(oc get route nexus3 -n ${NEXUS_NAMESPACE} --template='{{ .spec.host }}')/repository/maven-group/
export MAVEN_URL_RELEASES=http://$(oc get route nexus3 -n ${NEXUS_NAMESPACE} --template='{{ .spec.host }}')/repository/maven-releases/
export MAVEN_URL_SNAPSHOTS=http://$(oc get route nexus3 -n ${NEXUS_NAMESPACE} --template='{{ .spec.host }}')/repository/maven-snapshots/

awk -v path="$MAVEN_URL" '/<url>/{sub(/>.*</,">"path"<")}1' maven-settings-template.xml > maven-settings.xml

rm -fr maven-settings-template.xml
```

### `SECURITY LAB: STEP 6 - RED HAT CONTAINER CATALOG SECRET FOR PULLING IMAGES`

```sh
# NOTE. In order to import Red Hat container images, you must setup your credentials on openshift. See: https://access.redhat.com/articles/3399531
# The config.json can be found at: /var/lib/origin/.docker/ on openshift master node.
# create a secret with your container credentials

oc delete secret redhat.io -n $PROJECT_NAMESPACE
oc create secret generic "redhat.io" --from-file=.dockerconfigjson=config.json --type=kubernetes.io/dockerconfigjson -n microservices-security

oc create secret generic registry.redhat.io --from-file=.dockerconfigjson=config.json --type=kubernetes.io/dockerconfigjson -n $PROJECT_NAMESPACE
oc secrets link default registry.redhat.io --for=pull -n $PROJECT_NAMESPACE
oc secrets link builder registry.redhat.io -n $PROJECT_NAMESPACE
```

### `SECURITY LAB: STEP 7 - MICROSERVICES DEPLOYMENT`

```sh
# Deploy parent project on nexus
mvn clean package deploy -DnexusReleaseRepoUrl=$MAVEN_URL_RELEASES -DnexusSnapshotRepoUrl=$MAVEN_URL_SNAPSHOTS -s ./maven-settings.xml -e -X -N

# Deploy stock-api
# oc delete all -lapp=stock-api
oc new-app openjdk-8-rhel8:latest~https://github.com/aelkz/microservices-security.git --name=stock-api --context-dir=/stock --build-env='MAVEN_MIRROR_URL='${MAVEN_URL} -e MAVEN_MIRROR_URL=${MAVEN_URL}

oc patch svc stock-api -p '{"spec":{"ports":[{"name":"http","port":8080,"protocol":"TCP","targetPort":8080}]}}'

oc label svc stock-api monitor=springboot2-api

# Deploy supplier-api
# oc delete all -lapp=supplier-api
oc new-app openjdk-8-rhel8:latest~https://github.com/aelkz/microservices-security.git --name=supplier-api --context-dir=/supplier --build-env='MAVEN_MIRROR_URL='${MAVEN_URL} -e MAVEN_MIRROR_URL=${MAVEN_URL}

oc patch svc supplier-api -p '{"spec":{"ports":[{"name":"http","port":8080,"protocol":"TCP","targetPort":8080}]}}'

oc label svc supplier-api monitor=springboot2-api

# Deploy product-api
# oc delete all -lapp=product-api
oc new-app openjdk-8-rhel8:latest~https://github.com/aelkz/microservices-security.git --name=product-api --context-dir=/product --build-env='MAVEN_MIRROR_URL='${MAVEN_URL} -e MAVEN_MIRROR_URL=${MAVEN_URL}

oc patch svc product-api -p '{"spec":{"ports":[{"name":"http","port":8080,"protocol":"TCP","targetPort":8080}]}}'

oc label svc product-api monitor=springboot2-api
```

### `SECURITY LAB: STEP 8 - ARCHIVE SSO-COMMON LIBRARY JAR ON NEXUS`

```sh
# NOTE: To make sure the auth-integration-api (FUSE) works correctly, we need to archive a library that will be used to provide authentication and authorizations capabilities on top of Red Hat Single Sing-On (Keycloak).Then, this library will be used on auth-integration-api to enable such capabilities.

# Deploy auth-sso-common library on nexus
mvn clean package deploy -DnexusReleaseRepoUrl=$MAVEN_URL_RELEASES -DnexusSnapshotRepoUrl=$MAVEN_URL_SNAPSHOTS -s ./maven-settings.xml -e -X -pl auth-sso-common
```

This will create the following artifact on Nexus:
<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/06.png" title="auth-sso-common artifact on nexus" width="35%" height="35%" />
</p>

### `SECURITY LAB: STEP 9 - INTEGRATION DEPLOYMENT (FUSE)`
Now that the microservices APIs are deployed, let’s deploy the integration layer.

```sh
# import a new spring-boot camel template
curl -o s2i-microservices-fuse74-spring-boot-camel.yaml -s https://raw.githubusercontent.com/aelkz/microservices-security/master/_configuration/openshift/s2i-microservices-fuse74-spring-boot-camel.yaml

oc delete template s2i-microservices-fuse74-spring-boot-camel -n ${PROJECT_NAMESPACE}
oc create -n ${PROJECT_NAMESPACE} -f s2i-microservices-fuse74-spring-boot-camel.yaml

export NEXUS_NAMESPACE=cicd-devtools
export PROJECT_NAMESPACE=microservices-security
export APP=auth-integration-api
export APP_NAME=auth-integration
export APP_GROUP=com.redhat.microservices
export APP_GIT=https://github.com/aelkz/microservices-security.git
export APP_GIT_BRANCH=master
export MAVEN_URL=http://$(oc get route nexus3 -n ${NEXUS_NAMESPACE} --template='{{ .spec.host }}')/repository/maven-group/

# the previous template have some modifications regarding services,route and group definitions.
# oc delete all -lapp=${APP}
oc new-app --template=s2i-microservices-fuse74-spring-boot-camel --name=${APP} --build-env='MAVEN_MIRROR_URL='${MAVEN_URL} -e MAVEN_MIRROR_URL=${MAVEN_URL} --param GIT_REPO=${APP_GIT} --param APP_NAME=${APP} --param ARTIFACT_DIR=${APP_NAME}/target --param GIT_REF=${APP_GIT_BRANCH} --param MAVEN_ARGS_APPEND='-pl '${APP_NAME}' --also-make'

# check the created services:
# 1 for default app-context and 1 for /metrics endpoint.
oc get svc -n ${PROJECT_NAMESPACE} | grep ${APP_NAME}

# in order to auth-integration-api call the others APIs, we need to change it's configuration:
curl -o application.yaml -s https://raw.githubusercontent.com/aelkz/microservices-security/master/_configuration/openshift/auth-integration/application.yaml

# NOTE. If you have changed the service or application's name, you need to edit and change the downloaded application.yaml file with your definitions.

# create a configmap and mount a volume for auth-integration-api

oc delete configmap ${APP}

oc create configmap ${APP}-config --from-file=application.yaml

oc set volume dc/${APP} --add --overwrite --name=${APP}-config-volume -m /deployments/config -t configmap --configmap-name=${APP}-config

rm -fr application.yaml
```

### `SECURITY LAB: STEP 10 - RHSSO REALMS CONFIGURATION`
Now that all APIs are alive and kicking, let's define some configurations on RHSSO to prepare some ground for 3Scale automatic app synchronization.

```sh
1. Login into RHSSO
2. Create 3 realms with default settings:
  - 3scale-api
  - 3scale-admin
  - 3scale-devportal
```

After creating the realms, you'll have this:
<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/07.png" title="RHSSO realms" width="20%" height="20%" />
</p>

On `3scale-api` realm, create a client `3scale` with the following definition:
<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/08.png" title="realm:3scale-api client:3scale" width="40%" height="40%" />
</p>

Leave blank the fields: `root URL` , `base URL` and `admin URL`.

On `Service Account Roles` tab, assign the role `manage-clients` from `realm-management`.

Copy the `client-secret` that was genereated for this client.
It will be something like:
`823b6ek5-1936-42e6-1135-d48rt3a1f632`

Under the realm `3scale-api` create a new user with the following definition:
<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/09.png" title="realm:3scale-api user:john" width="60%" height="60%" />
</p>

Also, set a new password for this user on `Credentials` tab with `temporary=false` and set to `true` the `Email Verified` on `Details` tab.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/10.png" title="realm:3scale-api user:john" width="40%" height="40%" />
</p>

<b>Troublehsooting</b>: After creating the API definition on 3Scale, check if the generated client was created into 3scale-api realm on RHSSO. If you're using a self-signed certificate, you'll need to make additional configurations in order to enable the zync-que 3Scale application synchronizes correctly. Please refer to the [Documentation: Troubleshooting SSL issues](https://access.redhat.com/documentation/en-us/red_hat_3scale_api_management/2.6/html-single/operating_3scale/index#troubleshooting_ssl_issues) and [Configure Zync to use custom CA certificates](https://access.redhat.com/documentation/en-us/red_hat_3scale_api_management/2.4/html-single/api_authentication/index#zync-oidc-integration)

To <b>fix</b> this, you can proceed with the self-signed certificate configuration:
```sh
export THREESCALE_NAMESPACE=3scale26
export THREESCALE_ZYNC_QUE_POD=zync-que-2-mlrh2
export RHSSO_URI=sso73.apps.<YOUR-DOMAIN>.com

echo | openssl s_client -showcerts -servername ${RHSSO_URI} -connect ${RHSSO_URI}:443 2>/dev/null | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > self-signed-cert.pem
# Validate the connection first! must return HTTP/1.1 200 OK
curl -v https://${RHSSO_URI}/auth/realms/master --cacert self-signed-cert.pem

oc exec ${THREESCALE_ZYNC_QUE_POD} cat /etc/pki/tls/cert.pem > zync-que.pem -n ${THREESCALE_NAMESPACE}

cp zync-que.pem zync-que-original.pem

echo '\n# Red Hat Single Sign-On CA '${RHSSO_URI} >> zync-que.pem
cat self-signed-cert.pem >> zync-que.pem

# oc delete configmap zync-que-ca-bundle
oc create configmap zync-que-ca-bundle --from-file=./zync-que.pem -n ${THREESCALE_NAMESPACE}
oc label configmap zync-que-ca-bundle app=3scale-api-management -n ${THREESCALE_NAMESPACE}

oc set volume dc/zync-que --overwrite --add --name=zync-que-ca-bundle --mount-path /etc/pki/tls/zync-que/zync-que.pem --sub-path zync-que.pem --source='{"configMap":{"name":"zync-que-ca-bundle","items":[{"key":"zync-que.pem","path":"zync-que.pem"}]}}' -n ${THREESCALE_NAMESPACE}

oc patch dc/zync-que --type=json -p '[{"op": "add", "path": "/spec/template/spec/containers/0/volumeMounts/0/subPath", "value":"zync-que.pem"}]' -n ${THREESCALE_NAMESPACE}

oc exec ${THREESCALE_ZYNC_QUE_POD} cat /etc/pki/tls/zync-que/zync-que.pem -n ${THREESCALE_NAMESPACE}

oc set env dc/zync-que SSL_CERT_FILE=/etc/pki/tls/zync-que/zync-que.pem -n ${THREESCALE_NAMESPACE}

# wait for the container restart.
# Voila! You have the 3Scale in sync with RHSSO.
```

### `SECURITY LAB: STEP 11 - 3SCALE MICROSERVICES CONFIGURATION`
In this step we will register the APIs and configure them to enable 3Scale automatic synchronization with RHSSO.
Let's setup the `auth-integration-api` and the `supplier-api`.

Create a new API on 3Scale admin portal. You can hit the `NEW API` link on the main dashboard.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/12.png" title="3Scale admin portal - New API" width="10%" height="10%" />
</p>

This new API will represent the `auth-integration-api`, previously deployed.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/11.png" title="3Scale admin portal - auth-integration-api" width="70%" height="70%" />
</p>

Then, navigate through the `Configuration` menu under `Integration`, to setup the API mappings and security.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/13.png" title="3Scale admin portal - auth-integration-api configuration" width="85%" height="85%" />
</p>

Choose `APICast` for the gateway and `OpenID Connect` in Integration Settings,

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/14.png" title="APICast Gateway" width="30%" height="30%" />&nbsp;&nbsp;&nbsp;<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/15.png" title="OpenID Connect" width="30%" height="30%" />
</p>

<b>NOTE</b>. The OpenID Connection is used because we will protect our API with OAuth2 capabilities provided by RHSSO.

Then click on <img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/16.png" title="button: add the base URL of your API and save the configuration" width="35%" height="35%" />

Next, define the `Private Base URL` that is, your auth-integration-api URL and the `staging` and `production` URLs:

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/17.png" title="3Scale admin portal - auth-integration-api configuration" width="70%" height="70%" />
</p>

<b>NOTE</b>. Set your correct domain under each URL (that will be your public address for Openshift).

Next, define all mapping rules for this API, accordingly to the following image:

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/18.png" title="3Scale admin portal - auth-integration-api configuration" width="70%" height="70%" />
</p>

Next, define the authentication mechanism for this API:

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/19.png" title="3Scale admin portal - auth-integration-api configuration" width="70%" height="70%" />
</p>

Select `Authorization Code Flow` , `Service Accounts Flow` and `Direct Access Grant Flow` under `OIDC AUTHORIZATION FLOW` section.

Leave the rest as default, and save the configuration.

<b>NOTE</b>. After every changes, remember to promote the staging configuration to production.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/19.png" title="3Scale admin portal - auth-integration-api configuration promotion" width="15%" height="15%" />
</p>

You auth-integration-api is ready to be used.

Define the same steps for creating the `Supplier API`. This API will have only one mapping rule: `GET /api/v1/sync` and `Authorization Code Flow` and `Direct Access Grant Flow` under `OIDC AUTHORIZATION FLOW` section.

### `SECURITY LAB: STEP 12 - 3SCALE MICROSERVICES APPLICATION PLANS`

Let's the define the APIs `Application Plans`. These plans will be used upon client registration for creating a new `Application`.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/21.png" title="3Scale admin portal - Application Plans" width="50%" height="50%" />
</p>

### `EXTERNAL REFERENCES`

API Key Generator
https://codepen.io/corenominal/pen/rxOmMJ<br>
JWT Key Generator
http://jwt.io
OpenID Connect Debugger
https://openidconnect.net

- - - - - - - - - -
Thanks for reading and taking the time to comment!<br>
Feel free to create a <b>PR</b><br>
[raphael abreu](rabreu@redhat.com)


