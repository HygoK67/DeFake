<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage, FormRules } from "element-plus";
defineOptions({
  name: "OrganizationApply"
});

const route = useRoute();
const router = useRouter();
const organizationId = ref(route.params.id);
const loading = ref(false);

// 模拟获取组织信息
const organization = reactive({
  id: organizationId.value,
  name: "",
  organization: "",
  description: ""
});

// 申请表单
const applyForm = reactive({
  name: "",
  email: "",
  phone: "",
  institution: "",
  position: "",
  reason: ""
});

// 表单规则
const rules = {
  name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { type: "email", message: "请输入正确的邮箱地址", trigger: "blur" }
  ],
  phone: [{ required: true, message: "请输入联系电话", trigger: "blur" }],
  institution: [{ required: true, message: "请输入所属机构", trigger: "blur" }],
  reason: [{ required: true, message: "请输入申请理由", trigger: "blur" }]
};

const formRef = ref(null);

// 获取组织信息
onMounted(() => {
  loading.value = true;
  
  // 模拟API请求
  setTimeout(() => {
    // 模拟数据
    if (organizationId.value === "5") {
      organization.name = "AAA大学2024年毕业设计";
      organization.organization = "AAA大学";
      organization.description = "AAA大学2024年毕业设计作品收集与评比活动";
    } else if (organizationId.value === "6") {
      organization.name = "AAB大学2024年毕业设计";
      organization.organization = "AAB大学";
      organization.description = "AAB大学2024年毕业设计作品收集与评比活动";
    } else {
      organization.name = `组织${organizationId.value}`;
      organization.organization = `测试组织${organizationId.value}`;
      organization.description = "这是一个测试组织描述";
    }
    
    loading.value = false;
  }, 800);
});

// 提交申请
const submitApplication = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate((valid, fields) => {
    if (valid) {
      loading.value = true;
      
      // 模拟API提交
      setTimeout(() => {
        loading.value = false;
        
        // 显示成功消息
        ElMessage({
          message: "申请已提交，请等待审核",
          type: "success"
        });
        
        // 返回组织列表
        router.push("/organization/main");
      }, 1000);
    }
  });
};

// 返回
const goBack = () => {
  router.go(-1);
};
</script>

<template>
  <div class="organization-apply-container">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <el-page-header @back="goBack">
            <template #content>
              <span class="page-title">申请加入组织</span>
            </template>
          </el-page-header>
        </div>
      </template>
      
      <!-- 组织信息 -->
      <div class="organization-info">
        <h3>{{ organization.name }}</h3>
        <p class="org-detail">所属组织: {{ organization.organization }}</p>
        <p class="org-description">{{ organization.description }}</p>
        
        <el-divider>申请信息</el-divider>
      </div>
      
      <!-- 申请表单 -->
      <el-form 
        ref="formRef"
        :model="applyForm" 
        :rules="rules"
        label-width="100px"
        label-position="top"
        class="apply-form"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="applyForm.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="applyForm.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="applyForm.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          
          <el-col :span="12">
            <el-form-item label="所属机构" prop="institution">
              <el-input v-model="applyForm.institution" placeholder="请输入所属机构" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="职位" prop="position">
          <el-input v-model="applyForm.position" placeholder="请输入职位（可选）" />
        </el-form-item>
        
        <el-form-item label="申请理由" prop="reason">
          <el-input 
            v-model="applyForm.reason" 
            type="textarea" 
            :rows="4"
            placeholder="请描述您申请加入该组织的理由" 
          />
        </el-form-item>
        
        <el-form-item>
          <div class="form-actions">
            <el-button @click="goBack">取消</el-button>
            <el-button type="primary" @click="submitApplication" :loading="loading">提交申请</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
.organization-apply-container {
  padding: 20px;
  max-width: 900px;
  margin: 0 auto;
  
  .card-header {
    display: flex;
    align-items: center;
    
    .page-title {
      font-size: 18px;
      font-weight: bold;
    }
  }
  
  .organization-info {
    margin-bottom: 20px;
    
    h3 {
      margin: 0 0 10px 0;
      font-size: 18px;
      color: #303133;
    }
    
    .org-detail {
      color: #606266;
      margin: 0 0 10px 0;
    }
    
    .org-description {
      color: #606266;
      line-height: 1.5;
    }
  }
  
  .apply-form {
    margin-top: 20px;
    
    .form-actions {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
    }
  }
}
</style>