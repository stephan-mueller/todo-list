import { Component, OnInit } from '@angular/core';
import { Todo } from '../todo';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { TodoService } from '../service/todo.service';

@Component({
  templateUrl: './todo-detail.component.html',
})
export class TodoDetailComponent implements OnInit {
  public todo: Observable<Todo>;

  constructor(private service: TodoService, private route: ActivatedRoute) {
  }

  public ngOnInit() {
    this.todo = this.route.paramMap.pipe(
      switchMap((params: ParamMap) => this.service.get(+params.get('id'))),
    );
  }
}
