<script setup lang="ts">
import { useNav } from "@/layout/hooks/useNav";
import { reactive, ref, computed } from "vue";
import { ElMessage, ElDialog } from "element-plus";
import { useUserStoreHook } from "@/store/modules/user";
import { getEmailCode } from "@/api/user"; // 新增接口导入
import { getToken } from "@/utils/auth";

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
const handleAvatarSuccess = async (response) => {
  try {
    if (response.code === 0) {
      const respond2 = await useUserStoreHook().updateAvatar(response.data);
      if (respond2.code === 0) {
        avatar.value = response.data; // 使用后端返回的 data 作为头像路径
        ElMessage.success("头像更新成功！");
      } else {
        ElMessage.error(respond2.message || "头像更新失败！");
      }
    }
  } catch (error) {
    ElMessage.error(error.message || "网络错误，头像上传失败！");
  }
};

const handleAvatarError = (error) => {
  ElMessage.error(error.message || "头像上传失败！");
};

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isJPG) {
    ElMessage.error('上传头像图片只能是 JPG 或 PNG 格式！');
  }
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB！');
  }
  return isJPG && isLt2M;
};

const isSubmitting = ref(false);
const currentField = ref('');
const dialogVisible = ref(false);
const tempForm = reactive({
  username: '',
  phone: '',
  email: '',
  school: '',
  verifyCode: '',
});

const openDialog = (field: string) => {
  console.log('Opening dialog for field:', field); // 调试信息
  currentField.value = field;
  tempForm[field as keyof typeof tempForm] = userForm[field as keyof typeof userForm];
  console.log(tempForm);
  dialogVisible.value = true;
};

const handleInputSubmit = async () => {
  if (isSubmitting.value) return; // 防止重复提交
  // 表单验证（示例：必填项检查）
  // const requiredFields = ['username', 'phone', 'email'];
  // const missing = requiredFields.filter(f => !tempForm[f as keyof typeof tempForm]);
  // if (missing.length > 0) {
  //   ElMessage.warning(`请填写：${missing.join('、')}`);
  //   return;
  // }
  try {
    isSubmitting.value = true;
    // 调用接口函数
    const respond = await useUserStoreHook().updateUserInfo(tempForm, currentField.value);
    if (respond.code === 0) {
      userForm[currentField.value as keyof typeof userForm] = tempForm[currentField.value as keyof typeof tempForm];
      ElMessage.success(respond.message || '更新成功！');
    } else {
      throw new Error(respond.message || '更新失败，请重试');
    }
    dialogVisible.value = false;
  } catch (error) {
    ElMessage.error(error.message || '更新失败，请重试');
  } finally {
    tempForm['verifyCode'] = ''; // 清空验证码
    isSubmitting.value = false;
  }
};

const loading = ref(false);
const verificationSent = ref(false);
const countdown = ref(0); // 倒计时秒数


const sendCode = async () => {
  if (!tempForm.email) {
    ElMessage.warning("请先输入邮箱！");
    return;
  }
  try {
    loading.value = true;
    const response = await getEmailCode({ email: tempForm.email });
    console.log(response);
    if (response.code === 0) {
      ElMessage.success("验证码已发送，请检查邮箱！");
      verificationSent.value = true;
      startCountdown(); // 开始倒计时
    } else {
      ElMessage.error("验证码发送失败，请重试！");
      console.error(response.message);
    }
  } catch (error) {
    ElMessage.error("请求失败，请检查网络或稍后重试！");
    console.error(error);
  } finally {
    loading.value = false;
  }
};

const startCountdown = () => {
  countdown.value = 3; // 设置倒计时为 60 秒
  const timer = setInterval(() => {
    countdown.value--;
    if (countdown.value <= 0) {
      clearInterval(timer); // 倒计时结束，清除定时器
      verificationSent.value = false; // 允许重新发送验证码
    }
  }, 1000);
};

// const uploadHeader = computed(() => {
//   const token = getToken();
//   return {
//     "token": token.accessToken
//   }
// })
</script>

<template>
  <div class="user-info-container">
    <div class="user-info-card">
      <div class="content-wrapper">
        <el-form :model="userForm" label-position="left" label-width="80px" class="info-form">
          <!-- 每个输入框添加点击事件，弹出对话框 -->
          <el-form-item label="用户名">
            <el-input class="el-input__inner" v-model="userForm.username" @click="openDialog('username')" readonly
              style="cursor: pointer;" />
          </el-form-item>
          <el-form-item label="电话">
            <el-input class="el-input__inner" v-model="userForm.phone" @click="openDialog('phone')" readonly
              style="cursor: pointer;" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input class="el-input__inner" v-model="userForm.email" @click="openDialog('email')" readonly
              style="cursor: pointer;" />
          </el-form-item>
          <el-form-item label="学校">
            <el-input class="el-input__inner" v-model="userForm.school" @click="openDialog('school')" readonly
              style="cursor: pointer;" />
          </el-form-item>
        </el-form>

        <!-- 右侧头像 -->
        <div class="avatar-container">

          <el-upload class="avatar-uploader" action="http://122.9.35.116:8080/api/file/upload" :headers="{
            'jwtToken': getToken().accessToken.trim()
          }" :show-file-list="false" :on-success="handleAvatarSuccess" :on-error="handleAvatarError"
            :before-upload="beforeAvatarUpload" name="files">
            <label for="avatar-upload" class="avatar-label">
              <img :src="avatar" alt="用户头像" class="avatar" />
            </label>
          </el-upload>
        </div>
      </div>
    </div>
    <el-dialog class="my_dialog" width="500px" v-model="dialogVisible" title="修改信息"
      :before-close="() => dialogVisible = false">
      <el-form :model="tempForm" label-position="left" label-width="75px">
        <el-form-item :label="currentField">
          <el-input v-model="tempForm[currentField]" :key="currentField">
            <template #suffix>
              <el-button class="send-code-button" size="small" type="primary" :loading="loading" @click="sendCode"
                :disabled="verificationSent" v-if="currentField === 'email'">
                {{ verificationSent ? `${countdown}s` : "验证" }}
              </el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="verifyCode" v-if="currentField === 'email'">
          <el-input v-model="tempForm['verifyCode']" :key="currentField" />
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

.send-code-button {
  padding: 0 px;
  height: 100%;
  margin-right: -14px;
}

.el-input__inner {
  transition: all 0.3s ease;
}

/* 鼠标悬停时边框颜色加深，并向上浮动 2px */
.el-input__inner:hover {
  border-color: #409eff !important;
  transform: translateY(-0.8px);
}
</style>