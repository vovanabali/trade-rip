import {Routes, RouterModule} from '@angular/router';
import {ModuleWithProviders} from '@angular/core';
import {NewItemsComponent} from "./components/new-items/new-items.component";
import {SettingsComponent} from "./components/settings/settings.component";
import {ItemMoreInfoComponent} from "./components/item-more-info/item-more-info.component";
import {BlackListComponent} from "./components/black-list/black-list.component";

export const routes: Routes = [
  {path: '', component: NewItemsComponent},
  {path: 'mode-info', component: ItemMoreInfoComponent},
  {path: 'settings', component: SettingsComponent},
  {path: 'black-list', component: BlackListComponent},
];

export const AppRoutes: ModuleWithProviders = RouterModule.forRoot(routes);
