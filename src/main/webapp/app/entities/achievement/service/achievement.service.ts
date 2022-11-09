import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAchievement, getAchievementIdentifier } from '../achievement.model';

export type EntityResponseType = HttpResponse<IAchievement>;
export type EntityArrayResponseType = HttpResponse<IAchievement[]>;

@Injectable({ providedIn: 'root' })
export class AchievementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/achievements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(achievement: IAchievement): Observable<EntityResponseType> {
    return this.http.post<IAchievement>(this.resourceUrl, achievement, { observe: 'response' });
  }

  update(achievement: IAchievement): Observable<EntityResponseType> {
    return this.http.put<IAchievement>(`${this.resourceUrl}/${getAchievementIdentifier(achievement) as number}`, achievement, {
      observe: 'response',
    });
  }

  partialUpdate(achievement: IAchievement): Observable<EntityResponseType> {
    return this.http.patch<IAchievement>(`${this.resourceUrl}/${getAchievementIdentifier(achievement) as number}`, achievement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAchievement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAchievement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAchievementToCollectionIfMissing(
    achievementCollection: IAchievement[],
    ...achievementsToCheck: (IAchievement | null | undefined)[]
  ): IAchievement[] {
    const achievements: IAchievement[] = achievementsToCheck.filter(isPresent);
    if (achievements.length > 0) {
      const achievementCollectionIdentifiers = achievementCollection.map(achievementItem => getAchievementIdentifier(achievementItem)!);
      const achievementsToAdd = achievements.filter(achievementItem => {
        const achievementIdentifier = getAchievementIdentifier(achievementItem);
        if (achievementIdentifier == null || achievementCollectionIdentifiers.includes(achievementIdentifier)) {
          return false;
        }
        achievementCollectionIdentifiers.push(achievementIdentifier);
        return true;
      });
      return [...achievementsToAdd, ...achievementCollection];
    }
    return achievementCollection;
  }
}
