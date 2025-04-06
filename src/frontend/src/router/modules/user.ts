export default [
  {
    path: "/user-info",
    name: "UserInfo",
    component: () => import("@/views/user/displayInfo/index.vue"),
    meta: {
      title: "个人信息",
      icon: "ep:user", // 图标（可选）
      showLink: true // 是否在侧边栏显示
    }
  },
  {
    path: "/change-password",
    name: "ChangePassword",
    component: () => import("@/views/user/changePassWord/index.vue"),
    meta: {
      title: "修改密码",
      icon: "ep:lock", // 图标（可选）
      showLink: false,
    }
  }
] as RouteConfigsTable[];