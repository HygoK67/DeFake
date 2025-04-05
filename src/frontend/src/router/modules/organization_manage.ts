const Layout = () => import("@/layout/index.vue");

export default {
  path: "/organization_manage",
  name: "organization_manage",
  component: Layout,
  redirect: "/organization_manage/management",
  meta: {
    icon: "ep:data-analysis",
    title: "组织管理",
    rank: 5 // 调整菜单显示顺序
  },
  children: [
    {
      path: "/organization_manage/management",
      name: "OrganizationManagement",
      component: () => import("@/views/organization_manager/index.vue"),
      meta: {
        title: "组织管理-测试",
        showLink: true,
        icon: "ri:file-search-line"
      }
    }
  ]
} satisfies RouteConfigsTable;