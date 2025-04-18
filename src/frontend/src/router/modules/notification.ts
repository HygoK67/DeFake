const Layout = () => import("@/layout/index.vue");

export default {
  path: "/notification",
  name: "notification",
  component: Layout,
  redirect: "/notification/center",
  meta: {
    icon: "ep:bell-filled",
    title: "通知管理",
    rank: 6 // 调整菜单显示顺序
  },
  children: [
    {
      path: "/notification/center/:typeKey?", // 固定路径，例如 "center" 或 "all" 或 "index"
      name: "NotificationCenter", // 唯一的路由名称
      component: () => import('@/views/notification/index.vue'), // 指向我们之前创建的那个包含类型选择按钮的页面
      meta: {
        title: "消息中心", // 这个标题会显示在侧边栏
        icon: "ep:bell-filled", // 可以用相同的图标，或省略使用父级的
        showLink: true // !!! 这个需要为 true，生成侧边栏链接 !!!
      }
    }
  ]
} satisfies RouteConfigsTable;