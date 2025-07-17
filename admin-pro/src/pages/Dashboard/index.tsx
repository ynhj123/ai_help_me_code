import { getDashboardStats } from '@/services/api';
import { PageContainer } from '@ant-design/pro-components';
import { Card, Col, Row, Statistic } from 'antd';
import { ShoppingCartOutlined, UserOutlined, ShoppingOutlined, TruckOutlined } from '@ant-design/icons';
import React, { useEffect, useState } from 'react';

const Dashboard: React.FC = () => {
  const [stats, setStats] = useState({
    totalUsers: 0,
    totalProducts: 0,
    totalOrders: 0,
    totalShipments: 0,
  });

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      const response: any = await getDashboardStats();
      if (response.code === 200) {
        setStats(response.data);
      }
    } catch (error) {
      console.error('获取统计数据失败:', error);
    }
  };

  return (
    <PageContainer>
      <div style={{ padding: 24 }}>
        <Row gutter={24}>
          <Col span={6}>
            <Card>
              <Statistic
                title="总用户数"
                value={stats.totalUsers}
                prefix={<UserOutlined />}
                valueStyle={{ color: '#3f8600' }}
              />
            </Card>
          </Col>
          <Col span={6}>
            <Card>
              <Statistic
                title="商品总数"
                value={stats.totalProducts}
                prefix={<ShoppingOutlined />}
                valueStyle={{ color: '#cf1322' }}
              />
            </Card>
          </Col>
          <Col span={6}>
            <Card>
              <Statistic
                title="订单总数"
                value={stats.totalOrders}
                prefix={<ShoppingCartOutlined />}
                valueStyle={{ color: '#1890ff' }}
              />
            </Card>
          </Col>
          <Col span={6}>
            <Card>
              <Statistic
                title="物流单数"
                value={stats.totalShipments}
                prefix={<TruckOutlined />}
                valueStyle={{ color: '#722ed1' }}
              />
            </Card>
          </Col>
        </Row>
        
        <Row gutter={24} style={{ marginTop: 24 }}>
          <Col span={12}>
            <Card title="最近订单" style={{ height: 400 }}>
              <div>订单数据图表</div>
            </Card>
          </Col>
          <Col span={12}>
            <Card title="销售统计" style={{ height: 400 }}>
              <div>销售数据图表</div>
            </Card>
          </Col>
        </Row>
      </div>
    </PageContainer>
  );
};

export default Dashboard;