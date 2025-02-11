import { Component, inject } from '@angular/core';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MatInput } from '@angular/material/input';
import { Router, RouterLink } from '@angular/router';
import { ToastService } from '../../../shared/services/toast.service';
import { _isPasswordStrongValidator } from '../../../shared/utils';
import { HttpErrorResponse } from '@angular/common/http';
import { MatIcon } from '@angular/material/icon';
import { UserService } from '../../../shared/services/user.service';
import { NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [
    FormsModule,
    MatButton,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    MatIcon,
    RouterLink,
    NgOptimizedImage,
  ],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.scss',
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
    this.#userService.signin$(form).subscribe({
      next: user => {
        this.#toastService.success(`Bienvenue ${user.name}, ton compte à bien été créé`);
        this.#router.navigate(['auth', 'login']);
      },
      error: (err: HttpErrorResponse) => {
        console.log(err.message);
        if (err.status === 400) {
          this.#toastService.error('veuillez vérifier les informations saisies');
        } else if (err.status === 401) {
          this.#toastService.error('Un compte avec cette adresse email existe déjà');
        } else {
          this.#toastService.error('Une erreur est survenue, veuillez réessayer ultérieurement');
        }
      },
    });
  }
}
