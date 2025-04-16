export interface Group {
  id: number;
  groupname: string;
  introduction: string | null;
  ddl: string;
  status: 'in' | 'pending_apply' | 'not_in';
  role: string | null;
  createdAt: string;
};

export interface FileItem {
  id: number;
  fileName: string;
  fileType: string;
  uploader: string;
  uploaderId: number
  uploadTime: string;
  fileSize: string;
  status: '待检测' | '处理中' | '已完成';
  score?: string;
}

export interface MemberItem {
  id?: number;
  userId: number;
  username: string;
  groupId?: number;
  createdAt?: string;
  status?: string;
  role: string;
  email: string;
  //检测次数，造假率
}
