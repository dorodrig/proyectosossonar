import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEnrollment, Enrollment } from '../enrollment.model';
import { EnrollmentService } from '../service/enrollment.service';

@Component({
  selector: 'ceet-enrollment-update',
  templateUrl: './enrollment-update.component.html',
})
export class EnrollmentUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    registrationNumber: [null, [Validators.required, Validators.maxLength(50)]],
    startDate: [],
  });

  constructor(protected enrollmentService: EnrollmentService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enrollment }) => {
      if (enrollment.id === undefined) {
        const today = dayjs().startOf('day');
        enrollment.startDate = today;
      }

      this.updateForm(enrollment);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enrollment = this.createFromForm();
    if (enrollment.id !== undefined) {
      this.subscribeToSaveResponse(this.enrollmentService.update(enrollment));
    } else {
      this.subscribeToSaveResponse(this.enrollmentService.create(enrollment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnrollment>>): void {
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

  protected updateForm(enrollment: IEnrollment): void {
    this.editForm.patchValue({
      id: enrollment.id,
      registrationNumber: enrollment.registrationNumber,
      startDate: enrollment.startDate ? enrollment.startDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IEnrollment {
    return {
      ...new Enrollment(),
      id: this.editForm.get(['id'])!.value,
      registrationNumber: this.editForm.get(['registrationNumber'])!.value,
      startDate: this.editForm.get(['startDate'])!.value ? dayjs(this.editForm.get(['startDate'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
