import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IFiche } from 'app/shared/model/fiche.model';

type EntityResponseType = HttpResponse<IFiche>;
type EntityArrayResponseType = HttpResponse<IFiche[]>;

@Injectable({ providedIn: 'root' })
export class FicheService {
  public resourceUrl = SERVER_API_URL + 'api/fiches';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/fiches';

  constructor(protected http: HttpClient) {}

  create(fiche: IFiche): Observable<EntityResponseType> {
    return this.http.post<IFiche>(this.resourceUrl, fiche, { observe: 'response' });
  }

  update(fiche: IFiche): Observable<EntityResponseType> {
    return this.http.put<IFiche>(this.resourceUrl, fiche, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFiche>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFiche[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFiche[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
