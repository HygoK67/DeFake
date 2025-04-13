import { http } from "@/utils/http";
// import { useUserStoreHook } from "@/store/modules/user";
export type basicResult = {
    code: number;
    message: string;
    data: null;
  };



export const createGroup = (data: {groupname:string;introduction:string; ddl:string}) => {
    return http.request<basicResult>(
      "post",
      "api/group/create",
      {data}
    );
  }

export const applyGroup = (data: {groupId: string; title: string; content: string}) =>{
    return http.request<basicResult>(
      "post",
      "api/group/apply",
      {data}
    );
  }


export const getGroupId = (data: {groupname:string}) => {
    return http.request<basicResult>(
      "get",
      "api/group/search",
      {data}
    );
  }


export const getAllUserInGroup = (data: {groupId: string}) =>{
  return http.request<basicResult>(
    "get",
    "api/group/members",
    {data}
  )
}

export const getAllGroup = () => {
  return http.request<basicResult>(
    "get",
    "api/group/all",
    {}
  )
}

export const groupid2Groupname = (data: {groupId: string}) => {
  return http.request<basicResult>(
    "get",
    "api/group/id2name",
    {data}
  )
}

export const getAllGroupByUserId = () => {
  return http.request<basicResult>(
    "get",
    "api/group/list",
    {}
  )
}
