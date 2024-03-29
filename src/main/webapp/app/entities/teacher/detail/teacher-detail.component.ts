import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITeacher } from '../teacher.model';

@Component({
  selector: 'ceet-teacher-detail',
  templateUrl: './teacher-detail.component.html',
})
export class TeacherDetailComponent implements OnInit {
  teacher: ITeacher | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teacher }) => {
      this.teacher = teacher;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
