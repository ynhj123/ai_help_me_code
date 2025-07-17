import { getShipments, updateShipment } from '@/services/api';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import { Button, message, Modal, Form, Select } from 'antd';
import React, { useRef, useState } from 'react';

const { Option } = Select;

const LogisticsList: React.FC = () => {
  const actionRef = useRef<ActionType>(null);
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<any>();
  const [form] = Form.useForm();

  const handleUpdateStatus = async (fields: any) => {
    const hide = message.loading('正在更新');
    try {
      await updateShipment(currentRow.id, { status: fields.status });
      hide();
      message.success('更新成功');
      handleUpdateModalVisible(false);
      setCurrentRow(undefined);
      actionRef.current?.reload();
    } catch (error) {
      hide();
      message.error('更新失败请重试！');
    }
  };

  const columns: ProColumns<any>[] = [
    {
      title: '物流单号',
      dataIndex: 'trackingNumber',
      valueType: 'text',
    },
    {
      title: '订单号',
      dataIndex: 'order',
      valueType: 'text',
      render: (_, record) => record.order?.orderNumber || '-',
    },
    {
      title: '收货人',
      dataIndex: 'recipient',
      valueType: 'text',
      render: (_, record) => record.order?.shippingAddress?.name || '-',
    },
    {
      title: '物流公司',
      dataIndex: 'carrier',
      valueType: 'text',
    },
    {
      title: '状态',
      dataIndex: 'status',
      valueEnum: {
        pending: { text: '待发货', status: 'Warning' },
        shipped: { text: '已发货', status: 'Processing' },
        in_transit: { text: '运输中', status: 'Processing' },
        delivered: { text: '已送达', status: 'Success' },
        failed: { text: '配送失败', status: 'Error' },
      },
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      valueType: 'dateTime',
      hideInForm: true,
    },
    {
      title: '更新时间',
      dataIndex: 'updatedAt',
      valueType: 'dateTime',
      hideInForm: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <a
          key="view"
          onClick={() => {
            // 查看物流详情
            console.log('查看物流详情', record);
          }}
        >
          查看
        </a>,
        <a
          key="update"
          onClick={() => {
            handleUpdateModalVisible(true);
            setCurrentRow(record);
            form.setFieldsValue(record);
          }}
        >
          更新状态
        </a>,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable
        headerTitle="物流列表"
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              actionRef.current?.reload();
            }}
          >
            刷新
          </Button>,
        ]}
        request={async (params) => {
          const response: any = await getShipments(params);
          return {
            data: response.data?.data || [],
            total: response.data?.total || 0,
            success: response.code === 200,
          };
        }}
        columns={columns}
      />
      
      <Modal
        title="更新物流状态"
        width="400px"
        open={updateModalVisible}
        onOk={() => {
          form.validateFields().then((values) => {
            handleUpdateStatus(values);
            form.resetFields();
          });
        }}
        onCancel={() => {
          handleUpdateModalVisible(false);
          setCurrentRow(undefined);
          form.resetFields();
        }}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            label="物流状态"
            name="status"
            rules={[{ required: true, message: '请选择状态' }]}
          >
            <Select placeholder="请选择状态">
              <Option value="pending">待发货</Option>
              <Option value="shipped">已发货</Option>
              <Option value="in_transit">运输中</Option>
              <Option value="delivered">已送达</Option>
              <Option value="failed">配送失败</Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </PageContainer>
  );
};

export default LogisticsList;