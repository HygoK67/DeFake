const Layout = () => import("@/layout/index.vue");

export default {
  path: "/manager",
  name: "manager",
  component: Layout,
  redirect: "/manager/management",
  meta: {
    icon: "ep:data-analysis",
    title: "平台管理",
    rank: 5 // 调整菜单显示顺序
  },
  children: [
    {
      path: "/manager/management",
      name: "Manager",
      component: () => import("@/views/manager/main.vue"),
      meta: {
        title: "平台管理",
        showLink: true,
        icon: "ri:file-search-line"
      }
    }
  ]
} satisfies RouteConfigsTable;