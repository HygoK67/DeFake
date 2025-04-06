// import { defineFakeRoute } from "vite-plugin-fake-server/client";
// import { ElMessage } from "element-plus";

// export default defineFakeRoute([
//   {
//     url: "/api/user/updatePassword", // 模拟的接口路径
//     method: "post", // 请求方法
//     response: ({ body }) => {
//       const { oldPassword, newPassword, username } = body;
//       // 模拟校验逻辑
//       if (!oldPassword || !newPassword || !username) {
//         return {
//           code: 400,
//           message: "参数缺失，请检查输入",
//           data: null
//         };
//       }
//       if (newPassword !== oldPassword) {
//         console.log("密码修改成功" + username);
//         return {
//           code: 0,
//           message: "密码修改成功",
//           data: null
//         };
//       } else if (oldPassword === newPassword) {
//         return {
//           code: 400,
//           message: "新密码不能与旧密码相同",
//           data: null
//         };
//       } else {
//         return {
//           code: 401,
//           message: "旧密码错误",
//           data: null
//         };
//       }
//     }
//   }
// ]);