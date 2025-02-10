import {Component, inject} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {Store} from '@ngxs/store';
import {Router, RouterLink} from '@angular/router';
import {ToastService} from '../../../shared/services/toast.service';
import {UserActions} from '../../../shared/store';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatLabel,
    MatInput,
    MatFormField,
    MatButton,
    MatIcon,
    RouterLink
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  #fb = inject(FormBuilder);
  #store = inject(Store);
  #router = inject(Router);
  #toastService = inject(ToastService);



  loginForm = this.#fb.nonNullable.group({
    username: ['' as string, [Validators.required]],
    password: ['' as string, [Validators.required]]
  });

  login({ username, password }: { username: string; password: string }) {
    console.log('login', username, password);
    if (this.loginForm.valid) {
      console.log('loginForm.valid');
      this.#store.dispatch(new UserActions.Login(username, password)).subscribe({
        next: () => this.#router.navigate(['/']),
        error: (err: HttpErrorResponse) => {
            this.#toastService.error('Connexion refusée, veuillez vérifier vos identifiants.');
        },
      });
      console.log('dispatched');
    }
  }

}
