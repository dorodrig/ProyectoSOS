import { IEnrollment } from 'app/entities/enrollment/enrollment.model';
import { ICeetUser } from 'app/entities/ceet-user/ceet-user.model';

export interface IStudent {
  id?: number;
  attendantName?: string;
  kin?: string;
  enrollment?: IEnrollment | null;
  ceetUser?: ICeetUser | null;
}

export class Student implements IStudent {
  constructor(
    public id?: number,
    public attendantName?: string,
    public kin?: string,
    public enrollment?: IEnrollment | null,
    public ceetUser?: ICeetUser | null
  ) {}
}

export function getStudentIdentifier(student: IStudent): number | undefined {
  return student.id;
}
