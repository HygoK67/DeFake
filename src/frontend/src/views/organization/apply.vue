<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage, FormRules, FormInstance } from "element-plus"; // Added FormInstance
import { applyGroup, getGroupInfo } from "@/api/group"; // Import applyGroup and getGroupInfo
// Assuming getGroupInfoResult is correctly defined in "@/api/organization"
// Assuming basicResult is correctly defined somewhere (e.g., "@/api/user" or a common types file)

defineOptions({
  name: "OrganizationApply"
});

const route = useRoute();
const router = useRouter();
// Ensure organizationId is treated as a string for API calls
const organizationId = ref(String(route.params.id || '')); // Get ID as string, provide default empty string
const loading = ref(false);
const fetchLoading = ref(false); // Separate loading state for fetching info

// Group/Organization information
const organization = reactive({
  id: organizationId.value,
  name: "",
  description: ""
});

// Apply form data
const applyForm = reactive({
  // The form collects details, but the API might only need title/content.
  // The backend likely identifies the user via auth token.
  // We'll use the 'reason' field for the API 'content'.
  name: "",
  email: "",
  phone: "",
  institution: "",
  position: "",
  reason: "" // This will be used as the 'content' for the API call
});

// Form validation rules
const rules: FormRules = { // Explicitly type as FormRules
  name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] } // Added change trigger
  ],
  phone: [{ required: true, message: "请输入联系电话", trigger: "blur" }],
  institution: [{ required: true, message: "请输入所属机构", trigger: "blur" }],
  // Position is optional based on placeholder
  reason: [{ required: true, message: "请输入申请理由", trigger: "blur" }]
};

const formRef = ref<FormInstance | null>(null); // Type the formRef

// Fetch group/organization information on mount
const fetchOrganizationInfo = async () => {
  if (!organizationId.value) {
    ElMessage.error("无效的组织ID");
    router.push("/organization/main"); // Or some error page
    return;
  }
  fetchLoading.value = true;
  try {
    const res = await getGroupInfo({ groupId: organizationId.value });
    if (res.code === 0 && res.data) {
      organization.name = res.data.groupname;
      // Use introduction if available, otherwise fallback or leave empty
      organization.description = res.data.introduction || `关于 ${res.data.groupname} 的描述信息。`;
    } else {
      ElMessage.error(res.message || "获取组织信息失败");
      // Optionally redirect if the group doesn't exist or access is denied
      // router.push("/organization/main");
    }
  } catch (error) {
    console.error("Error fetching organization info:", error);
    ElMessage.error("获取组织信息时发生错误");
    // Optionally redirect
    // router.push("/organization/main");
  } finally {
    fetchLoading.value = false;
  }
};

onMounted(() => {
  fetchOrganizationInfo();
});

// Submit the application using the API
const submitApplication = async () => {
  if (!formRef.value) return;

  // Validate the form first
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true; // Start loading indicator for submission
      try {
        // Prepare data for the applyGroup API
        const apiData = {
          groupId: organizationId.value,
          // Define a suitable title. Could be generic or include user info if available.
          // Let's use a generic title for now.
          title: `申请加入组织: ${organization.name}`,
          // Use the reason field from the form as the content.
          content: applyForm.reason
        };

        // Call the API
        const res = await applyGroup(apiData);

        if (res.code === 0) {
          // Success
          ElMessage({
            message: "申请已提交，请等待审核。",
            type: "success",
            duration: 3000 // Optional: duration in ms
          });
          // Redirect back to the organization list or detail page
          router.push("/organization/main"); // Adjust as needed
        } else {
          // API returned an error
          ElMessage.error(res.message || "提交申请失败，请稍后重试。");
        }
      } catch (error) {
        // Network or other unexpected errors
        console.error("Error submitting application:", error);
        ElMessage.error("提交申请时发生网络错误，请检查您的连接。");
      } finally {
        loading.value = false; // Stop loading indicator
      }
    } else {
      // Form validation failed
      ElMessage.warning("请完整填写申请信息。");
      return false;
    }
  });
};

