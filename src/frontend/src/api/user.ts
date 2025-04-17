import { http } from "@/utils/http";
import { useUserStoreHook } from "@/store/modules/user";

export type basicResult<T> = {
  code: number;
  message: string;
  data: T;
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

/** 获取邮箱验证码 */
export const getEmailCode = (data: { email: string }) => {
  return http.request<basicResult<null>>(
    "get", // 请求方法
    "/api/user/sendEmailCode", // 请求 URL
    { params: data }
  );
};

/** 注册 */
export const registerUser = (data: { username: string; phone: string; password: string; email: string }, verificationCode: string) => {
  return http.request<basicResult<null>>(
    "post", // 请求方法
    "/api/user/register", // 请求 URL
    {
      data, // 请求体
      params: { verificationCode }, // Query 参数
    }
  );
};

/** 登录 */
export const getLoginWithoutInfo = (data?: object) => {
  return http.request<basicResult<string>>("post", "/api/user/login", { data });
};

/** 获取用户信息 */
export const getUserInfo = () => {
  return http.request<UserInfoResult>("get", "/api/user/info", {});
}

/**修改个人信息 */
export const updateUserInfo = (data) => {
  return http.request<basicResult<null>>(
    "put", // 请求方法
    "/api/user/info", // 请求 URL
    { data }
  );
};

export const updateEmail = (data: { email: string }, verificationCode: string) => {
  return http.request<basicResult<null>>(
    "put", // 请求方法
    "/api/user/info", // 请求 URL
    {
      data, // 请求体
      params: { verificationCode }, // Query 参数
    }
  );
}

/** 邮箱获得id */
export const getIdByEmail = (data: { email: string }) => {
  return http.request<basicResult<number>>(
    "get",
    "/api/user/id",
    { params: data }
  );
}