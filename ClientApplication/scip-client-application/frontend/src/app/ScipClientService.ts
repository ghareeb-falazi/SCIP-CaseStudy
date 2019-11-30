import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EmsState } from './EmsState';
import { interval, Observable } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
import { LogEntry } from './LogEntry';

@Injectable()
export class ScipClientService {
  constructor(private http: HttpClient) {
  }

  public queryState(): Promise<EmsState> {
    const url = 'http://localhost:8080/query';
    return this.http.get<EmsState>(url)
      .toPromise();
  }

  public invokeWorkflow(): Observable<string> {
    const url = 'http://localhost:8080/workflow';
    return this.http.post(url, null, {responseType: 'text'});
  }

  public pollWorkflowState(): Observable<LogEntry[]> {
    return interval(2000)
      .pipe(
        startWith(0),
        switchMap(() => this.getWorkflowState())
      )
  }

  public getWorkflowState(): Promise<LogEntry[]> {
    console.info("inside getWorkflow State!")
    const url = 'http://localhost:8080/workflow/query';
    return this.http.get<LogEntry[]>(url).toPromise();
  }

}
