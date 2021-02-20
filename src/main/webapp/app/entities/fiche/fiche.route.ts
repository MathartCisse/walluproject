import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFiche, Fiche } from 'app/shared/model/fiche.model';
import { FicheService } from './fiche.service';
import { FicheComponent } from './fiche.component';
import { FicheDetailComponent } from './fiche-detail.component';
import { FicheUpdateComponent } from './fiche-update.component';

@Injectable({ providedIn: 'root' })
export class FicheResolve implements Resolve<IFiche> {
  constructor(private service: FicheService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFiche> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((fiche: HttpResponse<Fiche>) => {
          if (fiche.body) {
            return of(fiche.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Fiche());
  }
}

export const ficheRoute: Routes = [
  {
    path: '',
    component: FicheComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'walluApp.fiche.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FicheDetailComponent,
    resolve: {
      fiche: FicheResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'walluApp.fiche.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FicheUpdateComponent,
    resolve: {
      fiche: FicheResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'walluApp.fiche.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FicheUpdateComponent,
    resolve: {
      fiche: FicheResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'walluApp.fiche.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
