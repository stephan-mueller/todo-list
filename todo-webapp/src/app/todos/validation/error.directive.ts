import { Directive, ElementRef, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import { NgModel } from '@angular/forms';
import { Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged, filter, map, tap } from 'rxjs/operators';
import { ErrorMappingService } from '../../error-mapping.service';

@Directive({
  selector: '[appError]',
})
export class ErrorDirective implements OnInit, OnDestroy {
  private statusSubscription: Subscription;

  constructor(
    private element: ElementRef,
    private renderer: Renderer2,
    private control: NgModel,
    private service: ErrorMappingService,
  ) {
  }

  public ngOnInit() {
    const errorContainer = this.renderer.createElement('div');
    this.renderer.appendChild(this.element.nativeElement.parentNode, errorContainer);

    this.statusSubscription = this.control.statusChanges.pipe(
      filter(() => this.control.touched || this.control.dirty),
      debounceTime(500),
      map(() => this.control.errors !== null ? Object.keys(this.control.errors) : []),
      distinctUntilChanged(),
    ).subscribe(errors => {
      // TODO: find a cleaner solution; use debugger to find out what happens.
      errorContainer.innerText = '';
      for (const code of errors) {
        const text = this.renderer.createText(this.service.getErrorMessage(code));
        const div = this.renderer.createElement('div');
        this.renderer.addClass(div, 'error');
        this.renderer.appendChild(div, text);
        this.renderer.appendChild(errorContainer, div);
      }
    });
  }

  public ngOnDestroy() {
    this.statusSubscription.unsubscribe();
  }
}
