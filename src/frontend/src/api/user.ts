import { http } from "@/utils/http";
import { useUserStoreHook } from "@/store/modules/user";

export type LoginResult = {
  code: number;
  message: string;
  data: string
};

export type UserInfoResult = {
  code: number;
  message: string;
  data: {
    id: number;
    username: string;
    email: string;
    phone: string;
    password: null;
    passwordHash: string;
    avatarPath: null;
    createdAt: string;
    updatedAt: null;
    lastLoginAt: null;
    userRole: null;
  };
}

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

/** 获取邮箱验证码 */
export const getEmailCode = (data: { email: string }) => {
  return http.request<basicResult>(
    "get", // 请求方法
    "api/user/sendEmailCode", // 请求 URL
    { params: data }
  );
};

/** 注册 */
export const registerUser = (data: { username: string; phone: string; password: string; mail: string }, verificationCode: string) => {
  return http.request<{ result: "SUC" | "FAIL", message: string }>(
    "post", // 请求方法
    "/api/register", // 请求 URL
    {
      data, // 请求体
      params: { verificationCode }, // Query 参数
    }
  );
};

/** 登录 */
export const getLoginWithoutInfo = (data?: object) => {
  return http.request<LoginResult>("post", "/login", { data });
};

/** 获取用户信息 */
export const getUserInfo = () => {
  return http.request<UserInfoResult>("get", "/api/user/getUserInfo", {});
}

/** 刷新`token` */
export const refreshTokenApi = (data?: object) => {
  return http.request<RefreshTokenResult>("post", "/refresh-token", { data });
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
