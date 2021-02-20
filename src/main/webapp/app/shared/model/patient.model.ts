import { IFiche } from 'app/shared/model/fiche.model';

export interface IPatient {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  age?: number;
  salary?: number;
  idCardNumber?: number;
  patient?: IFiche;
}

export class Patient implements IPatient {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public phoneNumber?: string,
    public age?: number,
    public salary?: number,
    public idCardNumber?: number,
    public patient?: IFiche
  ) {}
}
