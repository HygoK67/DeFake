const Layout = () => import("@/layout/index.vue");

export default {
  path: "/result",
  name: "Result",
  component: Layout,
  redirect: "/result/management",
  meta: {
    icon: "ep:data-analysis",
    title: "结果管理",
    rank: 5 // 调整菜单显示顺序
  },
  children: [
    {
      path: "/result/management",
      name: "ResultManagement",
      component: () => import("@/views/results/management/index.vue"),
      meta: {
        title: "检测结果",
        showLink: true,
        icon: "ri:file-search-line"
      }
    },
    {
      path: "/result/detail/:id",
      name: "ResultDetail",
      component: () => import("@/views/results/detail/index.vue"),
      meta: {
        title: "检测详情",
        showLink: false // 不在菜单中显示，只作为导航用
      }
    }
  ]
} satisfies RouteConfigsTable;