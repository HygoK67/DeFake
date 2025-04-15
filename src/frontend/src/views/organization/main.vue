<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import { createGroup, getAllGroupByUserId, getAllGroup } from "@/api/group"; // 引入所需API
import { ElMessage } from "element-plus";
import { Search } from '@element-plus/icons-vue'; // 引入图标
import { Group } from '@/types/organization'

defineOptions({
  name: "OrganizationManagement"
});

const router = useRouter();
const searchKeyword = ref("");

// --- 状态和加载控制 ---
const personalLoading = ref(false);
const activeLoading = ref(false);
const createOrgLoading = ref(false);
const createOrganizationDialogVisible = ref(false);

// --- 数据存储 ---
const personalPreviewList = ref<Group[]>([]); // 个人活动预览列表
const activePreviewList = ref<Group[]>([]);   // 开放活动预览列表
const personalGroupIds = ref<Set<number>>(new Set()); // 存储个人相关活动的ID，用于过滤开放活动

const PREVIEW_LIMIT = 4; // 设置预览显示的数量

// --- 创建组织表单 ---
const createOrgForm = ref({
  name: '',
  description: '',
});

// --- 数据获取 ---
onMounted(() => {
  fetchPreviews();
});

async function fetchPreviews() {
  await fetchPersonalPreview(); // 先获取个人活动，拿到ID
  await fetchActivePreview();   // 再获取开放活动，进行过滤
}

// 获取个人活动预览
async function fetchPersonalPreview() {
  personalLoading.value = true;
  personalPreviewList.value = [];
  personalGroupIds.value.clear();
  try {
    console.log("正在获取个人活动预览...");
    const res = await getAllGroupByUserId();
    const mappedData = res.data.map((item: Group) => {
      personalGroupIds.value.add(item.id); // 记录ID
      return {
        id: item.id,
        groupname: item.groupname,
        introduction: item.introduction || "暂无简介",
        ddl: item.ddl,
        status: item.status, // '组长', '已加入', '申请中'
        role: item.role,
        createdAt: item.createdAt ? item.createdAt.split('T')[0] : 'N/A',
      };
    });

    const getSortPriority = (org: Group) => {
      if (org.role === 'leader' && org.status === 'in') return 1;
      if (org.role === 'member' && org.status === 'in') return 2;
      if (org.status === 'pending_apply') return 3;
      return 4;
    };
    mappedData.sort((a, b) => getSortPriority(a) - getSortPriority(b));
    personalPreviewList.value = mappedData.slice(0, PREVIEW_LIMIT); // 截取预览数量
  } catch (error) {
    console.error("获取个人活动预览失败:", error);
    ElMessage.error("获取个人活动预览失败");
  } finally {
    personalLoading.value = false;
  }
}

// 获取开放活动预览
async function fetchActivePreview() {
  activeLoading.value = true;
  activePreviewList.value = [];
  try {
    console.log("正在获取ALL group");
    const res = await getAllGroup();
    console.log("正在获取ALL group");
    const allGroupsData = res.data;
    const filteredData = allGroupsData
      .filter((item: any) => !personalGroupIds.value.has(item.id))
      .map((item: any) => ({ // 映射数据结构
        id: item.id,
        groupname: item.groupname,
        introduction: item.introduction || "暂无简介",
        ddl: item.ddl,
        status: 'not_in' as const,
        role: null,
        createdAt: item.createdAt ? item.createdAt.split('T')[0] : 'N/A',
      }))
      .sort((a: Group, b: Group) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());

    activePreviewList.value = filteredData.slice(0, PREVIEW_LIMIT);

  } catch (error) {
    console.error("获取开放活动预览失败:", error);
    ElMessage.error("获取开放活动预览失败");
  } finally {
    activeLoading.value = false;
  }
}

// 复用之前的状态映射函数
const mapApiStatus = (status: string, role: string | null): string => {
  if (status === 'in') {
    return role === 'leader' ? '管理员' : '已加入';
  }
  if (status === 'pending_apply') {
    return '申请中';
  }
  return status; // Fallback
};
// 复用之前的标签类型获取函数
const getStatusTagType = (status: string) => {
  switch (status) {
    case '管理员': return 'warning';
    case '已加入': return 'success';
    case '申请中': return 'info';
    case '未加入': return 'primary'; // 未加入用 primary 按钮样式匹配
    default: return 'info';
  }
};

// 关于 点击卡片
const handlePersonalCardClick = (orgId: number) => {
  router.push(`/organization/detail/${orgId}`);
};

