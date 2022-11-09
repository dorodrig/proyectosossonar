import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CourseService } from '../service/course.service';
import { ICourse, Course } from '../course.model';
import { IArea } from 'app/entities/area/area.model';
import { AreaService } from 'app/entities/area/service/area.service';

import { CourseUpdateComponent } from './course-update.component';

describe('Course Management Update Component', () => {
  let comp: CourseUpdateComponent;
  let fixture: ComponentFixture<CourseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let courseService: CourseService;
  let areaService: AreaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CourseUpdateComponent],
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
      .overrideTemplate(CourseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CourseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    courseService = TestBed.inject(CourseService);
    areaService = TestBed.inject(AreaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Area query and add missing value', () => {
      const course: ICourse = { id: 456 };
      const areas: IArea[] = [{ id: 25474 }];
      course.areas = areas;

      const areaCollection: IArea[] = [{ id: 46398 }];
      jest.spyOn(areaService, 'query').mockReturnValue(of(new HttpResponse({ body: areaCollection })));
      const additionalAreas = [...areas];
      const expectedCollection: IArea[] = [...additionalAreas, ...areaCollection];
      jest.spyOn(areaService, 'addAreaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(areaService.query).toHaveBeenCalled();
      expect(areaService.addAreaToCollectionIfMissing).toHaveBeenCalledWith(areaCollection, ...additionalAreas);
      expect(comp.areasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const course: ICourse = { id: 456 };
      const areas: IArea = { id: 2710 };
      course.areas = [areas];

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(course));
      expect(comp.areasSharedCollection).toContain(areas);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Course>>();
      const course = { id: 123 };
      jest.spyOn(courseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: course }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(courseService.update).toHaveBeenCalledWith(course);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Course>>();
      const course = new Course();
      jest.spyOn(courseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: course }));
      saveSubject.complete();

      // THEN
      expect(courseService.create).toHaveBeenCalledWith(course);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Course>>();
      const course = { id: 123 };
      jest.spyOn(courseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(courseService.update).toHaveBeenCalledWith(course);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAreaById', () => {
      it('Should return tracked Area primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAreaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedArea', () => {
      it('Should return option if no Area is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedArea(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Area for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedArea(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Area is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedArea(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
