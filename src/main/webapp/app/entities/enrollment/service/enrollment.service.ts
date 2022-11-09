import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEnrollment, getEnrollmentIdentifier } from '../enrollment.model';

export type EntityResponseType = HttpResponse<IEnrollment>;
export type EntityArrayResponseType = HttpResponse<IEnrollment[]>;

@Injectable({ providedIn: 'root' })
export class EnrollmentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/enrollments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(enrollment: IEnrollment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(enrollment);
    return this.http
      .post<IEnrollment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(enrollment: IEnrollment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(enrollment);
    return this.http
      .put<IEnrollment>(`${this.resourceUrl}/${getEnrollmentIdentifier(enrollment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(enrollment: IEnrollment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(enrollment);
    return this.http
      .patch<IEnrollment>(`${this.resourceUrl}/${getEnrollmentIdentifier(enrollment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEnrollment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEnrollment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEnrollmentToCollectionIfMissing(
    enrollmentCollection: IEnrollment[],
    ...enrollmentsToCheck: (IEnrollment | null | undefined)[]
  ): IEnrollment[] {
    const enrollments: IEnrollment[] = enrollmentsToCheck.filter(isPresent);
    if (enrollments.length > 0) {
      const enrollmentCollectionIdentifiers = enrollmentCollection.map(enrollmentItem => getEnrollmentIdentifier(enrollmentItem)!);
      const enrollmentsToAdd = enrollments.filter(enrollmentItem => {
        const enrollmentIdentifier = getEnrollmentIdentifier(enrollmentItem);
        if (enrollmentIdentifier == null || enrollmentCollectionIdentifiers.includes(enrollmentIdentifier)) {
          return false;
        }
        enrollmentCollectionIdentifiers.push(enrollmentIdentifier);
        return true;
      });
      return [...enrollmentsToAdd, ...enrollmentCollection];
    }
    return enrollmentCollection;
  }

  protected convertDateFromClient(enrollment: IEnrollment): IEnrollment {
    return Object.assign({}, enrollment, {
      startDate: enrollment.startDate?.isValid() ? enrollment.startDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((enrollment: IEnrollment) => {
        enrollment.startDate = enrollment.startDate ? dayjs(enrollment.startDate) : undefined;
      });
    }
    return res;
  }
}
