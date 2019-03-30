import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TodoListComponent } from './todos/list/todo-list.component';
import { TodoDetailComponent } from './todos/detail/todo-detail.component';
import { TodoAddComponent } from './todos/add/todo-add.component';
import { TodoEditComponent } from './todos/edit/todo-edit.component';

const routes: Routes = [
  {
    path: 'todos',
    children: [
      {
        path: 'add',
        component: TodoAddComponent,
      },
      {
        path: 'edit/:id',
        component: TodoEditComponent,
      },
      {
        path: ':id',
        component: TodoDetailComponent,
      },
      {
        path: '',
        component: TodoListComponent,
      },
    ],
  },
  {
    path: '',
    redirectTo: '/todos',
    pathMatch: 'full',
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
