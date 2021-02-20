import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFiche } from 'app/shared/model/fiche.model';

@Component({
  selector: 'jhi-fiche-detail',
  templateUrl: './fiche-detail.component.html',
})
export class FicheDetailComponent implements OnInit {
  fiche: IFiche | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fiche }) => (this.fiche = fiche));
  }

  previousState(): void {
    window.history.back();
  }
}
