import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Author} from "../domains/author";
import {User} from "../domains/user";

@Injectable()
export class SettingsService {

  private url = "http://localhost:9100/settings";

  constructor(private http: HttpClient) { }

  getSettings(): Observable<User> {
    return this.http.get<User>(this.url +'/');
  }

  save(user): Observable<User> {
    return this.http.post<User>(this.url + '/save', user);
  }

}
