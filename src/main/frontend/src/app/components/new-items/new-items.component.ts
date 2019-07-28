import {Component, OnDestroy, OnInit} from '@angular/core';
import {MainService} from "../../services/main.service";
import {MessageService} from "primeng/components/common/messageservice";
import {ItemMoreService} from "../../services/itemMore.service";
import {ItemMore} from "../../domains/ItemMore";
import {User} from "../../domains/user";
import {SettingsService} from "../../services/settings.service";
import {BlackItemService} from "../../services/blackItem.service";
import {BlackItem} from "../../domains/blackItem";
import * as _ from 'lodash';
import {Subscription} from "rxjs";


@Component({
  selector: 'app-new-items',
  templateUrl: './new-items.component.html',
  styleUrls: ['./new-items.component.css']
})
export class NewItemsComponent implements OnInit, OnDestroy {
  ngOnDestroy(): void {
    if (!!this.ws) {
      this.ws.close();
    }
  }

  cols: any[];
  items: any[] = [];
  ws: any;
  user: User;

  constructor(private mainService: MainService,
              private itemMoreService: ItemMoreService,
              private settingsService: SettingsService,
              private blackItemService: BlackItemService
  ) {
  }

  itemMoresMap = {};

  ngOnInit() {
    this.cols = [
      {field: 'lastDateUpdate', header: 'Дата'},
      {field: 'marketHashName', header: 'Наименование'},
      {field: 'gems', header: 'Гемы'},
      {field: 'price', header: 'Цена'},
      {field: 'status', header: 'Статус'}
    ];
    this.settingsService.getSettings().subscribe(user => {
      this.user = user
    });
    setInterval(() => {
      this.itemMoreService.getItemsMore().subscribe(response => {
        this.mapMoreItems(response);
      });
    }, 120 * 1000)
  }

  start() {
    this.itemMoreService.getItemsMore().subscribe(response => {
      this.mapMoreItems(response);
      this.startListen();
    });
  }

  mapMoreItems(itemMores: ItemMore[]) {
    _.filter(itemMores, (item) => item.wasBuy || !item.createdSystem).forEach(itemMore => {
      this.itemMoresMap[itemMore.name.trim()] = itemMore;
      if (!!itemMore.ruName) {
        this.itemMoresMap[itemMore.ruName.trim()] = itemMore;
      }
    })
  }



  startListen = () => {
    this.ws = new WebSocket('wss://wsn.dota2.net/wsn/');
    this.ws.onopen = () => {
      this.ws.send("newitems_cs");
    };

    this.ws.onclose = () => {
      console.log("Socket is close reload after 5 seconds");
      setTimeout(() => {
        this.start();
      }, 5000);
    };

    this.ws.onmessage = (obj) => {
      const data: any = JSON.parse(obj.data);
      const item: any = JSON.parse(data.data);
      this.checkEndBuy({
        marketHashName: item['i_market_hash_name'],
        price: item['ui_price'],
        lastDateUpdate: new Date().toISOString(),
        identity: item['i_classid'] + '_' + item['i_instanceid'],
        status: "В обработке",
        gems: ''
      });
    };
  };

  checkEndBuy(item: any) {
    if (!!this.itemMoresMap[item.marketHashName.trim()]) {
      console.log(`Found item from item more table: ${item.marketHashName}`);
      this.itemMoreService.getItemDescription(item.identity, this.user.marketApi).subscribe(value => {
        const descriptions = value.description as any[];
        const itemMores = this.checkBuyDescription(descriptions, item.marketHashName);
        if (itemMores.length > 0) {
          const maxPrice = _.sumBy(itemMores, 'price') + this.itemMoresMap[item.marketHashName].price;
          item.gems =_.join(_.map(itemMores, 'name'), '; \n');
          if (maxPrice >= item.price) {
            this.blackItemService.existInList([..._.map(itemMores, 'name'), item.marketHashName]).subscribe((response: boolean) => {
              if (!response) {
                this.itemMoreService.buyItem(item.identity, item.price * 100, this.user.marketApi).subscribe((buyResponse: any) => {
                  if (buyResponse.id) {
                    item.status = "Предмет успешно куплен";
                    this.items = [item, ...this.items];
                    this.blackItemService.saveAll(
                      _.map([..._.map(itemMores, 'name'), item.marketHashName], (name) => {
                        const blackItem = new BlackItem();
                        blackItem.name = name;
                        return blackItem;
                      })
                    );
                  } else {
                    item.status = "ОШИБКА, ошибка покупки: " + JSON.stringify(buyResponse);
                    this.items = [item, ...this.items];
                  }
                });
              } else {
                item.status = "Ошибка, предмет в чёрном списке";
                this.items = [item, ...this.items];
              }
            });
          } else {
            item.status = `Ошибка, предмет цена завышена, необходима ниже ${maxPrice}`;
            this.items = [item, ...this.items];
          }
        }
      });
    }
  }

  checkBuyDescription(descriptions: any[], itemMainName: String): ItemMore[] {
    const html: string = _.join(_.map(_.filter(descriptions, ['type', 'html']), 'value'), '');
    const htmlObject = document.createElement('div');
    htmlObject.innerHTML = html;
    let moreItems = [];
    const divs = htmlObject.getElementsByTagName('span');
    for (let i = 0; i < divs.length; i++) {
      console.log(divs[i].innerText);
      const itemMore = this.itemMoresMap[divs[i].innerText.trim()];
      if (!!itemMore && itemMore.name.trim() !== itemMainName.trim()) {
        moreItems.push(itemMore);
      }
    }
    return moreItems;
  }

  stop() {
    if (!!this.ws) {
      this.ws.close();
    }
  }
}
