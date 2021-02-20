import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPatient, Patient } from 'app/shared/model/patient.model';
import { PatientService } from './patient.service';
import { IFiche } from 'app/shared/model/fiche.model';
import { FicheService } from 'app/entities/fiche/fiche.service';

@Component({
  selector: 'jhi-patient-update',
  templateUrl: './patient-update.component.html',
})
export class PatientUpdateComponent implements OnInit {
  isSaving = false;
  patients: IFiche[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [],
    lastName: [],
    email: [],
    phoneNumber: [],
    age: [],
    salary: [],
    idCardNumber: [],
    patient: [],
  });

  constructor(
    protected patientService: PatientService,
    protected ficheService: FicheService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patient }) => {
      this.updateForm(patient);

      this.ficheService
        .query({ filter: 'patient-is-null' })
        .pipe(
          map((res: HttpResponse<IFiche[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IFiche[]) => {
          if (!patient.patient || !patient.patient.id) {
            this.patients = resBody;
          } else {
            this.ficheService
              .find(patient.patient.id)
              .pipe(
                map((subRes: HttpResponse<IFiche>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IFiche[]) => (this.patients = concatRes));
          }
        });
    });
  }

  updateForm(patient: IPatient): void {
    this.editForm.patchValue({
      id: patient.id,
      firstName: patient.firstName,
      lastName: patient.lastName,
      email: patient.email,
      phoneNumber: patient.phoneNumber,
      age: patient.age,
      salary: patient.salary,
      idCardNumber: patient.idCardNumber,
      patient: patient.patient,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const patient = this.createFromForm();
    if (patient.id !== undefined) {
      this.subscribeToSaveResponse(this.patientService.update(patient));
    } else {
      this.subscribeToSaveResponse(this.patientService.create(patient));
    }
  }

  private createFromForm(): IPatient {
    return {
      ...new Patient(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      age: this.editForm.get(['age'])!.value,
      salary: this.editForm.get(['salary'])!.value,
      idCardNumber: this.editForm.get(['idCardNumber'])!.value,
      patient: this.editForm.get(['patient'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatient>>): void {
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

  trackById(index: number, item: IFiche): any {
    return item.id;
  }
}
