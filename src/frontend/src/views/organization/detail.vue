<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { getAllGroup } from "@/api/group"; // Import the API function

defineOptions({
  name: "OrganizationDetail"
});

const route = useRoute();
const router = useRouter();
const groupId = ref(route.params.id);
const loading = ref(false);



const mapStatusToDisplay = (status: string, role: string | null): string => {
  if (status === 'in') {
    return role === 'leader' ? '管理员' : '已加入';
  }
  if (status === 'pending_apply') {
    return '申请中';
  }
  return '未加入';
};

// Get the tag type based on status
const getTagType = (status: string): string => {
  switch (status) {
    case '管理员': return 'danger';
    case '已加入': return 'success';
    case '申请中': return 'info';
    case '未加入': return 'primary';
    default: return 'info';
  }
};

// Get organization details
onMounted(async () => {
  loading.value = true;
  
  try {
    // response = await getGroupInfo TODO @
  } catch (error) {
    console.error("获取组织详情失败:", error);
    ElMessage.error("获取组织详情失败，请检查网络连接");
  } finally {
    loading.value = false;
  }
});

// Handle application to join
const handleJoinRequest = () => {
  router.push(`/organization/apply/${organizationId.value}`);
};

// Handle submission of reports
const submitReport = () => {
  // This would typically navigate to a report submission page
  ElMessage.info(`组织 ${organization.groupname} 的报告提交功能尚未实现`);
  // Example: router.push(`/report/submit/${organizationId.value}`);
};

// Return to organization list
const goBack = () => {
  router.go(-1);
};
</script>

<template>
  <div class="organization-detail-container">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <el-page-header @back="goBack">
            <template #content>
              <span class="page-title">{{ organization.groupname }}</span>
            </template>
          </el-page-header>
        </div>
      </template>

      <div class="detail-content">
        <!-- 基本信息 -->
        <div class="info-section">
          <h3>基本信息</h3>
          <div class="info-item">
            <span class="label">活动名称:</span>
            <span>{{ organization.groupname }}</span>
          </div>
          <div class="info-item">
            <span class="label">创建日期:</span>
            <span>{{ organization.createdAt }}</span>
          </div>
        </div>

        <div class="info-section">
          <h3>组织介绍</h3>
          <div class="info-item description">
            <p>{{ organization.introduction }}</p>
          </div>
        </div>

        <div class="info-section">
          <h3>截止日期</h3>
          <div class="info-item">
            <span class="label">提交截止日期:</span>
            <span class="deadline">{{ organization.ddl }}</span>
          </div>
          <div class="info-item">
            <span class="label">状态:</span>
            <el-tag :type="getTagType(memberStatus)">{{ memberStatus }}</el-tag>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="action-buttons">
          <el-button 
            v-if="memberStatus === '已加入'"
            type="primary" 
            @click="submitReport">
            提交报告
          </el-button>
          
          <el-button 
            v-if="memberStatus === '未加入'"
            type="primary" 
            @click="handleJoinRequest">
            申请加入
          </el-button>
          
          <el-button 
            v-if="memberStatus === '申请中'"
            type="info" 
            disabled>
            申请处理中
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
.organization-detail-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;

  .card-header {
    .page-title {
      font-size: 18px;
      font-weight: bold;
    }
  }

  .detail-content {
    .info-section {
      margin-bottom: 25px;

      h3 {
        margin: 0 0 15px 0;
        font-size: 18px;
        color: #303133;
        font-weight: bold;
        padding-bottom: 10px;
        border-bottom: 1px solid #ebeef5;
      }

      .info-item {
        margin-bottom: 12px;
        display: flex;

        &.description {
          display: block;
          
          p {
            line-height: 1.6;
            color: #606266;
            margin: 0;
          }
        }

        .label {
          color: #606266;
          width: 120px;
          font-weight: 500;
        }

        .deadline {
          color: #f56c6c;
          font-weight: 500;
        }
      }
    }

    .action-buttons {
      display: flex;
      gap: 15px;
      flex-wrap: wrap;
    }
  }
}
</style>