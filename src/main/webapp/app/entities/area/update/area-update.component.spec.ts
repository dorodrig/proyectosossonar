import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AreaService } from '../service/area.service';
import { IArea, Area } from '../area.model';
import { ILevel } from 'app/entities/level/level.model';
import { LevelService } from 'app/entities/level/service/level.service';

import { AreaUpdateComponent } from './area-update.component';

describe('Area Management Update Component', () => {
  let comp: AreaUpdateComponent;
  let fixture: ComponentFixture<AreaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let areaService: AreaService;
  let levelService: LevelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AreaUpdateComponent],
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
      .overrideTemplate(AreaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AreaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    areaService = TestBed.inject(AreaService);
    levelService = TestBed.inject(LevelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Level query and add missing value', () => {
      const area: IArea = { id: 456 };
      const levels: ILevel[] = [{ id: 72939 }];
      area.levels = levels;

      const levelCollection: ILevel[] = [{ id: 25124 }];
      jest.spyOn(levelService, 'query').mockReturnValue(of(new HttpResponse({ body: levelCollection })));
      const additionalLevels = [...levels];
      const expectedCollection: ILevel[] = [...additionalLevels, ...levelCollection];
      jest.spyOn(levelService, 'addLevelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ area });
      comp.ngOnInit();

      expect(levelService.query).toHaveBeenCalled();
      expect(levelService.addLevelToCollectionIfMissing).toHaveBeenCalledWith(levelCollection, ...additionalLevels);
      expect(comp.levelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const area: IArea = { id: 456 };
      const levels: ILevel = { id: 57588 };
      area.levels = [levels];

      activatedRoute.data = of({ area });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(area));
      expect(comp.levelsSharedCollection).toContain(levels);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Area>>();
      const area = { id: 123 };
      jest.spyOn(areaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ area });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: area }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(areaService.update).toHaveBeenCalledWith(area);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Area>>();
      const area = new Area();
      jest.spyOn(areaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ area });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: area }));
      saveSubject.complete();

      // THEN
      expect(areaService.create).toHaveBeenCalledWith(area);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Area>>();
      const area = { id: 123 };
      jest.spyOn(areaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ area });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(areaService.update).toHaveBeenCalledWith(area);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackLevelById', () => {
      it('Should return tracked Level primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLevelById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedLevel', () => {
      it('Should return option if no Level is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedLevel(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Level for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedLevel(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Level is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedLevel(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
