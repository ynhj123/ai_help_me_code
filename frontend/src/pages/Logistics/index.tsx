import { Table, Button, Space, Tag, message } from 'antd'
import { useState, useEffect } from 'react'

const Logistics: React.FC = () => {
  const [loading, setLoading] = useState(false)
  const [shippingOrders, setShippingOrders] = useState<any[]>([])

  // 模拟物流订单数据
  const mockShippingOrders = [
    {
      id: 1,
      orderNumber: 'ORD-2024-001',
      trackingNumber: 'SF1234567890',
      carrier: {
        id: 1,
        name: '顺丰速运',
        code: 'SF',
        contact: '400-811-1111',
        status: 'ACTIVE',
      },
      status: 'SHIPPED',
      estimatedDelivery: '2024-01-18',
      actualDelivery: null,
      shippingAddress: {
        fullName: '张三',
        phone: '13800138000',
        province: '北京市',
        city: '北京市',
        district: '朝阳区',
        address: '建国门外大街1号',
      },
      createdAt: '2024-01-15T10:00:00Z',
    },
    {
      id: 2,
      orderNumber: 'ORD-2024-002',
      trackingNumber: 'YT0987654321',
      carrier: {
        id: 2,
        name: '圆通速递',
        code: 'YT',
        contact: '95554',
        status: 'ACTIVE',
      },
      status: 'IN_TRANSIT',
      estimatedDelivery: '2024-01-19',
      actualDelivery: null,
      shippingAddress: {
        fullName: '李四',
        phone: '13900139000',
        province: '上海市',
        city: '上海市',
        district: '浦东新区',
        address: '陆家嘴金融中心1号',
      },
      createdAt: '2024-01-16T10:00:00Z',
    },
  ]

  useEffect(() => {
    fetchShippingOrders()
  }, [])

  const fetchShippingOrders = async () => {
    setLoading(true)
    try {
      // 这里将集成实际的API调用
      setShippingOrders(mockShippingOrders)
    } catch (error) {
      message.error('获取物流订单失败')
    } finally {
      setLoading(false)
    }
  }

  const getStatusColor = (status: string) => {
    const colors: Record<string, string> = {
      PENDING: 'orange',
      SHIPPED: 'blue',
      IN_TRANSIT: 'cyan',
      DELIVERED: 'green',
      RETURNED: 'red',
    }
    return colors[status] || 'default'
  }

  const getStatusText = (status: string) => {
    const texts: Record<string, string> = {
      PENDING: '待发货',
      SHIPPED: '已发货',
      IN_TRANSIT: '运输中',
      DELIVERED: '已送达',
      RETURNED: '已退回',
    }
    return texts[status] || status
  }

  const handleTrack = (trackingNumber: string) => {
    message.info(`正在查询物流单号: ${trackingNumber}`)
  }

  const handleUpdateStatus = async (orderId: number, status: string) => {
    try {
      // 这里将集成实际的API调用
      message.success('状态更新成功')
      fetchShippingOrders()
    } catch (error) {
      message.error('状态更新失败')
    }
  }

  const columns = [
    {
      title: '订单号',
      dataIndex: 'orderNumber',
      key: 'orderNumber',
    },
    {
      title: '物流单号',
      dataIndex: 'trackingNumber',
      key: 'trackingNumber',
    },
    {
      title: '承运商',
      key: 'carrier',
      render: (_: any, record: any) => record.carrier.name,
    },
    {
      title: '收货人',
      key: 'recipient',
      render: (_: any, record: any) => record.shippingAddress.fullName,
    },
    {
      title: '状态',
      key: 'status',
      render: (_: any, record: any) => (
        <Tag color={getStatusColor(record.status)}>
          {getStatusText(record.status)}
        </Tag>
      ),
    },
    {
      title: '预计送达',
      dataIndex: 'estimatedDelivery',
      key: 'estimatedDelivery',
    },
    {
      title: '实际送达',
      dataIndex: 'actualDelivery',
      key: 'actualDelivery',
      render: (actualDelivery: string) => actualDelivery || '-',
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      key: 'createdAt',
    },
    {
      title: '操作',
      key: 'actions',
      render: (_: any, record: any) => (
        <Space>
          <Button type="link" onClick={() => handleTrack(record.trackingNumber)}>
            跟踪
          </Button>
          <Button 
            type="link" 
            onClick={() => handleUpdateStatus(record.id, 'DELIVERED')}
            disabled={record.status === 'DELIVERED'}
          >
            标记送达
          </Button>
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
        <h1>物流管理</h1>
      </div>

      <Table
        dataSource={shippingOrders}
        columns={columns}
        loading={loading}
        rowKey="id"
      />
    </div>
  )
}

export default Logistics