// 查看组织详情 (两个列表的卡片都可触发)
const viewOrganizationDetail = (orgId: number) => {
  router.push(`/organization/detail/${orgId}`);
};


// 处理申请加入 (只在开放活动卡片上触发)
const handleJoinRequest = (orgId: number) => {
  router.push(`/organization/apply/${orgId}`);
};

// 搜索组织 (当前实现: 跳转到列表页并带上搜索词)
const searchOrganizations = () => {
  console.log(`搜索关键词: ${searchKeyword.value}`);
  // 跳转到 "当前开放活动" 列表页进行搜索
  router.push({ path: '/organization/list/active', query: { search: searchKeyword.value } });
  // 注意：目标列表页 (/organization/list/active) 需要能接收并处理 query 参数中的 search
};

// 打开创建组织对话框
const openCreateOrganizationDialog = () => {
  createOrganizationDialogVisible.value = true;
  // 重置表单
  createOrgForm.value = { name: '', description: '' };
};

// 提交创建组织申请
async function submitCreateOrganization(): Promise<void> {
  if (!createOrgForm.value.name) {
    ElMessage.warning('请输入组织名称');
    return;
  }
  createOrgLoading.value = true;
  try {
    const response = await createGroup({
      groupname: createOrgForm.value.name, // 假设API需要groupname
      introduction: createOrgForm.value.description, // 假设API需要introduction
      ddl: "2021.12.11"
    });

    // API 返回 code 为 0 表示成功 (根据你的API调整)
    if (response.code !== 0) {
      ElMessage.error(response.message || '组织申请失败'); // 显示API返回的错误信息
    } else {
      ElMessage.success('组织申请提交成功，请等待审核'); // 创建通常需要审核
      createOrganizationDialogVisible.value = false;

    }

  } catch (error: any) {
    console.error("创建组织失败:", error);
    ElMessage.error(error.message || '组织申请过程中发生错误');
  } finally {
    createOrgLoading.value = false;
  }
}

</script>

<template>
  <div class="organization-management-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">查找组织</h2>
    </div>

    <!-- 搜索栏 -->
    <div class="search-container">
      <el-input v-model="searchKeyword" placeholder="输入活动名称、组织或关键词搜索..." class="search-input" clearable
        @keyup.enter="searchOrganizations">
        <template #prefix>
          <el-icon>
            <search />
          </el-icon>
        </template>
      </el-input>

      <div class="search-actions">
        <el-button type="primary" @click="searchOrganizations">搜索</el-button>
        <el-button type="success" @click="openCreateOrganizationDialog">创建组织</el-button> <!-- 改为 success 颜色 -->
      </div>
    </div>

    <!-- 组织列表 -->
    <div class="organization-lists">
      <!-- 个人活动 -->
      <el-card class="organization-section" shadow="never" v-loading="personalLoading">
        <template #header>
          <div class="section-header">
            <h3>个人活动</h3>
          </div>
        </template>

        <div v-if="!personalLoading && personalPreviewList.length > 0" class="organization-cards">
          <el-card v-for="org in personalPreviewList" :key="org.id" class="org-card" shadow="hover"
            @click="handlePersonalCardClick(org.id)">
            <div class="org-card-content">
              <h4 class="org-name">{{ org.groupname }}</h4>
              <p class="org-detail">{{ org.introduction }} - {{ org.createdAt }}</p>
              <div class="org-action">
                <!-- 使用 mapApiStatus 后的状态判断 -->
                <el-tag :type="getStatusTagType(org.status)" size="small">
                  {{ mapApiStatus(org.status, org.role) }}
                </el-tag>
              </div>
            </div>
          </el-card>
        </div>
        <el-empty v-if="!personalLoading && personalPreviewList.length === 0" description="暂无个人相关活动"
          :image-size="80"></el-empty>

        <div v-if="!personalLoading && personalPreviewList.length > 0" class="view-more">
          <router-link to="/organization/list/personal">查看全部个人活动 ></router-link>
        </div>
      </el-card>

      <!-- 当前开放活动 -->
      <el-card class="organization-section" shadow="never" v-loading="activeLoading">
        <template #header>
          <div class="section-header">
            <h3>当前开放活动</h3>
          </div>
        </template>

        <div v-if="!activeLoading && activePreviewList.length > 0" class="organization-cards">
          <el-card v-for="org in activePreviewList" :key="org.id" class="org-card" shadow="hover"
            @click="viewOrganizationDetail(org.id)">
            <div class="org-card-content">
              <h4 class="org-name">{{ org.groupname }}</h4>
              <p class="org-detail">{{ org.introduction }} - {{ org.createdAt }}</p>
              <div class="org-action">
                <!-- 开放活动的状态固定为 '未加入'，按钮为 '申请加入' -->
                <el-button type="primary" size="small" @click.stop="handleJoinRequest(org.id)">申请加入</el-button>
              </div>
            </div>
          </el-card>
        </div>
        <el-empty v-if="!activeLoading && activePreviewList.length === 0" description="暂无开放活动"
          :image-size="80"></el-empty>

        <div v-if="!activeLoading && activePreviewList.length > 0" class="view-more">
          <router-link to="/organization/list/active">查看全部开放活动 ></router-link>
        </div>
      </el-card>
    </div>

    <!-- 创建组织对话框 -->
    <el-dialog v-model="createOrganizationDialogVisible" title="申请创建组织" width="500px" :close-on-click-modal="false"
      :before-close="() => createOrganizationDialogVisible = false">
      <el-form :model="createOrgForm" label-position="top">
        <el-form-item label="组织/活动名称" required>
          <el-input v-model="createOrgForm.name" placeholder="请输入组织或活动的全称"></el-input>
        </el-form-item>
        <el-form-item label="描述/简介">
          <el-input type="textarea" v-model="createOrgForm.description" placeholder="（选填）可以简单介绍组织或活动的目的、范围等"
            :rows="4"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="createOrganizationDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitCreateOrganization" :loading="createOrgLoading">
            提交申请
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.organization-management-container {
  padding: 20px;
  background-color: #f5f7fa; // 添加背景色，使卡片更突出
}

