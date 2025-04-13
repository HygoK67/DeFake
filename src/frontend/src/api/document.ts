import { http } from "@/utils/http";
import type { FileWithMetadata } from "@/types/document";

/**
 * @param filesData 带有元数据的文件对象数组
 * @returns 上传结果，包含所有文件的ID和状态
 */
export const uploadFiles = (fileData: FileWithMetadata) => {
  const formData = new FormData();

  formData.append(`files`, fileData.file);
  formData.append(`metadata`, JSON.stringify(fileData.metadata));

  return http.post<{ code: string; message: string; data: any[] }, FormData>(
    "/api/file/upload",
    formData as any,
    {
      headers: {
        // 使用浏览器自动设置的Content-Type和boundary
      },
      timeout: 30000 // 增加超时时间到30秒
    }
  );
};

/**
 * 提交用户选择的图片
 * @param selectedImages 用户选择的图片列表
 * @returns 提交结果
 */
export const submitSelectedImages = (selectedImages: any[]) => {
  return http.post<{ code: string; message: string; data: any }>(
    "/api/file/submit-selection",
    { images: selectedImages }, // 确保包装在images属性中
    {
      headers: {
        "Content-Type": "application/json"
      }
    }
  );
};
