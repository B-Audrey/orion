export interface Page<T> {
  data: T[];
  pagination: {
    total: number;
    page: number;
    size: number;
  };
}
