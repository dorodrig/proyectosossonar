import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEnrollment } from '../enrollment.model';

@Component({
  selector: 'ceet-enrollment-detail',
  templateUrl: './enrollment-detail.component.html',
})
export class EnrollmentDetailComponent implements OnInit {
  enrollment: IEnrollment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enrollment }) => {
      this.enrollment = enrollment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
