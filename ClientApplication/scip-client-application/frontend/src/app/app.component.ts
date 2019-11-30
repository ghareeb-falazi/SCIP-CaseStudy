import { Component } from '@angular/core';
import { ScipClientService } from './ScipClientService';
import { EmsState } from './EmsState';
import { LogEntry } from './LogEntry';

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
  logs: LogEntry[] = null;

  constructor(private scipService: ScipClientService) {
  }

  query() {
    this.querying = true;
    this.scipService.queryState().then(result => {
      console.debug(result);
      this.ems = result;
      this.querying = false;
    });
  }

  startWorkflow() {
    this.workflowRunning = true;
    this.scipService
      .invokeWorkflow()
      .toPromise()
      .then(() => this.scipService.pollWorkflowState().subscribe(logEntries => {
        console.debug(logEntries);
        this.logs = logEntries;
      }));

  }
}
