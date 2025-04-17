export interface Notification {
  id: number;
  templateId: number;
  userIdSent: number;
  userIdRec: number;
  groupId: number;
  title: string;
  content: string;
  sentAt: string;
  readAt: string | null;
}