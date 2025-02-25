import { Topic } from './topic';

export interface User {
  uuid?: string;
  email: string;
  name: string;
  password?: string;
  topics?: Topic[];
  createdAt?: Date;
  updatedAt?: Date;
  deletedAt?: Date | null;
}
