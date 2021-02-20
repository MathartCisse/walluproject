import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WalluTestModule } from '../../../test.module';
import { FicheDetailComponent } from 'app/entities/fiche/fiche-detail.component';
import { Fiche } from 'app/shared/model/fiche.model';

describe('Component Tests', () => {
  describe('Fiche Management Detail Component', () => {
    let comp: FicheDetailComponent;
    let fixture: ComponentFixture<FicheDetailComponent>;
    const route = ({ data: of({ fiche: new Fiche(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WalluTestModule],
        declarations: [FicheDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FicheDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FicheDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fiche on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fiche).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
