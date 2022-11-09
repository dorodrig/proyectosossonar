import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LevelService } from '../service/level.service';
import { ILevel, Level } from '../level.model';
import { IEnrollment } from 'app/entities/enrollment/enrollment.model';
import { EnrollmentService } from 'app/entities/enrollment/service/enrollment.service';

import { LevelUpdateComponent } from './level-update.component';

describe('Level Management Update Component', () => {
  let comp: LevelUpdateComponent;
  let fixture: ComponentFixture<LevelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let levelService: LevelService;
  let enrollmentService: EnrollmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LevelUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(LevelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LevelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    levelService = TestBed.inject(LevelService);
    enrollmentService = TestBed.inject(EnrollmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Enrollment query and add missing value', () => {
      const level: ILevel = { id: 456 };
      const enrollments: IEnrollment[] = [{ id: 4265 }];
      level.enrollments = enrollments;

      const enrollmentCollection: IEnrollment[] = [{ id: 29542 }];
      jest.spyOn(enrollmentService, 'query').mockReturnValue(of(new HttpResponse({ body: enrollmentCollection })));
      const additionalEnrollments = [...enrollments];
      const expectedCollection: IEnrollment[] = [...additionalEnrollments, ...enrollmentCollection];
      jest.spyOn(enrollmentService, 'addEnrollmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ level });
      comp.ngOnInit();

      expect(enrollmentService.query).toHaveBeenCalled();
      expect(enrollmentService.addEnrollmentToCollectionIfMissing).toHaveBeenCalledWith(enrollmentCollection, ...additionalEnrollments);
      expect(comp.enrollmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const level: ILevel = { id: 456 };
      const enrollments: IEnrollment = { id: 8299 };
      level.enrollments = [enrollments];

      activatedRoute.data = of({ level });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(level));
      expect(comp.enrollmentsSharedCollection).toContain(enrollments);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Level>>();
      const level = { id: 123 };
      jest.spyOn(levelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ level });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: level }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(levelService.update).toHaveBeenCalledWith(level);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Level>>();
      const level = new Level();
      jest.spyOn(levelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ level });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: level }));
      saveSubject.complete();

      // THEN
      expect(levelService.create).toHaveBeenCalledWith(level);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Level>>();
      const level = { id: 123 };
      jest.spyOn(levelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ level });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(levelService.update).toHaveBeenCalledWith(level);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEnrollmentById', () => {
      it('Should return tracked Enrollment primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEnrollmentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedEnrollment', () => {
      it('Should return option if no Enrollment is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedEnrollment(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Enrollment for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedEnrollment(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Enrollment is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedEnrollment(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
