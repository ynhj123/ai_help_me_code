// 操作日志接口
export interface OperationLog {
  id: number
  userId?: number
  username?: string
  operation: string
  method: string
  url?: string
  ip?: string
  userAgent?: string
  requestParams?: string
  responseResult?: string
  executionTime?: number
  createdAt: string
}