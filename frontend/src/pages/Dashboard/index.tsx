import React from 'react';
import { Card, Row, Col, Statistic, Typography } from 'antd';
import { UserOutlined, ShoppingOutlined, ShoppingCartOutlined, DollarOutlined } from '@ant-design/icons';
import './Dashboard.css';

const { Title } = Typography;

const Dashboard: React.FC = () => {
  const stats = [
    {
      title: '总用户数',
      value: 1234,
      icon: <UserOutlined />,
      color: '#1890ff',
      prefix: '',
    },
    {
      title: '商品总数',
      value: 567,
      icon: <ShoppingOutlined />,
      color: '#52c41a',
      prefix: '',
    },
    {
      title: '订单总数',
      value: 890,
      icon: <ShoppingCartOutlined />,
      color: '#faad14',
      prefix: '',
    },
    {
      title: '总销售额',
      value: 123456,
      icon: <DollarOutlined />,
      color: '#f5222d',
      prefix: '¥',
    },
  ];

  return (
    <div className="dashboard-container">
      <Title level={2} className="dashboard-title">
        仪表盘
      </Title>

      <Row gutter={[16, 16]} className="stats-row">
        {stats.map((stat, index) => (
          <Col key={index} xs={24} sm={12} md={12} lg={6} xl={6}>
            <Card hoverable className="stat-card">
              <Statistic
                title={stat.title}
                value={stat.value}
                prefix={
                  <div style={{ color: stat.color, fontSize: 24, marginRight: 8 }}>
                    {stat.icon}
                  </div>
                }
                valueStyle={{ color: stat.color }}
              />
            </Card>
          </Col>
        ))}
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
        <Col xs={24} lg={12}>
          <Card title="最近订单" className="dashboard-card">
            <p>订单数据图表将在这里显示</p>
          </Card>
        </Col>
        <Col xs={24} lg={12}>
          <Card title="销售趋势" className="dashboard-card">
            <p>销售趋势图表将在这里显示</p>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Dashboard;