import { http } from "@/utils/http";
import { useUserStoreHook } from "@/store/modules/user";

export type UserResult = {
  success: boolean;
  data: {
    /** 头像 */
    avatar: string;
    /** 用户名 */
    username: string;
    /** 昵称 */
    nickname: string;
    /** 当前登录用户的角色 */
    roles: Array<string>;
    /** 按钮级别权限 */
    permissions: Array<string>;
    /** `token` */
    accessToken: string;
    /** 用于调用刷新`accessToken`的接口时所需的`token` */
    refreshToken: string;
    /** `accessToken`的过期时间（格式'xxxx/xx/xx xx:xx:xx'） */
    expires: Date;
  };
};

export type RefreshTokenResult = {
  success: boolean;
  data: {
    /** `token` */
    accessToken: string;
    /** 用于调用刷新`accessToken`的接口时所需的`token` */
    refreshToken: string;
    /** `accessToken`的过期时间（格式'xxxx/xx/xx xx:xx:xx'） */
    expires: Date;
  };
};

/** 修改密码  邮箱验证*/
export type basicResult = {
  code: number;
  message: string;
  data: null;
};

/** 注册 */
export const registerUser = (data: { username: string; phone: string; password: string; mail: string }, verificationCode: string) => {
  return http.request<{ result: "SUC" | "FAIL", message: string }>(
    "post", // 请求方法
    "/api/register", // 请求 URL
    {
      data, // 请求体
      params: { verificationCode } // Query 参数
    }
  );
};

/** 登录 */
export const getLogin = (data?: object) => {
  return http.request<UserResult>("post", "/login", { data });
};

/** 刷新`token` */
export const refreshTokenApi = (data?: object) => {
  return http.request<RefreshTokenResult>("post", "/refresh-token", { data });
};

/** 获取邮箱验证码 */
export const getEmailCode = (data: { email: string }) => {
  return http.request<basicResult>(
    "get", // 请求方法
    "/api/user/sendEmailCode", // 请求 URL
    { params: data }
  );
};

/**修改密码 */
export const updatePassword = (data: { oldPassword: string; newPassword: string }) => {
  const userStore = useUserStoreHook();
  const { username } = userStore;
  return http.request<basicResult>(
    "post", // 请求方法
    "/api/user/updatePassword", // 请求 URL
    {
      data: {
        ...data,
        username
      }
    }
  );
};
