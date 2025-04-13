// import { defineFakeRoute } from "vite-plugin-fake-server/client";

// export default defineFakeRoute([
//   {
//     url: "/api/user/info", // 模拟获取用户信息接口
//     method: "get", // 请求方法为 GET
//     response: () => {
//       return {
//         code: 0, // 状态码，表示成功
//         message: "获取用户信息成功", // 提示信息
//         data: {
//           id: 1,
//           username: "testuser",
//           email: "testuser@example.com",
//           phone: "1234567890",
//           password: null,
//           passwordHash: "hashed_password_example",
//           avatarPath: null,
//           createdAt: "2025-04-01T12:00:00Z",
//           updatedAt: null,
//           lastLoginAt: "2025-04-05T15:30:00Z",
//           userRole: "admin"
//         }
//       };
//     }
//   }
// ]);