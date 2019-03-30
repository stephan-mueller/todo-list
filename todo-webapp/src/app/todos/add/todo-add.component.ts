import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TodoService } from '../service/todo.service';
import moment from 'moment';

@Component({
  templateUrl: './todo-add.component.html',
})
export class TodoAddComponent {
  public title: string;
  public description: string;
  public dueDate: string;
  public done: boolean;
  public lockForm = false;

  constructor(private service: TodoService, private router: Router) {
  }

  public onSubmit() {
    this.lockForm = true;
    this.service.post({
      title: this.title,
      description: this.description || '',
      dueDate: moment(this.dueDate, ['DD.MM.YYYY', 'DD.MM.YYYY HH:mm']).toDate(),
      done: !!this.done,
    }).subscribe(todo => {
      this.lockForm = false;
      this.router.navigate(['/todos', todo.id], { queryParamsHandling: 'merge'});
    }, error => {
      // TODO: add error handling in the frontend
      console.error('Could not add todo due to an error', error);
      this.router.navigate(['/todos'], { queryParamsHandling: 'merge' });
    });
  }
}
