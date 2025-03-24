/* eslint-disable no-unused-vars */
//need to disable unused var because enum is read as un used

export interface Page<T> {
  content: T[];
  pagination: Pagination;
}

export interface Pagination {
  total: number;
  page: number;
  size: number;
}

export interface PageQueryParams {
  page?: number;
  size?: number;
  sort?: SortDirection;
  search?: string;
}

export enum SortDirection {
  ASC = 'ASC',
  DESC = 'DESC',
}
