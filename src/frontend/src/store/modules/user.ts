import { defineStore } from "pinia";
import {
  type userType,
  store,
  router,
  resetRouter,
  routerArrays,
  storageLocal
} from "../utils";
import {
  type basicResult,
  getLoginWithoutInfo,
  getUserInfo,
  updateUserInfo,
  updateEmail,
} from "@/api/user";
import { getAllNotification } from "@/api/notification";
import { useMultiTagsStoreHook } from "./multiTags";
import { type DataInfo, removeToken, userKey, mySetToken } from "@/utils/auth";
import { Notification } from "@/types/notification";
import { ElMessage } from "element-plus";
import { handleApply } from "@/api/group";

export const useUserStore = defineStore({
  id: "pure-user",
  state: (): userType => ({
    // 头像
    avatar: storageLocal().getItem<DataInfo>(userKey)?.avatar ?? "",
    // 用户名
    username: storageLocal().getItem<DataInfo>(userKey)?.username ?? "",
    // 邮箱
    email: storageLocal().getItem<DataInfo>(userKey)?.email ?? "",
    // 手机号
    phone: storageLocal().getItem<DataInfo>(userKey)?.phone ?? "",
    // 通知
    notifications: storageLocal().getItem<DataInfo>(userKey)?.notifications ?? [],
    // // 页面级别权限
    // roles: storageLocal().getItem<DataInfo<number>>(userKey)?.roles ?? [],
    // // 按钮级别权限
    // permissions:
    //   storageLocal().getItem<DataInfo<number>>(userKey)?.permissions ?? [],
    // // 是否勾选了登录页的免登录
    // isRemembered: false,
    // // 登录页的免登录存储几天，默认7天
    // loginDay: 7
  }),
  actions: {
    updateStorage<K extends keyof userType>(key: K, value: userType[K]) {
      const userInfo: Partial<DataInfo> = storageLocal().getItem<DataInfo>(userKey) || {};
      userInfo[key] = value;
      storageLocal().setItem(userKey, userInfo);
    },
    /** 存储头像 */
    SET_AVATAR(avatar: string) {
      this.avatar = avatar; // 更新 Pinia 状态
      this.updateStorage("avatar", avatar);
    },
    /** 存储用户名 */
    SET_USERNAME(username: string) {
      this.username = username;
      this.updateStorage("username", username);
    },
    /** 存储邮箱 */
    SET_EMAIL(email: string) {
      this.email = email;
      this.updateStorage("email", email);
    },
    /** 存储手机号 */
    SET_PHONE(phone: string) {
      this.phone = phone;
      this.updateStorage("phone", phone);
    },
    SET_NOTIFICATIONS(notifications: Notification[]) {
      this.notifications = notifications;
      this.updateStorage("notifications", notifications);
    },
    // /** 存储角色 */
    // SET_ROLES(roles: Array<string>) {
    //   this.roles = roles;
    // },
    // /** 存储是否勾选了登录页的免登录 */
    // SET_ISREMEMBERED(bool: boolean) {
    //   this.isRemembered = bool;
    // },
    // /** 设置登录页的免登录存储几天 */
    // SET_LOGINDAY(value: number) {
    //   this.loginDay = Number(value);
    // },
    /** 登入 */
    async loginByUsername(data) {
      return new Promise<basicResult<string>>(async (resolve, reject) => {
        try {
          const loginResponse = await getLoginWithoutInfo(data);

          if (loginResponse?.code === 0) {
            mySetToken(loginResponse.data);
            // 获取用户信息
            try {
              const userInfoResponse = await getUserInfo();
              if (userInfoResponse?.code === 0) {
                const userInfo = userInfoResponse.data;
                this.SET_AVATAR(userInfo.avatarPath ?? "");
                this.SET_USERNAME(userInfo.username ?? "");
                this.SET_EMAIL(userInfo.email ?? "");
                this.SET_PHONE(userInfo.phone ?? "");
                // this.SET_ROLES(userInfo.userRole ? [userInfo.userRole] : []);
              } else {
                ElMessage.error(userInfoResponse.message)
              }
            } catch (error) {
              ElMessage.error("获取用户信息出错");
              console.error(error);
            }
            // 获取通知
            this.getAndStoreNotification();
            resolve(loginResponse);
          } else {
            reject(new Error(loginResponse?.message || "登录失败"));
            console.log("登录失败", loginResponse)
          }
        } catch (error) {
          reject(error);
        }
      });
    },
    async getAndStoreNotification() {
      try {
        const notificationResponse = await getAllNotification({ condition: null });
        if (notificationResponse?.code === 0) {
          this.SET_NOTIFICATIONS(notificationResponse.data ?? []);
        } else {
          ElMessage.error("获取通知失败");
          console.error(notificationResponse?.message);
        }
      } catch (error) {
        ElMessage.error("获取通知出错");
        console.error(error);
      }
    },
    /** 更新个人信息 */
    async updateUserInfo(data: { phone: string, username: string, email: string, verifyCode: string }, msg: string) {
      return new Promise<basicResult<null>>(async (resolve, reject) => {
        try {
          if (msg === "phone") {
            const response = await updateUserInfo({ phone: data.phone });
            if (response?.code === 0) {
              this.SET_PHONE(data.phone);
              response.message = "手机号更新成功";
              resolve(response);
            } else {
              reject(new Error(response?.message || "更新失败"));
            }
          } else if (msg === "username") {
            const response = await updateUserInfo({ username: data.username });
            if (response?.code === 0) {
              this.SET_USERNAME(data.username);
              response.message = "用户名更新成功";
              resolve(response);
            } else {
              reject(new Error(response?.message || "更新失败"));
            }
          } else if (msg === "email") {
            const response = await updateEmail({ email: data.email }, data.verifyCode);
            if (response?.code === 0) {
              this.SET_EMAIL(data.email);
              response.message = "邮箱更新成功";
              resolve(response);
            } else {
              reject(new Error(response?.message || "更新失败"));
            }
          } else {
            reject(new Error("未知操作"));
          }
        } catch (error) {
          reject(error);
        }
      });
    },
    async updatePassword(data: { oldPassword: string, password: string }) {
      return new Promise<basicResult<null>>(async (resolve, reject) => {
        try {
          const response = await updateUserInfo({ password: data.password, oldPassword: data.oldPassword });
          if (response?.code === 0) {
            response.message = "密码更新成功";
            resolve(response);
          } else {
            reject(new Error(response?.message || "更新失败"));
          }
        } catch (error) {
          reject(error);
        }
      });
    },
    /** 更新头像 */
    async updateAvatar(data) {
      return new Promise<basicResult<null>>(async (resolve, reject) => {
        try {
          const response = await updateUserInfo({ avatarPath: data });
          if (response?.code === 0) {
            this.SET_AVATAR(data);
            resolve(response);
          } else {
            reject(new Error(response?.message || "更新失败"));
          }
        } catch (error) {
          reject(error);
        }
      });
    },
    async handleApplyNoti(data: { groupId: string; userIdSent: string; isAgree: string }) {
      return new Promise<basicResult<null>>(async (resolve, reject) => {
        try {
          const response = await handleApply(data);
          if (response?.code === 0) {
            this.getAndStoreNotification();
            resolve(response);
          } else {
            reject(new Error(response?.message || "处理申请失败"));
          }
        } catch (error) {
          reject(error);
        }
      });
    },
    /** 前端登出（不调用接口） */
    logOut() {
      this.username = "";
      this.roles = [];
      this.permissions = [];
      removeToken();
      useMultiTagsStoreHook().handleTags("equal", [...routerArrays]);
      resetRouter();
      router.push("/login");
    },
  }
});

export function useUserStoreHook() {
  return useUserStore(store);
}
