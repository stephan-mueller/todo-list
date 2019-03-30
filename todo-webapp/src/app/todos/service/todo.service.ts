import { Todo } from '../todo';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable()
export abstract class TodoService {
  abstract getAll(): Observable<Todo[]>;
  abstract get(id: number): Observable<Todo>;
  abstract post(todo: Todo): Observable<Todo>;
  abstract delete(id: number): Observable<boolean>;
  abstract put(id: number, changed: Todo): Observable<boolean>;
}
