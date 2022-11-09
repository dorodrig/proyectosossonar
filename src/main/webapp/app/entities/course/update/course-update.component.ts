import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICourse, Course } from '../course.model';
import { CourseService } from '../service/course.service';
import { IArea } from 'app/entities/area/area.model';
import { AreaService } from 'app/entities/area/service/area.service';

@Component({
  selector: 'ceet-course-update',
  templateUrl: './course-update.component.html',
})
export class CourseUpdateComponent implements OnInit {
  isSaving = false;

  areasSharedCollection: IArea[] = [];

  editForm = this.fb.group({
    id: [],
    nameCourse: [null, [Validators.required, Validators.maxLength(30)]],
    areas: [],
  });

  constructor(
    protected courseService: CourseService,
    protected areaService: AreaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ course }) => {
      this.updateForm(course);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const course = this.createFromForm();
    if (course.id !== undefined) {
      this.subscribeToSaveResponse(this.courseService.update(course));
    } else {
      this.subscribeToSaveResponse(this.courseService.create(course));
    }
  }

  trackAreaById(_index: number, item: IArea): number {
    return item.id!;
  }

  getSelectedArea(option: IArea, selectedVals?: IArea[]): IArea {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourse>>): void {
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

  protected updateForm(course: ICourse): void {
    this.editForm.patchValue({
      id: course.id,
      nameCourse: course.nameCourse,
      areas: course.areas,
    });

    this.areasSharedCollection = this.areaService.addAreaToCollectionIfMissing(this.areasSharedCollection, ...(course.areas ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.areaService
      .query()
      .pipe(map((res: HttpResponse<IArea[]>) => res.body ?? []))
      .pipe(map((areas: IArea[]) => this.areaService.addAreaToCollectionIfMissing(areas, ...(this.editForm.get('areas')!.value ?? []))))
      .subscribe((areas: IArea[]) => (this.areasSharedCollection = areas));
  }

  protected createFromForm(): ICourse {
    return {
      ...new Course(),
      id: this.editForm.get(['id'])!.value,
      nameCourse: this.editForm.get(['nameCourse'])!.value,
      areas: this.editForm.get(['areas'])!.value,
    };
  }
}
