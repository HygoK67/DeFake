import { defineStore } from "pinia";
import { store, router, storageLocal } from "../utils";
import { type GroupInfo, getGroupInfo, getGroupMembers } from "@/api/group"; // 假设这些 API 函数存在
import { groupKey } from "@/utils/auth"; // 假设已定义

// 使用您提供的 GroupType 接口
interface GroupType {
  // 组织 ID
  groupid: number;
  // 组织名称
  groupname: string;
  // 组织描述
  description: string;
  // 更新时间
  updated_at: string;
  // 组织介绍
  introduction: string;
  // 截止日期
  ddl: string;
}

export const useGroupStore = defineStore({
  id: "pure-group",
  state: (): GroupType => ({
    // 组织 ID
    groupid: storageLocal().getItem<GroupInfo>(groupKey)?.id ?? 0,
    // 组织名称
    groupname: storageLocal().getItem<GroupInfo>(groupKey)?.groupname ?? "",
    // 组织描述
    description: storageLocal().getItem<GroupInfo>(groupKey)?.description ?? "",
    // 更新时间
    updated_at: storageLocal().getItem<GroupInfo>(groupKey)?.updated_at ?? "",
    // 组织介绍
    introduction: storageLocal().getItem<GroupInfo>(groupKey)?.introduction ?? "",
    // 截止日期
    ddl: storageLocal().getItem<GroupInfo>(groupKey)?.ddl ?? "",
  }),
  actions: {
    /** 更新 storageLocal 中的组织信息 */
    updateStorage(key: keyof GroupType, value: any) {
      const groupInfo: GroupInfo = storageLocal().getItem<GroupInfo>(groupKey) || {} as GroupInfo;
      groupInfo[key] = value; // 更新指定字段
      storageLocal().setItem(groupKey, groupInfo); // 保存到 storageLocal
    },
    /** 存储组织 ID */
    SET_ID(id: number) {
      this.id = id;
      this.updateStorage("id", id);
    },
    /** 存储组织名称 */
    SET_GROUPNAME(groupname: string) {
      this.groupname = groupname;
      this.updateStorage("groupname", groupname);
    },
    /** 存储组织描述 */
    SET_DESCRIPTION(description: string) {
      this.description = description;
      this.updateStorage("description", description);
    },
    /** 存储更新时间 */
    SET_UPDATED_AT(updated_at: string) {
      this.updated_at = updated_at;
      this.updateStorage("updated_at", updated_at);
    },
    /** 存储组织介绍 */
    SET_INTRODUCTION(introduction: string) {
      this.introduction = introduction;
      this.updateStorage("introduction", introduction);
    },
    /** 存储截止日期 */
    SET_DDL(ddl: string) {
      this.ddl = ddl;
      this.updateStorage("ddl", ddl);
    },
    /** 获取组织信息 */
    async getGroupInfo(groupId: number) {
      return new Promise<GroupInfo>(async (resolve, reject) => {
        try {
          // 调用获取组织信息接口
          const response = await getGroupInfo(groupId);
          if (response?.code === 0) {
            const groupInfo = response.data;
            // 更新状态管理中的组织信息
            this.SET_ID(groupInfo.id);
            this.SET_GROUPNAME(groupInfo.groupname);
            this.SET_DESCRIPTION(groupInfo.description);
            this.SET_UPDATED_AT(groupInfo.updated_at);
            this.SET_INTRODUCTION(groupInfo.introduction);
            this.SET_DDL(groupInfo.ddl);
            
            resolve(groupInfo);
          } else {
            reject(new Error(response?.message || "获取组织信息失败"));
          }
        } catch (error) {
          reject(error);
        }
      });
    },
    /** 更新组织信息 */
    async updateGroupInfo(data: Partial<GroupType>) {
      try {
        // 假设有一个更新组织信息的 API
        const response = await updateGroupInfo(this.id, data);
        if (response?.code === 0) {
          // 更新成功，刷新本地存储
          if (data.groupname) this.SET_GROUPNAME(data.groupname);
          if (data.description) this.SET_DESCRIPTION(data.description);
          if (data.introduction) this.SET_INTRODUCTION(data.introduction);
          if (data.ddl) this.SET_DDL(data.ddl);
          
          return true;
        } else {
          console.error(response?.message || "更新组织信息失败");
          return false;
        }
      } catch (error) {
        console.error("更新组织信息出错:", error);
        return false;
      }
    },
    /** 离开组织 */
    leaveGroup() {
      // 清空组织信息
      this.SET_ID(0);
      this.SET_GROUPNAME("");
      this.SET_DESCRIPTION("");
      this.SET_UPDATED_AT("");
      this.SET_INTRODUCTION("");
      this.SET_DDL("");
      
      // 可以在这里添加调用离开组织的 API
      
      // 跳转到组织选择页面
      router.push("/select-group");
    }
  }
});

export function useGroupStoreHook() {
  return useGroupStore(store);
}