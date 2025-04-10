<script setup lang="ts">
import { useNav } from "@/layout/hooks/useNav";
import { reactive, ref } from "vue";
import { ElMessage, ElDialog } from "element-plus";
import { useUserStoreHook } from "@/store/modules/user";
import { uploadFile, updateUserInfo } from "@/api/user"; // 新增接口导入

const {
  username,
  userAvatar,
  email,
  phone,
} = useNav();

const userForm = reactive({
  username: username.value,
  phone: phone.value,
  email: email.value,
  school: "北京航空航天大学"
});

// 头像相关
const avatar = ref(userAvatar.value); // 当前头像
const handleAvatarChange = async (event: Event) => {
  const file = (event.target as HTMLInputElement).files?.[0];
  if (file) {
    try {
      // 调用上传接口
      const res = await uploadFile({ file });
      if (res.code === 0) {
        avatar.value = res.data; // 使用后端返回的 data 作为头像路径
        useUserStoreHook().SET_AVATAR(avatar.value); // 更新到状态管理
        ElMessage.success("头像已更新！");
      } else {
        ElMessage.error(res.message || "头像上传失败！");
      }
    } catch (error) {
      ElMessage.error(error.message || "网络错误，头像上传失败！");
    }
  }
};

const isSubmitting = ref(false);

const currentField = ref('');
const dialogVisible = ref(false);
const tempForm = reactive({
  username: '',
  phone: '',
  email: '',
  school: '',
});

const openDialog = (field: string) => {
  console.log('Opening dialog for field:', field); // 调试信息
  currentField.value = field;
  tempForm[field as keyof typeof tempForm] = userForm[field as keyof typeof userForm];
  console.log(tempForm);
  dialogVisible.value = true;
};

const handleInputSubmit = async () => {
  // if (isSubmitting.value) return; // 防止重复提交

  // // 表单验证（示例：必填项检查）
  // const requiredFields = ['username', 'phone', 'email'];
  // const missing = requiredFields.filter(f => !tempForm[f as keyof typeof tempForm]);
  // if (missing.length > 0) {
  //   ElMessage.warning(`请填写：${missing.join('、')}`);
  //   return;
  // }

  // try {
  //   isSubmitting.value = true;
  //   // 更新 userForm
  //   userForm[currentField.value as keyof typeof userForm] = tempForm[currentField.value as keyof typeof tempForm];
  //   // 调用后端接口（模拟传递所有表单数据）
  //   const res = await updateUserInfo(userForm);
  //   if (res.code === 0) {
  //     ElMessage.success(`「${currentField.value}」更新成功！`);
  //     // 同步到状态管理（示例）
  //     useUserStoreHook().updateUserInfo(userForm);
  //     dialogVisible.value = false;
  //   } else {
  //     ElMessage.error(res.message || '更新失败，请重试');
  //   }
  // } catch (error) {
  //   ElMessage.error('网络错误，请检查连接');
  // } finally {
  //   isSubmitting.value = false;
  // }
};
</script>

<template>
  <div class="user-info-container">
    <div class="user-info-card">
      <div class="content-wrapper">
        <el-form :model="userForm" label-position="left" label-width="80px" class="info-form">
          <!-- 每个输入框添加点击事件，弹出对话框 -->
          <el-form-item label="用户名">
            <el-input v-model="userForm.username" @click="openDialog('username')" :disabled="false"
              style="cursor: pointer;" />
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="userForm.phone" @click="openDialog('phone')" :disabled="false"
              style="cursor: pointer;" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="userForm.email" @click="openDialog('email')" :disabled="false"
              style="cursor: pointer;" />
          </el-form-item>
          <el-form-item label="学校">
            <el-input v-model="userForm.school" @click="openDialog('school')" :disabled="false"
              style="cursor: pointer;" />
          </el-form-item>
        </el-form>

        <!-- 右侧头像 -->
        <div class="avatar-container">
          <label for="avatar-upload" class="avatar-label">
            <img :src="avatar" alt="用户头像" class="avatar" />
          </label>
          <input id="avatar-upload" type="file" accept="image/*" class="avatar-input" @change="handleAvatarChange" />
        </div>
      </div>
    </div>
    <el-dialog v-model="dialogVisible" title="修改信息" :before-close="() => dialogVisible = false">
      <el-form :model="tempForm" label-position="left" label-width="80px">
        <el-form-item :label="currentField">
          <el-input v-model="tempForm[currentField]" :key="currentField" />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleInputSubmit" :loading="isSubmitting">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.user-info-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
  background-color: #f8faff;
  margin: 0 auto !important;
}

.user-info-card {
  width: 800px;
  height: auto;
  /* 扩大卡片宽度 */
  padding: 40px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s, box-shadow 0.3s;
}

.content-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 40px;
}

.info-form {
  flex: 1;
  margin-top: 0;
}

.avatar-container {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar {
  width: 160px;
  height: 160px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid #409eff;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.avatar:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
}

.upload-hint {
  margin-top: 12px;
  color: #666;
  font-size: 0.9em;
  transition: color 0.3s;
}

.avatar-label:hover .upload-hint {
  color: #409eff;
}

.avatar-input {
  display: none;
}

.el-form-item {
  margin-bottom: 22px;
}

.el-input {
  width: 100%;
  pointer-events: auto;
  /* 确保输入框可以接收点击事件 */
}

.el-dialog__wrapper {
  z-index: 9999 !important;
  /* 确保对话框在最上层 */
}
</style>