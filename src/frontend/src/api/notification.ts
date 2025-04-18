import { http } from "@/utils/http";
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

export interface GetAllNotificationResult {
  code: number;
  message: string;
  data: Notification[];
}

export interface ReadNotificationResult {
  code: number;
  message: string;
  data?: any;
}


/** 获取通知 */
export const getAllNotification = (data: { condition: string | null }) => {
  if (data.condition === null) {
    return http.request<GetAllNotificationResult>(
      "get",
      "/api/notification/filter",
      {
        params: {
          condition: "sent_at_asc"
        }
      }
    )
  }
  return http.request<GetAllNotificationResult>(
    "get",
    "/api/notification/filter",
    {
      params: data
    }
  );
};

/** 阅读通知 */
export const readNotification = (data: { notificationId: string }) => {
  return http.request<ReadNotificationResult>(
    "put",
    "/api/notification/read",
    { data }
  );
};