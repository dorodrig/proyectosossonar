import { ICourse } from 'app/entities/course/course.model';
import { StateAchievement } from 'app/entities/enumerations/state-achievement.model';

export interface IAchievement {
  id?: number;
  achievementDescription?: string;
  statusAchievement?: StateAchievement | null;
  courses?: ICourse[] | null;
}

export class Achievement implements IAchievement {
  constructor(
    public id?: number,
    public achievementDescription?: string,
    public statusAchievement?: StateAchievement | null,
    public courses?: ICourse[] | null
  ) {}
}

export function getAchievementIdentifier(achievement: IAchievement): number | undefined {
  return achievement.id;
}
