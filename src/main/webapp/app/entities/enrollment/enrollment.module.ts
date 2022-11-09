import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EnrollmentComponent } from './list/enrollment.component';
import { EnrollmentDetailComponent } from './detail/enrollment-detail.component';
import { EnrollmentUpdateComponent } from './update/enrollment-update.component';
import { EnrollmentDeleteDialogComponent } from './delete/enrollment-delete-dialog.component';
import { EnrollmentRoutingModule } from './route/enrollment-routing.module';

@NgModule({
  imports: [SharedModule, EnrollmentRoutingModule],
  declarations: [EnrollmentComponent, EnrollmentDetailComponent, EnrollmentUpdateComponent, EnrollmentDeleteDialogComponent],
  entryComponents: [EnrollmentDeleteDialogComponent],
})
export class EnrollmentModule {}
