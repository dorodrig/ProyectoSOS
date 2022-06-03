import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TeacherService } from '../service/teacher.service';
import { ITeacher, Teacher } from '../teacher.model';
import { ICeetUser } from 'app/entities/ceet-user/ceet-user.model';
import { CeetUserService } from 'app/entities/ceet-user/service/ceet-user.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

import { TeacherUpdateComponent } from './teacher-update.component';

describe('Teacher Management Update Component', () => {
  let comp: TeacherUpdateComponent;
  let fixture: ComponentFixture<TeacherUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let teacherService: TeacherService;
  let ceetUserService: CeetUserService;
  let courseService: CourseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TeacherUpdateComponent],
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
      .overrideTemplate(TeacherUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TeacherUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    teacherService = TestBed.inject(TeacherService);
    ceetUserService = TestBed.inject(CeetUserService);
    courseService = TestBed.inject(CourseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ceetUser query and add missing value', () => {
      const teacher: ITeacher = { id: 456 };
      const ceetUser: ICeetUser = { id: 5508 };
      teacher.ceetUser = ceetUser;

      const ceetUserCollection: ICeetUser[] = [{ id: 50695 }];
      jest.spyOn(ceetUserService, 'query').mockReturnValue(of(new HttpResponse({ body: ceetUserCollection })));
      const expectedCollection: ICeetUser[] = [ceetUser, ...ceetUserCollection];
      jest.spyOn(ceetUserService, 'addCeetUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      expect(ceetUserService.query).toHaveBeenCalled();
      expect(ceetUserService.addCeetUserToCollectionIfMissing).toHaveBeenCalledWith(ceetUserCollection, ceetUser);
      expect(comp.ceetUsersCollection).toEqual(expectedCollection);
    });

    it('Should call Course query and add missing value', () => {
      const teacher: ITeacher = { id: 456 };
      const courses: ICourse[] = [{ id: 43087 }];
      teacher.courses = courses;

      const courseCollection: ICourse[] = [{ id: 73277 }];
      jest.spyOn(courseService, 'query').mockReturnValue(of(new HttpResponse({ body: courseCollection })));
      const additionalCourses = [...courses];
      const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
      jest.spyOn(courseService, 'addCourseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      expect(courseService.query).toHaveBeenCalled();
      expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(courseCollection, ...additionalCourses);
      expect(comp.coursesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const teacher: ITeacher = { id: 456 };
      const ceetUser: ICeetUser = { id: 49864 };
      teacher.ceetUser = ceetUser;
      const courses: ICourse = { id: 77026 };
      teacher.courses = [courses];

      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(teacher));
      expect(comp.ceetUsersCollection).toContain(ceetUser);
      expect(comp.coursesSharedCollection).toContain(courses);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Teacher>>();
      const teacher = { id: 123 };
      jest.spyOn(teacherService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: teacher }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(teacherService.update).toHaveBeenCalledWith(teacher);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Teacher>>();
      const teacher = new Teacher();
      jest.spyOn(teacherService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: teacher }));
      saveSubject.complete();

      // THEN
      expect(teacherService.create).toHaveBeenCalledWith(teacher);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Teacher>>();
      const teacher = { id: 123 };
      jest.spyOn(teacherService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ teacher });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(teacherService.update).toHaveBeenCalledWith(teacher);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCeetUserById', () => {
      it('Should return tracked CeetUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCeetUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCourseById', () => {
      it('Should return tracked Course primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCourseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedCourse', () => {
      it('Should return option if no Course is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedCourse(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Course for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedCourse(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Course is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedCourse(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
