import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MatError, MatFormField } from '@angular/material/form-field';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MatInput } from '@angular/material/input';
import { Router, RouterLink } from '@angular/router';
import { ToastService } from '../../../shared/services/toast.service';
import { _isPasswordStrongValidator } from '../../../shared/utils';
import { HttpErrorResponse } from '@angular/common/http';
import { UserService } from '../../../shared/services/user.service';
import { NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'mdd-signin',
  standalone: true,
  imports: [
    FormsModule,
    MatButton,
    MatError,
    MatFormField,
    MatInput,
    ReactiveFormsModule,
    RouterLink,
    NgOptimizedImage,
  ],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SigninComponent {
  #fb = inject(FormBuilder);
  #router = inject(Router);
  #toastService = inject(ToastService);
  #userService = inject(UserService);

  signinForm = this.#fb.nonNullable.group({
    name: ['' as string, Validators.required],
    email: ['' as string, [Validators.required, Validators.email]],
    password: ['' as string, [Validators.required, _isPasswordStrongValidator]],
  });

  signin() {
    const form = this.signinForm.getRawValue();
    this.#userService.postUser$(form).subscribe({
      next: user => {
        this.#toastService.success(`Bienvenue ${user.name}, ton compte à bien été créé`);
        this.#router.navigate(['auth', 'login']);
      },
      error: (err: HttpErrorResponse) => {
        if (err.status === 400) {
          return this.#toastService.error('veuillez vérifier les informations saisies');
        } else if (err.status === 401) {
          return this.#toastService.error('Un compte avec cette adresse email existe déjà');
        } else if (err.status === 500) {
          return this.#toastService.error('Erreur 500, le service ne répond pas');
        } else {
          this.#toastService.error('Une erreur est survenue, veuillez réessayer ultérieurement');
        }
      },
    });
  }
}
