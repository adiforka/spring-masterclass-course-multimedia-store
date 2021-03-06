import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from "@angular/common/http"
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { Api } from './api';
import { UserService } from "./service/user.service";
import { ProductService } from "./service/product.service";
import { UsersListComponent } from './component/users-list/users-list.component';
import { UserFormComponent } from './component/user-form/user-form.component';
import { ProductsListComponent } from './component/products-list/products-list.component';
import { ProductFormComponent } from './component/product-form/product-form.component';
import { UserSearchComponent } from './component/user-search/user-search.component';

@NgModule({
  declarations: [
    AppComponent,
    UsersListComponent,
    UserFormComponent,
    ProductsListComponent,
    ProductFormComponent,
    UserSearchComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  //api will not be injected into the service
  providers: [
    Api,
    UserService,
    ProductService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
