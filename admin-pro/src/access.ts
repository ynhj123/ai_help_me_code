/**
 * @name 权限配置
 * 控制页面权限，需要配合 initialState 使用
 * @doc https://umijs.org/docs/max/access
 */
export default function access(initialState: { currentUser?: any | undefined }) {
  const { currentUser } = initialState || {};

  return {
    // 管理员权限
    adminRoute: currentUser && currentUser.role === 'admin',
    // 普通用户权限
    userRoute: currentUser && currentUser.role === 'user',
    // 登录权限
    canLogin: !!currentUser,
    // 用户管理权限
    canUser: currentUser && currentUser.role === 'admin',
    // 商品管理权限
    canProduct: currentUser && (currentUser.role === 'admin' || currentUser.role === 'user'),
    // 订单管理权限
    canOrder: currentUser && (currentUser.role === 'admin' || currentUser.role === 'user'),
    // 物流管理权限
    canShipment: currentUser && (currentUser.role === 'admin' || currentUser.role === 'user'),
    // 数据统计权限
    canDashboard: currentUser && (currentUser.role === 'admin' || currentUser.role === 'user'),
  };
}
