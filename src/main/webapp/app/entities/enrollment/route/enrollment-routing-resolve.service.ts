import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEnrollment, Enrollment } from '../enrollment.model';
import { EnrollmentService } from '../service/enrollment.service';

@Injectable({ providedIn: 'root' })
export class EnrollmentRoutingResolveService implements Resolve<IEnrollment> {
  constructor(protected service: EnrollmentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEnrollment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((enrollment: HttpResponse<Enrollment>) => {
          if (enrollment.body) {
            return of(enrollment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Enrollment());
  }
}
