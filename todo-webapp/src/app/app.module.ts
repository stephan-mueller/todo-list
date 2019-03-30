import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TodoListComponent } from './todos/list/todo-list.component';
import { TodoDetailComponent } from './todos/detail/todo-detail.component';
import { TodoAddComponent } from './todos/add/todo-add.component';
import { FormsModule } from '@angular/forms';
import { DateValidationDirective } from './todos/validation/date-validation.directive';
import { ErrorDirective } from './todos/validation/error.directive';
import { TodoEditComponent } from './todos/edit/todo-edit.component';
import { HttpClientModule } from '@angular/common/http';
import { APOLLO_OPTIONS, ApolloModule } from 'apollo-angular';
import { HttpLink, HttpLinkModule } from 'apollo-angular-link-http';
import { InMemoryCache } from 'apollo-cache-inmemory';
import { ConfigService } from './todos/service/config.service';
import { TodoService } from './todos/service/todo.service';
import { BackendSelectionService } from './todos/service/backend-selection.service';
import { SseBoxComponent } from './todos/server-sent-events/sse-box.component';

@NgModule({
  declarations: [
    AppComponent,
    TodoListComponent,
    TodoDetailComponent,
    TodoAddComponent,
    TodoEditComponent,
    ErrorDirective,
    DateValidationDirective,
    SseBoxComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    ApolloModule,
    HttpLinkModule,
  ],
  providers: [
    {
      provide: TodoService,
      useClass: BackendSelectionService,
    },
    {
      provide: APOLLO_OPTIONS,
      useFactory: (httpLink: HttpLink, config: ConfigService) => {
        return {
          cache: new InMemoryCache(),
          link: httpLink.create({
            uri: config.graphqlBackendUrl,
          })
        };
      },
      deps: [HttpLink, ConfigService]
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
