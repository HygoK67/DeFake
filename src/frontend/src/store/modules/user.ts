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
  type LoginResult,
  type basicResult,
  getLoginWithoutInfo,
  getUserInfo,
  updateUserInfo,
  updateEmail
} from "@/api/user";
import { useMultiTagsStoreHook } from "./multiTags";
import { type DataInfo, removeToken, userKey, mySetToken } from "@/utils/auth";

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
    /** 更新 storageLocal 中的用户信息 */
    updateStorage(key: keyof userType, value: string) {
      const userInfo: DataInfo = storageLocal().getItem<DataInfo>(userKey) || {} as DataInfo;
      userInfo[key] = value; // 更新指定字段
      storageLocal().setItem(userKey, userInfo); // 保存到 storageLocal
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
    // /** 存储角色 */
    // SET_ROLES(roles: Array<string>) {
    //   this.roles = roles;
    // },
    // /** 存储按钮级别权限 */
    // SET_PERMS(permissions: Array<string>) {
    //   this.permissions = permissions;
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
      return new Promise<LoginResult>(async (resolve, reject) => {
        try {
          // 调用登录接口
          const loginResponse = await getLoginWithoutInfo(data);
          if (loginResponse?.code === 0) {
            // 存储 Token
            mySetToken(loginResponse.data);

            // 调用获取用户信息接口
            const userInfoResponse = await getUserInfo();
            if (userInfoResponse?.code === 0) {
              const userInfo = userInfoResponse.data;
              // 更新状态管理中的用户信息
              this.SET_AVATAR(userInfo.avatarPath ?? "");
              this.SET_USERNAME(userInfo.username ?? "");
              this.SET_EMAIL(userInfo.email ?? "");
              this.SET_PHONE(userInfo.phone ?? "");
              // this.SET_ROLES(userInfo.userRole ? [userInfo.userRole] : []);
              // this.SET_PERMS([]); // 如果有权限信息，可以在此设置
            } else {
              console.log("获取用户信息失败", userInfoResponse)
            }
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
    /** 更新个人信息 */
    async updateUserInfo(data: { phone: string, username: string, email: string, verifyCode: string }, msg: string) {
      return new Promise<basicResult>(async (resolve, reject) => {
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
      return new Promise<basicResult>(async (resolve, reject) => {
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
      return new Promise<basicResult>(async (resolve, reject) => {
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
