import { IEnrollment } from 'app/entities/enrollment/enrollment.model';
import { IArea } from 'app/entities/area/area.model';

export interface ILevel {
  id?: number;
  nameCurse?: string;
  courseAcronym?: string;
  enrollments?: IEnrollment[] | null;
  areas?: IArea[] | null;
}

export class Level implements ILevel {
  constructor(
    public id?: number,
    public nameCurse?: string,
    public courseAcronym?: string,
    public enrollments?: IEnrollment[] | null,
    public areas?: IArea[] | null
  ) {}
}

export function getLevelIdentifier(level: ILevel): number | undefined {
  return level.id;
}
