/**
 * 文件元数据接口
 */
export interface Metadata {
  title: string;
  author: string;
  institution: string;
  publishDate: string;
  keywords: string;
  description: string;
}

/**
 * 带元数据的文件接口
 */
export interface FileWithMetadata {
  file: File;
  metadata: Metadata;
}
