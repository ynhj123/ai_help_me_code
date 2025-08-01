import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/store/user'
import MainLayout from '@/components/layout/MainLayout.vue'
import Login from '@/views/auth/Login.vue'
import Register from '@/views/auth/Register.vue'
import Dashboard from '@/views/dashboard/Index.vue'
import UserList from '@/views/user/List.vue'
import UserDetail from '@/views/user/Detail.vue'
import UserEdit from '@/views/user/Edit.vue'
import ProductList from '@/views/product/List.vue'
import ProductDetail from '@/views/product/Detail.vue'
import CategoryList from '@/views/category/List.vue'
import CategoryDetail from '@/views/category/Detail.vue'
import OrderList from '@/views/order/List.vue'
import OrderDetail from '@/views/order/Detail.vue'

// 路由配置
const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: {
      title: '登录'
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: {
      title: '注册'
    }
  },
  {
    path: '/',
    component: MainLayout,
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: {
          title: '仪表盘',
          requiresAuth: true
        }
      },
      {
        path: '/user',
        name: 'UserList',
        component: UserList,
        meta: {
          title: '用户管理',
          requiresAuth: true,
          permissions: ['ADMIN']
        }
      },
      {
        path: '/user/detail/:id',
        name: 'UserDetail',
        component: UserDetail,
        meta: {
          title: '用户详情',
          requiresAuth: true,
          permissions: ['ADMIN']
        }
      },
      {
        path: '/user/create',
        name: 'UserCreate',
        component: UserEdit,
        meta: {
          title: '新增用户',
          requiresAuth: true,
          permissions: ['ADMIN']
        }
      },
      {
        path: '/user/edit/:id',
        name: 'UserEdit',
        component: UserEdit,
        meta: {
          title: '编辑用户',
          requiresAuth: true,
          permissions: ['ADMIN']
        }
      },
      {
        path: '/product',
        name: 'ProductList',
        component: ProductList,
        meta: {
          title: '商品管理',
          requiresAuth: true,
          permissions: ['ADMIN']
        }
      },
      {
        path: '/product/detail/:id',
        name: 'ProductDetail',
        component: ProductDetail,
        meta: {
          title: '商品详情',
          requiresAuth: true,
          permissions: ['ADMIN']
        }
      },
      {
        path: '/product/create',
        name: 'ProductCreate',
        component: ProductList,
        meta: {
          title: '新增商品',
          requiresAuth: true,
          permissions: ['ADMIN']
        }
      },
      {
        path: '/product/edit/:id',
        name: 'ProductEdit',
        component: ProductList,
        meta: {
          title: '编辑商品',
          requiresAuth: true,
          permissions: ['ADMIN']
        }
      },
      {
        path: '/category',
        name: 'CategoryList',
        component: CategoryList,
        meta: {
          title: '分类管理',
          requiresAuth: true,
          permissions: ['ADMIN']
        }
      },
      {
        path: '/category/detail/:id',
        name: 'CategoryDetail',
        component: CategoryDetail,
        meta: {
          title: '分类详情',
          requiresAuth: true,
          permissions: ['ADMIN']
        }
      },
      {
        path: '/category/create',
        name: 'CategoryCreate',
        component: CategoryList,
        meta: {
          title: '新增分类',
          requiresAuth: true,
          permissions: ['ADMIN']
        }
      },
      {
        path: '/category/edit/:id',
        name: 'CategoryEdit',
        component: CategoryList,
        meta: {
          title: '编辑分类',
          requiresAuth: true,
          permissions: ['ADMIN']
        }
      },
      {
        path: '/order',
        name: 'OrderList',
        component: OrderList,
        meta: {
          title: '订单管理',
          requiresAuth: true,
          permissions: ['ADMIN']
        }
      },
      {
        path: '/order/detail/:id',
        name: 'OrderDetail',
        component: OrderDetail,
        meta: {
          title: '订单详情',
          requiresAuth: true,
          permissions: ['ADMIN']
        }
      }
    ]
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 电商管理系统`
  }
  
  // 检查是否需要认证
  if (to.meta.requiresAuth) {
    const userStore = useUserStore()
    
    // 检查是否有token
    if (!userStore.token) {
      next('/login')
      return
    }
    
    // 检查权限
    if (to.meta.permissions) {
      const requiredPermissions = to.meta.permissions as string[]
      const hasPermission = requiredPermissions.some(permission => 
        userStore.userInfo?.role === permission
      )
      
      if (!hasPermission) {
        next('/dashboard')
        return
      }
    }
  }
  
  // 已登录用户访问登录页时重定向到首页
  if (to.path === '/login' && useUserStore().token) {
    next('/')
    return
  }
  
  next()
})

export default router