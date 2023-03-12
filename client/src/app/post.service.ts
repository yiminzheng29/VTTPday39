import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { Post, PostDetails, PostResponse } from "./models";

@Injectable()
export class PostService {

    constructor(private http: HttpClient) {}

    postComment(post: Post): Promise<PostResponse> {

        const form = new FormData()
        form.set("email", post.email)
        form.set("title", post.title)
        form.set("text", post.text)
        form.set("image", post.image)

        return firstValueFrom(this.http.post<PostResponse>('/post', form))
    }

    getPost(postId: string): Promise<PostDetails> {
        console.info("Retrieving info for postId: ", postId)
        return firstValueFrom(this.http.get<PostDetails>(`/post/${postId}`))
    }

    votePost(postId: string, vote: string): Promise<PostDetails> {
        console.info("Voted: ", vote)
        return firstValueFrom(this.http.post<PostDetails>(`/post/${postId}/${vote}`, vote))
    }
}