import { http } from "@/utils/http";
import { getIdByEmail } from "@/api/user";
import { ca } from "element-plus/lib/locale/index.js";
import { FileItem, MemberItem, Group } from '@/types/organization';

export type basicResult = {
  code: number;
  message: string;
  data: string | null;
};

export type getMyGroupResult = {
  code: number;
  message: string;
  data: {
    id: number;
    groupname: string;
    introduction: string | null;
    ddl: string;
    status: 'in' | 'pending_apply' | 'not_in';
    role: string;
    createdAt: string;
    updatedAt: null;
  }[];
};

export type getAllGroupResult = {
  code: number;
  message: string;
  data: {
    id: number;
    groupname: string;
    introduction: string | null;
    ddl: string;
    createdAt: string;
    updatedAt: null;
  }[];
};

export type getAllGroupMemberResult = {
  code: number;
  message: string;
  data: MemberItem[];
}

export const createGroup = (data: { groupname: string; introduction: string; ddl: string }) => {
  return http.request<basicResult>(
    "post",
    "api/group/create",
    { data }
  );
}

export const applyGroup = (data: { groupId: string; title: string; content: string }) => {
  return http.request<basicResult>(
    "post",
    "api/group/apply",
    { data }
  );
}


export const getGroupId = (data: { groupname: string }) => {
  return http.request<basicResult>(
    "get",
    "api/group/search",
    { data }
  );
}


export const getAllUserInGroup = (data: { groupId: string }) => {
  return http.request<basicResult>(
    "get",
    "api/group/members",
    { data }
  )
}

export const getAllGroup = () => {
  return http.request<getAllGroupResult>(
    "get",
    "api/group/all",
    {}
  )
}

export type getGroupInfoResult = {
  code: number;
  message: string;
  data: {
    id: number;
    groupname: string;
    createdAt: string;
    updatedAt: null;
    introduction: string | null;
    ddl: string;
  };
}

export const getGroupInfo = (data: { groupId: string }) => {
  return http.request<basicResult>(
    "get",
    "api/group/info",
    { params:data }
  )
}

export const getAllGroupByUserId = () => {
  return http.request<getMyGroupResult>(
    "get",
    "api/group/list",
    {}
  )
}

export const getAllGroupMember = (data: { groupId: string }) => {
  return http.request<getAllGroupMemberResult>(
    "get",
    "api/group/members",
    { params: data }
  )
}

/** 邀请用户 */
export const inviteMember = async (data: { groupId: string; email: string; title: string; content: string }) => {
  var userIdRec: string;
  try {
    const res = await getIdByEmail({ email: data.email });
    if (res.code === 0) {
      userIdRec = String(res.data);
    } else {
      throw new Error(res.message);
    }
  } catch (error) {
    console.error(error);
  }

  return http.request<basicResult>(
    "post",
    "api/group/invite",
    {
      data: {
        ...data,
        userIdRec,
      }
    }
  )
}

/** 踢出用户 */
export const kickMember = (data: { userIdRec: string; groupId: string; }) => {
  return http.request<basicResult>(
    "post",
    "api/group/kick",
    { data }
  )
}