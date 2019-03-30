import { Injectable, Injector } from '@angular/core';
import { TodoService } from './todo.service';
import { RestTodoService } from './rest-todo.service';
import { GraphqlTodoService } from './graphql-todo.service';
import { Todo } from '../todo';
import { Observable } from 'rxjs';
import { BackendType, ConfigService } from './config.service';
import { ActivatedRoute } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class BackendSelectionService implements TodoService {
  private service: TodoService;

  constructor(injector: Injector, config: ConfigService, route: ActivatedRoute) {
    route.queryParamMap.subscribe(queryMap => {
      if (!queryMap.has('backendType')) {
        console.log(`Loading default backend (${config.defaultBackend === BackendType.Rest ? 'REST' : 'GraphQL'})`);
        this.service = injector.get(config.defaultBackend === BackendType.Rest ? RestTodoService : GraphqlTodoService);
      } else {
        const type = queryMap.get('backendType');
        console.log(`Loading ${type.toLowerCase() === 'rest' ? 'REST' : 'GraphQL'} backend`);
        this.service = injector.get(type.toLowerCase() === 'rest' ? RestTodoService : GraphqlTodoService);
      }
    });
  }

  public delete(id: number): Observable<boolean> {
    return this.service.delete(id);
  }

  public get(id: number): Observable<Todo> {
    return this.service.get(id);
  }

  public getAll(): Observable<Todo[]> {
    return this.service.getAll();
  }

  public post(todo: Todo): Observable<Todo> {
    return this.service.post(todo);
  }

  public put(id: number, changed: Todo): Observable<boolean> {
    return this.service.put(id, changed);
  }
}
