import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CeetUserComponent } from '../list/ceet-user.component';
import { CeetUserDetailComponent } from '../detail/ceet-user-detail.component';
import { CeetUserUpdateComponent } from '../update/ceet-user-update.component';
import { CeetUserRoutingResolveService } from './ceet-user-routing-resolve.service';

const ceetUserRoute: Routes = [
  {
    path: '',
    component: CeetUserComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CeetUserDetailComponent,
    resolve: {
      ceetUser: CeetUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CeetUserUpdateComponent,
    resolve: {
      ceetUser: CeetUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CeetUserUpdateComponent,
    resolve: {
      ceetUser: CeetUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ceetUserRoute)],
  exports: [RouterModule],
})
export class CeetUserRoutingModule {}
