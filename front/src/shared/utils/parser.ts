import { PageQueryParams } from '../interfaces';

export const parsePaginationParams = (params: PageQueryParams) => {
  let base = '?';
  if (params.page) {
    base += `page=${params.page}&`;
  }
  if (params.size) {
    base += `size=${params.size}`;
  }
  if (params.sort) {
    base += `&sort=${params.sort}`;
  }
  return base;
};
