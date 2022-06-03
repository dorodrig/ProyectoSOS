import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CeetUserDetailComponent } from './ceet-user-detail.component';

describe('CeetUser Management Detail Component', () => {
  let comp: CeetUserDetailComponent;
  let fixture: ComponentFixture<CeetUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CeetUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ ceetUser: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CeetUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CeetUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ceetUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.ceetUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
