import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEnrollment, Enrollment } from '../enrollment.model';

import { EnrollmentService } from './enrollment.service';

describe('Enrollment Service', () => {
  let service: EnrollmentService;
  let httpMock: HttpTestingController;
  let elemDefault: IEnrollment;
  let expectedResult: IEnrollment | IEnrollment[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EnrollmentService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      registrationNumber: 'AAAAAAA',
      startDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          startDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Enrollment', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          startDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Enrollment()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Enrollment', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          registrationNumber: 'BBBBBB',
          startDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Enrollment', () => {
      const patchObject = Object.assign(
        {
          registrationNumber: 'BBBBBB',
        },
        new Enrollment()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          startDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Enrollment', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          registrationNumber: 'BBBBBB',
          startDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Enrollment', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEnrollmentToCollectionIfMissing', () => {
      it('should add a Enrollment to an empty array', () => {
        const enrollment: IEnrollment = { id: 123 };
        expectedResult = service.addEnrollmentToCollectionIfMissing([], enrollment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(enrollment);
      });

      it('should not add a Enrollment to an array that contains it', () => {
        const enrollment: IEnrollment = { id: 123 };
        const enrollmentCollection: IEnrollment[] = [
          {
            ...enrollment,
          },
          { id: 456 },
        ];
        expectedResult = service.addEnrollmentToCollectionIfMissing(enrollmentCollection, enrollment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Enrollment to an array that doesn't contain it", () => {
        const enrollment: IEnrollment = { id: 123 };
        const enrollmentCollection: IEnrollment[] = [{ id: 456 }];
        expectedResult = service.addEnrollmentToCollectionIfMissing(enrollmentCollection, enrollment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(enrollment);
      });

      it('should add only unique Enrollment to an array', () => {
        const enrollmentArray: IEnrollment[] = [{ id: 123 }, { id: 456 }, { id: 45202 }];
        const enrollmentCollection: IEnrollment[] = [{ id: 123 }];
        expectedResult = service.addEnrollmentToCollectionIfMissing(enrollmentCollection, ...enrollmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const enrollment: IEnrollment = { id: 123 };
        const enrollment2: IEnrollment = { id: 456 };
        expectedResult = service.addEnrollmentToCollectionIfMissing([], enrollment, enrollment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(enrollment);
        expect(expectedResult).toContain(enrollment2);
      });

      it('should accept null and undefined values', () => {
        const enrollment: IEnrollment = { id: 123 };
        expectedResult = service.addEnrollmentToCollectionIfMissing([], null, enrollment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(enrollment);
      });

      it('should return initial array if no Enrollment is added', () => {
        const enrollmentCollection: IEnrollment[] = [{ id: 123 }];
        expectedResult = service.addEnrollmentToCollectionIfMissing(enrollmentCollection, undefined, null);
        expect(expectedResult).toEqual(enrollmentCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
