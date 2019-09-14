import { Injectable } from '@angular/core';
import { KeycloakService, KeycloakOptions } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class AppInitService {
  private options: KeycloakOptions;

  // _env is defined in env.js
  constructor(private keycloak: KeycloakService) {
    this.options = {
      config: {
        realm: window['_env'].realm,
        url: window['_env'].url,
        clientId: window['_env'].clientId
      },
      initOptions: {
        onLoad: 'login-required'
      }
    };
  }

  init(): Promise<any> {
    let promise: Promise<any>;

    if (window['_env'].enabled === 'false') {
      console.log('keycloak is not enabled');
      promise = Promise.resolve(true);
    } else {
      promise = this.keycloak.init(this.options);
    }

    return promise;
  }
}
