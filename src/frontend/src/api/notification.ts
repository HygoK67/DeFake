import { http } from "@/utils/http";
import { Notification } from "@/types/notification";

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
          condition: "send_at_desc"
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