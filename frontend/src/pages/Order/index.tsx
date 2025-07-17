import { Table, Button, Space, Tag, message } from 'antd'
import { useState, useEffect } from 'react'
import { Order } from '@/types'

const OrderList: React.FC = () => {
  const [loading, setLoading] = useState(false)
  const [orders, setOrders] = useState<Order[]>([])

  // 模拟订单数据
  const mockOrders: Order[] = [
    {
      id: 1,
      orderNumber: 'ORD-2024-001',
      user: {
        id: 1,
        username: 'user1',
        email: 'user1@example.com',
        firstName: 'John',
        lastName: 'Doe',
        status: 'ACTIVE',
        roles: [],
        createdAt: '2024-01-01T00:00:00Z',
        updatedAt: '2024-01-01T00:00:00Z',
      },
      items: [
        {
          id: 1,
          product: {
            id: 1,
            name: 'iPhone 15 Pro',
            description: '最新款iPhone',
            category: {
              id: 1,
              name: '手机',
              description: '智能手机',
              sortOrder: 1,
              status: 'ACTIVE',
              createdAt: '2024-01-01T00:00:00Z',
              updatedAt: '2024-01-01T00:00:00Z',
            },
            skus: [],
            images: [],
            attributes: [],
            status: 'ACTIVE',
            createdAt: '2024-01-01T00:00:00Z',
            updatedAt: '2024-01-01T00:00:00Z',
          },
          sku: {
            id: 1,
            skuCode: 'IPHONE15PRO-128GB',
            price: 7999,
            stock: 100,
            attributes: { color: '深空黑色', storage: '128GB' },
            status: 'ACTIVE',
            images: [],
          },
          quantity: 1,
          price: 7999,
        },
      ],
      totalAmount: 7999,
      status: 'PENDING',
      paymentStatus: 'PENDING',
      paymentMethod: 'ALIPAY',
      shippingAddress: {
        id: 1,
        fullName: '张三',
        phone: '13800138000',
        province: '北京市',
        city: '北京市',
        district: '朝阳区',
        address: '建国门外大街1号',
        postalCode: '100000',
      },
      createdAt: '2024-01-15T10:00:00Z',
      updatedAt: '2024-01-15T10:00:00Z',
    },
  ]

  useEffect(() => {
    fetchOrders()
  }, [])

  const fetchOrders = async () => {
    setLoading(true)
    try {
      // 这里将集成实际的API调用
      setOrders(mockOrders)
    } catch (error) {
      message.error('获取订单列表失败')
    } finally {
      setLoading(false)
    }
  }

  const handleView = (order: Order) => {
    // 查看订单详情
    message.info(`查看订单 ${order.orderNumber}`)
  }

  const handleUpdateStatus = async (orderId: number, status: string) => {
    try {
      // 这里将集成实际的API调用
      message.success('状态更新成功')
      fetchOrders()
    } catch (error) {
      message.error('状态更新失败')
    }
  }

  const getStatusColor = (status: string) => {
    const colors: Record<string, string> = {
      PENDING: 'orange',
      CONFIRMED: 'blue',
      SHIPPED: 'cyan',
      DELIVERED: 'green',
      CANCELLED: 'red',
    }
    return colors[status] || 'default'
  }

  const getStatusText = (status: string) => {
    const texts: Record<string, string> = {
      PENDING: '待处理',
      CONFIRMED: '已确认',
      SHIPPED: '已发货',
      DELIVERED: '已送达',
      CANCELLED: '已取消',
    }
    return texts[status] || status
  }

  const getPaymentStatusColor = (status: string) => {
    const colors: Record<string, string> = {
      PENDING: 'orange',
      PAID: 'green',
      FAILED: 'red',
      REFUNDED: 'red',
    }
    return colors[status] || 'default'
  }

  const getPaymentStatusText = (status: string) => {
    const texts: Record<string, string> = {
      PENDING: '待支付',
      PAID: '已支付',
      FAILED: '支付失败',
      REFUNDED: '已退款',
    }
    return texts[status] || status
  }

  const columns = [
    {
      title: '订单号',
      dataIndex: 'orderNumber',
      key: 'orderNumber',
    },
    {
      title: '客户',
      key: 'customer',
      render: (_: any, record: Order) => record.user.username,
    },
    {
      title: '商品',
      key: 'items',
      render: (_: any, record: Order) => (
        <Space direction="vertical">
          {record.items.map(item => (
            <div key={item.id}>
              {item.product.name} x {item.quantity}
            </div>
          ))}
        </Space>
      ),
    },
    {
      title: '总金额',
      key: 'totalAmount',
      render: (_: any, record: Order) => `¥${record.totalAmount.toFixed(2)}`,
    },
    {
      title: '订单状态',
      key: 'status',
      render: (_: any, record: Order) => (
        <Tag color={getStatusColor(record.status)}>
          {getStatusText(record.status)}
        </Tag>
      ),
    },
    {
      title: '支付状态',
      key: 'paymentStatus',
      render: (_: any, record: Order) => (
        <Tag color={getPaymentStatusColor(record.paymentStatus)}>
          {getPaymentStatusText(record.paymentStatus)}
        </Tag>
      ),
    },
    {
      title: '支付方式',
      key: 'paymentMethod',
      render: (_: any, record: Order) => {
        const methods: Record<string, string> = {
          ALIPAY: '支付宝',
          WECHAT: '微信支付',
          UNIONPAY: '银联支付',
          CREDIT_CARD: '信用卡',
        }
        return methods[record.paymentMethod] || record.paymentMethod
      },
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      key: 'createdAt',
    },
    {
      title: '操作',
      key: 'actions',
      render: (_: any, record: Order) => (
        <Space>
          <Button type="link" onClick={() => handleView(record)}>
            查看
          </Button>
          <Button 
            type="link" 
            onClick={() => handleUpdateStatus(record.id, 'SHIPPED')}
            disabled={record.status !== 'CONFIRMED'}
          >
            发货
          </Button>
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
        <h1>订单管理</h1>
      </div>

      <Table
        dataSource={orders}
        columns={columns}
        loading={loading}
        rowKey="id"
      />
    </div>
  )
}

export default OrderList