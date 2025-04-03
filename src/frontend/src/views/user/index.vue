<script setup lang="ts">
import { useNav } from "@/layout/hooks/useNav";

// 从 useNav 中解构所需的数据
const {
  username, // 用户名
  userAvatar, // 用户头像
  logout // 退出登录方法
} = useNav();

// 模拟其他用户信息
const phone = "13812345678"; // 电话
const email = "user@example.com"; // 邮箱
const school = "北京航空航天大学"; // 学校
</script>

<template>
  <div class="user-info-container">
    <div class="user-info-card">
      <!-- 用户头像 -->
      <div class="avatar-container">
        <img :src="userAvatar" alt="用户头像" class="avatar" />
      </div>

      <!-- 用户信息 -->
      <div class="info-container">
        <p><strong>用户名：</strong>{{ username }}</p>
        <p><strong>电话：</strong>{{ phone }}</p>
        <p><strong>邮箱：</strong>{{ email }}</p>
        <p><strong>学校：</strong>{{ school }}</p>
      </div>

      <!-- 退出登录按钮 -->
      <el-button type="danger" @click="logout">退出登录</el-button>
    </div>
  </div>
</template>

<style scoped>
.user-info-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-color: #f5f5f5;
}

.user-info-card {
  width: 400px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.avatar-container {
  margin-bottom: 20px;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #409eff;
}

.info-container p {
  margin: 10px 0;
  font-size: 16px;
  color: #333;
}
</style>
