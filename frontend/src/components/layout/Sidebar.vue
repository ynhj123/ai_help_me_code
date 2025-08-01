<template>
  <a-layout-sider
    v-model:collapsed="localCollapsed"
    :trigger="null"
    collapsible
    class="sidebar"
  >
    <div class="sidebar-menu">
      <a-menu
        v-model:selectedKeys="selectedKeys"
        v-model:openKeys="openKeys"
        mode="inline"
        theme="dark"
        :inline-collapsed="localCollapsed"
      >
        <template v-for="item in menuItems" :key="item.key">
          <a-menu-item
            v-if="!item.children"
            :key="item.key"
            @click="handleMenuClick(item)"
          >
            <component :is="item.icon" />
            <span>{{ item.title }}</span>
          </a-menu-item>
          
          <a-sub-menu
            v-else
            :key="item.key"
            :title="item.title"
          >
            <template #icon>
              <component :is="item.icon" />
            </template>
            
            <a-menu-item
              v-for="child in item.children"
              :key="child.key"
              @click="handleMenuClick(child)"
            >
              <component :is="child.icon" />
              <span>{{ child.title }}</span>
            </a-menu-item>
          </a-sub-menu>
        </template>
      </a-menu>
    </div>
  </a-layout-sider>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  DashboardOutlined,
  UserOutlined,
  ShoppingCartOutlined,
  FileOutlined,
  BarChartOutlined
} from '@ant-design/icons-vue'
import { useUserStore } from '@/store/user'

// 定义属性
const props = defineProps<{
  collapsed: boolean
}>()

// 定义事件
const emit = defineEmits<{
  (e: 'update:collapsed', collapsed: boolean): void
}>()

// 路由
const router = useRouter()
const route = useRoute()

// 用户状态
const userStore = useUserStore()

// 本地折叠状态
const localCollapsed = computed({
  get: () => props.collapsed,
  set: (value) => emit('update:collapsed', value)
})

// 选中的菜单项
const selectedKeys = ref<string[]>([])

// 展开的菜单项
const openKeys = ref<string[]>([])

// 菜单配置
const menuItems = computed(() => {
  const items = [
    {
      key: '/dashboard',
      title: '仪表盘',
      icon: DashboardOutlined,
      path: '/dashboard'
    },
    {
      key: 'user-management',
      title: '用户管理',
      icon: UserOutlined,
      children: [
        {
          key: '/user',
          title: '用户列表',
          icon: UserOutlined,
          path: '/user'
        }
      ]
    },
    {
      key: 'product-management',
      title: '商品管理',
      icon: ShoppingCartOutlined,
      children: [
        {
          key: '/product',
          title: '商品列表',
          icon: ShoppingCartOutlined,
          path: '/product'
        },
        {
          key: '/category',
          title: '商品分类',
          icon: FileOutlined,
          path: '/category'
        }
      ]
    },
    {
      key: 'order-management',
      title: '订单管理',
      icon: FileOutlined,
      children: [
        {
          key: '/order',
          title: '订单列表',
          icon: FileOutlined,
          path: '/order'
        }
      ]
    },
    {
      key: 'statistics',
      title: '数据统计',
      icon: BarChartOutlined,
      children: [
        {
          key: '/reports',
          title: '销售报表',
          icon: BarChartOutlined,
          path: '/reports'
        }
      ]
    }
  ]
  
  // 根据用户角色过滤菜单
  return items.filter(item => {
    // 管理员可以看到所有菜单
    if (userStore.userRole === 'ADMIN') {
      return true
    }
    
    // 其他角色只能看到部分菜单
    if (item.key === 'user-management') {
      return false
    }
    
    return true
  })
})

// 处理菜单点击
const handleMenuClick = (item: any) => {
  if (item.path) {
    router.push(item.path)
  }
}

// 监听路由变化，更新选中菜单
watch(
  () => route.path,
  (path) => {
    // 查找匹配的菜单项
    const findMenuItem = (items: any[]): any => {
      for (const item of items) {
        if (item.path === path) {
          return item
        }
        if (item.children) {
          const child = findMenuItem(item.children)
          if (child) {
            return child
          }
        }
      }
      return null
    }
    
    const menuItem = findMenuItem(menuItems.value)
    if