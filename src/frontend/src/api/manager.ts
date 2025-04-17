import { http } from "@/utils/http";
import { getIdByEmail } from "@/api/user";
import { ca } from "element-plus/lib/locale/index.js";
import { FileItem, MemberItem, Group } from '@/types/organization';

export type operationDetail = {
    email: string;
    jwtToken: string
}
export type UserActivityLogItem = {
    id: number;
    userId: number;
    operationType: string;
    operationDetailJsonString: string | null;
    operationDetail: operationDetail; // You might want to define a more specific type here if you know the structure.
    ipAddress: string;
    userEmail: string;
    userName: string;
    avatarURL: string | null;
  }
  
  export type GetAllActivityLogResult = {
    code: number;
    message: string;
    data: UserActivityLogItem[];
  };
  
  export type NotificationInfoItem = {
    id: number;
    template_id: number;
    user_id: number;
    title: string;
    content: string;
    sentAt: string;
    readAt: string | null;
  };
  
  export type GetNotificationInfoResult = {
    code: number;
    message: string;
    data: NotificationInfoItem;
  };
  
  export type GetAllNotificationInfoResult = {
      code: number;
      message: string;
      data: NotificationInfoItem[];
  };
  
  export type UserInfoItem = {
      id: number;
      username: string;
      email: string;
      phone: string;
      password: string | null;
      passwordHash: string;
      avatarPath: string | null;
      createdAt: string;
      updatedAt: string | null;
      lastLoginAt: string | null;
      role: string;
  }
  
  export type GetAllUserInfoResult = {
      code: number;
      message: string;
      data: UserInfoItem[];
  }
  
  
  /** 获取所有用户活动日志 */
  export const getAllActivityLog = () => {
    return http.request<GetAllActivityLogResult>(
      "get",
      "/api/admin/operationLogs/all",
      {}
    );
  };
  
  /** 删除通知 */
  export const deleteNotification = (data: { notificationId: string }) => {
      return http.request<basicResult>( // TODO
        "delete",
        "/api/admin/notification/delete",
        { params:data }
      );
    };
  
  /** 获取单个通知信息 */
  export const getNotificationInfo = (data: { notificationId: string }) => {
    return http.request<GetNotificationInfoResult>(
      "get",
      "/api/admin/notification/info",
      {params:data}
    );
  };
  
  /** 获取所有通知信息 */
  export const getAllNotificationInfo = () => {
      return http.request<GetAllNotificationInfoResult>(
          "get",
          "/api/admin/notification/all",
          {}
      );
  };
  
  /** 获取所有用户信息 */
  export const getAllUserInfo = () => {
      return http.request<GetAllUserInfoResult>(
          "get",
          "/api/admin/user/all",
          {}
      );
  };