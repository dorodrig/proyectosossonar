import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IArea } from '../area.model';
import { AreaService } from '../service/area.service';

@Component({
  templateUrl: './area-delete-dialog.component.html',
})
export class AreaDeleteDialogComponent {
  area?: IArea;

  constructor(protected areaService: AreaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.areaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
