import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILevel, Level } from '../level.model';
import { LevelService } from '../service/level.service';
import { IEnrollment } from 'app/entities/enrollment/enrollment.model';
import { EnrollmentService } from 'app/entities/enrollment/service/enrollment.service';

@Component({
  selector: 'ceet-level-update',
  templateUrl: './level-update.component.html',
})
export class LevelUpdateComponent implements OnInit {
  isSaving = false;

  enrollmentsSharedCollection: IEnrollment[] = [];

  editForm = this.fb.group({
    id: [],
    nameCurse: [null, [Validators.required, Validators.maxLength(10)]],
    courseAcronym: [null, [Validators.required, Validators.maxLength(10)]],
    enrollments: [],
  });

  constructor(
    protected levelService: LevelService,
    protected enrollmentService: EnrollmentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ level }) => {
      this.updateForm(level);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const level = this.createFromForm();
    if (level.id !== undefined) {
      this.subscribeToSaveResponse(this.levelService.update(level));
    } else {
      this.subscribeToSaveResponse(this.levelService.create(level));
    }
  }

  trackEnrollmentById(_index: number, item: IEnrollment): number {
    return item.id!;
  }

  getSelectedEnrollment(option: IEnrollment, selectedVals?: IEnrollment[]): IEnrollment {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILevel>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(level: ILevel): void {
    this.editForm.patchValue({
      id: level.id,
      nameCurse: level.nameCurse,
      courseAcronym: level.courseAcronym,
      enrollments: level.enrollments,
    });

    this.enrollmentsSharedCollection = this.enrollmentService.addEnrollmentToCollectionIfMissing(
      this.enrollmentsSharedCollection,
      ...(level.enrollments ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.enrollmentService
      .query()
      .pipe(map((res: HttpResponse<IEnrollment[]>) => res.body ?? []))
      .pipe(
        map((enrollments: IEnrollment[]) =>
          this.enrollmentService.addEnrollmentToCollectionIfMissing(enrollments, ...(this.editForm.get('enrollments')!.value ?? []))
        )
      )
      .subscribe((enrollments: IEnrollment[]) => (this.enrollmentsSharedCollection = enrollments));
  }

  protected createFromForm(): ILevel {
    return {
      ...new Level(),
      id: this.editForm.get(['id'])!.value,
      nameCurse: this.editForm.get(['nameCurse'])!.value,
      courseAcronym: this.editForm.get(['courseAcronym'])!.value,
      enrollments: this.editForm.get(['enrollments'])!.value,
    };
  }
}
