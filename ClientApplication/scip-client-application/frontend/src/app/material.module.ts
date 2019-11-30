import { NgModule } from '@angular/core';

import { MatButtonModule, MatCardModule, MatDividerModule } from '@angular/material';
import { MatListModule } from '@angular/material/list';
import { MatInputModule } from '@angular/material/input';

@NgModule({
  imports: [
    MatButtonModule,
    MatCardModule,
    MatDividerModule,
    MatListModule,
    MatInputModule
  ],
  exports: [
    MatButtonModule,
    MatCardModule,
    MatDividerModule,
    MatListModule,
    MatInputModule,
  ]
})
export class MaterialModule {
}
