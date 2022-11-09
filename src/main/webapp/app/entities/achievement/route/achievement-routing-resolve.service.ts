import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAchievement, Achievement } from '../achievement.model';
import { AchievementService } from '../service/achievement.service';

@Injectable({ providedIn: 'root' })
export class AchievementRoutingResolveService implements Resolve<IAchievement> {
  constructor(protected service: AchievementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAchievement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((achievement: HttpResponse<Achievement>) => {
          if (achievement.body) {
            return of(achievement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Achievement());
  }
}
