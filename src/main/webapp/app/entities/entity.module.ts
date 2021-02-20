import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'patient',
        loadChildren: () => import('./patient/patient.module').then(m => m.WalluPatientModule),
      },
      {
        path: 'fiche',
        loadChildren: () => import('./fiche/fiche.module').then(m => m.WalluFicheModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class WalluEntityModule {}
