import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WalluSharedModule } from 'app/shared/shared.module';
import { FicheComponent } from './fiche.component';
import { FicheDetailComponent } from './fiche-detail.component';
import { FicheUpdateComponent } from './fiche-update.component';
import { FicheDeleteDialogComponent } from './fiche-delete-dialog.component';
import { ficheRoute } from './fiche.route';

@NgModule({
  imports: [WalluSharedModule, RouterModule.forChild(ficheRoute)],
  declarations: [FicheComponent, FicheDetailComponent, FicheUpdateComponent, FicheDeleteDialogComponent],
  entryComponents: [FicheDeleteDialogComponent],
})
export class WalluFicheModule {}
