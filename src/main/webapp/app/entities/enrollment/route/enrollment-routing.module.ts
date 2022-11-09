import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EnrollmentComponent } from '../list/enrollment.component';
import { EnrollmentDetailComponent } from '../detail/enrollment-detail.component';
import { EnrollmentUpdateComponent } from '../update/enrollment-update.component';
import { EnrollmentRoutingResolveService } from './enrollment-routing-resolve.service';

const enrollmentRoute: Routes = [
  {
    path: '',
    component: EnrollmentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EnrollmentDetailComponent,
    resolve: {
      enrollment: EnrollmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EnrollmentUpdateComponent,
    resolve: {
      enrollment: EnrollmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EnrollmentUpdateComponent,
    resolve: {
      enrollment: EnrollmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(enrollmentRoute)],
  exports: [RouterModule],
})
export class EnrollmentRoutingModule {}
