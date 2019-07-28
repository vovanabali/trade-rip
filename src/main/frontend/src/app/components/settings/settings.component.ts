import { Component, OnInit } from '@angular/core';
import {SettingsService} from "../../services/settings.service";
import {User} from "../../domains/user";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  user = new User();

  constructor(private settingsService: SettingsService) { }

  ngOnInit() {
    this.settingsService.getSettings().subscribe(user => {
      this.user = user;
    });
  }

  save() {
    this.settingsService.save(this.user).subscribe(user => {
      this.user = user;
    });
  }
}
