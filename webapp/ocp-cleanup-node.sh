#!/bin/bash

proj_name="$(oc whoami)-ntier"
oc project ${proj_name}
oc delete all --selector app=nodejs-app
