#!/usr/bin/env bash

# https://docs.okd.io/latest/dev_guide/copy_files_to_container.html

pod_name=$(oc get pods --selector app=nodejs-app | { read line1 ; read line2 ; echo "$line2" ; } | awk '{print $1;}')

# directory on pod
deploy_dir=/opt/app-root/src/dist

# exploded war directory
dist_dir=$(pwd)/dist/

oc rsync ${dist_dir} ${pod_name}:${deploy_dir} --watch
