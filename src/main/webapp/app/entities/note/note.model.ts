import dayjs from 'dayjs/esm';
import { ICourse } from 'app/entities/course/course.model';

export interface INote {
  id?: number;
  qualification?: string;
  period?: string;
  startDate?: dayjs.Dayjs | null;
  courses?: ICourse[] | null;
}

export class Note implements INote {
  constructor(
    public id?: number,
    public qualification?: string,
    public period?: string,
    public startDate?: dayjs.Dayjs | null,
    public courses?: ICourse[] | null
  ) {}
}

export function getNoteIdentifier(note: INote): number | undefined {
  return note.id;
}
