export interface Todo {
  readonly id?: number;
  readonly title: string;
  readonly description?: string;
  readonly dueDate: Date;
  readonly done: boolean;
}
