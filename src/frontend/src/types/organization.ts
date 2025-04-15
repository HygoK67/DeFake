export interface Group {
  id: number;
  groupname: string;
  introduction: string | null;
  ddl: string;
  status: 'in' | 'pending_apply' | 'not_in';
  role: string | null;
  createdAt: string;
};

export interface AllGroup {
  id: number;
  groupname: string;
  introduction: string | null;
  ddl: string;
  createdAt: string;
  updatedAt?: null;
}
