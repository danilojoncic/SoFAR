import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { appRoutes } from './app.routes';
import { tokenInterceptor} from './interceptors/token.interceptor';

export const appConfig = {
  providers: [
    provideRouter(appRoutes),
    provideHttpClient(
      withInterceptors([tokenInterceptor])
    )
  ]
};
