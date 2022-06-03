import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITeacher, Teacher } from '../teacher.model';
import { TeacherService } from '../service/teacher.service';
import { ICeetUser } from 'app/entities/ceet-user/ceet-user.model';
import { CeetUserService } from 'app/entities/ceet-user/service/ceet-user.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

@Component({
  selector: 'ceet-teacher-update',
  templateUrl: './teacher-update.component.html',
})
export class TeacherUpdateComponent implements OnInit {
  isSaving = false;

  ceetUsersCollection: ICeetUser[] = [];
  coursesSharedCollection: ICourse[] = [];

  editForm = this.fb.group({
    id: [],
    ceetUser: [],
    courses: [],
  });

  constructor(
    protected teacherService: TeacherService,
    protected ceetUserService: CeetUserService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teacher }) => {
      this.updateForm(teacher);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const teacher = this.createFromForm();
    if (teacher.id !== undefined) {
      this.subscribeToSaveResponse(this.teacherService.update(teacher));
    } else {
      this.subscribeToSaveResponse(this.teacherService.create(teacher));
    }
  }

  trackCeetUserById(_index: number, item: ICeetUser): number {
    return item.id!;
  }

  trackCourseById(_index: number, item: ICourse): number {
    return item.id!;
  }

  getSelectedCourse(option: ICourse, selectedVals?: ICourse[]): ICourse {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeacher>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(teacher: ITeacher): void {
    this.editForm.patchValue({
      id: teacher.id,
      ceetUser: teacher.ceetUser,
      courses: teacher.courses,
    });

    this.ceetUsersCollection = this.ceetUserService.addCeetUserToCollectionIfMissing(this.ceetUsersCollection, teacher.ceetUser);
    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing(
      this.coursesSharedCollection,
      ...(teacher.courses ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ceetUserService
      .query({ filter: 'teacher-is-null' })
      .pipe(map((res: HttpResponse<ICeetUser[]>) => res.body ?? []))
      .pipe(
        map((ceetUsers: ICeetUser[]) =>
          this.ceetUserService.addCeetUserToCollectionIfMissing(ceetUsers, this.editForm.get('ceetUser')!.value)
        )
      )
      .subscribe((ceetUsers: ICeetUser[]) => (this.ceetUsersCollection = ceetUsers));

    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(
        map((courses: ICourse[]) =>
          this.courseService.addCourseToCollectionIfMissing(courses, ...(this.editForm.get('courses')!.value ?? []))
        )
      )
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));
  }

  protected createFromForm(): ITeacher {
    return {
      ...new Teacher(),
      id: this.editForm.get(['id'])!.value,
      ceetUser: this.editForm.get(['ceetUser'])!.value,
      courses: this.editForm.get(['courses'])!.value,
    };
  }
}
