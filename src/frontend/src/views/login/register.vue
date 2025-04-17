<script setup lang="ts">
import Motion from "./utils/motion";
import { useRouter } from "vue-router";
import { bg, avatar, illustration } from "./utils/static";
import { useRenderIcon } from "@/components/ReIcon/src/hooks";
import { ref, reactive, toRaw } from "vue";
import { useDataThemeChange } from "@/layout/hooks/useDataThemeChange";
import { ElMessage } from "element-plus";
import { registerUser, getEmailCode } from "@/api/user";

import dayIcon from "@/assets/svg/day.svg?component";
import darkIcon from "@/assets/svg/dark.svg?component";
import Lock from "@iconify-icons/ri/lock-fill";
import User from "@iconify-icons/ri/user-3-fill";
import Phone from "@iconify-icons/ri/phone-line";
import Mail from "@iconify-icons/ri/mail-line";

const router = useRouter();
const loading = ref(false);
const verificationSent = ref(false); // 是否已发送验证码
const countdown = ref(0); // 倒计时秒数

const registerForm = reactive({
  username: "11",
  phone: "13965656565",
  mail: "",
  password: "11",
  confirmPassword: "11",
  verificationCode: "" // 验证码字段
});

const rules = {
  mail: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { type: "email" as const, message: "请输入有效的邮箱地址", trigger: "blur" }
  ],
  verificationCode: [
    { required: true, message: "请输入验证码", trigger: "blur" }
  ],
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

const sendCode = async () => {
  if (!registerForm.mail) {
    ElMessage.warning("请先输入邮箱！");
    return;
  }
  try {
    loading.value = true;
    const response = await getEmailCode({ email: registerForm.mail });
    if (response.code === 0) {
      ElMessage.success("验证码已发送，请检查邮箱！");
      verificationSent.value = true;
      startCountdown(); // 开始倒计时
    } else {
      ElMessage.error(response.message);
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

const onRegister = async () => {
  const formRef = registerFormRef.value;
  if (!formRef) return;

  await formRef.validate(async (valid: boolean) => {
    if (valid) {
      loading.value = true;
      try {
        const response = await registerUser({
          username: registerForm.username,
          phone: registerForm.phone,
          email: registerForm.mail,
          password: registerForm.password
        }, registerForm.verificationCode);
        if (response.code === 0) {
          ElMessage.success("注册成功！");
          router.push("/login");
        } else {
          ElMessage.error(response.message);
          console.log(response);
        }
      } catch (error) {
        ElMessage.error("请求失败，请检查网络或稍后重试！");
        console.error(error);
      } finally {
        loading.value = false;
      }
    } else {
      ElMessage.warning("请填写正确的注册信息！");
    }
  });
};

const { dataTheme, overallStyle, dataThemeChange } = useDataThemeChange();
dataThemeChange(overallStyle.value);
</script>

<template>
  <div class="select-none">
    <img :src="bg" class="wave" />
    <div class="flex-c absolute right-5 top-3">
      <!-- 主题 -->
      <el-switch v-model="dataTheme" inline-prompt :active-icon="dayIcon" :inactive-icon="darkIcon"
        @change="dataThemeChange" />
    </div>
    <div class="login-container">
      <div class="img">
        <component :is="toRaw(illustration)" />
      </div>
      <div class="login-box">
        <div class="login-form">
          <avatar class="avatar" style="
              margin-left: 130px;
              margin-right: 20px;
              width: 128px;
              height: 128px;
              margin-bottom: 0px;
            " />
          <el-form :model="registerForm" :rules="rules" ref="registerFormRef" size="large">
            <Motion :delay="100">
              <el-form-item prop="username">
                <el-input v-model="registerForm.username" placeholder="请输入用户名" clearable
                  :prefix-icon="useRenderIcon(User)" />
              </el-form-item>
            </Motion>

            <Motion :delay="150">
              <el-form-item prop="phone">
                <el-input v-model="registerForm.phone" placeholder="请输入手机号" clearable
                  :prefix-icon="useRenderIcon(Phone)" />
              </el-form-item>
            </Motion>

            <Motion :delay="200">
              <el-form-item prop="mail">
                <el-input v-model="registerForm.mail" placeholder="请输入邮箱" :prefix-icon="useRenderIcon(Mail)">
                  <template #suffix>
                    <el-button class="send-code-button" size="small" type="primary" :loading="loading" @click="sendCode"
                      :disabled="verificationSent">
                      {{ verificationSent ? `${countdown}s` : "验证" }}
                    </el-button>
                  </template>
                </el-input>
              </el-form-item>
            </Motion>

            <Motion :delay="250">
              <el-form-item prop="verificationCode">
                <el-input v-model="registerForm.verificationCode" placeholder="请输入验证码" clearable
                  :prefix-icon="useRenderIcon(Lock)" />
              </el-form-item>
            </Motion>

            <Motion :delay="300">
              <el-form-item prop="password">
                <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" clearable
                  :prefix-icon="useRenderIcon(Lock)" />
              </el-form-item>
            </Motion>

            <Motion :delay="350">
              <el-form-item prop="confirmPassword">
                <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" clearable
                  :prefix-icon="useRenderIcon(Lock)" />
              </el-form-item>
            </Motion>

            <Motion :delay="400">
              <el-button class="w-full mt-3" size="default" type="primary" :loading="loading" @click="onRegister">
                注册
              </el-button>
            </Motion>

            <Motion :delay="450">
              <el-button class="w-full mt-2.5" size="default" :loading="loading" @click="() => router.push('/login')">
                返回
              </el-button>
            </Motion>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url("@/style/login.css");
</style>

<style lang="scss" scoped>
.send-code-button {
  padding: 0 px;
  height: 100%;
  margin-right: -14px;
}
</style>
