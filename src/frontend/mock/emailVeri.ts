import { defineFakeRoute } from "vite-plugin-fake-server/client";

export default defineFakeRoute([
  {
    url: "/api/user/sendEmailCode", // 模拟发送邮箱验证码接口
    method: "get", // 请求方法为 GET
    response: ({ query }) => {
      const { email } = query;

      // 校验邮箱是否存在
      if (email === "11@qq.com") {
        return {
          code: 1,
          message: "邮箱已存在",
          data: null
        };
      }
      // 模拟发送成功
      return {
        code: 0,
        message: "验证码发送成功",
        data: null
      };
    }
  }
]);