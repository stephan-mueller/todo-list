import { Directive, forwardRef } from '@angular/core';
import { AbstractControl, NG_VALIDATORS, ValidationErrors, Validator } from '@angular/forms';
import moment from 'moment';

@Directive({
  selector: '[appDate]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useClass: forwardRef(() => DateValidationDirective),
      multi: true,
    },
  ],
})
export class DateValidationDirective implements Validator {
  public validate(control: AbstractControl): ValidationErrors | null {
    const date = moment(control.value, ['DD.MM.YYYY', 'DD.MM.YYYY HH:mm'], true);
    return date.isValid() ? null : { invalidDate: true };
  }
}
