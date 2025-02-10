export interface User {
  uuid?: string,
  email: string,
  name: string,
  password?: string,
  createdAt?: Date,
  updatedAt?: Date,
  deletedAt?: Date | null,
}
