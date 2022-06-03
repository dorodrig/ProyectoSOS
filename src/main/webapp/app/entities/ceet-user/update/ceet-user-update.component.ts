import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICeetUser, CeetUser } from '../ceet-user.model';
import { CeetUserService } from '../service/ceet-user.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

@Component({
  selector: 'ceet-ceet-user-update',
  templateUrl: './ceet-user-update.component.html',
})
export class CeetUserUpdateComponent implements OnInit {
  isSaving = false;

  studentsCollection: IStudent[] = [];

  editForm = this.fb.group({
    id: [],
    phone: [null, [Validators.required, Validators.maxLength(10)]],
    address: [null, [Validators.required, Validators.maxLength(80)]],
    documentNumber: [null, [Validators.required, Validators.maxLength(20)]],
    documentType: [null, [Validators.required, Validators.maxLength(10)]],
    student: [],
  });

  constructor(
    protected ceetUserService: CeetUserService,
    protected studentService: StudentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ceetUser }) => {
      this.updateForm(ceetUser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ceetUser = this.createFromForm();
    if (ceetUser.id !== undefined) {
      this.subscribeToSaveResponse(this.ceetUserService.update(ceetUser));
    } else {
      this.subscribeToSaveResponse(this.ceetUserService.create(ceetUser));
    }
  }

  trackStudentById(_index: number, item: IStudent): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICeetUser>>): void {
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

  protected updateForm(ceetUser: ICeetUser): void {
    this.editForm.patchValue({
      id: ceetUser.id,
      phone: ceetUser.phone,
      address: ceetUser.address,
      documentNumber: ceetUser.documentNumber,
      documentType: ceetUser.documentType,
      student: ceetUser.student,
    });

    this.studentsCollection = this.studentService.addStudentToCollectionIfMissing(this.studentsCollection, ceetUser.student);
  }

  protected loadRelationshipsOptions(): void {
    this.studentService
      .query({ filter: 'ceetuser-is-null' })
      .pipe(map((res: HttpResponse<IStudent[]>) => res.body ?? []))
      .pipe(
        map((students: IStudent[]) => this.studentService.addStudentToCollectionIfMissing(students, this.editForm.get('student')!.value))
      )
      .subscribe((students: IStudent[]) => (this.studentsCollection = students));
  }

  protected createFromForm(): ICeetUser {
    return {
      ...new CeetUser(),
      id: this.editForm.get(['id'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      address: this.editForm.get(['address'])!.value,
      documentNumber: this.editForm.get(['documentNumber'])!.value,
      documentType: this.editForm.get(['documentType'])!.value,
      student: this.editForm.get(['student'])!.value,
    };
  }
}
