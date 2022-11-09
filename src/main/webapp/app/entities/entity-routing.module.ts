import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'student',
        data: { pageTitle: 'proyectoSosApp.student.home.title' },
        loadChildren: () => import('./student/student.module').then(m => m.StudentModule),
      },
      {
        path: 'enrollment',
        data: { pageTitle: 'proyectoSosApp.enrollment.home.title' },
        loadChildren: () => import('./enrollment/enrollment.module').then(m => m.EnrollmentModule),
      },
      {
        path: 'level',
        data: { pageTitle: 'proyectoSosApp.level.home.title' },
        loadChildren: () => import('./level/level.module').then(m => m.LevelModule),
      },
      {
        path: 'area',
        data: { pageTitle: 'proyectoSosApp.area.home.title' },
        loadChildren: () => import('./area/area.module').then(m => m.AreaModule),
      },
      {
        path: 'course',
        data: { pageTitle: 'proyectoSosApp.course.home.title' },
        loadChildren: () => import('./course/course.module').then(m => m.CourseModule),
      },
      {
        path: 'achievement',
        data: { pageTitle: 'proyectoSosApp.achievement.home.title' },
        loadChildren: () => import('./achievement/achievement.module').then(m => m.AchievementModule),
      },
      {
        path: 'note',
        data: { pageTitle: 'proyectoSosApp.note.home.title' },
        loadChildren: () => import('./note/note.module').then(m => m.NoteModule),
      },
      {
        path: 'teacher',
        data: { pageTitle: 'proyectoSosApp.teacher.home.title' },
        loadChildren: () => import('./teacher/teacher.module').then(m => m.TeacherModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