// Function to navigate back
const goBack = () => {
  router.go(-1);
};
</script>

<template>
  <div class="organization-apply-container">
    <!-- Use fetchLoading for the card's loading state during initial info fetch -->
    <el-card v-loading="fetchLoading">
      <template #header>
        <div class="card-header">
          <el-page-header @back="goBack">
            <template #content>
              <span class="page-title">申请加入组织</span>
            </template>
          </el-page-header>
        </div>
      </template>

      <!-- Display organization info once loaded -->
      <div v-if="!fetchLoading && organization.name" class="organization-info">
        <h3>{{ organization.name }}</h3>
        <p class="org-description">{{ organization.description || '暂无描述信息' }}</p>
        <el-divider>填写申请信息</el-divider>
      </div>
      <div v-else-if="!fetchLoading && !organization.name">
         <p>无法加载组织信息。</p>
      </div>

      <!-- Application Form -->
      <!-- Hide form until org info is loaded to prevent applying to unknown org -->
      <el-form
        v-if="!fetchLoading && organization.name"
        ref="formRef"
        :model="applyForm"
        :rules="rules"
        label-width="100px"
        label-position="top"
        class="apply-form"
        @submit.prevent="submitApplication"
      >
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="applyForm.name" placeholder="请输入您的姓名" />
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="applyForm.email" placeholder="请输入您的常用邮箱" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :xs="24" :sm="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input
                v-model="applyForm.phone"
                placeholder="请输入您的联系电话"
              />
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="12">
            <el-form-item label="所属机构" prop="institution">
              <el-input
                v-model="applyForm.institution"
                placeholder="例如：XX大学，XX公司"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="职位" prop="position">
          <el-input
            v-model="applyForm.position"
            placeholder="请输入您的职位（可选）"
          />
        </el-form-item>

        <el-form-item label="申请理由" prop="reason">
          <el-input
            v-model="applyForm.reason"
            type="textarea"
            :rows="4"
            placeholder="请详细描述您申请加入该组织的理由、您的相关经验或期望等。"
          />
        </el-form-item>

        <el-form-item>
          <div class="form-actions">
            <el-button @click="goBack">取消</el-button>
            <!-- Use 'loading' ref for the submit button's loading state -->
            <el-button
              type="primary"
              :loading="loading"
              @click="submitApplication"
              >提交申请</el-button
            >
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
      font-size: 1.1rem; // Use relative units
      font-weight: 600; // Slightly bolder
      color: #303133;
    }
  }

  .organization-info {
    margin-bottom: 25px; // Increased spacing

    h3 {
      margin: 0 0 12px 0; // Increased spacing
      font-size: 1.2rem; // Slightly larger
      color: #303133;
      font-weight: 600;
    }

    .org-description {
      color: #606266;
      line-height: 1.6; // Improved readability
      font-size: 0.95rem;
    }

    .el-divider {
        margin: 25px 0; // Consistent spacing around divider
    }
  }

  .apply-form {
    margin-top: 20px;

    // Improve responsiveness
    .el-row {
        margin-bottom: 0; // Let form item handle bottom margin
    }
    .el-form-item {
        margin-bottom: 22px; // Standardize form item spacing
    }

    // Adjust label style if needed
    :deep(.el-form-item__label) {
        padding-bottom: 4px; // Space between label and input
        line-height: normal;
        font-weight: 500;
    }

    .form-actions {
      display: flex;
      justify-content: flex-end;
      gap: 12px; // Slightly increased gap
      margin-top: 10px; // Add some space above actions
    }
  }

  // Responsive adjustments if needed
  @media (max-width: 768px) {
     .apply-form .el-col {
        margin-bottom: 0; // Reset col margin on smaller screens if needed
     }
  }
}
</style>