export interface Post {
    email: string
    title: string
    text: string
    image: File
}

export interface PostResponse{
    postId: string
}

export interface PostDetails {
    postId: string
    postDate: string
    userId: string
    email: string
    name: string
    title: string
    text: string
    imageUrl: string
    like?: number
    dislike?: number
}