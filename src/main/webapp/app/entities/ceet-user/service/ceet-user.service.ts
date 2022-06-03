import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICeetUser, getCeetUserIdentifier } from '../ceet-user.model';

export type EntityResponseType = HttpResponse<ICeetUser>;
export type EntityArrayResponseType = HttpResponse<ICeetUser[]>;

@Injectable({ providedIn: 'root' })
export class CeetUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ceet-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ceetUser: ICeetUser): Observable<EntityResponseType> {
    return this.http.post<ICeetUser>(this.resourceUrl, ceetUser, { observe: 'response' });
  }

  update(ceetUser: ICeetUser): Observable<EntityResponseType> {
    return this.http.put<ICeetUser>(`${this.resourceUrl}/${getCeetUserIdentifier(ceetUser) as number}`, ceetUser, { observe: 'response' });
  }

  partialUpdate(ceetUser: ICeetUser): Observable<EntityResponseType> {
    return this.http.patch<ICeetUser>(`${this.resourceUrl}/${getCeetUserIdentifier(ceetUser) as number}`, ceetUser, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICeetUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICeetUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCeetUserToCollectionIfMissing(ceetUserCollection: ICeetUser[], ...ceetUsersToCheck: (ICeetUser | null | undefined)[]): ICeetUser[] {
    const ceetUsers: ICeetUser[] = ceetUsersToCheck.filter(isPresent);
    if (ceetUsers.length > 0) {
      const ceetUserCollectionIdentifiers = ceetUserCollection.map(ceetUserItem => getCeetUserIdentifier(ceetUserItem)!);
      const ceetUsersToAdd = ceetUsers.filter(ceetUserItem => {
        const ceetUserIdentifier = getCeetUserIdentifier(ceetUserItem);
        if (ceetUserIdentifier == null || ceetUserCollectionIdentifiers.includes(ceetUserIdentifier)) {
          return false;
        }
        ceetUserCollectionIdentifiers.push(ceetUserIdentifier);
        return true;
      });
      return [...ceetUsersToAdd, ...ceetUserCollection];
    }
    return ceetUserCollection;
  }
}
