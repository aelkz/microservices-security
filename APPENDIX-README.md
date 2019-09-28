### Using a custom FUSE image:

You could use the Dockerfile inside the _configuration folder.

This Dockerfile contains an example on how to add a custom CA certificate.

In my environment I needed to add the RHSSO self-signed certificate in order to avoid http 401 for keycloak-adapter validation during realm public key retrieval.
