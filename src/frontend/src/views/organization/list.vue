<script setup lang="ts">
import { getAllGroup, getAllGroupByUserId } from "@/api/group";
import { ref, reactive, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";

defineOptions({
  name: "OrganizationList"
});

const route = useRoute();
const router = useRouter();
const loading = ref(false);
const searchKeyword = ref("");
const listType = computed(() => route.query.type);
const pageTitle = computed(() => {
    return listType.value === "personal" ? "个人活动" : "当前开放活动";
});

// 组织列表
const organizations = ref([]);

// 获取组织列表
onMounted(() => {
  loading.value = true;
  
  if (listType.value === "personal") {
    // 获取用户已加入或申请的活动
    getAllGroupByUserId()
      .then((res) => {
        // 转换API返回的数据格式以匹配表格需要的格式
        organizations.value = res.data.map(item => ({
          id: item.id,
          name: item.groupname,
          organization: item.groupname,
            status: item.status === 'in'
            ? (item.role === 'leader' ? '管理员' : '已加入')
            : item.status === 'pending_apply'
                ? '申请中'
                : '未加入',
          date: item.createdAt.split('T')[0] // 格式化日期
        }));
        loading.value = false;
      })
      .catch((err) => {
        console.error(err);
        loading.value = false;
      });
  } else {
    // 获取所有开放活动
    getAllGroup()
      .then((res) => {
        // 转换API返回的数据格式以匹配表格需要的格式
        organizations.value = res.data.map(item => ({
          id: item.id,
          name: item.groupname,
          organization: item.groupname,
          status: '未加入', // 默认为未加入
          date: item.createdAt.split('T')[0] // 格式化日期
        }));
        loading.value = false;
      })
      .catch((err) => {
        console.error(err);
        loading.value = false;
      });
  }
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
  if (!searchKeyword.value) {
    return;
  }
  
  loading.value = true;
  // 这里可以调用后端API进行搜索，或者前端过滤
  // 前端过滤示例：
  const keyword = searchKeyword.value.toLowerCase();
  
  if (listType.value === "personal") {
    getAllGroupByUserId()
      .then((res) => {
        const filteredData = res.data.filter(item => 
          item.groupname.toLowerCase().includes(keyword)
        ).map(item => ({
          id: item.id,
          name: item.groupname,
          organization: item.groupname,
          status: item.status === 'in' ? '已加入' : 
                  item.status === 'pending_apply' ? '申请中' : '未加入',
          date: item.createdAt.split('T')[0]
        }));
        
        organizations.value = filteredData;
        loading.value = false;
      })
      .catch((err) => {
        console.error(err);
        loading.value = false;
      });
  } else {
    getAllGroup()
      .then((res) => {
        const filteredData = res.data.filter(item => 
          item.groupname.toLowerCase().includes(keyword)
        ).map(item => ({
          id: item.id,
          name: item.groupname,
          organization: item.groupname,
          status: '未加入',
          date: item.createdAt.split('T')[0]
        }));
        
        organizations.value = filteredData;
        loading.value = false;
      })
      .catch((err) => {
        console.error(err);
        loading.value = false;
      });
  }
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
        @keyup.enter="searchOrganizations"
      >
        <template #prefix>
          <el-icon><search /></el-icon>
        </template>
      </el-input>

      <el-button type="primary" @click="searchOrganizations">搜索</el-button>
    </div>

    <!-- 组织列表 -->
    <el-card v-loading="loading" shadow="hover">
      <el-table :data="organizations" style="width: 100%">
        <el-table-column prop="name" label="活动名称" min-width="200">
          <template #default="scope">
            <a
              href="javascript:;"
              class="org-link"
              @click="viewOrganizationDetail(scope.row.id)"
            >
              {{ scope.row.name }}
            </a>
          </template>
        </el-table-column>
        <el-table-column prop="date" label="创建日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag 
              :type="scope.row.status === '已加入' 
                ? 'success' 
                : scope.row.status === '申请中' 
                  ? 'info' 
                  : 'warning'"
            >
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button
              size="small"
              @click="viewOrganizationDetail(scope.row.id)"
              >查看详情</el-button
            >

            <el-button
              v-if="scope.row.status === '未加入'"
              size="small"
              type="primary"
              @click="handleJoinRequest(scope.row.id)"
              >申请加入</el-button
            >
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
    color: #409eff;
    text-decoration: none;

    &:hover {
      text-decoration: underline;
    }
  }
}
</style>