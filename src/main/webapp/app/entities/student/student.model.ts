import { IEnrollment } from 'app/entities/enrollment/enrollment.model';
import { IUser } from 'app/entities/user/user.model';

export interface IStudent {
  id?: number;
  attendantName?: string;
  kin?: string;
  enrollment?: IEnrollment | null;
  user?: IUser | null;
}

export class Student implements IStudent {
  constructor(
    public id?: number,
    public attendantName?: string,
    public kin?: string,
    public enrollment?: IEnrollment | null,
    public user?: IUser | null
  ) {}
}

export function getStudentIdentifier(student: IStudent): number | undefined {
  return student.id;
}
