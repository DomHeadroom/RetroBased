/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from './base-service';
import { ApiConfiguration } from './api-configuration';
import { StrictHttpResponse } from './strict-http-response';

import { getSellerProducts } from './fn/seller-controller/get-seller-products';
import { GetSellerProducts$Params } from './fn/seller-controller/get-seller-products';

@Injectable({ providedIn: 'root' })
export class SellerControllerService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `getSellerProducts()` */
  static readonly GetSellerProductsPath = '/seller/public/{seller}/products';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getSellerProducts()` instead.
   *
   * This method doesn't expect any request body.
   */
  getSellerProducts$Response(params: GetSellerProducts$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return getSellerProducts(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getSellerProducts$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getSellerProducts(params: GetSellerProducts$Params, context?: HttpContext): Observable<{
}> {
    return this.getSellerProducts$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

}