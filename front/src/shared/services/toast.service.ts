import { inject, Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  private snackBar = inject(MatSnackBar);

  success(message: string) {
    this.openSnackBar(message, 'success');
  }

  error(message: string) {
    this.openSnackBar(message, 'error');
  }

  private openSnackBar(message: string, type: 'success' | 'error') {
    const config: MatSnackBarConfig = {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: [type],
    };
    this.snackBar.open(message, 'Close', config);
  }
}
