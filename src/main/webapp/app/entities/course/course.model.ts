import { IArea } from 'app/entities/area/area.model';
import { IAchievement } from 'app/entities/achievement/achievement.model';
import { INote } from 'app/entities/note/note.model';
import { ITeacher } from 'app/entities/teacher/teacher.model';

export interface ICourse {
  id?: number;
  nameCourse?: string;
  areas?: IArea[] | null;
  achievements?: IAchievement[] | null;
  notes?: INote[] | null;
  teachers?: ITeacher[] | null;
}

export class Course implements ICourse {
  constructor(
    public id?: number,
    public nameCourse?: string,
    public areas?: IArea[] | null,
    public achievements?: IAchievement[] | null,
    public notes?: INote[] | null,
    public teachers?: ITeacher[] | null
  ) {}
}

export function getCourseIdentifier(course: ICourse): number | undefined {
  return course.id;
}
