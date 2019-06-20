import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, tap } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';
import { News } from './common/news.model';



const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
  })
};

@Injectable({
  providedIn: 'root',
})
export class AppService {
  constructor(private http: HttpClient) { }
  
  
  getAllNews() {
    // now returns an Observable of Config
    return this.http.get("http://localhost:8080/theshield/all", httpOptions)
  }


  getByTileOrContent(query: String): Observable<News[]> {
    // now returns an Observable of Config
    return this.http.get<News[]>("http://localhost:8080/theshield/findNewsByTitleOrContent/" + query, httpOptions)
    .pipe(
        data => data
    );
  }

}