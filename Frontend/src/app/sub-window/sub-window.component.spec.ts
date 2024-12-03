import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubWindowComponent } from './sub-window.component';

describe('SubWindowComponent', () => {
  let component: SubWindowComponent;
  let fixture: ComponentFixture<SubWindowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubWindowComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SubWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
