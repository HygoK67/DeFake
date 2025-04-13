import { http } from "@/utils/http";
// import { useUserStoreHook } from "@/store/modules/user";
export type basicResult = {
    code: number;
    message: string;
    data: null;
  };



export const createGroup = (data: {groupname:string;}) => {
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