import {
  ApplicationConfig,
  importProvidersFrom,
  LOCALE_ID,
  provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { routes } from './app/app.routes';
import { provideStore } from '@ngxs/store';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './app/auth-interceptor';
import { UserState } from './shared';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { NgxsReduxDevtoolsPluginModule } from '@ngxs/devtools-plugin';
import { registerLocaleData } from '@angular/common';
import localeFr from '@angular/common/locales/fr';
import localEn from '@angular/common/locales/en';

registerLocaleData(localeFr);
registerLocaleData(localEn);

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes, withComponentInputBinding()),
    provideStore([UserState]),
    importProvidersFrom(
      BrowserAnimationsModule,
      NgxsReduxDevtoolsPluginModule.forRoot({
        disabled: false,
      }),
    ),
    { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'outline' } },
    provideAnimationsAsync(),
    provideHttpClient(withInterceptors([authInterceptor]), withFetch()),
    { provide: LOCALE_ID, useValue: navigator.language }, // Utilisation de la langue du navigateur
  ],
};
