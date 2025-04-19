/**
 * 文件元数据接口
 */
export interface Metadata {
  title: string;
  authorList: string[];
  abstracT: string;
  doi: string;
  publishAt: string;
}

/**
 * 带元数据的文件接口
 */
export interface FileWithMetadata {
  file: File;
  metadata: Metadata;
}
