import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

private _keycloak: Keycloak | undefined;

  constructor() { }

  async init() {
    
  }
}