.page-header {
  margin-bottom: 20px;

  .page-title {
    font-size: 20px;
    font-weight: bold;
    color: #303133;
    margin: 0;
  }
}

.search-container {
  display: flex;
  gap: 15px;
  margin-bottom: 25px;
  background-color: #fff; // 搜索栏背景色
  padding: 15px 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);

  .search-input {
    flex: 1; // 占据更多空间
  }

  .search-actions {
    display: flex;
    gap: 10px;
  }
}

.organization-lists {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr)); // 响应式布局
  gap: 25px;

  .organization-section {

    // 使用 ElCard 代替 div 作为 section 容器
    :deep(.el-card__header) {
      padding: 15px 20px; // 调整卡片头部内边距
      border-bottom: 1px solid #ebeef5; // 添加分隔线
    }

    :deep(.el-card__body) {
      padding: 15px 20px; // 调整卡片主体内边距
      min-height: 200px; // 给个最小高度，防止空状态时太矮
      display: flex;
      flex-direction: column;
      justify-content: space-between; // 让内容和查看更多按钮分开
    }

    .section-header {
      h3 {
        font-size: 17px; // 调整标题大小
        margin: 0;
        font-weight: 600; // 加粗
        color: #303133;
      }
    }

    .organization-cards {
      display: flex;
      flex-direction: column;
      gap: 12px; // 调整卡片间距
      margin-bottom: 15px; // 和查看更多按钮的间距

      .org-card {
        cursor: pointer;
        transition: box-shadow 0.3s ease; // 平滑阴影过渡

        &:hover {
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); // 悬浮效果
        }

        .org-card-content {
          display: flex;
          flex-direction: column; // 让内容垂直排列

          .org-name {
            font-weight: 600; // 名称加粗
            margin: 0 0 5px 0; // 调整间距
            font-size: 15px;
            color: #303133;
            white-space: nowrap; // 不换行
            overflow: hidden; // 隐藏溢出
            text-overflow: ellipsis; // 显示省略号
          }

          .org-detail {
            color: #909399; // 组织和日期用灰色
            margin: 0 0 10px 0; // 调整间距
            font-size: 13px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }

          .org-action {
            margin-top: auto; // 将按钮推到底部
            display: flex;
            justify-content: flex-end; // 按钮右对齐
          }
        }
      }
    }

    .view-more {
      text-align: center; // 居中

      a {
        color: var(--el-color-primary); // 使用 Element Plus 主题色
        text-decoration: none;
        font-size: 14px;
        transition: color 0.2s;

        &:hover {
          color: var(--el-color-primary-light-3); // 悬浮颜色变浅
          text-decoration: underline;
        }
      }
    }

    .el-empty {
      padding: 20px 0; // 调整空状态的内边距
    }
  }
}
</style>