import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WalluTestModule } from '../../../test.module';
import { FicheUpdateComponent } from 'app/entities/fiche/fiche-update.component';
import { FicheService } from 'app/entities/fiche/fiche.service';
import { Fiche } from 'app/shared/model/fiche.model';

describe('Component Tests', () => {
  describe('Fiche Management Update Component', () => {
    let comp: FicheUpdateComponent;
    let fixture: ComponentFixture<FicheUpdateComponent>;
    let service: FicheService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WalluTestModule],
        declarations: [FicheUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FicheUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FicheUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FicheService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Fiche(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Fiche();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
