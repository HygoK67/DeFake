<script setup lang="ts">
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { updatePassword } from "@/api/user";
import { useNav } from "@/layout/hooks/useNav";

const { logout } = useNav();

const loading = ref(false);

// 表单数据
const form = ref({
  currentPassword: "",
  newPassword: "",
  confirmPassword: ""
});



// 表单校验规则
const rules = {
  currentPassword: [{ required: true, message: "请输入当前密码", trigger: "blur" }],
  newPassword: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    { min: 6, message: "密码长度不能少于6位", trigger: "blur" }
  ],
  confirmPassword: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    { min: 6, message: "密码长度不能少于6位", trigger: "blur" }
  ]
};

// 提交表单
const onSubmit = async (formEl: any) => {
  if (!formEl) return;

  await formEl.validate(async (valid: boolean) => {
    if (valid) {
      loading.value = true;
      try {
        const response = await updatePassword({
          oldPassword: form.value.currentPassword,
          newPassword: form.value.newPassword,
        });
        if (response.code === 0) {
          ElMessage.success("密码修改成功！请重新登录");
          logout();
        } else {
          ElMessage.error(response.message);
        }
        // form.value.currentPassword = "";
        // form.value.newPassword = "";
        // form.value.confirmPassword = "";
      } catch (error) {
        ElMessage.error("请求失败，请检查网络或稍后重试！");
        console.error(error);
      } finally {
        loading.value = false;
      }
    } else {
      ElMessage.error("请检查输入内容！");
    }
  });
};

// 重置表单
const onReset = (formEl: any) => {
  if (!formEl) return;
  formEl.resetFields();
};
</script>

<template>
  <div class="change-password-container">
    <div class="change-password-card">
      <h2 class="title">修改密码</h2>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="150px" class="form">
        <!-- 当前密码 -->
        <el-form-item label="当前密码" prop="currentPassword">
          <el-input v-model="form.currentPassword" type="password" placeholder="请输入当前密码" clearable />
        </el-form-item>

        <!-- 新密码 -->
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="form.newPassword" type="password" placeholder="请输入新密码" clearable />
        </el-form-item>

        <!-- 确认新密码 -->
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入新密码" clearable />
        </el-form-item>

        <!-- 操作按钮 -->
        <el-form-item>
          <el-button type="primary" size="large" @click="onSubmit($refs.formRef)">
            提交
          </el-button>
          <el-button size="large" @click="onReset($refs.formRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.change-password-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
  background-color: #f8faff;
  margin: 0 auto !important;
}

.change-password-card {
  width: 500px;
  /* 增加卡片宽度 */
  padding: 30px;
  /* 增加内边距 */
  background: #fff;
  border-radius: 12px;
  /* 调整圆角 */
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  /* 增强阴影效果 */
}

.title {
  text-align: center;
  font-size: 24px;
  /* 增大标题字体 */
  font-weight: bold;
  margin-bottom: 40px;
  /* 增加标题与表单的间距 */
}

.form {
  width: 100%;
}

.el-form-item {
  font-size: 15px;
  /* 增大表单标签字体 */
}

::v-deep(.el-form-item__label) {
  font-size: 14px !important;
  /* 增大 label 的字体 */
  font-weight: bold !important;
  /* 加粗 label */
  margin-right: 25px !important;
  /* 增加 label 与输入框的间距 */
  margin-left: -40px !important;
  /* 调整 label 的位置 */
}

.el-button {
  font-size: 14px;
  /* 增大按钮字体 */
  padding: 10px 20px;
  /* 增大按钮内边距 */
}
</style>