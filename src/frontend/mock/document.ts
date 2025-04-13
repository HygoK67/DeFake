import type { MockMethod } from "vite-plugin-mock";

export default [
  {
    url: "api/file/upload",
    method: "post",
    response: ({ body }) => {
      try {
        const imageCount = Math.floor(Math.random() * 3) + 1; // 返回1-3张图片
        const imageList = [];
        const imageTypes = ['风景', '人物', '建筑', '自然', '动物'];

        const reliableIds = [1, 10, 20, 30, 100];

        for (let i = 0; i < imageCount; i++) {
          const id = reliableIds[i % reliableIds.length];
          const width = 300;
          const height = 200;
          const imageType = imageTypes[i % imageTypes.length];
          imageList.push({
            url: `https://picsum.photos/id/${id}/${width}/${height}`,
            name: `${imageType}_图片_${i + 1}.jpg`,
          });
        }

        return {
          code: "123456",
          message: "success",
          data: imageList,
        };
      } catch (error) {
        console.error("Mock API 生成数据出错", error);
        return {
          code: "500",
          message: "mock生成数据失败",
          data: []
        };
      }
    }
  },

  // 添加处理选定图片的API
  {
    url: "/api/file/submit-selection",
    method: "post",
    response: ({ body }) => {
      try {
        console.log("接收到用户选择的图片:");

        // 增加错误处理，确保body.images存在
        const images = body?.images || [];
        const imageCount = Array.isArray(images) ? images.length : 0;

        return {
          code: "200",
          message: "图片选择已保存",
          data: {
            processId: "proc_" + Date.now(),
            selectedCount: imageCount,
            success: true
          }
        };
      } catch (error) {
        console.error("处理提交选择请求出错:", error);
        return {
          code: "500",
          message: "处理请求失败",
          data: {
            success: false,
            error: error.message
          }
        };
      }
    }
  }
] as MockMethod[];
