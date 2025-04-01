<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";

defineOptions({
  name: "OrganizationManagement"
});

const router = useRouter();
const searchKeyword = ref("");
const activeTab = ref("all");

// 个人活动数据
const personalOrganizations = ref([
  {
    id: 1,
    name: "ABC大学2024年毕业设计",
    organization: "ABC大学",
    status: "已加入"
  },
  {
    id: 2,
    name: "CDE会议2025年投稿",
    organization: "CDE学会",
    status: "已加入"
  },
  {
    id: 3,
    name: "FGH会议2025年度投稿",
    organization: "FGH出版社",
    status: "申请中"
  },
]);

// 活跃组织数据
const activeOrganizations = ref([
  {
    id: 5,
    name: "AAA大学000学院2024年毕业设计",
    organization: "AAA大学000学院",
    status: "未加入"
  },
  {
    id: 6,
    name: "AAA大学001学院2024年毕业设计",
    organization: "AAA大学001学院",
    status: "未加入"
  },
  {
    id: 7,
    name: "AAB大学xyz学院2024年毕业设计",
    organization: "AAB大学xyz学院",
    status: "未加入"
  },
  {
    id: 8,
    name: "AAD大学2024年毕业设计",
    organization: "AAD大学",
    status: "未加入"
  }
]);

// 筛选条件
const filterOptions = ref({
  types: ["全部", "学校", "会议", "期刊", "研究所"],
  status: ["全部", "已加入", "申请中", "未加入"]
});

// 处理申请加入
const handleJoinRequest = (orgId: number) => {
  router.push(`/organization/apply/${orgId}`);
};

// 查看组织详情
const viewOrganizationDetail = (orgId: number) => {
  router.push(`/organization/detail/${orgId}`);
};

// 查看更多
const viewMore = (type: string) => {
  console.log(`查看更多 ${type} 组织`);
  // 可以实现分页或加载更多逻辑
};

// 搜索组织
const searchOrganizations = () => {
  console.log(`搜索关键词: ${searchKeyword.value}`);
  // 实际搜索逻辑
};

// 筛选组织
const filterOrganizations = () => {
  console.log("筛选组织");
  // 打开筛选对话框
};

// 分类组织
const classifyOrganizations = () => {
  console.log("分类组织");
  // 打开分类对话框
};
</script>

<template>
  <div class="organization-management-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">查找组织</h2>
    </div>

    <!-- 搜索栏 -->
    <div class="search-container">
      <el-input
        v-model="searchKeyword"
        placeholder="输入组织名称、活动名称或关键词搜索..."
        class="search-input"
        clearable
      >
        <template #prefix>
          <el-icon><search /></el-icon>
        </template>
      </el-input>
      
      <div class="search-actions">
        <el-button type="primary" @click="searchOrganizations">搜索</el-button>
        <el-button @click="filterOrganizations">筛选</el-button>
        <el-button @click="classifyOrganizations">分类</el-button>
      </div>
    </div>

    <!-- 组织列表 -->
    <div class="organization-lists">
      <!-- 个人活动 -->
      <div class="organization-section">
        <div class="section-header">
          <h3>个人活动</h3>
        </div>
        
        <div class="organization-cards">
          <el-card 
            v-for="org in personalOrganizations" 
            :key="org.id" 
            class="org-card"
            shadow="hover"
            @click="viewOrganizationDetail(org.id)"
          >
            <div class="org-card-content">
              <h4 class="org-name">{{ org.name }}</h4>
              <p class="org-detail">所属组织: {{ org.organization }}</p>
              
              <div class="org-action">
                <el-button 
                  v-if="org.status === '已加入'" 
                  type="success" 
                  size="small" 
                  disabled
                >已加入</el-button>
                <el-button 
                  v-else-if="org.status === '申请中'" 
                  type="info" 
                  size="small" 
                  disabled
                >申请中</el-button>
                <el-button 
                  v-else
                  type="primary" 
                  size="small"
                  @click.stop="handleJoinRequest(org.id)"
                >申请加入</el-button>
              </div>
            </div>
          </el-card>
        </div>
        
        <div class="view-more">
          <router-link to="/organization/list/personal">查看更多 ></router-link>
        </div>
      </div>
      
      <!-- Active活动 -->
      <div class="organization-section">
        <div class="section-header">
          <h3>Active活动</h3>
        </div>
        
        <div class="organization-cards">
          <el-card 
            v-for="org in activeOrganizations" 
            :key="org.id" 
            class="org-card"
            shadow="hover"
            @click="viewOrganizationDetail(org.id)"
          >
            <div class="org-card-content">
              <h4 class="org-name">{{ org.name }}</h4>
              <p class="org-detail">所属组织: {{ org.organization }}</p>
              
              <div class="org-action">
                <el-button 
                  v-if="org.status === '已加入'" 
                  type="success" 
                  size="small" 
                  disabled
                >已加入</el-button>
                <el-button 
                  v-else-if="org.status === '申请中'" 
                  type="info" 
                  size="small" 
                  disabled
                >申请中</el-button>
                <el-button 
                  v-else
                  type="primary" 
                  size="small"
                  @click.stop="handleJoinRequest(org.id)"
                >申请加入</el-button>
              </div>
            </div>
          </el-card>
        </div>
        
        <div class="view-more">
          <router-link to="/organization/list/active">查看更多 ></router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.organization-management-container {
  padding: 20px;
  
  .page-header {
    margin-bottom: 20px;
    
    .page-title {
      font-size: 20px;
      font-weight: bold;
      color: #333;
      margin: 0;
    }
  }
  
  .search-container {
    display: flex;
    gap: 15px;
    margin-bottom: 25px;
    
    .search-input {
      flex: 1;
    }
    
    .search-actions {
      display: flex;
      gap: 10px;
    }
  }
  
  .organization-lists {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 25px;
    
    @media (max-width: 992px) {
      grid-template-columns: 1fr;
    }
    
    .organization-section {
      background-color: #fff;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
      
      .section-header {
        margin-bottom: 15px;
        
        h3 {
          font-size: 18px;
          margin: 0;
          font-weight: bold;
        }
      }
      
      .organization-cards {
        display: flex;
        flex-direction: column;
        gap: 15px;
        
        .org-card {
          cursor: pointer;
          transition: transform 0.2s;
          
          &:hover {
            transform: translateY(-3px);
          }
          
          .org-card-content {
            .org-name {
              font-weight: bold;
              margin: 0 0 8px 0;
              font-size: 16px;
            }
            
            .org-detail {
              color: #606266;
              margin: 0 0 15px 0;
              font-size: 14px;
            }
            
            .org-action {
              display: flex;
              justify-content: flex-end;
            }
          }
        }
      }
      
      .view-more {
        margin-top: 15px;
        text-align: center;
        
        a {
          color: #409EFF;
          text-decoration: none;
          font-size: 14px;
          
          &:hover {
            text-decoration: underline;
          }
        }
      }
    }
  }
}
</style>