import { getUsers, createUser, updateUser, deleteUser } from '@/services/api';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import { Button, message, Modal, Form, Input, Select, Switch } from 'antd';
import React, { useRef, useState } from 'react';

const { Option } = Select;

const UserList: React.FC = () => {
  const actionRef = useRef<ActionType>(null);
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<any>();
  const [form] = Form.useForm();

  const handleAdd = async (fields: any) => {
    const hide = message.loading('正在添加');
    try {
      await createUser({ ...fields });
      hide();
      message.success('添加成功');
      handleModalVisible(false);
      actionRef.current?.reload();
    } catch (error) {
      hide();
      message.error('添加失败请重试！');
    }
  };

  const handleUpdate = async (fields: any) => {
    const hide = message.loading('正在配置');
    try {
      await updateUser(currentRow.id, { ...fields });
      hide();
      message.success('配置成功');
      handleUpdateModalVisible(false);
      setCurrentRow(undefined);
      actionRef.current?.reload();
    } catch (error) {
      hide();
      message.error('配置失败请重试！');
    }
  };

  const handleRemove = async (selectedRow: any) => {
    const hide = message.loading('正在删除');
    try {
      await deleteUser(selectedRow.id);
      hide();
      message.success('删除成功，即将刷新');
      actionRef.current?.reload();
    } catch (error) {
      hide();
      message.error('删除失败，请重试');
    }
  };

  const columns: ProColumns<any>[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      hideInForm: true,
    },
    {
      title: '用户名',
      dataIndex: 'username',
      valueType: 'text',
      formItemProps: {
        rules: [{ required: true, message: '请输入用户名' }],
      },
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      valueType: 'text',
      formItemProps: {
        rules: [{ required: true, message: '请输入邮箱' }],
      },
    },
    {
      title: '角色',
      dataIndex: 'role',
      valueEnum: {
        admin: { text: '管理员', status: 'Success' },
        user: { text: '普通用户', status: 'Default' },
      },
      formItemProps: {
        rules: [{ required: true, message: '请选择角色' }],
      },
    },
    {
      title: '状态',
      dataIndex: 'status',
      render: (_, record) => (
        <Switch checked={record.status === 'active'} disabled />
      ),
      valueEnum: {
        active: { text: '启用', status: 'Success' },
        inactive: { text: '禁用', status: 'Error' },
      },
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      valueType: 'dateTime',
      hideInForm: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <a
          key="config"
          onClick={() => {
            handleUpdateModalVisible(true);
            setCurrentRow(record);
            form.setFieldsValue(record);
          }}
        >
          编辑
        </a>,
        <a
          key="delete"
          onClick={() => {
            Modal.confirm({
              title: '删除用户',
              content: '确定要删除这个用户吗？',
              onOk: () => handleRemove(record),
            });
          }}
        >
          删除
        </a>,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable
        headerTitle="用户列表"
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
              handleModalVisible(true);
            }}
          >
            <PlusOutlined /> 新建
          </Button>,
        ]}
        request={async (params) => {
          const response: any = await getUsers(params);
          return {
            data: response.data?.data || [],
            total: response.data?.total || 0,
            success: response.code === 200,
          };
        }}
        columns={columns}
        rowSelection={{
          onChange: (_, selectedRows) => {
            console.log(selectedRows);
          },
        }}
      />
      
      <Modal
        title="新建用户"
        width="400px"
        open={createModalVisible}
        onOk={() => {
          form.validateFields().then((values) => {
            handleAdd(values);
            form.resetFields();
          });
        }}
        onCancel={() => {
          handleModalVisible(false);
          form.resetFields();
        }}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            label="用户名"
            name="username"
            rules={[{ required: true, message: '请输入用户名' }]}
          >
            <Input placeholder="请输入用户名" />
          </Form.Item>
          <Form.Item
            label="邮箱"
            name="email"
            rules={[{ required: true, message: '请输入邮箱' }]}
          >
            <Input placeholder="请输入邮箱" />
          </Form.Item>
          <Form.Item
            label="密码"
            name="password"
            rules={[{ required: true, message: '请输入密码' }]}
          >
            <Input.Password placeholder="请输入密码" />
          </Form.Item>
          <Form.Item
            label="角色"
            name="role"
            rules={[{ required: true, message: '请选择角色' }]}
          >
            <Select placeholder="请选择角色">
              <Option value="admin">管理员</Option>
              <Option value="user">普通用户</Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="编辑用户"
        width="400px"
        open={updateModalVisible}
        onOk={() => {
          form.validateFields().then((values) => {
            handleUpdate(values);
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
            label="用户名"
            name="username"
            rules={[{ required: true, message: '请输入用户名' }]}
          >
            <Input placeholder="请输入用户名" />
          </Form.Item>
          <Form.Item
            label="邮箱"
            name="email"
            rules={[{ required: true, message: '请输入邮箱' }]}
          >
            <Input placeholder="请输入邮箱" />
          </Form.Item>
          <Form.Item
            label="角色"
            name="role"
            rules={[{ required: true, message: '请选择角色' }]}
          >
            <Select placeholder="请选择角色">
              <Option value="admin">管理员</Option>
              <Option value="user">普通用户</Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </PageContainer>
  );
};

export default UserList;