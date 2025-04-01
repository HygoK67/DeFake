<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

defineOptions({
  name: "OrganizationDetail"
});

const route = useRoute();
const router = useRouter();
const organizationId = ref(route.params.id);
const loading = ref(false);

// 组织信息 - 简化版
const organization = reactive({
  id: organizationId.value,
  name: "",
  organization: "",
  manager: "",
  submissionStartDate: "",
  submissionEndDate: "",
});

// 组织状态
const memberStatus = ref("未加入"); // "已加入", "已提交"

// 获取组织详情
onMounted(() => {
  loading.value = true;
  
  // 模拟API请求
  setTimeout(() => {
    // 根据ID加载不同的数据
    if (organizationId.value === "1") {
      organization.name = "ABC大学2024年毕业设计";
      organization.organization = "ABC大学";
      organization.manager = "王老师";
      organization.submissionStartDate = "2024-03-15";
      organization.submissionEndDate = "2024-05-31";
      memberStatus.value = "已加入";
    } else if (organizationId.value === "3") {
      organization.name = "FGH期刊2025年度投稿";
      organization.organization = "FGH出版社";
      organization.manager = "李编辑";
      organization.submissionStartDate = "2024-04-01";
      organization.submissionEndDate = "2024-12-31";
      memberStatus.value = "已提交";
    } else {
      // 默认数据
      organization.name = `组织${organizationId.value}`;
      organization.organization = `测试组织${organizationId.value}`;
      organization.manager = "管理员";
      organization.submissionStartDate = "2024-03-01";
      organization.submissionEndDate = "2024-06-30";
      memberStatus.value = "未加入";
    }
    
    loading.value = false;
  }, 800);
});

// 提交报告
const submitReport = () => {
  router.push(`/report/submit/${organizationId.value}`);
};
</script>

<template>
  <div class="organization-detail-container">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <span class="page-title">{{ organization.name }}</span>
        </div>
      </template>
      
      <div class="detail-content">
        <!-- 基本信息 -->
        <div class="info-section">
          <h3>基本信息</h3>
          <div class="info-item">
            <span class="label">活动名称:</span>
            <span>{{ organization.name }}</span>
          </div>
          <div class="info-item">
            <span class="label">所属组织:</span>
            <span>{{ organization.organization }}</span>
          </div>
          <div class="info-item">
            <span class="label">负责人:</span>
            <span>{{ organization.manager }}</span>
          </div>
        </div>
        
        <div class="info-section">
          <h3>投稿信息</h3>
          <div class="info-item">
            <span class="label">投稿开始日期:</span>
            <span>{{ organization.submissionStartDate }}</span>
          </div>
          <div class="info-item">
            <span class="label">投稿截止日期:</span>
            <span class="deadline">{{ organization.submissionEndDate }}</span>
          </div>
          <div class="info-item">
            <span class="label">状态:</span>
            <el-tag type="success">{{ memberStatus }}</el-tag>
          </div>
        </div>
        
        <!-- 操作按钮 -->
        <div class="action-buttons">
          <el-button 
            type="primary" 
            @click="submitReport"
          >提交报告</el-button>
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