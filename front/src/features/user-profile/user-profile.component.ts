import { ChangeDetectionStrategy, Component, inject, OnInit } from '@angular/core';
import { Store } from '@ngxs/store';
import { UserActions, UserState } from '../../shared/store/user';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatButton } from '@angular/material/button';
import { User } from '../../shared/interfaces/user';
import { MatDialog } from '@angular/material/dialog';
import { UserProfileDialogComponent } from './user-profile-dialog.component';
@Component({
  selector: 'mdd-user-profile',
  standalone: true,
  imports: [FormsModule, MatFormField, MatInput, ReactiveFormsModule, MatButton, MatLabel],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserProfileComponent implements OnInit {
  #store = inject(Store);
  #fb = inject(FormBuilder);
  dialog = inject(MatDialog);

  updateForm = this.#fb.nonNullable.group({
    name: ['' as string, Validators.required],
    email: ['' as string, [Validators.required, Validators.email]],
  });

  logout() {
    this.#store.dispatch(new UserActions.Logout());
  }

  updateUser() {
    this.#store.dispatch(new UserActions.Update(this.updateForm.value as User));
  }

  ngOnInit(): void {
    this.#store.select(UserState.getMe).subscribe(user => {
      this.updateForm.patchValue(
        {
          name: user?.name || '',
          email: user?.email || '',
        },
        { emitEvent: false },
      );
    });
  }

  changePassword() {
    this.dialog.open(UserProfileDialogComponent);
  }
}
