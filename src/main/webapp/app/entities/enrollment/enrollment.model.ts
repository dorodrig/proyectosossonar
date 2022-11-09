import dayjs from 'dayjs/esm';
import { IStudent } from 'app/entities/student/student.model';
import { ILevel } from 'app/entities/level/level.model';

export interface IEnrollment {
  id?: number;
  registrationNumber?: string;
  startDate?: dayjs.Dayjs | null;
  student?: IStudent | null;
  levels?: ILevel[] | null;
}

export class Enrollment implements IEnrollment {
  constructor(
    public id?: number,
    public registrationNumber?: string,
    public startDate?: dayjs.Dayjs | null,
    public student?: IStudent | null,
    public levels?: ILevel[] | null
  ) {}
}

export function getEnrollmentIdentifier(enrollment: IEnrollment): number | undefined {
  return enrollment.id;
}
