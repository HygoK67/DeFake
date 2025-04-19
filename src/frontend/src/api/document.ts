import { http } from "@/utils/http";
import type { FileWithMetadata } from "@/types/document";
import { basicResult } from "@/api/user";

// 上传文件
export const uploadFile = async (file: File) => {
  const formData = new FormData();
  formData.append('files', file);

  const response = await http.post<any, { code: string; message: string; data: any[] }>('/api/file/upload', formData, {
    headers: {
    }
  });

  return response.data.url;
};

// 上传图片
export const uploadFigure = async (data: { filePath: string }) => {
  const response = await http.request<basicResult<null>>(
    "post",
    "api/figure",
    { data }
  );
  return response.data;
};

// 获取图片
export const getFigure = async (figureId: number) => {
  const response = await http.get<{ code: string; message: string; data: any }, { figureId: number }>('/api/figure', {
    params: { figureId }
  });
  return response.data.filePath;
};

// 上传论文
export const uploadPaper = async (data: {
  title: string,
  authorList: string[],
  abstracT: string,
  doi: string,
  publishAt: string,
  filePath: string
}) => {
  const response = await http.request<basicResult<null>>(
    "post",
    "api/paper",
    { data }
  );
  return response.data;
};


// 获取论文信息
export const getPaper = async (paperId: number) => {
  const response = await http.get<{ code: string; message: string; data: any }, { paperId: number }>('/api/paper', {
    params: { paperId }
  });
  return response.data;
};


// 获取论文的所有图片
export const getPaperFigures = async (paperId: number) => {
  const response = await http.get<{ code: string; message: string; data: any[] }, { paperId: number }>('/api/paper/figures', {
    params: { paperId }
  });
  return response.data;
};