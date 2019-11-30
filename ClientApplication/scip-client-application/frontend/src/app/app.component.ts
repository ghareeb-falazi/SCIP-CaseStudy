import { Component } from '@angular/core';
import { ScipClientService } from './ScipClientService';
import { EmsState } from './EmsState';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Smart Contract Invocation Protocol (SCIP) Case Study';
  ems: EmsState = null;
  querying = false;
  workflowRunning = false;
  logs: string = null;

  constructor(private scipService: ScipClientService) {
  }

  query() {
    this.querying = true;
    this.scipService.queryState().then(result => {
      this.ems = result;
      this.querying = false;
    });
  }

  startWorkflow() {
    this.workflowRunning = true;
    this.scipService
      .invokeWorkflow()
      .toPromise()
      .then(() => {
        const subscription = this.scipService.pollWorkflowState().subscribe(logEntries => {
          let builder = '';
          let currentEntry: string;

          for (const entry of logEntries) {
            currentEntry = '';

            if (!entry.done) {
              if (entry.error) {
                currentEntry = '[ERR] ';
              }

              if (entry.isoDateTime) {
                currentEntry += `[${entry.isoDateTime}]: `;
              }

              currentEntry += `${entry.message}\n\n`;
              builder = builder + currentEntry;
            } else {
              subscription.unsubscribe();
              this.workflowRunning = false;
              break;
            }
          }

          this.logs = builder;
        });
      });
  }

}
