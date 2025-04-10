import { defineFakeRoute } from "vite-plugin-fake-server/client";

export default defineFakeRoute([
  {
    url: "/api/file/upload", // 模拟的上传接口地址
    method: "post", // 请求方法为 POST
    response: ({ body }) => {
      const { file } = body;

      // 模拟文件上传成功的返回结果
      if (file) {
        return {
          code: 0, // 表示成功
          message: "上传成功",
          data: `https://se-project-1301778434.cos.ap-beijing.myqcloud.com/cede0001-041c-49e9-99a2-6856a13c9e1a.png` // 模拟返回文件的访问路径
        };
      } else {
        return {
          code: 1, // 表示失败
          message: "文件上传失败",
          data: null
        };
      }
    }
  }
]);