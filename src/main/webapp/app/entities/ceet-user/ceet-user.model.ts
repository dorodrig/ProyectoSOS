import { IStudent } from 'app/entities/student/student.model';
import { ITeacher } from 'app/entities/teacher/teacher.model';

export interface ICeetUser {
  id?: number;
  phone?: string;
  address?: string;
  documentNumber?: string;
  documentType?: string;
  student?: IStudent | null;
  teacher?: ITeacher | null;
}

export class CeetUser implements ICeetUser {
  constructor(
    public id?: number,
    public phone?: string,
    public address?: string,
    public documentNumber?: string,
    public documentType?: string,
    public student?: IStudent | null,
    public teacher?: ITeacher | null
  ) {}
}

export function getCeetUserIdentifier(ceetUser: ICeetUser): number | undefined {
  return ceetUser.id;
}
