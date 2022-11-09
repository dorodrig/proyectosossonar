import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAchievement, Achievement } from '../achievement.model';
import { AchievementService } from '../service/achievement.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { StateAchievement } from 'app/entities/enumerations/state-achievement.model';

@Component({
  selector: 'ceet-achievement-update',
  templateUrl: './achievement-update.component.html',
})
export class AchievementUpdateComponent implements OnInit {
  isSaving = false;
  stateAchievementValues = Object.keys(StateAchievement);

  coursesSharedCollection: ICourse[] = [];

  editForm = this.fb.group({
    id: [],
    achievementDescription: [null, [Validators.required, Validators.maxLength(500)]],
    statusAchievement: [],
    courses: [],
  });

  constructor(
    protected achievementService: AchievementService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ achievement }) => {
      this.updateForm(achievement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const achievement = this.createFromForm();
    if (achievement.id !== undefined) {
      this.subscribeToSaveResponse(this.achievementService.update(achievement));
    } else {
      this.subscribeToSaveResponse(this.achievementService.create(achievement));
    }
  }

  trackCourseById(_index: number, item: ICourse): number {
    return item.id!;
  }

  getSelectedCourse(option: ICourse, selectedVals?: ICourse[]): ICourse {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAchievement>>): void {
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

  protected updateForm(achievement: IAchievement): void {
    this.editForm.patchValue({
      id: achievement.id,
      achievementDescription: achievement.achievementDescription,
      statusAchievement: achievement.statusAchievement,
      courses: achievement.courses,
    });

    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing(
      this.coursesSharedCollection,
      ...(achievement.courses ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(
        map((courses: ICourse[]) =>
          this.courseService.addCourseToCollectionIfMissing(courses, ...(this.editForm.get('courses')!.value ?? []))
        )
      )
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));
  }

  protected createFromForm(): IAchievement {
    return {
      ...new Achievement(),
      id: this.editForm.get(['id'])!.value,
      achievementDescription: this.editForm.get(['achievementDescription'])!.value,
      statusAchievement: this.editForm.get(['statusAchievement'])!.value,
      courses: this.editForm.get(['courses'])!.value,
    };
  }
}
