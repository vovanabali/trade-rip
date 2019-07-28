import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Author} from "../domains/author";
import {User} from "../domains/user";
import {ItemMore} from "../domains/ItemMore";
import {BlackItem} from "../domains/blackItem";

@Injectable()
export class BlackItemService {

  private url = "http://localhost:9100/black-item";

  constructor(private http: HttpClient) { }

  getAll(): Observable<ItemMore[]> {
    return this.http.get<ItemMore[]>(this.url +'/');
  }

  save(itemMore): Observable<BlackItem> {
    return this.http.post<ItemMore>(this.url + '/save', itemMore);
  }

  delete(id): Observable<boolean> {
    return this.http.get<boolean>(this.url + '/delete', {
      params: {
        id: id
      }
    });
  }

  exist(name: string): Observable<boolean> {
    return this.http.get<boolean>(this.url + '/exist', {
      params: {
        name: name
      }
    });
  }

  existInList(names: string[]): Observable<boolean> {
    return this.http.post<boolean>(this.url + '/exist_in_list', names);
  }

  saveAll(blackItems: BlackItem[]) {
    this.http.post<boolean>(this.url + '/save_all', blackItems).subscribe();
  }

}
