import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFiche } from 'app/shared/model/fiche.model';
import { FicheService } from './fiche.service';

@Component({
  templateUrl: './fiche-delete-dialog.component.html',
})
export class FicheDeleteDialogComponent {
  fiche?: IFiche;

  constructor(protected ficheService: FicheService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ficheService.delete(id).subscribe(() => {
      this.eventManager.broadcast('ficheListModification');
      this.activeModal.close();
    });
  }
}
