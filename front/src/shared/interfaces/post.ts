import { Topic } from './topic';
import { User } from './user';

export interface Post {
  uuid: string;
  title: string;
  content: string;
  createdAt: string;
  updatedAt: string;
  deletedAt: string;
  topic: Topic;
  user: User;
  comments: Comment[];
}

export interface Comment {
  uuid: string;
  content: string;
  createdAt: string;
  updatedAt: string;
  deletedAt: string;
  post: Post;
  user: User;
}
