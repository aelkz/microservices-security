# auth-sso-parent

Este projeto tem como objetivo conter a implementação da obtenção do token JWT utilizando o RH-SSO utilizando o grant_type `Client Credentials` [Client Credentials](#referncias).

Nesta implementação de referência iremos utilizar uma biblioteca chamada ```keycloak-admin-client``` com versão compatível com a versão do adapter do RH-SSO, neste momento utilizamos o adapter de versão 7.2.4.

**NOTA**: Ambas as versões necessitam ser compatíveis uma com a outra, caso contrário poderá haver problemas na utilização de classes compartilhadas. Para saber qual a versão do pacote necessitamos verificar a versão contida em cada módulo (ex: ```keycloak-core```) dentro do zip do adapter do keycloak. Neste momento a versão é ```3.4.12.Final-redhat-2```.

# Referências

[Client Credentials](https://www.oauth.com/oauth2-servers/access-tokens/client-credentials/)

Para utilizá-lo defina a seguinte estrutura:

```xml
<parent>
    <groupId>com.microservices.apigateway.security.common</groupId>
    <artifactId>auth-sso-common</artifactId>
    <version>1.0</version>
</parent>
```
