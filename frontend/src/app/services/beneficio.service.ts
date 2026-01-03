import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Beneficio, Transferencia } from '../models/beneficio.model';

@Injectable({
  providedIn: 'root'
})
export class BeneficioService {
  private apiUrl = 'http://localhost:8080/api/v1/beneficios';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(this.apiUrl).pipe(
      catchError(this.handleError)
    );
  }

  getById(id: number): Observable<Beneficio> {
    return this.http.get<Beneficio>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  create(beneficio: Beneficio): Observable<Beneficio> {
    return this.http.post<Beneficio>(this.apiUrl, beneficio).pipe(
      catchError(this.handleError)
    );
  }

  update(id: number, beneficio: Beneficio): Observable<Beneficio> {
    return this.http.put<Beneficio>(`${this.apiUrl}/${id}`, beneficio).pipe(
      catchError(this.handleError)
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  transfer(transferencia: Transferencia): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/transfer`, transferencia).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Ocorreu um erro desconhecido';
    
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erro: ${error.error.message}`;
    } else {
      errorMessage = error.error?.message || error.message || errorMessage;
    }
    
    return throwError(() => new Error(errorMessage));
  }
}

