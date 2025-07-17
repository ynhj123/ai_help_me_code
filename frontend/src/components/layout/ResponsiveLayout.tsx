import React, { useState, useEffect } from 'react';
import { Layout, Drawer, Button, Menu } from 'antd';
import {
  MenuOutlined,
  CloseOutlined,
  DashboardOutlined,
  UserOutlined,
  ShoppingOutlined,
  ShoppingCartOutlined,
  TruckOutlined,
  BarChartOutlined,
} from '@ant-design/icons';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuthStore } from '../../stores/auth';
import '../../styles/responsive.css';

const { Header, Content } = Layout;

interface ResponsiveLayoutProps {
  children: React.ReactNode;
}

const ResponsiveLayout: React.FC<ResponsiveLayoutProps> = ({ children }) => {
  const [mobileOpen, setMobileOpen] = useState(false);
  const [isMobile, setIsMobile] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const { logout } = useAuthStore();

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth <= 768);
    };

    handleResize();
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  const menuItems = [
    {
      key: '/dashboard',
      icon: <DashboardOutlined />,
      label: '仪表盘',
    },
    {
      key: '/users',
      icon: <UserOutlined />,
      label: '用户管理',
    },
    {
      key: '/products',
      icon: <ShoppingOutlined />,
      label: '商品管理',
    },
    {
      key: '/orders',
      icon: <ShoppingCartOutlined />,
      label: '订单管理',
    },
    {
      key: '/logistics',
      icon: <TruckOutlined />,
      label: '物流管理',
    },
    {
      key: '/analytics',
      icon: <BarChartOutlined />,
      label: '数据分析',
    },
  ];

  const handleMenuClick = (key: string) => {
    navigate(key);
    if (isMobile) {
      setMobileOpen(false);
    }
  };

  const renderMenu = () => (
    <Menu
      mode="inline"
      selectedKeys={[location.pathname]}
      items={menuItems}
      onClick={({ key }) => handleMenuClick(key)}
      style={{ height: '100%', borderRight: 0 }}
    />
  );

  const renderMobileMenu = () => (
    <Drawer
      title="菜单"
      placement="left"
      onClose={() => setMobileOpen(false)}
      open={mobileOpen}
      width={280}
      bodyStyle={{ padding: 0 }}
    >
      {renderMenu()}
    </Drawer>
  );

  const renderDesktopMenu = () => (
    <div className="sidebar desktop-only">
      <div className="sidebar-header">
        <h2>管理后台</h2>
      </div>
      {renderMenu()}
    </div>
  );

  return (
    <Layout className="layout-container">
      {isMobile && renderMobileMenu()}
      {renderDesktopMenu()}

      <Layout className="main-layout">
        <Header className="header">
          <div className="header-content">
            {isMobile && (
              <Button
                className="mobile-menu-button"
                type="text"
                icon={<MenuOutlined />}
                onClick={() => setMobileOpen(true)}
              />
            )}
            
            <div className="header-title">
              <h1>管理后台</h1>
            </div>

            <div className="header-actions">
              <Button type="text" onClick={logout}>
                退出登录
              </Button>
            </div>
          </div>
        </Header>

        <Content className="main-content">
          <div className="content-wrapper">{children}</div>
        </Content>
      </Layout>
    </Layout>
  );
};

export default ResponsiveLayout;