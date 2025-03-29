const { VITE_HIDE_HOME } = import.meta.env;
const Layout = () => import("@/layout/index.vue");

export default {
  path: "/",
  name: "Home",
  component: Layout,
  redirect: "/welcome",
  meta: {
    icon: "ep:home-filled",
    title: "首页",
    rank: 0
  },
  children: [
    {
      path: "/welcome",
      name: "Welcome",
      component: () => import("@/views/welcome/index.vue"),
      meta: {
        title: "首页",
        showLink: VITE_HIDE_HOME === "true" ? false : true
      }
    },
    {
      path: "/user-info",
      name: "UserInfo",
      component: () => import("@/views/user/index.vue"),
      meta: {
        title: "个人信息",
        icon: "ep:user", // 图标（可选）
        showLink: true // 是否在侧边栏显示
      }
    }
  ]
} satisfies RouteConfigsTable;