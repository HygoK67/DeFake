<script lang="ts" setup>
import Motion from "../login/utils/motion";
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";

import Lock from "@iconify-icons/ri/lock-fill";
import User from "@iconify-icons/ri/user-3-fill";
import Phone from "@iconify-icons/ri/phone-line";

const router = useRouter();
const loading = ref(false);
const loading2 = ref(false);

const registerForm = reactive({
  username: "",
  phone: "",
  password: "",
  confirmPassword: ""
});

const rules = {
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    { pattern: /^1[3-9]\d{9}$/, message: "请输入有效的手机号", trigger: "blur" }
  ],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
  confirmPassword: [
    { required: true, message: "请再次输入密码", trigger: "blur" },
    {
      validator: (rule: any, value: string, callback: Function) => {
        if (value !== registerForm.password) {
          callback(new Error("两次输入的密码不一致"));
        } else {
          callback();
        }
      },
      trigger: "blur"
    }
  ]
};

const registerFormRef = ref();

const onRegister = async () => {
  const formRef = registerFormRef.value;
  if (!formRef) return;

  // 调用 validate 方法校验表单
  await formRef.validate((valid, fields) => {
    if (valid) {
      loading.value = true;

      // 模拟注册成功
      setTimeout(() => {
        loading.value = false;
        ElMessage.success("注册成功！");
        router.push("/login"); // 跳转到登录界面
      }, 1000);
    } else {
      // 校验失败时，显示错误提示
      ElMessage.warning("请填写正确的注册信息！");
    }
  });
};

const returnToLogin = () => {
  loading2.value = true;
  setTimeout(() => {
    loading2.value = false;
    router.push("/login"); // 跳转到登录界面
  }, 200);
};
</script>

<template>
  <div class="register-container">
    <div class="register-box">
      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="rules"
        size="large"
      >
        <Motion :delay="100">
          <el-form-item prop="username">
            <el-input
              v-model="registerForm.username"
              placeholder="请输入用户名"
              clearable
              :prefix-icon="useRenderIcon(User)"
            />
          </el-form-item>
        </Motion>

        <Motion :delay="150">
          <el-form-item prop="phone">
            <el-input
              v-model="registerForm.phone"
              placeholder="请输入手机号"
              clearable
              :prefix-icon="useRenderIcon(Phone)"
            />
          </el-form-item>
        </Motion>

        <Motion :delay="200">
          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="请输入密码"
              clearable
              :prefix-icon="useRenderIcon(Lock)"
            />
          </el-form-item>
        </Motion>

        <Motion :delay="250">
          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              clearable
              :prefix-icon="useRenderIcon(Lock)"
            />
          </el-form-item>
        </Motion>

        <Motion :delay="300">
          <el-button
            class="w-full mt-3"
            size="default"
            type="primary"
            :loading="loading"
            @click="onRegister"
          >
            注册
          </el-button>
        </Motion>

        <Motion :delay="350">
          <el-button
            class="w-full mt-3"
            size="default"
            type="default"
            :loading="loading2"
            @click="returnToLogin"
          >
            返回
          </el-button>
        </Motion>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.register-box {
  width: 400px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
</style>
