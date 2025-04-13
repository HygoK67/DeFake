import { http } from "@/utils/http";
import type { FileWithMetadata } from "@/types/document";

/**
 * 上传文件（第一步）
 * @param fileData 文件对象
 * @returns 上传结果，包含文件URL
 */
export const uploadFiles = (fileData: FileWithMetadata) => {
  const formData = new FormData();

  formData.append(`files`, fileData.file);

  return http.post<any, { code: string; message: string; data: any[] }>(
    "/api/file/upload",
    {
      data: formData as unknown as { code: string; message: string; data: any[] },
      headers: {
        "Content-Type": undefined
      },
      timeout: 30000
    }
  );
};

/**
 * 提交文件元数据（第二步）
 * @param fileUrl 已上传文件的URL
 * @param metadata 文件的元数据
 * @returns 提取出的图片列表
 */
export const submitFileMetadata = (fileUrl: string, metadata: any) => {
  return http.post<any, { code: string; message: string; data: any[] }>(
    "/api/file/process", // 根据实际API路径调整
    {
      fileUrl,
      metadata
    },
    {
      headers: {
        "Content-Type": "application/json"
      }
    }
  );
};

/**
 * 提交用户选择的图片
 * @param selectedImages 用户选择的图片列表
 * @returns 提交结果
 */
export const submitSelectedImages = (selectedImages: any[]) => {
  return http.post<any, { code: string; message: string; data: { images: any[] } }>(
    "/api/file/submit-selection",
    { data: { images: selectedImages } }, // 确保包装在data属性中
    {
      headers: {
        "Content-Type": "application/json"
      }
    }
  );
};

/**
 * 一次性完成文件上传和元数据提交（组合步骤）
 * @param fileData 文件对象
 * @param metadata 文件的元数据
 * @returns 处理结果，包含提取出的图片列表
 */
export const uploadAndProcessFile = async (fileData: FileWithMetadata) => {
  try {
    const uploadResult = await uploadFiles(fileData);
    if (uploadResult.data) {
      const fileUrl = uploadResult.data;
      const processResult = await submitFileMetadata(fileUrl, fileData.metadata);
      return processResult;
    } else {
      throw new Error("文件上传失败: " + uploadResult.message);
    }
  } catch (error) {
    console.error("文件上传和处理失败:", error);
    throw error;
  }
};
