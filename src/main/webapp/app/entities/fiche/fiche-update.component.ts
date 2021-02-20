import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFiche, Fiche } from 'app/shared/model/fiche.model';
import { FicheService } from './fiche.service';

@Component({
  selector: 'jhi-fiche-update',
  templateUrl: './fiche-update.component.html',
})
export class FicheUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    groupeSanguin: [],
    poids: [],
    taille: [],
  });

  constructor(protected ficheService: FicheService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fiche }) => {
      this.updateForm(fiche);
    });
  }

  updateForm(fiche: IFiche): void {
    this.editForm.patchValue({
      id: fiche.id,
      groupeSanguin: fiche.groupeSanguin,
      poids: fiche.poids,
      taille: fiche.taille,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fiche = this.createFromForm();
    if (fiche.id !== undefined) {
      this.subscribeToSaveResponse(this.ficheService.update(fiche));
    } else {
      this.subscribeToSaveResponse(this.ficheService.create(fiche));
    }
  }

  private createFromForm(): IFiche {
    return {
      ...new Fiche(),
      id: this.editForm.get(['id'])!.value,
      groupeSanguin: this.editForm.get(['groupeSanguin'])!.value,
      poids: this.editForm.get(['poids'])!.value,
      taille: this.editForm.get(['taille'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFiche>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
