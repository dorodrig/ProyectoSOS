import { ICeetUser } from 'app/entities/ceet-user/ceet-user.model';
import { ICourse } from 'app/entities/course/course.model';

export interface ITeacher {
  id?: number;
  ceetUser?: ICeetUser | null;
  courses?: ICourse[] | null;
}

export class Teacher implements ITeacher {
  constructor(public id?: number, public ceetUser?: ICeetUser | null, public courses?: ICourse[] | null) {}
}

export function getTeacherIdentifier(teacher: ITeacher): number | undefined {
  return teacher.id;
}
