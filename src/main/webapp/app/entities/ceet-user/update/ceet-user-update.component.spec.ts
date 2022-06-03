import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CeetUserService } from '../service/ceet-user.service';
import { ICeetUser, CeetUser } from '../ceet-user.model';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

import { CeetUserUpdateComponent } from './ceet-user-update.component';

describe('CeetUser Management Update Component', () => {
  let comp: CeetUserUpdateComponent;
  let fixture: ComponentFixture<CeetUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ceetUserService: CeetUserService;
  let studentService: StudentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CeetUserUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CeetUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CeetUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ceetUserService = TestBed.inject(CeetUserService);
    studentService = TestBed.inject(StudentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call student query and add missing value', () => {
      const ceetUser: ICeetUser = { id: 456 };
      const student: IStudent = { id: 36292 };
      ceetUser.student = student;

      const studentCollection: IStudent[] = [{ id: 37161 }];
      jest.spyOn(studentService, 'query').mockReturnValue(of(new HttpResponse({ body: studentCollection })));
      const expectedCollection: IStudent[] = [student, ...studentCollection];
      jest.spyOn(studentService, 'addStudentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ceetUser });
      comp.ngOnInit();

      expect(studentService.query).toHaveBeenCalled();
      expect(studentService.addStudentToCollectionIfMissing).toHaveBeenCalledWith(studentCollection, student);
      expect(comp.studentsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ceetUser: ICeetUser = { id: 456 };
      const student: IStudent = { id: 43287 };
      ceetUser.student = student;

      activatedRoute.data = of({ ceetUser });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ceetUser));
      expect(comp.studentsCollection).toContain(student);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CeetUser>>();
      const ceetUser = { id: 123 };
      jest.spyOn(ceetUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ceetUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ceetUser }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ceetUserService.update).toHaveBeenCalledWith(ceetUser);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CeetUser>>();
      const ceetUser = new CeetUser();
      jest.spyOn(ceetUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ceetUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ceetUser }));
      saveSubject.complete();

      // THEN
      expect(ceetUserService.create).toHaveBeenCalledWith(ceetUser);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CeetUser>>();
      const ceetUser = { id: 123 };
      jest.spyOn(ceetUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ceetUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ceetUserService.update).toHaveBeenCalledWith(ceetUser);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackStudentById', () => {
      it('Should return tracked Student primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackStudentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
