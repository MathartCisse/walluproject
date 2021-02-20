import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { WalluTestModule } from '../../../test.module';
import { FicheComponent } from 'app/entities/fiche/fiche.component';
import { FicheService } from 'app/entities/fiche/fiche.service';
import { Fiche } from 'app/shared/model/fiche.model';

describe('Component Tests', () => {
  describe('Fiche Management Component', () => {
    let comp: FicheComponent;
    let fixture: ComponentFixture<FicheComponent>;
    let service: FicheService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WalluTestModule],
        declarations: [FicheComponent],
      })
        .overrideTemplate(FicheComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FicheComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FicheService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Fiche(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.fiches && comp.fiches[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
