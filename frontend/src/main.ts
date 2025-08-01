import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import './assets/styles/index.less'

// 创建应用实例
const app = createApp(App)

// 使用Ant Design Vue
app.use(Antd)

// 使用路由
app.use(router)

// 使用状态管理
app.use(store)

// 挂载应用
app.mount('#app')