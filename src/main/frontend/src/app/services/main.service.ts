import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Author} from "../domains/author";
import {User} from "../domains/user";

@Injectable()
export class MainService {

  private url = "http://localhost:9100/main";

  constructor(private http: HttpClient) { }
}
