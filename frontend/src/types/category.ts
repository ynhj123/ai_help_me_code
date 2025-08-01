// 分类信息接口
export interface Category {
  id: number
  name: string
  description?: string
  parentId: number
  sortOrder: number
  status: number
  createdAt: string
  updatedAt: string
}

// 分类表单接口
export interface CategoryForm {
  name: string
  description?: string
  parentId: number
  sortOrder: number
  status: number
}

// 分类树节点接口
export interface CategoryTreeNode {
  id: number
  name: string
  children?: CategoryTreeNode[]
}

// 分类树节点DTO接口
export interface CategoryTreeNodeDTO {
  id: number
  name: string
  description?: string
  parentId: number
  sortOrder: number
  status: number
  children?: CategoryTreeNodeDTO[]
}