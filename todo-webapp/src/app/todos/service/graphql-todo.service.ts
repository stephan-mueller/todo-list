import { Injectable } from '@angular/core';
import { TodoService } from './todo.service';
import { Todo } from '../todo';
import { Observable } from 'rxjs';
import { Apollo } from 'apollo-angular';
import { map } from 'rxjs/operators';
import gql from 'graphql-tag';
import moment from 'moment';


@Injectable({
  providedIn: 'root',
})
export class GraphqlTodoService implements TodoService {
  constructor(private apollo: Apollo) {
  }

  public getAll(): Observable<Todo[]> {
    console.log(`Calling GraphqlTodoService.getAll`);
    return this.apollo.query({
      query: gql`
        query {
          todos {
            id, title, dueDate, done
          }
        }
    `, fetchPolicy: 'network-only', // TODO: remove this and do a reload of cache
    }).pipe(
      map(result => (result.data as { todos: Todo[] }).todos),
    );
  }

  public get(id: number): Observable<Todo> {
    console.log(`Calling GraphqlTodoService.get with id ${id}`);
    return this.apollo.query({
      query: gql`
        query {
          todo(todoId:${id}) {
            id, title, description, dueDate, done
          }
        }
    `, fetchPolicy: 'network-only', // TODO: remove this and do a reload of cache
    }).pipe(
      map(result => (result.data as { todo: Todo }).todo),
    );
  }

  public post(todo: Todo): Observable<Todo> {
    console.log(`Calling GraphqlTodoService.post with todo`, todo);
    return this.apollo.mutate({
      mutation: gql`
        mutation {
          createTodo(newTodo: {
            title:"${todo.title}",
            description:"${todo.description}",
            dueDate:"${moment(todo.dueDate).format('YYYY-MM-DDTHH:mm:ss')}",
            done:${todo.done}
          }) {
            id, title, description, dueDate, done
          }
        }
    `
    }).pipe(
      map(result => (result.data as { createTodo: Todo }).createTodo),
    );
  }

  public delete(id: number): Observable<boolean> {
    console.log(`Calling GraphqlTodoService.delete with id ${id}`);
    return this.apollo.mutate({
      mutation: gql`
        mutation {
          deleteTodo(todoId:${id})
        }
    `
    }).pipe(
      map(result => (result.data as { deleteTodo: boolean }).deleteTodo),
    );
  }

  public put(id: number, changed: Todo): Observable<boolean> {
    console.log(`Calling GraphqlTodoService.get with id ${id} and todo`, changed);
    return this.apollo.mutate({
      mutation: gql`
        mutation {
          updateTodo(todoId:${id}, modifiedTodo: {
            title:"${changed.title}",
            description:"${changed.description}",
            dueDate:"${moment(changed.dueDate).format('YYYY-MM-DDTHH:mm:ss')}",
            done:${changed.done}
          })
        }
    `
    }).pipe(
      map(result => (result.data as { updateTodo: boolean }).updateTodo),
    );
  }
}
