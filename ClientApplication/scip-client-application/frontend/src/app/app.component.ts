import { Component, OnInit } from '@angular/core';
import { ScipClientService } from './ScipClientService';
import { EmsState } from './EmsState';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Smart Contract Invocation Protocol (SCIP) Case Study';
  ems: EmsState = null;
  querying = false;
  workflowRunning = false;
  changingPrice = false;
  logs: string = null;
  bulkPrice: number;

  ngOnInit(): void {
    this.changingPrice = true;
    this.scipService.queryState().then(result => {
      this.bulkPrice = parseInt(result.powerPlantPrice, 10);
      this.changingPrice = false;
    });
  }

  constructor(private scipService: ScipClientService, private snackBar: MatSnackBar) {
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

  changePrice(): void {
    this.changingPrice = true;
    this.scipService.changeBulkPrice(this.bulkPrice).then(() => {
      this.snackBar.open('Price Changed!', null, { duration: 2000 });
    })
      .catch(error => {
        this.snackBar.open(`Failed to change price. Reason: ${error.statusText}`, null, { duration: 5000 });
      })
      .finally(() => this.changingPrice = false);
  }

}
