import { KeycloakProfile, KeycloakInstance } from 'keycloak-js';

export class Auth {
  isLoggedIn?: boolean;
  instance?: KeycloakInstance;
  logoutUrl?: string;
  accountUrl?: string;
  profile?: KeycloakProfile;
}
