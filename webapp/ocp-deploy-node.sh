#!/bin/bash

# prefixing project with user to allow multiple people building the same project on the same cluster
proj_name="$(oc whoami)-ntier"
oc project ${proj_name}

oc new-app https://github.com/mechevarria/ocp-sso \
--context-dir=node \
--name=nodejs-app
 
oc create route edge --service=nodejs-app --cert=server.cert --key=server.key

oc set env --from=configmap/ntier-config dc/nodejs-app
