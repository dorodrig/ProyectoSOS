import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CeetUserComponent } from './list/ceet-user.component';
import { CeetUserDetailComponent } from './detail/ceet-user-detail.component';
import { CeetUserUpdateComponent } from './update/ceet-user-update.component';
import { CeetUserDeleteDialogComponent } from './delete/ceet-user-delete-dialog.component';
import { CeetUserRoutingModule } from './route/ceet-user-routing.module';

@NgModule({
  imports: [SharedModule, CeetUserRoutingModule],
  declarations: [CeetUserComponent, CeetUserDetailComponent, CeetUserUpdateComponent, CeetUserDeleteDialogComponent],
  entryComponents: [CeetUserDeleteDialogComponent],
})
export class CeetUserModule {}
