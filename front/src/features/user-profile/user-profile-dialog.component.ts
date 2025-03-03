import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserActions } from '../../shared';
import { Store } from '@ngxs/store';
import { _isPasswordStrongValidator } from '../../shared';
import { MatError, MatFormField, MatLabel, MatSuffix } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'mdd-user-profile-dialog',
  templateUrl: 'user-profile-dialog.component.html',
  styleUrls: ['user-profile-dialog.component.scss'],
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatError,
    MatFormField,
    ReactiveFormsModule,
    MatInput,
    MatLabel,
    MatSuffix,
    MatIcon,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
})
export class UserProfileDialogComponent {
  #fb = inject(FormBuilder);
  #store = inject(Store);
  dialogRef = inject(MatDialogRef<UserProfileDialogComponent>);

  passVisible = signal<boolean>(false);

  passForm = this.#fb.nonNullable.group({
    actualPassword: ['', Validators.required],
    newPassword: ['', [Validators.required, _isPasswordStrongValidator]],
  });

  submit() {
    if (this.passForm.valid) {
      this.#store.dispatch(
        new UserActions.ChangePassword(
          this.passForm.controls.actualPassword.value,
          this.passForm.controls.newPassword.value,
        ),
      );
      this.dialogRef.close();
    }
  }

  togglePassVisibility() {
    this.passVisible.set(!this.passVisible());
  }
}
