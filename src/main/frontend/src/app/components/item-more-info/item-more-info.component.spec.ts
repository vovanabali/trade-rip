import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemMoreInfoComponent } from './item-more-info.component';

describe('ItemMoreInfoComponent', () => {
  let component: ItemMoreInfoComponent;
  let fixture: ComponentFixture<ItemMoreInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ItemMoreInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemMoreInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
