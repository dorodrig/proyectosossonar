import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEnrollment } from '../enrollment.model';
import { EnrollmentService } from '../service/enrollment.service';

@Component({
  templateUrl: './enrollment-delete-dialog.component.html',
})
export class EnrollmentDeleteDialogComponent {
  enrollment?: IEnrollment;

  constructor(protected enrollmentService: EnrollmentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.enrollmentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
