import {Component, OnInit, ViewChild} from '@angular/core';
import {ItemMore} from "../../domains/ItemMore";
import {ItemMoreService} from "../../services/itemMore.service";

@Component({
  selector: 'app-item-more-info',
  templateUrl: './item-more-info.component.html',
  styleUrls: ['./item-more-info.component.css']
})
export class ItemMoreInfoComponent implements OnInit {

  itemMoreAdd = new ItemMore();
  itemMoreList: ItemMore[] = [];
  cols: any[];
  loading = false;
  @ViewChild('searchElement') searchElement: any;

  constructor(private itemMoreService: ItemMoreService) {
    this.cols = [
      {field: 'name', header: 'Наименование'},
      {field: 'ruName', header: 'Русское наименование'},
      {field: 'price', header: 'Цена'},
      {field: 'item', header: 'Это предмет'}
    ];
  }

  ngOnInit() {
    this.loading = true;
    this.itemMoreService.getItemsMore().subscribe(value => {
      this.itemMoreList = value;
      this.loading = false;
    });
  }

  save() {
    this.itemMoreAdd.wasBuy = true;
    if (!this.itemMoreAdd.id) {
      this.itemMoreAdd.createdSystem = false;
    }
    this.itemMoreService.save(this.itemMoreAdd).subscribe(value => {
      this.itemMoreAdd = new ItemMore();
      this.itemMoreList.push(value);
    });
  }

  edit(val) {
    this.itemMoreAdd = val;
    this.itemMoreList.splice(this.itemMoreList.indexOf(val), 1);
  }

  delete(item) {
    this.itemMoreService.delete(item.id).subscribe(value => {
      this.itemMoreList.splice(this.itemMoreList.indexOf(item), 1);
    })
  }

  loadFromC5() {
    this.loading = true;
    this.itemMoreService.loadFromC5().subscribe((response) => {
      this.itemMoreList = response;
      this.loading = false;
    } );
  }

  deleteAll() {
    this.itemMoreService.deleteAll().subscribe((response) => {
      this.itemMoreList = [];
    });
  }

}
