const Layout = () => import("@/layout/index.vue");

export default {
  path: "/organization",
  name: "Organization",
  component: Layout,
  redirect: "/organization/main",
  meta: {
    icon: "ri:organization-chart",
    title: "组织管理",
    rank: 4
  },
  children: [
    {
      path: "/organization/main",
      name: "OrganizationMain",
      component: () => import("@/views/organization/main.vue"),
      meta: {
        title: "查找组织",
        icon: "ri:search-line",
        showLink: true
      }
    },
    {
      path: "/organization/apply/:id",
      name: "OrganizationApply",
      component: () => import("@/views/organization/apply.vue"),
      meta: {
        title: "申请加入",
        showLink: false // 不在导航菜单中显示
      }
    },
    {
      path: "/organization/detail/:id",
      name: "OrganizationDetail",
      component: () => import("@/views/organization/detail.vue"),
      meta: {
        title: "组织详情",
        showLink: false // 不在导航菜单中显示
      }
    },
    {
      path: "/organization/list/:type",
      name: "OrganizationList",
      component: () => import("@/views/organization/list.vue"),
      meta: {
        title: "组织列表",
        showLink: false // 不在导航菜单中显示
      }
    }
  ]
} satisfies RouteConfigsTable;