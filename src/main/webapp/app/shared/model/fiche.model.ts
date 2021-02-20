export interface IFiche {
  id?: number;
  groupeSanguin?: string;
  poids?: number;
  taille?: number;
}

export class Fiche implements IFiche {
  constructor(public id?: number, public groupeSanguin?: string, public poids?: number, public taille?: number) {}
}
