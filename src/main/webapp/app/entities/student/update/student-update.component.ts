import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStudent, Student } from '../student.model';
import { StudentService } from '../service/student.service';
import { IEnrollment } from 'app/entities/enrollment/enrollment.model';
import { EnrollmentService } from 'app/entities/enrollment/service/enrollment.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'ceet-student-update',
  templateUrl: './student-update.component.html',
})
export class StudentUpdateComponent implements OnInit {
  isSaving = false;

  enrollmentsCollection: IEnrollment[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    attendantName: [null, [Validators.required, Validators.maxLength(30)]],
    kin: [null, [Validators.required, Validators.maxLength(20)]],
    enrollment: [],
    user: [],
  });

  constructor(
    protected studentService: StudentService,
    protected enrollmentService: EnrollmentService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ student }) => {
      this.updateForm(student);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const student = this.createFromForm();
    if (student.id !== undefined) {
      this.subscribeToSaveResponse(this.studentService.update(student));
    } else {
      this.subscribeToSaveResponse(this.studentService.create(student));
    }
  }

  trackEnrollmentById(_index: number, item: IEnrollment): number {
    return item.id!;
  }

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudent>>): void {
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

  protected updateForm(student: IStudent): void {
    this.editForm.patchValue({
      id: student.id,
      attendantName: student.attendantName,
      kin: student.kin,
      enrollment: student.enrollment,
      user: student.user,
    });

    this.enrollmentsCollection = this.enrollmentService.addEnrollmentToCollectionIfMissing(this.enrollmentsCollection, student.enrollment);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, student.user);
  }

  protected loadRelationshipsOptions(): void {
    this.enrollmentService
      .query({ filter: 'student-is-null' })
      .pipe(map((res: HttpResponse<IEnrollment[]>) => res.body ?? []))
      .pipe(
        map((enrollments: IEnrollment[]) =>
          this.enrollmentService.addEnrollmentToCollectionIfMissing(enrollments, this.editForm.get('enrollment')!.value)
        )
      )
      .subscribe((enrollments: IEnrollment[]) => (this.enrollmentsCollection = enrollments));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IStudent {
    return {
      ...new Student(),
      id: this.editForm.get(['id'])!.value,
      attendantName: this.editForm.get(['attendantName'])!.value,
      kin: this.editForm.get(['kin'])!.value,
      enrollment: this.editForm.get(['enrollment'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
