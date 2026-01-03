import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { BeneficioListComponent } from './beneficio-list/beneficio-list.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, BeneficioListComponent],
  template: `
    <div class="container">
      <div class="header">
        <h1>💼 Gerenciamento de Benefícios</h1>
        <p>Sistema completo de CRUD e transferências</p>
      </div>
      <router-outlet></router-outlet>
      <app-beneficio-list></app-beneficio-list>
    </div>
  `,
  styles: []
})
export class AppComponent {
  title = 'Benefícios';
}

