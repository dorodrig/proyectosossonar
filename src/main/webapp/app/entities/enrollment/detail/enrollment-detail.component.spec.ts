import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EnrollmentDetailComponent } from './enrollment-detail.component';

describe('Enrollment Management Detail Component', () => {
  let comp: EnrollmentDetailComponent;
  let fixture: ComponentFixture<EnrollmentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EnrollmentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ enrollment: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EnrollmentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EnrollmentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load enrollment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.enrollment).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
