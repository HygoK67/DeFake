<script setup lang="ts">
import { useNav } from "@/layout/hooks/useNav";
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { useUserStoreHook } from "@/store/modules/user";

// 从 useNav 中解构所需的数据
const {
  username, // 用户名
  userAvatar, // 用户头像
} = useNav();

// 定义表单对象
const userForm = reactive({
  username: username.value, // 用户名
  phone: "13812345678", // 电话
  email: "user@example.com", // 邮箱
  school: "北京航空航天大学" // 学校
});

// 头像相关
const avatar = ref(userAvatar.value); // 当前头像
const handleAvatarChange = (event: Event) => {
  const file = (event.target as HTMLInputElement).files?.[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = () => {
      avatar.value = reader.result as string; // 更新头像为本地预览
      useUserStoreHook().SET_AVATAR(avatar.value);
      ElMessage.success("头像已更新！");
    };
    reader.readAsDataURL(file);
  }
};
</script>

<template>
  <div class="user-info-container">
    <div class="user-info-card">
      <!-- 用户头像 -->
      <div class="avatar-container">
        <label for="avatar-upload" class="avatar-label">
          <img :src="avatar" alt="用户头像" class="avatar" />
        </label>
        <input id="avatar-upload" type="file" accept="image/*" class="avatar-input" @change="handleAvatarChange" />
      </div>

      <!-- 用户信息表单 -->
      <el-form :model="userForm" label-position="left" label-width="80px" class="info-form">
        <el-form-item label="用户名">
          <el-input v-model="userForm.username" disabled />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="userForm.phone" disabled />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="userForm.email" disabled />
        </el-form-item>
        <el-form-item label="学校">
          <el-input v-model="userForm.school" disabled />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
/* 背景样式 */
.user-info-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
  background-color: #f8faff;
  margin: 0 auto !important;
}

/* 卡片样式 */
.user-info-card {
  width: 400px;
  padding: 30px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  text-align: center;
  transition: transform 0.3s, box-shadow 0.3s;
}

/* 头像样式 */
.avatar-container {
  margin-bottom: 20px;
  position: relative;
}

.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid #409eff;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.avatar:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
}

/* 隐藏文件输入框 */
.avatar-input {
  display: none;
}

.avatar-label {
  cursor: pointer;
}

/* 表单样式 */
.info-form {
  text-align: left;
  margin-top: 20px;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-input {
  width: 100%;
}
</style>