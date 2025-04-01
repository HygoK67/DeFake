<script setup lang="ts">
import { ref, reactive, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";

defineOptions({
  name: "OrganizationList"
});

const route = useRoute();
const router = useRouter();
const loading = ref(false);
const searchKeyword = ref("");
const listType = ref(route.params.type);

// 计算标题
const pageTitle = computed(() => {
  return listType.value === 'personal' ? '个人活动' : 'Active活动';
});

// 组织列表
const organizations = ref([]);

// 获取组织列表
onMounted(() => {
  loading.value = true;
  
  // 模拟API请求
  setTimeout(() => {
    if (listType.value === 'personal') {
      // 个人活动列表
      organizations.value = [
        {
          id: 1,
          name: "ABC大学2024年毕业设计",
          organization: "ABC大学",
          status: "已加入",
          date: "2024-03-01"
        },
        {
          id: 2,
          name: "CDE会议2025年投稿",
          organization: "CDE学会",
          status: "已加入",
          date: "2024-02-15"
        },
        {
          id: 3,
          name: "FGH期刊2025年度投稿",
          organization: "FGH出版社",
          status: "申请中",
          date: "2024-01-20"
        },
        {
          id: 4,
          name: "IJK研究所项目申报",
          organization: "IJK研究院",
          status: "申请中",
          date: "2024-01-10"
        },
        // 添加更多数据
        {
          id: 9,
          name: "LMN大学学术交流会",
          organization: "LMN大学",
          status: "已加入",
          date: "2023-12-05"
        },
        {
          id: 10,
          name: "OPQ研究院合作项目",
          organization: "OPQ研究院",
          status: "已加入",
          date: "2023-11-20"
        }
      ];
    } else {
      // Active活动列表
      organizations.value = [
        {
          id: 5,
          name: "AAA大学2024年毕业设计",
          organization: "AAA大学",
          status: "未加入",
          date: "2024-03-10"
        },
        {
          id: 6,
          name: "AAB大学2024年毕业设计",
          organization: "AAB大学",
          status: "未加入",
          date: "2024-03-05"
        },
        {
          id: 7,
          name: "AAC大学2024年毕业设计",
          organization: "AAC大学",
          status: "未加入",
          date: "2024-03-02"
        },
        {
          id: 8,
          name: "AAD大学2024年毕业设计",
          organization: "AAD大学",
          status: "未加入",
          date: "2024-02-28"
        },
        // 添加更多数据
        {
          id: 11,
          name: "RST会议论文征集",
          organization: "RST学会",
          status: "未加入",
          date: "2024-02-20"
        },
        {
          id: 12,
          name: "UVW期刊特刊征稿",
          organization: "UVW出版社",
          status: "未加入",
          date: "2024-02-15"
        },
        {
          id: 13,
          name: "XYZ科研竞赛",
          organization: "XYZ基金会",
          status: "未加入",
          date: "2024-02-10"
        }
      ];
    }
    loading.value = false;
  }, 800);
});

// 处理申请加入
const handleJoinRequest = (orgId: number) => {
  router.push(`/organization/apply/${orgId}`);
};

// 查看组织详情
const viewOrganizationDetail = (orgId: number) => {
  router.push(`/organization/detail/${orgId}`);
};

// 返回
const goBack = () => {
  router.push("/organization/main");
};

// 搜索组织
const searchOrganizations = () => {
  console.log(`搜索关键词: ${searchKeyword.value}`);
  // 实际搜索逻辑
};
</script>

<template>
  <div class="organization-list-container">
    <!-- 页头 -->
    <div class="page-header">
      <el-page-header @back="goBack">
        <template #content>
          <span class="page-title">{{ pageTitle }}</span>
        </template>
      </el-page-header>
    </div>
    
    <!-- 搜索栏 -->
    <div class="search-container">
      <el-input
        v-model="searchKeyword"
        placeholder="输入组织名称、活动名称搜索..."
        class="search-input"
      >
        <template #prefix>
          <el-icon><search /></el-icon>
        </template>
      </el-input>
      
      <el-button type="primary" @click="searchOrganizations">搜索</el-button>
    </div>
    
    <!-- 组织列表 -->
    <el-card shadow="hover" v-loading="loading">
      <el-table :data="organizations" style="width: 100%">
        <el-table-column prop="name" label="活动名称" min-width="200">
          <template #default="scope">
            <a href="javascript:;" @click="viewOrganizationDetail(scope.row.id)" class="org-link">
              {{ scope.row.name }}
            </a>
          </template>
        </el-table-column>
        <el-table-column prop="organization" label="所属组织" min-width="150" />
        <el-table-column prop="date" label="创建日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <!-- <el-tag 
              :type="scope.row.status === '已加入' 
                ? 'success' 
                : scope.row.status === '申请中' 
                  ? 'info' 
                  : 'default'"
            >
              {{ scope.row.status }}
            </el-tag> -->
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button 
              size="small" 
              @click="viewOrganizationDetail(scope.row.id)"
            >查看详情</el-button>
            
            <el-button 
              v-if="scope.row.status === '未加入'"
              size="small" 
              type="primary"
              @click="handleJoinRequest(scope.row.id)"
            >申请加入</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="organizations.length"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="10"
        />
      </div>
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
.organization-list-container {
  padding: 20px;
  
  .page-header {
    margin-bottom: 20px;
    
    .page-title {
      font-size: 18px;
      font-weight: bold;
    }
  }
  
  .search-container {
    display: flex;
    gap: 15px;
    margin-bottom: 20px;
    
    .search-input {
      width: 400px;
    }
  }
  
  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
  
  .org-link {
    color: #409EFF;
    text-decoration: none;
    
    &:hover {
      text-decoration: underline;
    }
  }
}
</style>