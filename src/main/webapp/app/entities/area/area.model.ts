import { ILevel } from 'app/entities/level/level.model';
import { ICourse } from 'app/entities/course/course.model';

export interface IArea {
  id?: number;
  areaName?: string;
  levels?: ILevel[] | null;
  courses?: ICourse[] | null;
}

export class Area implements IArea {
  constructor(public id?: number, public areaName?: string, public levels?: ILevel[] | null, public courses?: ICourse[] | null) {}
}

export function getAreaIdentifier(area: IArea): number | undefined {
  return area.id;
}
