import { defineFakeRoute } from "vite-plugin-fake-server/client";

export default defineFakeRoute([
  {
    url: "/api/register", // 模拟注册接口
    method: "post", // 请求方法为 POST
    response: ({ body, query }) => {
      const { username, phone, password, mail } = body;
      const { verificationCode } = query;

      // 校验验证码是否存在
      if (verificationCode === "111") {
        return {
          result: "FAIL", // 注册失败
          message: "验证码错误"
        };
      }

      // 校验注册信息是否完整
      if (!username || !phone || !password || !mail) {
        return {
          result: "FAIL", // 注册失败
          message: "缺少必要的注册信息"
        };
      }

      // 模拟注册成功
      return {
        result: "SUC", // 注册成功
        message: "注册成功"
      };
    }
  }
]);