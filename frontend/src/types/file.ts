// 文件信息接口
export interface FileInfo {
  id: number
  originalName: string
  fileName: string
  filePath: string
  fileSize: number
  fileType?: string
  module?: string
  createdAt: string
  createdBy?: number
}