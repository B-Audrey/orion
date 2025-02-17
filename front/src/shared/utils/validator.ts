import { AbstractControl, ValidationErrors } from '@angular/forms';

export function _isPasswordStrongValidator(control: AbstractControl): ValidationErrors | null {
  const password = control.value;
  const hasNumber = /[0-9]+/.test(password);
  const hasUpperCase = /[A-Z]+/.test(password);
  const hasLowerCase = /[a-z]+/.test(password);
  const hasSymbols = /[ !@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]+/.test(password);
  const isLongEnough = password.length >= 8;
  const isPasswordStrong = hasNumber && hasUpperCase && hasLowerCase && hasSymbols && isLongEnough;
  return !isPasswordStrong
    ? { hasNumber, hasUpperCase, hasLowerCase, hasSymbols, isLongEnough }
    : null;
}
