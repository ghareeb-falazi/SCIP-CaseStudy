import { NgModule } from '@angular/core';

import { MatButtonModule, MatCardModule, MatDividerModule} from '@angular/material';
import { MatListModule } from '@angular/material/list';

@NgModule({
  imports: [
    MatButtonModule,
    MatCardModule,
    MatDividerModule,
    MatListModule
  ],
  exports: [
    MatButtonModule,
    MatCardModule,
    MatDividerModule,
    MatListModule
  ]
})
export class MaterialModule {
}
