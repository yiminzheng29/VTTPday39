import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { PostDetails } from '../models';
import { PostService } from '../post.service';

@Component({
  selector: 'app-post-details',
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.css']
})
export class PostDetailsComponent implements OnInit, OnDestroy{

  params$!: Subscription
  post!: PostDetails 
  id: string = ""

  constructor(private activatedRoute: ActivatedRoute, private postSvc: PostService) {}

  ngOnInit(): void {
      console.info(">>>> in ngOnInit")
      this.params$ = this.activatedRoute.params.subscribe(
        (result) => {
        this.id = result['postId']
        this.postSvc.getPost(this.id)
          .then((result) => {
            this.post = result
            console.info("Results retrieved for postId", result)
          })
          .catch(error => {
            console.info("error: ", error)
          })
      }
      )
  }

  ngOnDestroy(): void {
      this.params$.unsubscribe()
  }

  async vote(vote: string) {
    this.post = await this.postSvc.votePost(this.id, vote)
  }

}
