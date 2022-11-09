import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IArea, Area } from '../area.model';

import { AreaService } from './area.service';

describe('Area Service', () => {
  let service: AreaService;
  let httpMock: HttpTestingController;
  let elemDefault: IArea;
  let expectedResult: IArea | IArea[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AreaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      areaName: 'AAAAAAA',
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

    it('should create a Area', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Area()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Area', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          areaName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Area', () => {
      const patchObject = Object.assign(
        {
          areaName: 'BBBBBB',
        },
        new Area()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Area', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          areaName: 'BBBBBB',
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

    it('should delete a Area', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAreaToCollectionIfMissing', () => {
      it('should add a Area to an empty array', () => {
        const area: IArea = { id: 123 };
        expectedResult = service.addAreaToCollectionIfMissing([], area);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(area);
      });

      it('should not add a Area to an array that contains it', () => {
        const area: IArea = { id: 123 };
        const areaCollection: IArea[] = [
          {
            ...area,
          },
          { id: 456 },
        ];
        expectedResult = service.addAreaToCollectionIfMissing(areaCollection, area);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Area to an array that doesn't contain it", () => {
        const area: IArea = { id: 123 };
        const areaCollection: IArea[] = [{ id: 456 }];
        expectedResult = service.addAreaToCollectionIfMissing(areaCollection, area);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(area);
      });

      it('should add only unique Area to an array', () => {
        const areaArray: IArea[] = [{ id: 123 }, { id: 456 }, { id: 90565 }];
        const areaCollection: IArea[] = [{ id: 123 }];
        expectedResult = service.addAreaToCollectionIfMissing(areaCollection, ...areaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const area: IArea = { id: 123 };
        const area2: IArea = { id: 456 };
        expectedResult = service.addAreaToCollectionIfMissing([], area, area2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(area);
        expect(expectedResult).toContain(area2);
      });

      it('should accept null and undefined values', () => {
        const area: IArea = { id: 123 };
        expectedResult = service.addAreaToCollectionIfMissing([], null, area, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(area);
      });

      it('should return initial array if no Area is added', () => {
        const areaCollection: IArea[] = [{ id: 123 }];
        expectedResult = service.addAreaToCollectionIfMissing(areaCollection, undefined, null);
        expect(expectedResult).toEqual(areaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
