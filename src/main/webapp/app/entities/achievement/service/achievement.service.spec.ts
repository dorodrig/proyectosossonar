import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { StateAchievement } from 'app/entities/enumerations/state-achievement.model';
import { IAchievement, Achievement } from '../achievement.model';

import { AchievementService } from './achievement.service';

describe('Achievement Service', () => {
  let service: AchievementService;
  let httpMock: HttpTestingController;
  let elemDefault: IAchievement;
  let expectedResult: IAchievement | IAchievement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AchievementService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      achievementDescription: 'AAAAAAA',
      statusAchievement: StateAchievement.Active,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Achievement', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Achievement()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Achievement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          achievementDescription: 'BBBBBB',
          statusAchievement: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Achievement', () => {
      const patchObject = Object.assign(
        {
          statusAchievement: 'BBBBBB',
        },
        new Achievement()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Achievement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          achievementDescription: 'BBBBBB',
          statusAchievement: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Achievement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAchievementToCollectionIfMissing', () => {
      it('should add a Achievement to an empty array', () => {
        const achievement: IAchievement = { id: 123 };
        expectedResult = service.addAchievementToCollectionIfMissing([], achievement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(achievement);
      });

      it('should not add a Achievement to an array that contains it', () => {
        const achievement: IAchievement = { id: 123 };
        const achievementCollection: IAchievement[] = [
          {
            ...achievement,
          },
          { id: 456 },
        ];
        expectedResult = service.addAchievementToCollectionIfMissing(achievementCollection, achievement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Achievement to an array that doesn't contain it", () => {
        const achievement: IAchievement = { id: 123 };
        const achievementCollection: IAchievement[] = [{ id: 456 }];
        expectedResult = service.addAchievementToCollectionIfMissing(achievementCollection, achievement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(achievement);
      });

      it('should add only unique Achievement to an array', () => {
        const achievementArray: IAchievement[] = [{ id: 123 }, { id: 456 }, { id: 55559 }];
        const achievementCollection: IAchievement[] = [{ id: 123 }];
        expectedResult = service.addAchievementToCollectionIfMissing(achievementCollection, ...achievementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const achievement: IAchievement = { id: 123 };
        const achievement2: IAchievement = { id: 456 };
        expectedResult = service.addAchievementToCollectionIfMissing([], achievement, achievement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(achievement);
        expect(expectedResult).toContain(achievement2);
      });

      it('should accept null and undefined values', () => {
        const achievement: IAchievement = { id: 123 };
        expectedResult = service.addAchievementToCollectionIfMissing([], null, achievement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(achievement);
      });

      it('should return initial array if no Achievement is added', () => {
        const achievementCollection: IAchievement[] = [{ id: 123 }];
        expectedResult = service.addAchievementToCollectionIfMissing(achievementCollection, undefined, null);
        expect(expectedResult).toEqual(achievementCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
