import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EmsState } from './EmsState';
import { interval, Observable } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
import { LogEntry } from './LogEntry';
import { environment } from '../environments/environment';

@Injectable()
export class ScipClientService {
  constructor(private http: HttpClient) {
  }

  private getBackendUrl() {

  }
  public queryState(): Promise<EmsState> {
    const url = `${environment.apiUrl}/query`;
    return this.http.get<EmsState>(url)
      .toPromise();
  }

  public invokeWorkflow(): Observable<string> {
    const url = `${environment.apiUrl}/workflow`;
    return this.http.post(url, null, { responseType: 'text' });
  }

  public pollWorkflowState(): Observable<LogEntry[]> {
    return interval(4000)
      .pipe(
        startWith(0),
        switchMap(() => this.getWorkflowState())
      );
  }

  public getWorkflowState(): Promise<LogEntry[]> {
    const url = `${environment.apiUrl}/workflow/query`;
    return this.http.get<LogEntry[]>(url).toPromise();
  }

}
