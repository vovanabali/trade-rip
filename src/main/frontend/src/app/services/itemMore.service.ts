import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Author} from "../domains/author";
import {User} from "../domains/user";
import {ItemMore} from "../domains/ItemMore";

@Injectable()
export class ItemMoreService {

  private url = "http://localhost:9100/item-more";

  constructor(private http: HttpClient) {
  }

  getItemsMore(): Observable<ItemMore[]> {
    return this.http.get<ItemMore[]>(this.url + '/');
  }

  save(itemMore): Observable<ItemMore> {
    return this.http.post<ItemMore>(this.url + '/save', itemMore);
  }

  loadFromC5(): Observable<ItemMore[]> {
    return this.http.get<ItemMore[]>(this.url + '/load_from_c5');
  }

  deleteAll(): Observable<ItemMore[]> {
    return this.http.get<ItemMore[]>(this.url + '/delete_all');
  }

  delete(id): Observable<boolean> {
    return this.http.get<boolean>(this.url + '/delete', {
      params: {
        id: id
      }
    });
  }

  getItemDescription(identity, api): Observable<any> {
    return this.http.get<any>('/api/GetItemDescription/' + identity + '/?key=' + api);
  }

  buyItem(identity: string, price: number, yourSecretKey: string): Observable<any> {
    return this.http.get<any>(`/api/Buy/${identity}/${price}/?key=${yourSecretKey}`);
  }

}
