import { IUser } from 'app/entities/user/user.model';

export interface ITeacher {
  id?: number;
  name?: string;
  lastName?: string;
  phone?: string;
  address?: string;
  documentNumber?: string;
  documentType?: string;
  user?: IUser | null;
}

export class Teacher implements ITeacher {
  constructor(
    public id?: number,
    public name?: string,
    public lastName?: string,
    public phone?: string,
    public address?: string,
    public documentNumber?: string,
    public documentType?: string,
    public user?: IUser | null
  ) {}
}

export function getTeacherIdentifier(teacher: ITeacher): number | undefined {
  return teacher.id;
}
