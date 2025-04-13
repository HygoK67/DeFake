// // 根据角色动态生成路由
// import { message } from "@/utils/message";
// import { defineFakeRoute } from "vite-plugin-fake-server/client";

// export default defineFakeRoute([
//   {
//     url: "/api/user/login",
//     method: "post",
//     response: ({ body }) => {
//       if (body.username !== "000000") {
//         return {
//           code: 0,
//           message: "登录成功",
//           data: "fsadfasfd",// 假Token
//         };
//       } else {
//         return {
//           code: 1,
//           message: "登录失败",
//           data: "fsadfasfddsf",// 假Token
//         };
//       }
//     }
//   }
// ]);
