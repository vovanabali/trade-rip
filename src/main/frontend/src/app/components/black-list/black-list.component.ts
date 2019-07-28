import { Component, OnInit } from '@angular/core';
import {BlackItem } from "../../domains/blackItem";
import {BlackItemService} from "../../services/blackItem.service";

@Component({
  selector: 'app-black-list',
  templateUrl: './black-list.component.html',
  styleUrls: ['./black-list.component.css']
})
export class BlackListComponent implements OnInit {

  blackItem = new BlackItem();
  blackItems:BlackItem[] = [];
  cols: any[];

  constructor(private blackItemService: BlackItemService) {
    this.cols = [
      { field: 'name', header: 'Наименование' }
    ];
  }

  ngOnInit() {
    this.blackItemService.getAll().subscribe(value => {
      this.blackItems = value;
    });
  }

  save() {
    this.blackItemService.save(this.blackItem).subscribe(value => {
      this.blackItem = new BlackItem();
      this.blackItems.push(value);
    });
  }

  edit(val) {
    this.blackItem = val;
    this.blackItems.splice(this.blackItems.indexOf(val), 1);
  }

  delete(item) {
    this.blackItemService.delete(item.id).subscribe(value => {
      this.blackItems.splice(this.blackItems.indexOf(item), 1);
    })
  }
}
