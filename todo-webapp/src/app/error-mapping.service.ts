import { Injectable } from '@angular/core';

const ERROR_MAP = {
  required: 'Eine Feldeingabe ist notwendig.',
  invalidDate: 'Das Datum ist ungültig. Es muss dem Format DD.MM.JJJJ HH:mm entsprechen.',
};

@Injectable({
  providedIn: 'root',
})
export class ErrorMappingService {
  public getErrorMessage(code: string): string {
    return ERROR_MAP[code] || '';
  }
}
