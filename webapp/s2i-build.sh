#!/bin/bash

# https://github.com/openshift/source-to-image/blob/master/docs/cli.md#s2i-build

source_location=.
builder_image=rhscl/nodejs-8-rhel7
tag=s2i-build/jboss-client
flags=-c

s2i build ${source_location} ${builder_image} ${tag} ${flags}
