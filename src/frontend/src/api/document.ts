import { http } from "@/utils/http";
import type { FileWithMetadata } from "@/types/document";

/**
 * 批量上传带有元数据的文件
 * @param filesData 带有元数据的文件对象数组
 * @returns 上传结果，包含所有文件的ID和状态
 */
export const uploadFiles = (filesData: FileWithMetadata[]) => {
  const formData = new FormData();

  // 添加所有文件和元数据到同一个FormData
  filesData.forEach((fileData, index) => {
    formData.append(`files[${index}]`, fileData.file);
    formData.append(`metadata[${index}]`, JSON.stringify(fileData.metadata));
  });

  return http.post<{ ids: string[]; status: string }, FormData>(
    "/files/upload",
    {
      data: formData,
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }
  );
};
