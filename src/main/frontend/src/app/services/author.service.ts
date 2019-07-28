import {Injectable} from '@angular/core';
import {Author} from '../domains/author';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class AuthorService {

  private uri = 'http://localhost:8080/admin/json/';

  constructor(private http: HttpClient) {
  }

  getAuthors(): Observable<Author[]> {
    return this.http.get<Author[]>(this.uri + 'authors');
  }

  slice(start: number, rows: number, sortField: string, sortOrder: number): Observable<Author[]> {
    let orderBy = 'asc';
    if (sortOrder === 1) {
      orderBy = 'ASC';
    } else {
      orderBy = 'DESC';
    }
    let sortBy = sortField;
    if (sortField === undefined) {
      sortBy = '';
    }
    return this.http.get<Author[]>(this.uri + 'authorsSlice', {
      params: {
        page: start.toString(),
        size: rows.toString(),
        sort: sortBy + ',' + orderBy
      }
    });
  }

  getAuthorById(id: number): Observable<Author> {
    return this.http.get<Author>(this.uri + 'author', {params: {id: id.toString()}});
  }

  addAuthor(author: Author): Observable<boolean> {
    return this.http.post<boolean>(this.uri + 'addAuthor', author);
  }

  updateAuthor(author: Author): Observable<boolean> {
    return this.http.post<boolean>(this.uri + 'updateAuthor', author);
  }

  deleteAuthorById(id: number): Observable<boolean> {
    return this.http.get<boolean>(this.uri + 'deleteAuthorById', {params: {id: id.toString()}});
  }
}
