import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { MainComponent } from './components/main.component';
import { PostService } from './post.service';
import { PostDetailsComponent } from './components/post-details.component';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {path: '', component: MainComponent},
  {path: 'main', component: MainComponent},
  {path: 'postdetails/:postId', component: PostDetailsComponent},
  {path: '**', redirectTo: '/', pathMatch: 'full'}
]

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    PostDetailsComponent
  ],
  imports: [
    BrowserModule, ReactiveFormsModule,
    HttpClientModule, RouterModule.forRoot(routes, {useHash: true})
  ],
  providers: [PostService],
  bootstrap: [AppComponent]
})
export class AppModule { }
