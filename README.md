# MICROSERVICES SECURITY
###### HOW TO SECURE APIs WITH RED HAT SINGLE SIGN-ON (KEYCLOAK), FUSE (CAMEL) AND 3SCALE.

![security](https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/01.png "Microservices Security")

<p align="center">

| Technology       | Version             |
| --------------- | -------------------- |
| [spring boot](https://spring.io/projects/spring-boot)     | 2.1.8.RELEASE        |
| [apache camel](https://camel.apache.org/)    | [7.4.0.fuse-740036-redhat-00002](https://www.redhat.com/en/technologies/jboss-middleware/fuse)<br>(w/ spring boot 1.5.22.RELEASE) |
| [3Scale](https://www.3scale.net)    |  [2.6](https://access.redhat.com/containers/#/product/RedHat3scaleApiManagement) |
| [Red Hat Single Sign-On](https://access.redhat.com/products/red-hat-single-sign-on)    | [7.3.3](https://access.redhat.com/containers/#/product/RedHatSingleSign-on)<br>(based on [keycloak 4.8](https://access.redhat.com/articles/2342881)) |

</p>

<b>TL;DR</b> This is a demonstration on how to protect APIs with Red Hat Single Sign-On (Keycloak) and 3Scale.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/04.png" title="Microservices Security" width="40%" height="40%" />
</p>

<b>WARNING</b>: This is a proof of concept. In production environments, there will be needed adittional configurations regarding scalability, security and using a proper CA trusted certificate.

<p align="left">
<div>
<img style="float: left; margin: 0px 15px 15px 0px;" src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/icon01.png" title="Microservices Security" width="7%" height="7%" /> This is a lengthy article with step by step instructions, screenshots of products and architecture concepts. All source-code is hosted on the <a href="https://github.com/aelkz/microservices-security" target="_blank">github</a>.
</div>
</p>

<b>The use-case scenario:</b>
The main proposal is to achieve some concepts regarding security of microservices using a wide use-case scenario. A webapp is offered to promote easier understanding of all API calls and authorizations used.

All APIs catalog is exposed bellow:

### `auth-integration-api` endpoints

<b>:8081</b>

| Method | URI | Description |
| ------ | --- | ----------- |
| GET    |/health | API actuator embedded health |
| GET    |/metrics | API actuator embedded metrics |

<b>:8080</b>

| Method | URI | Description | Secured? |
| ------ | --- | ----------- | -------- | 
| POST    | /api/v1/product             | Create new product | true |
| DELETE  | /api/v1/product/*           | Delete product by Id | true |
| PUT     | /api/v1/product/*           | Update product by Id | true |
| GET     | /api/v1/product$            | Retrieve all products | true |
| GET     | /api/v1/product/*           | // | true |
| GET     | /api/v1/status              | Check Integration API health | true |
| GET     | /api/v1/product/status      | Check Product API health | true |
| GET     | /api/v1/supplier/status     | Check Supplier API health | true |
| GET     | /api/v1/stock/status        | Check Stock API health | true |
| GET     | /api/v1/stock/maintenance   | Call Stock API maintenance | true |
| GET     | /api/v1/supplier/maintenance| Call Supplier API maintenance | true |

### `stock-api` endpoints

| Method | URI | Description |
| ------ | --- | ----------- |
| GET    |/api/v1/sync | Stock Maintenance |
| GET    |/actuator/health | Supplier Maintenance |

### `supplier-api` endpoints

| Method | URI | Description | Secured? |
| ------ | --- | ----------- | -------- |
| GET    |/api/v1/sync | Supplier Maintenance | true | 
| GET    |/actuator/health | Supplier Maintenance | true | 

### `product-api` endpoints

| Method | URI | Description | Secured? |
| ------ | --- | ----------- | -------- |
| GET    |/api/v1/product | Retrieve all products | true |
| GET    |/api/v1/product/{id} | Retrieve product by Id | true |
| POST   |/api/v1/product | Create new product | true |
| PUT    |/api/v1/product/{id} | Update product by Id | true |
| DELETE |/api/v1/product/{id} | Delete product by Id | true |

Each endpoint has it's own specifity, so in order to drive our test scenarios, I've ended up with 3 simple questions:

1. This API will be protected by an Integration Layer (FUSE)?
2. This API will be exposed as a unique service on 3Scale AMP? This will enable API self-service subscription for external clients.
3. This API will be managed by RHSSO (Keycloak) having it's own client-id, groups and roles?

This lead me to draw this requirements matrix:

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/05.png" title="APIs Requirements" width="100%" height="100%" />
</p>

As we can see, each API has some differences, and we will strive to demonstrate each one in this <b>microservices security lab</b>!

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

export $PROJECT_NAMESPACE=microservices-security

oc delete secret redhat.io -n $PROJECT_NAMESPACE
oc create secret generic "redhat.io" --from-file=.dockerconfigjson=config.json --type=kubernetes.io/dockerconfigjson -n $PROJECT_NAMESPACE

oc create secret generic redhat.io --from-file=.dockerconfigjson=config.json --type=kubernetes.io/dockerconfigjson -n $PROJECT_NAMESPACE
oc secrets link default redhat.io --for=pull -n $PROJECT_NAMESPACE
oc secrets link builder redhat.io -n $PROJECT_NAMESPACE
```

### `SECURITY LAB: STEP 7 - RHSSO REALMS CONFIGURATION`

In this step we will configure `realms` on RHSSO to register all the 5 applications.

```sh
1. Login into RHSSO
2. Create 3 realms with default settings:
  - 3scale-api
  - 3scale-admin
  - 3scale-devportal
```

After creating the realms, you'll have something like this:
<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/07.png" title="RHSSO realms" width="35%" height="35%" />
</p>

On `3scale-api` realm, create a client `3scale` with the following definition:
<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/08.png" title="realm:3scale-api client:3scale" width="40%" height="40%" />
</p>

Leave blank the fields: `root URL` , `base URL` and `admin URL`.

On `Service Account Roles` <b>tab</b>, assign the role `manage-clients` from `realm-management`.

Copy and save the `client-secret` that was genereated for this client. <b>This will be used later to configure OAuth service authentication on 3Scale.</b>

The client-secret will be something like this:
`823b6ek5-1936-42e6-1135-d48rt3a1f632`

Under the realm `3scale-api` create a <b>new user</b> with the following definition:
<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/09.png" title="realm:3scale-api user:john" width="60%" height="60%" />
</p>

Also, set a new password for this user on `Credentials` tab with `temporary=false` and set to `true` the `Email Verified` attribute on `Details` tab.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/10.png" title="realm:3scale-api user:john" width="55%" height="55%" />
</p>

### `SECURITY LAB: STEP 8 - 3SCALE MICROSERVICES CONFIGURATION`
In this step we will register the APIs and configure them to enable 3Scale automatic <b>synchronization</b> with RHSSO.
Let's setup the `auth-integration-api` and the `supplier-api`.

Create a new API on 3Scale admin portal. You can hit the `NEW API` link on the main dashboard.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/12.png" title="3Scale admin portal - New API" width="10%" height="10%" />
</p>

This new API will represent the `auth-integration-api`, previously deployed.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/11.png" title="3Scale admin portal - auth-integration-api" width="50%" height="50%" />
</p>

Then, navigate through the `Configuration` menu under `Integration`, to setup the API mappings and security.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/13.png" title="3Scale admin portal - auth-integration-api configuration" width="85%" height="85%" />
</p>

Choose `APICast` for the gateway and `OpenID Connect` in Integration Settings,

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/14.png" title="APICast Gateway" width="30%" height="30%" />&nbsp;&nbsp;&nbsp;<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/15.png" title="OpenID Connect" width="30%" height="30%" />
</p>

<b>NOTE</b>. The OpenID Connection is choosen because we are going to protect our APIs with OAuth2 capabilities provided by RHSSO.

Then click on <img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/16.png" title="button: add the base URL of your API and save the configuration" width="35%" height="35%" />

Next, define the `Private Base URL` that is, your auth-integration-api URL and the `staging` and `production` URLs:

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/17.png" title="3Scale admin portal - auth-integration-api configuration" width="70%" height="70%" />
</p>

<b>NOTE</b>. Set your correct domain under each URL (that will be your API route on Openshift).

Next, define all <b>mapping rules</b> for this API, accordingly to the following table:

| Verb    | Pattern                     | + | Metric or Method |
| ------- | --------------------------- | --| ---------------- |
| POST    | /api/v1/product             | 1 | hits             |
| DELETE  | /api/v1/product/*           | 1 | hits             |
| PUT     | /api/v1/product/*           | 1 | hits             |
| GET     | /api/v1/product$            | 1 | hits             |
| GET     | /api/v1/product/*           | 1 | hits             |
| GET     | /api/v1/status              | 1 | hits             |
| GET     | /api/v1/product/status      | 1 | hits             |
| GET     | /api/v1/supplier/status     | 1 | hits             |
| GET     | /api/v1/stock/status        | 1 | hits             |
| GET     | /api/v1/stock/maintenance   | 1 | hits             |
| GET     | /api/v1/supplier/maintenance| 1 | hits             |

Next, define the authentication mechanism for this API:

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/19.png" title="3Scale admin portal - auth-integration-api configuration" width="85%" height="85%" />
</p>

Next, configure API policies that will be required to enable proper communication between resources inside the Openshift Container Platform:

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/30.png" title="3Scale admin portal - auth-integration-api policies" width="70%" height="70%" />
</p>

#### Please follow the next steps carefully:

Select `Authorization Code Flow` , `Service Accounts Flow` and `Direct Access Grant Flow` under `OIDC AUTHORIZATION FLOW` section.

On `Credentials location` set <b>As HTTP Headers</b>

Netx, on `Policies` section add in this order:
- CORS
- 3Scale APIcast

Expand CORS configuration, and set:

<b>Enabled</b>=checked

<b>ALLOW_HEADERS</b> (add one by one per input array)<br>

| 3Scale CORS Policy: Headers  |
| ---------------------------- |
| Content-Type |
| Authorization |
| Content-Length |
| X-Requested-With |
| Origin |
| Accept |
| X-Requested-With |
| Content-Type |
| Access-Control-Request-Method |
| Access-Control-Request-Headers |
| Accept-Encoding |
| Accept-Language |
| Connection |
| Host |
| Referer |
| User-Agent |
| Access-Control-Allow-Origin |
| X-Business-FooBar |

<b>NOTE</b>. The latest header is used only for testing purposes.

<b>allow_credentials</b>=checked

<b>ALLOW_METHODS</b> (add one by one per input array)<br>

| 3Scale CORS Policy: Http Methods |
| -------------------------------- |
| GET |
| HEAD |
| POST |
| PUT |
| DELETE |
| OPTIONS |

<b>allow_origin</b>
Leave empty.

Leave the rest as default, and save the CORS configuration.

<b>NOTE</b>. After every change, remember to <b>promote</b> the <b>staging</b> configuration to production.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/20.png" title="3Scale admin portal - auth-integration-api configuration promotion" width="25%" height="25%" />
</p>

You `auth-integration-api` is ready to be used!

Repeat the same steps for `Supplier API`. This API will have only two mapping rules:

| Verb    | Pattern                     | + | Metric or Method |
| ------- | --------------------------- | --| ---------------- |
| POST    | /api/v1/sync                | 1 | hits             |
| GET     | /actuator/health            | 1 | hits             |

### `SECURITY LAB: STEP 9 - 3SCALE MICROSERVICES APPLICATION PLANS`

Let's the define the APIs `Application Plans`. These plans will be used upon client registration for creating a new `Application`.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/21.png" title="3Scale admin portal - Application Plans" width="60%" height="60%" />
</p>

Click on <img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/23.png" title="3Scale admin portal - New Application Plan" width="20%" height="20%" /> link under `Applications/Application Plans` menu.

<p align="center">
Set the following configuration:<br>
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/22.png" title="3Scale admin portal - New Application Plan" width="40%" height="40%" />
</p>

After this, click the `Publish` link to publish the application plan.

Define the same steps for the `Supplier API`. Remember to publish the application plan also.

After you have done all previous steps, you'll get something like this:

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/24.png" title="3Scale admin portal - Application Plans" width="70%" height="70%" />
</p>

### `SECURITY LAB: STEP 10 - 3SCALE MICROSERVICES APPLICATION`

Navigate through the `Audience` menu and under `Accounts/Listing` <img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/25.png" title="3Scale admin portal - New Account" width="7%" height="7%" /> a new account.

Create a new account with your credentials for this demo:

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/26.png" title="3Scale admin portal - New Account" width="25%" height="25%" />
</p>

This action will create for you a new 3Scale `application` for some APIs. If the application couldn't be created, just hit the <img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/27.png" title="3Scale admin portal - New Application" width="20%" height="20%" /> link.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/28.png" title="3Scale - New Application" width="75%" height="75%" />
</p>

The `Application` will be created for use with the `auth-integration-api`. A client-ID and a Client-Secret will be generated automatically, and pushed into RHSSO on `3Scale-api` realm by the `zynnc-que` 3Scale application.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/29.png" title="RHSSO - New Application" width="70%" height="70%" />
</p>

<b>Troublehsooting</b>: After creating the API definition on 3Scale, check if the generated client was pushed into 3scale-api realm on RHSSO. If you're using a <b>self-signed</b> certificate, you'll need to make additional configurations in order to enable the zync-que 3Scale application synchronization. Please refer to the [Documentation: Troubleshooting SSL issues](https://access.redhat.com/documentation/en-us/red_hat_3scale_api_management/2.6/html-single/operating_3scale/index#troubleshooting_ssl_issues) and [Configure Zync to use custom CA certificates](https://access.redhat.com/documentation/en-us/red_hat_3scale_api_management/2.4/html-single/api_authentication/index#zync-oidc-integration)

To <b>fix</b> this, you can proceed with the self-signed certificate installation:
```sh
export THREESCALE_NAMESPACE=3scale26
export THREESCALE_ZYNC_QUE_POD=$(oc get pods --selector deploymentconfig=zync-que -n 3scale26 | { read line1 ; read line2 ; echo "$line2" ; } | awk '{print $1;}')
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

# wait for the container restart and check the logs for any issue.
oc logs -f po/${THREESCALE_ZYNC_QUE_POD}

# Voila! You have the 3Scale in sync with RHSSO using a self-signed certificate.
```

### `SECURITY LAB: STEP 11 - NODEJS WEB APPLICATION DEPLOYMENT`

In this step, we will be testing all scenarios with a suited NodeJS webapp based on Angular and Bootstrap. This application was designed to easy the understanding process. It can be used to give some clarification regarding the authorization behavior using our `Jon Doe` user account.

```sh
# Deploy nodejs-web application
# https://access.redhat.com/containers/#/registry.access.redhat.com/rhscl/nodejs-8-rhel7

oc import-image rhscl/nodejs-8-rhel7 --from=registry.redhat.io/rhscl/nodejs-8-rhel7 -n openshift --confirm

export APIS_NAMESPACE=microservices-security

oc delete secret redhat.io -n $PROJECT_NAMESPACE
oc create secret generic "redhat.io" --from-file=.dockerconfigjson=config.json --type=kubernetes.io/dockerconfigjson -n $PROJECT_NAMESPACE

oc secrets link default redhat.io --for=pull -n $PROJECT_NAMESPACE
oc secrets link builder redhat.io -n $PROJECT_NAMESPACE

export THREESCALE_NAMESPACE=3scale26
export RHSSO_NAMESPACE=sso73
export RHSSO_URL=https://$(oc get route -n ${RHSSO_NAMESPACE} | grep secured | awk '{print $2;}')/auth
export THREESCALE_APP_DOMAIN=arekkusu.io
export THREESCALE_API_URL=https://$(oc get routes -n ${THREESCALE_NAMESPACE} | grep auth-integration | grep production | awk '{print $2;}')
export INTEGRATION_HEALTH_URL=http://$(oc get routes -n ${APIS_NAMESPACE} | grep auth-integration | grep metrics | awk '{print $2;}')

echo -e \
" PORT=4200\n" \
"AUTH_URL=${RHSSO_URL}\n" \
"AUTH_REALM=3scale-api\n" \
"AUTH_CLIENT_ID=nodejs-web\n" \
"KEYCLOAK=true\n" \
"INTEGRATION_URI=${THREESCALE_API_URL}\n" \
"INTEGRATION_HEALTH_URI=${INTEGRATION_HEALTH_URL}\n" \
"PRODUCT_PATH=/product\n" \
"STOCK_PATH=/stock\n" \
"SUPPLIER_PATH=/supplier\n" \
> temp

sed "s/^.//g" temp >> nodejs-config.properties
rm -fr temp

oc create configmap nodejs-web-config \
 --from-literal=AUTH_URL= \
 --from-literal=AUTH_REALM= \
 --from-literal=AUTH_CLIENT_ID= \
 --from-literal=KEYCLOAK= \
 --from-literal=INTEGRATION_URI= \
 --from-literal=INTEGRATION_HEALTH_URI= \
 --from-literal=PRODUCT_PATH= \
 --from-literal=STOCK_PATH= \
 --from-literal=SUPPLIER_PATH= 

# oc delete all -lapp=nodejs-web
oc new-app nodejs-8-rhel7:latest~https://github.com/aelkz/microservices-security.git --name=nodejs-web --context-dir=/webapp -n ${APIS_NAMESPACE}

# with the properties defined, set the environment variable on nodejs-web container.
oc set env --from=configmap/nodejs-web-config dc/nodejs-web -n ${APIS_NAMESPACE}
```

<b>NOTE</b>. Set all environment variables on `nodejs-web` container in order to enable APIs calls properly.

Expose the webapp route:

```
oc create route edge --service=nodejs-web --cert=webapp/server.cert --key=webapp/server.key -n ${APIS_NAMESPACE}
```

### `SECURITY LAB: STEP 12 - APPLICATION SETTINGS AND ROLES`

Next step we will create our application `roles`.
Theses roles will be assigned to the application users that will be used to login in our webapp.

Access the client-id that represents the `auth-integration` client registered previsously by 3Scale `application` process.

Go to the `Settings` tab on the client and apply additional configurations:

<b>Valid Redirect URIs:</b>
- http://*
- https://*

<b>Web Origins:</b> *

Go to the `Roles` tab on `Clients` menu on RHSSO (Keycloak) and create the following roles:

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/32.png" title="RHSSO - auth-integration-api client roles" width="75%" height="75%" />
</p>

Repeat the same steps for the `Supplier API` client. This client will have only one role defined:

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/33.png" title="RHSSO - supplier-api client roles" width="75%" height="75%" />
</p>

<b>NOTE</b>. This client was also generated through 3Scale. (You must create 2 `applications`: one for auth-integration-api and another for supplier-api.

### `SECURITY LAB: STEP 13 - USERS ROLES`

In this step, we will be assigning all `client` roles to `john doe` user and the `service-account` user that will handle the `supplier-service` calls <b>inside</b> the `auth-integration-api`.

Go to the `Role Mappings` tab on `John Doe` user-details page on `Users` menu.
Assign all roles to the user, following the image bellow:

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/34.png" title="RHSSO - user roles assignment" width="75%" height="75%" />
</p>

On `Step 7` we've created the `John Doe` user. We will need to create <b>another user</b> that will be used as a service-account to call the `Supplier API` inside de `auth-integration-api` (<b>see</b>: line 123 on [application.yaml](https://raw.githubusercontent.com/aelkz/microservices-security/master/product/src/main/resources/application.yaml)). This user will have a password also, reset its credentials with `12345`. The name of this user can be the the `id` of the `Supplier API` client-id generated by 3Scale appended with `_svcacc` suffix (<b>see</b>: line 131 on [application.yaml](https://raw.githubusercontent.com/aelkz/microservices-security/master/product/src/main/resources/application.yaml)).

We will also need to assign the `SUPPLIER_MAINTAINER` role to this user.

At last, create a realm-admin user. This user will serve to consume the RHSSO REST API.
Assign the credentials `12345` and all `realm-management` roles.

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/35.png" title="RHSSO - user roles assignment" width="75%" height="75%" />
</p>

At the end, we will have 3 users on `3scale-api` realm:

<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/36.png" title="RHSSO - 3scale-api realm users" width="75%" height="75%" />
</p>

### `SECURITY LAB: STEP 14 - ARCHIVE SSO-COMMON LIBRARY JAR ON NEXUS`

```sh
# NOTE: To make sure the auth-integration-api (FUSE) works correctly, we need to archive a library that will be used to provide authentication and authorizations capabilities on top of Red Hat Single Sing-On (Keycloak).Then, this library will be used on auth-integration-api to enable such capabilities.

# Deploy auth-sso-common library on nexus
mvn clean package deploy -DnexusReleaseRepoUrl=$MAVEN_URL_RELEASES -DnexusSnapshotRepoUrl=$MAVEN_URL_SNAPSHOTS -s ./maven-settings.xml -e -X -pl auth-sso-common
```

This will create the following artifact on Nexus:
<p align="center">
<img src="https://raw.githubusercontent.com/aelkz/microservices-security/master/_images/06.png" title="auth-sso-common artifact on nexus" width="35%" height="35%" />
</p>

### `SECURITY LAB: STEP 15 - MICROSERVICES DEPLOYMENT`

Retrieve RHSSO realm public key:

```sh
export RHSSO_REALM=3scale-api
export RHSSO_URI=sso73.apps.<YOUR-DOMAIN>.com
export TOKEN_URL=https://${RHSSO_URI}/auth/realms/${RHSSO_REALM}/protocol/openid-connect/token
export THREESCALE_REALM_USERNAME=admin
export THREESCALE_REALM_PASSWORD=12345

TKN=$(curl -k -X POST "$TOKEN_URL" \
 -H "Content-Type: application/x-www-form-urlencoded" \
 -d "username=$THREESCALE_REALM_USERNAME" \
 -d "password=$THREESCALE_REALM_PASSWORD" \
 -d "grant_type=password" \
 -d "client_id=admin-cli" \
 | sed 's/.*access_token":"//g' | sed 's/".*//g')

export REALM_KEYS_URL=https://${RHSSO_URI}/auth/admin/realms/${RHSSO_REALM}/keys

RSA_PUB_KEY=$(curl -k -X GET "$REALM_KEYS_URL" \
 -H "Authorization: Bearer $TKN" \
 | jq -r '.keys[]  | select(.type=="RSA") | .publicKey' )

# Create a valid .pem certificate

REALM_CERT=$RHSSO_REALM.pem

echo "-----BEGIN CERTIFICATE-----" > $REALM_CERT; echo $RSA_PUB_KEY >> $REALM_CERT; echo "-----END CERTIFICATE-----" >> $REALM_CERT

# Check the generated .pem certificate
# fold -s -w 64 $REALM_CERT > $RHSSO_REALM.fixed.pem
# openssl x509 -in $RHSSO_REALM.fixed.pem -text -noout
# openssl x509 -in $RHSSO_REALM.fixed.pem -noout -issuer -fingerprint
```

Deploy the `parent` project:

```sh
# Deploy parent project on nexus
mvn clean package deploy -DnexusReleaseRepoUrl=$MAVEN_URL_RELEASES -DnexusSnapshotRepoUrl=$MAVEN_URL_SNAPSHOTS -s ./maven-settings.xml -e -X -N
```

Deploy `stock-api`

```
# oc delete all -lapp=stock-api
oc new-app openjdk-8-rhel8:latest~https://github.com/aelkz/microservices-security.git --name=stock-api --context-dir=/stock --build-env='MAVEN_MIRROR_URL='${MAVEN_URL} -e MAVEN_MIRROR_URL=${MAVEN_URL}

oc patch svc stock-api -p '{"spec":{"ports":[{"name":"http","port":8080,"protocol":"TCP","targetPort":8080}]}}'

oc label svc stock-api monitor=springboot2-api
```

Deploy `supplier-api`

<b>NOTE</b>. Please check all settings on `application.yaml` file before continuing. 
The following attributes must be updated to reflect your actual environment:
- `rest.security.issuer-uri` on Line 61
- `security.oauth2.resource.id` on Line 71
- `security.oauth2.resource.jwt.key-value` on Line 75

```
# oc delete all -lapp=supplier-api
oc new-app openjdk-8-rhel8:latest~https://github.com/aelkz/microservices-security.git --name=supplier-api --context-dir=/supplier --build-env='MAVEN_MIRROR_URL='${MAVEN_URL} -e MAVEN_MIRROR_URL=${MAVEN_URL}

oc patch svc supplier-api -p '{"spec":{"ports":[{"name":"http","port":8080,"protocol":"TCP","targetPort":8080}]}}'

oc label svc supplier-api monitor=springboot2-api
```

Deploy `product-api`

<b>NOTE</b>. Please check all settings on `application.yaml` file before continuing. 
The following attributes must be updated to reflect your actual environment:
- `rest.security.issuer-uri` on Line 61
- `security.oauth2.resource.jwt.key-value` on Line 75

```
# oc delete all -lapp=product-api
oc new-app openjdk-8-rhel8:latest~https://github.com/aelkz/microservices-security.git --name=product-api --context-dir=/product --build-env='MAVEN_MIRROR_URL='${MAVEN_URL} -e MAVEN_MIRROR_URL=${MAVEN_URL}

oc patch svc product-api -p '{"spec":{"ports":[{"name":"http","port":8080,"protocol":"TCP","targetPort":8080}]}}'

oc label svc product-api monitor=springboot2-api
```

### `SECURITY LAB: STEP 16 - INTEGRATION DEPLOYMENT (FUSE)`
Now that the microservices APIs are deployed, let’s deploy the integration layer.

```sh
# import a new spring-boot camel template
curl -o s2i-microservices-fuse74-spring-boot-camel.yaml -s https://raw.githubusercontent.com/aelkz/microservices-security/master/_configuration/openshift/s2i-microservices-fuse74-spring-boot-camel.yaml

oc delete template s2i-microservices-fuse74-spring-boot-camel -n ${PROJECT_NAMESPACE}
oc create -n ${PROJECT_NAMESPACE} -f s2i-microservices-fuse74-spring-boot-camel.yaml

# NOTE. You may want to check the ..selfsigned.yaml template as it uses a customized imagestream for use with self-signed certificates. (see the APPENDIX-README.md for for info)

export NEXUS_NAMESPACE=cicd-devtools
export PROJECT_NAMESPACE=microservices-security
export APP=auth-integration-api
export APP_NAME=auth-integration
export APP_GROUP=com.redhat.microservices
export APP_GIT=https://github.com/aelkz/microservices-security.git
export APP_GIT_BRANCH=master
export MAVEN_URL=http://$(oc get route nexus3 -n ${NEXUS_NAMESPACE} --template='{{ .spec.host }}')/repository/maven-group/
export CUSTOM_TEMPLATE=s2i-microservices-fuse74-spring-boot-camel-selfsigned

# the previous template have some modifications regarding services,route and group definitions.
# oc delete all -lapp=${APP}
oc new-app --template=${CUSTOM_TEMPLATE} --name=${APP} --build-env='MAVEN_MIRROR_URL='${MAVEN_URL} -e MAVEN_MIRROR_URL=${MAVEN_URL} --param GIT_REPO=${APP_GIT} --param APP_NAME=${APP} --param ARTIFACT_DIR=${APP_NAME}/target --param GIT_REF=${APP_GIT_BRANCH} --param MAVEN_ARGS_APPEND='-pl '${APP_NAME}' --also-make'

# check the created services:
# 1 for default app-context and 1 for /metrics endpoint.
oc get svc -n ${PROJECT_NAMESPACE} | grep ${APP_NAME}

# in order to auth-integration-api call the others APIs, we need to change it's configuration:
curl -o application.yaml -s https://raw.githubusercontent.com/aelkz/microservices-security/master/_configuration/openshift/auth-integration/application.yaml

# NOTE. If you have changed the service or application's name, you need to edit and change the downloaded application.yaml file with your definitions.

# create a configmap and mount a volume for auth-integration-api

oc delete configmap ${APP} -n ${PROJECT_NAMESPACE}

oc create configmap ${APP}-config --from-file=application.yaml -n ${PROJECT_NAMESPACE}

oc set volume dc/${APP} --add --overwrite --name=${APP}-config-volume -m /deployments/config -t configmap --configmap-name=${APP}-config -n ${PROJECT_NAMESPACE}
```

### `SECURITY LAB: FINAL STEP`

Open the nodejs webapp into your browser:

```
export MICROSERVICES_NAMESPACE=microservices-security
echo http://$(oc get route nodejs-web -n ${MICROSERVICES_NAMESPACE} --template='{{ .spec.host }}')
```

If you're using a self-signed certificate, the browser will request authorization to open an insecure URL. Navigate through the menus and test all actions clicking on every button to see the final result. If some action return <b>401</b> or <b>403</b> it is probabilly some pending configuration on 3Scale or the credentials on every application. If you get the <b>500</b>, the application maybe is unavailable. Try changing `Jon Doe` roles and check every situation.

I hope you enjoyed this tutorial. The troubleshooting was not easy because of all OAuth2 adapters and security mechanisms involved. Please, let me know if you want to improve something or add more context to this PoC. Thank you!

### `EXTERNAL REFERENCES`

API Key Generator
https://codepen.io/corenominal/pen/rxOmMJ<br>
JWT Key Generator
http://jwt.io<br>
OpenID Connect Debugger
https://openidconnect.net
Generate Plain Old Java Objects from JSON or JSON-Schema<br>
http://www.jsonschema2pojo.org

- - - - - - - - - -
Thanks for reading and taking the time to comment!<br>
Feel free to create a <b>PR</b><br>
[raphael abreu](rabreu@redhat.com)

