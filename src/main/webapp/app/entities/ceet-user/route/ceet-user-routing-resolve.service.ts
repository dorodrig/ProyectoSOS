import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICeetUser, CeetUser } from '../ceet-user.model';
import { CeetUserService } from '../service/ceet-user.service';

@Injectable({ providedIn: 'root' })
export class CeetUserRoutingResolveService implements Resolve<ICeetUser> {
  constructor(protected service: CeetUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICeetUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ceetUser: HttpResponse<CeetUser>) => {
          if (ceetUser.body) {
            return of(ceetUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CeetUser());
  }
}
