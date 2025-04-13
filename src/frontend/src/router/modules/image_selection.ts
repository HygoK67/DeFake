const Layout = () => import("@/layout/index.vue");

export default {
  path: "/image-selection",
  name: "ImageSelectionModule",
  component: Layout,
  redirect: "/image-selection/select",
  meta: {
    // 不在菜单中显示
    showLink: false,
    rank: 999,
  },
  children: [
    {
      path: "/image-selection/select",
      name: "ImageSelection",
      component: () => import("@/views/image-selection/index.vue"),
      meta: {
        title: "图片选择",
        // 不在菜单中显示此子项
        showLink: false,
      }
    }
  ]
} satisfies RouteConfigsTable;
