// 统一响应结果接口
export interface Result<T> {
  code: number
  message: string
  data: T
  timestamp: number
}

// 分页参数接口
export interface PageParams {
  page: number
  size: number
}

// 分页结果接口
export interface PageResult<T> {
  content: T[]
  total: number
  page: number
  size: number
}