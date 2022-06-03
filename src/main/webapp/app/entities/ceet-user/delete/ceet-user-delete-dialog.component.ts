import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICeetUser } from '../ceet-user.model';
import { CeetUserService } from '../service/ceet-user.service';

@Component({
  templateUrl: './ceet-user-delete-dialog.component.html',
})
export class CeetUserDeleteDialogComponent {
  ceetUser?: ICeetUser;

  constructor(protected ceetUserService: CeetUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ceetUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
