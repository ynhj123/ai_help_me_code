import { getProducts, createProduct, updateProduct, deleteProduct } from '@/services/api';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import { Button, message, Modal, Form, Input, InputNumber, Select, Upload } from 'antd';
import React, { useRef, useState } from 'react';

const { Option } = Select;
const { TextArea } = Input;

const ProductList: React.FC = () => {
  const actionRef = useRef<ActionType>(null);
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<any>();
  const [form] = Form.useForm();

  const handleAdd = async (fields: any) => {
    const hide = message.loading('正在添加');
    try {
      await createProduct({ ...fields });
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
      await updateProduct(currentRow.id, { ...fields });
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
      await deleteProduct(selectedRow.id);
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
      title: '商品名称',
      dataIndex: 'name',
      valueType: 'text',
      formItemProps: {
        rules: [{ required: true, message: '请输入商品名称' }],
      },
    },
    {
      title: '商品描述',
      dataIndex: 'description',
      valueType: 'textarea',
      hideInSearch: true,
    },
    {
      title: '价格',
      dataIndex: 'price',
      valueType: 'money',
      formItemProps: {
        rules: [{ required: true, message: '请输入价格' }],
      },
    },
    {
      title: '库存',
      dataIndex: 'stock',
      valueType: 'digit',
      formItemProps: {
        rules: [{ required: true, message: '请输入库存' }],
      },
    },
    {
      title: '分类',
      dataIndex: 'category',
      valueType: 'text',
      formItemProps: {
        rules: [{ required: true, message: '请输入分类' }],
      },
    },
    {
      title: '状态',
      dataIndex: 'status',
      valueEnum: {
        active: { text: '上架', status: 'Success' },
        inactive: { text: '下架', status: 'Error' },
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
              title: '删除商品',
              content: '确定要删除这个商品吗？',
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
        headerTitle="商品列表"
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
          const response: any = await getProducts(params);
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
        title="新建商品"
        width="600px"
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
            label="商品名称"
            name="name"
            rules={[{ required: true, message: '请输入商品名称' }]}
          >
            <Input placeholder="请输入商品名称" />
          </Form.Item>
          <Form.Item
            label="商品描述"
            name="description"
            rules={[{ required: true, message: '请输入商品描述' }]}
          >
            <TextArea rows={4} placeholder="请输入商品描述" />
          </Form.Item>
          <Form.Item
            label="价格"
            name="price"
            rules={[{ required: true, message: '请输入价格' }]}
          >
            <InputNumber
              style={{ width: '100%' }}
              placeholder="请输入价格"
              min={0}
              precision={2}
            />
          </Form.Item>
          <Form.Item
            label="库存"
            name="stock"
            rules={[{ required: true, message: '请输入库存' }]}
          >
            <InputNumber
              style={{ width: '100%' }}
              placeholder="请输入库存"
              min={0}
            />
          </Form.Item>
          <Form.Item
            label="分类"
            name="category"
            rules={[{ required: true, message: '请输入分类' }]}
          >
            <Input placeholder="请输入分类" />
          </Form.Item>
          <Form.Item
            label="状态"
            name="status"
            rules={[{ required: true, message: '请选择状态' }]}
          >
            <Select placeholder="请选择状态">
              <Option value="active">上架</Option>
              <Option value="inactive">下架</Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="编辑商品"
        width="600px"
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
            label="商品名称"
            name="name"
            rules={[{ required: true, message: '请输入商品名称' }]}
          >
            <Input placeholder="请输入商品名称" />
          </Form.Item>
          <Form.Item
            label="商品描述"
            name="description"
            rules={[{ required: true, message: '请输入商品描述' }]}
          >
            <TextArea rows={4} placeholder="请输入商品描述" />
          </Form.Item>
          <Form.Item
            label="价格"
            name="price"
            rules={[{ required: true, message: '请输入价格' }]}
          >
            <InputNumber
              style={{ width: '100%' }}
              placeholder="请输入价格"
              min={0}
              precision={2}
            />
          </Form.Item>
          <Form.Item
            label="库存"
            name="stock"
            rules={[{ required: true, message: '请输入库存' }]}
          >
            <InputNumber
              style={{ width: '100%' }}
              placeholder="请输入库存"
              min={0}
            />
          </Form.Item>
          <Form.Item
            label="分类"
            name="category"
            rules={[{ required: true, message: '请输入分类' }]}
          >
            <Input placeholder="请输入分类" />
          </Form.Item>
          <Form.Item
            label="状态"
            name="status"
            rules={[{ required: true, message: '请选择状态' }]}
          >
            <Select placeholder="请选择状态">
              <Option value="active">上架</Option>
              <Option value="inactive">下架</Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </PageContainer>
  );
};

export default ProductList;