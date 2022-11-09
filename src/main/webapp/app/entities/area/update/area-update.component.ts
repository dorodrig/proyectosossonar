import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IArea, Area } from '../area.model';
import { AreaService } from '../service/area.service';
import { ILevel } from 'app/entities/level/level.model';
import { LevelService } from 'app/entities/level/service/level.service';

@Component({
  selector: 'ceet-area-update',
  templateUrl: './area-update.component.html',
})
export class AreaUpdateComponent implements OnInit {
  isSaving = false;

  levelsSharedCollection: ILevel[] = [];

  editForm = this.fb.group({
    id: [],
    areaName: [null, [Validators.required, Validators.maxLength(30)]],
    levels: [],
  });

  constructor(
    protected areaService: AreaService,
    protected levelService: LevelService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ area }) => {
      this.updateForm(area);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const area = this.createFromForm();
    if (area.id !== undefined) {
      this.subscribeToSaveResponse(this.areaService.update(area));
    } else {
      this.subscribeToSaveResponse(this.areaService.create(area));
    }
  }

  trackLevelById(_index: number, item: ILevel): number {
    return item.id!;
  }

  getSelectedLevel(option: ILevel, selectedVals?: ILevel[]): ILevel {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArea>>): void {
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

  protected updateForm(area: IArea): void {
    this.editForm.patchValue({
      id: area.id,
      areaName: area.areaName,
      levels: area.levels,
    });

    this.levelsSharedCollection = this.levelService.addLevelToCollectionIfMissing(this.levelsSharedCollection, ...(area.levels ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.levelService
      .query()
      .pipe(map((res: HttpResponse<ILevel[]>) => res.body ?? []))
      .pipe(
        map((levels: ILevel[]) => this.levelService.addLevelToCollectionIfMissing(levels, ...(this.editForm.get('levels')!.value ?? [])))
      )
      .subscribe((levels: ILevel[]) => (this.levelsSharedCollection = levels));
  }

  protected createFromForm(): IArea {
    return {
      ...new Area(),
      id: this.editForm.get(['id'])!.value,
      areaName: this.editForm.get(['areaName'])!.value,
      levels: this.editForm.get(['levels'])!.value,
    };
  }
}
