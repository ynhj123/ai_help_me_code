import { Table, Button, Space, Tag, Modal, Form, Input, Select, message } from 'antd'
import { useState, useEffect } from 'react'
import { Product } from '@/types'

const { Option } = Select

const ProductList: React.FC = () => {
  const [loading, setLoading] = useState(false)
  const [products, setProducts] = useState<Product[]>([])
  const [modalVisible, setModalVisible] = useState(false)
  const [editingProduct, setEditingProduct] = useState<Product | null>(null)
  const [form] = Form.useForm()

  // 模拟商品数据
  const mockProducts: Product[] = [
    {
      id: 1,
      name: 'iPhone 15 Pro',
      description: '最新款iPhone，配备A17 Pro芯片',
      category: {
        id: 1,
        name: '手机',
        description: '智能手机',
        sortOrder: 1,
        status: 'ACTIVE',
        createdAt: '2024-01-01T00:00:00Z',
        updatedAt: '2024-01-01T00:00:00Z',
      },
      skus: [
        {
          id: 1,
          skuCode: 'IPHONE15PRO-128GB',
          price: 7999,
          stock: 100,
          attributes: { color: '深空黑色', storage: '128GB' },
          status: 'ACTIVE',
          images: [],
        },
      ],
      images: [
        {
          id: 1,
          url: 'https://example.com/iphone15pro.jpg',
          alt: 'iPhone 15 Pro',
          sortOrder: 1,
          isPrimary: true,
        },
      ],
      attributes: [
        {
          id: 1,
          name: '颜色',
          value: '深空黑色',
          type: 'SELECT',
          required: true,
          sortOrder: 1,
        },
      ],
      status: 'ACTIVE',
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
  ]

  useEffect(() => {
    fetchProducts()
  }, [])

  const fetchProducts = async () => {
    setLoading(true)
    try {
      // 这里将集成实际的API调用
      setProducts(mockProducts)
    } catch (error) {
      message.error('获取商品列表失败')
    } finally {
      setLoading(false)
    }
  }

  const handleAdd = () => {
    setEditingProduct(null)
    form.resetFields()
    setModalVisible(true)
  }

  const handleEdit = (product: Product) => {
    setEditingProduct(product)
    form.setFieldsValue({
      ...product,
      categoryId: product.category.id,
    })
    setModalVisible(true)
  }

  const handleDelete = async (productId: number) => {
    try {
      // 这里将集成实际的API调用
      message.success('删除成功')
      fetchProducts()
    } catch (error) {
      message.error('删除失败')
    }
  }

  const handleSubmit = async (values: any) => {
    try {
      if (editingProduct) {
        // 更新商品
        message.success('更新成功')
      } else {
        // 创建商品
        message.success('创建成功')
      }
      setModalVisible(false)
      fetchProducts()
    } catch (error) {
      message.error('操作失败')
    }
  }

  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: '商品名称',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: '分类',
      key: 'category',
      render: (_: any, record: Product) => record.category.name,
    },
    {
      title: '状态',
      key: 'status',
      render: (_: any, record: Product) => (
        <Tag color={record.status === 'ACTIVE' ? 'green' : 'red'}>
          {record.status === 'ACTIVE' ? '激活' : '禁用'}
        </Tag>
      ),
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      key: 'createdAt',
    },
    {
      title: '操作',
      key: 'actions',
      render: (_: any, record: Product) => (
        <Space>
          <Button type="link" onClick={() => handleEdit(record)}>
            编辑
          </Button>
          <Button type="link" danger onClick={() => handleDelete(record.id)}>
            删除
          </Button>
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
        <h1>商品管理</h1>
        <Button type="primary" onClick={handleAdd}>
          新增商品
        </Button>
      </div>

      <Table
        dataSource={products}
        columns={columns}
        loading={loading}
        rowKey="id"
      />

      <Modal
        title={editingProduct ? '编辑商品' : '新增商品'}
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        onOk={() => form.submit()}
        okText="保存"
        cancelText="取消"
      >
        <Form form={form} onFinish={handleSubmit} layout="vertical">
          <Form.Item
            name="name"
            label="商品名称"
            rules={[{ required: true, message: '请输入商品名称' }]}
          >
            <Input />
          </Form.Item>
          
          <Form.Item
            name="description"
            label="商品描述"
            rules={[{ required: true, message: '请输入商品描述' }]}
          >
            <Input.TextArea rows={4} />
          </Form.Item>
          
          <Form.Item
            name="categoryId"
            label="分类"
            rules={[{ required: true, message: '请选择分类' }]}
          >
            <Select placeholder="请选择分类">
              <Option value={1}>手机</Option>
              <Option value={2}>电脑</Option>
              <Option value={3}>家电</Option>
            </Select>
          </Form.Item>
          
          <Form.Item
            name="status"
            label="状态"
            valuePropName="checked"
          >
            <Select>
              <Option value="ACTIVE">激活</Option>
              <Option value="INACTIVE">禁用</Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

export default ProductList