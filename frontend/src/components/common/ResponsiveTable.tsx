import React from 'react';
import { Table, Card, Space, Button, Typography } from 'antd';
import { EditOutlined, DeleteOutlined, EyeOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';

const { Text } = Typography;

interface ResponsiveTableProps<T> {
  columns: ColumnsType<T>;
  dataSource: T[];
  loading?: boolean;
  pagination?: any;
  onEdit?: (record: T) => void;
  onDelete?: (record: T) => void;
  onView?: (record: T) => void;
  rowKey?: string | ((record: T) => string);
  title?: string;
  scroll?: { x?: number | string; y?: number | string };
}

const ResponsiveTable = <T extends Record<string, any>>({
  columns,
  dataSource,
  loading = false,
  pagination,
  onEdit,
  onDelete,
  onView,
  rowKey = 'id',
  title,
  scroll,
}: ResponsiveTableProps<T>) => {
  // 移动端操作列
  const mobileColumns: ColumnsType<T> = [
    ...columns,
    {
      title: '操作',
      key: 'action',
      fixed: 'right',
      width: 100,
      render: (_, record) => (
        <Space size="small">
          {onView && (
            <Button
              type="text"
              icon={<EyeOutlined />}
              onClick={() => onView(record)}
              size="small"
            />
          )}
          {onEdit && (
            <Button
              type="text"
              icon={<EditOutlined />}
              onClick={() => onEdit(record)}
              size="small"
            />
          )}
          {onDelete && (
            <Button
              type="text"
              danger
              icon={<DeleteOutlined />}
              onClick={() => onDelete(record)}
              size="small"
            />
          )}
        </Space>
      ),
    },
  ];

  // 桌面端操作列
  const desktopColumns: ColumnsType<T> = [
    ...columns,
    {
      title: '操作',
      key: 'action',
      fixed: 'right',
      width: 150,
      render: (_, record) => (
        <Space size="middle">
          {onView && (
            <Button
              type="link"
              icon={<EyeOutlined />}
              onClick={() => onView(record)}
            >
              查看
            </Button>
          )}
          {onEdit && (
            <Button
              type="link"
              icon={<EditOutlined />}
              onClick={() => onEdit(record)}
            >
              编辑
            </Button>
          )}
          {onDelete && (
            <Button
              type="link"
              danger
              icon={<DeleteOutlined />}
              onClick={() => onDelete(record)}
            >
              删除
            </Button>
          )}
        </Space>
      ),
    },
  ];

  const getResponsiveColumns = () => {
    const isMobile = window.innerWidth <= 768;
    const hasActions = onEdit || onDelete || onView;
    
    if (isMobile && hasActions) {
      return mobileColumns;
    } else if (!isMobile && hasActions) {
      return desktopColumns;
    }
    
    return columns;
  };

  const getResponsiveScroll = () => {
    const isMobile = window.innerWidth <= 768;
    
    if (isMobile) {
      return { x: 'max-content', ...scroll };
    }
    
    return scroll || { x: '100%' };
  };

  const getResponsivePagination = () => {
    const isMobile = window.innerWidth <= 768;
    
    if (isMobile) {
      return {
        ...pagination,
        size: 'small' as const,
        showSizeChanger: false,
        showQuickJumper: false,
        showTotal: (total: number) => `共 ${total} 条`,
      };
    }
    
    return {
      ...pagination,
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: (total: number, range: [number, number]) =>
        `${range[0]}-${range[1]} 共 ${total} 条`,
    };
  };

  return (
    <div className="responsive-table-container">
      {title && (
        <div className="table-header">
          <h3>{title}</h3>
        </div>
      )}
      
      <div className="responsive-table-wrapper">
        <Table
          columns={getResponsiveColumns()}
          dataSource={dataSource}
          loading={loading}
          pagination={getResponsivePagination()}
          rowKey={rowKey}
          scroll={getResponsiveScroll()}
          className="responsive-table"
          size={window.innerWidth <= 768 ? 'small' : 'middle'}
          rowClassName="responsive-table-row"
        />
      </div>
    </div>
  );
};

export default ResponsiveTable;