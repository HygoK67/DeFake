<template>
  <div class="organization-manage-container">
    <el-card class="manage-card">
      <template #header>
        <div class="card-header">
          <h2>组织管理界面</h2>
        </div>
      </template>

      <el-tabs v-model="activeTab" @tab-click="handleTabChange">
        <!-- 成员管理栏目 -->
        <el-tab-pane label="成员管理" name="members">
          <!-- 搜索和筛选区域 -->
          <div class="filter-container">
            <div class="filter-left">
              <el-input
                v-model="memberSearchKeyword"
                placeholder="搜索成员名称"
                clearable
                class="search-input"
              >
                <template #prefix>
                  <el-icon><search /></el-icon>
                </template>
              </el-input>
            </div>

            <div class="filter-right">
              <el-button
                type="primary"
                @click="handleInviteUser"
              >
                邀请用户
              </el-button>
            </div>
          </div>

          <!-- 表格区域 -->
          <el-table
            v-loading="memberTableLoading"
            :data="sortedMemberData"
            border
            stripe
            style="width: 100%"
          >
            <el-table-column prop="username" label="成员名称" min-width="120" />
            <el-table-column prop="email" label="邮箱" min-width="180" />
            <el-table-column prop="role" label="角色" width="100">
              <template #default="scope">
                <el-tag
                  :type="scope.row.role === '管理员' ? 'danger' : 'info'"
                >
                  {{ scope.row.role }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="150">
              <template #default="scope">
                <el-tag
                  :type="getMemberStatusType(scope.row.status)"
                >
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="220">
              <template #default="scope">
                <el-button
                  v-if="scope.row.status === '申请加入组织中' && isCurrentUserAdmin"
                  size="small"
                  type="success"
                  @click="handleApproveJoin(scope.row)"
                >
                  批准加入
                </el-button>
                <el-button
                  v-if="scope.row.status === '申请加入组织中' && isCurrentUserAdmin"
                  size="small"
                  type="danger"
                  @click="handleRejectJoin(scope.row)"
                >
                  拒绝申请
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页器 -->
          <div class="pagination-container">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :total="membersDataTotal"
              :current-page="memberCurrentPage"
              @current-change="handleMemberCurrentChange"
            />
          </div>
        </el-tab-pane>

        <!-- 结果管理栏目 -->
        <el-tab-pane label="结果管理" name="results">
          <!-- 搜索区域 -->
          <div class="filter-container">
            <div class="filter-left">
              <el-input
                v-model="searchKeyword"
                placeholder="搜索文件名"
                clearable
                class="search-input"
              >
                <template #prefix>
                  <el-icon><search /></el-icon>
                </template>
              </el-input>
            </div>
          </div>

          <!-- 表格区域 -->
          <el-table
            v-loading="tableLoading"
            :data="filteredResultsData"
            border
            stripe
            style="width: 100%"
          >
            <el-table-column prop="fileName" label="文件名" min-width="180">
              <template #default="scope">
                <a href="javascript:void(0);" @click="handleViewDetails(scope.row)">{{ scope.row.fileName }}</a>
              </template>
            </el-table-column>
            <el-table-column prop="fileType" label="文件类型" width="120" />
            <el-table-column prop="uploader" label="上传者" width="100" />
            <el-table-column prop="uploadTime" label="上传时间" width="170" />
            <el-table-column prop="fileSize" label="文件大小" width="100" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="score" label="检测结果" width="100" />
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button size="small" type="primary" @click="handleViewDetails(scope.row)">
                  查看详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页器 -->
          <div class="pagination-container">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :total="resultsDataTotal"
              :current-page="currentPage"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 邀请用户弹窗 -->
    <el-dialog v-model="inviteDialogVisible" title="邀请用户加入组织" width="500px">
      <el-form :model="inviteForm" label-width="100px">
        <el-form-item label="邮箱地址">
          <el-input v-model="inviteForm.email" placeholder="请输入邮箱地址"></el-input>
        </el-form-item>
        <el-form-item label="邀请消息">
          <el-input 
            type="textarea" 
            v-model="inviteForm.message" 
            placeholder="请输入邀请消息"
            :rows="4"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="inviteDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitInvite" :loading="inviteLoading">
            发送邀请
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search } from '@element-plus/icons-vue';

// 定义类型
type TabType = 'members' | 'results';
type FileStatus = '待检测' | '处理中' | '已完成';
type MemberRole = '成员' | '管理员';
type MemberStatus = '已上传文件' | '未上传文件' | '管理员' | '申请加入组织中';


// TODO
interface FileItem {
  id: number;
  fileName: string;
  fileType: string;
  uploader: string;
  uploadTime: string;
  fileSize: string;
  status: FileStatus;
  score?: string;
}

interface MemberItem {
  id: number;
  username: string;
  role: MemberRole;
  status: MemberStatus;
  email: string;
}

const activeTab = ref<TabType>('members');
const searchKeyword = ref<string>('');
const memberSearchKeyword = ref<string>('');
const tableLoading = ref<boolean>(false);
const memberTableLoading = ref<boolean>(false);
const currentPage = ref<number>(1);
const memberCurrentPage = ref<number>(1);
const isCurrentUserAdmin = ref<boolean>(true); // 假设当前用户是管理员

const inviteDialogVisible = ref<boolean>(false);
const inviteLoading = ref<boolean>(false);
const inviteForm = ref({
  email: '',
  message: '诚挚邀请您加入我们的组织。'
});


// {
//   "code": 0,
//   "message": "OK",
//   "data": {
//     "id": 4,
//     "username": "Leon",
//     "email": "22373400@buaa.edu.cn",
//     "phone": "19163323916",
//     "password": null,
//     "passwordHash": "$2a$10$VmiYHZwW6tEQkr8h1K6Z2uBTsumoJae8OyQbm.ZpgdUV6bdrraOXa",
//     "avatarPath": null,
//     "createdAt": "2025-04-04T15:39:01",
//     "updatedAt": null,
//     "lastLoginAt": null,
//     "userRole": null
//   }
// }

// 成员数据
const membersData = ref<MemberItem[]>([
  {
      "id": 6,
      "userId": 4,
      "groupId": 7,
      "createdAt": "2025-04-08T21:46:51",
      "status": "in",
      "role": "leader"
  },
]);

const resultsData = ref<FileItem[]>([
  {
    id: 6,
    fileName: '科研论文A.pdf',
    fileType: 'PDF文档',
    uploader: '张明',
    uploadTime: '2025-03-15 14:30:22',
    fileSize: '2.7 MB',
    status: '已完成',
    score: '92%',
  },
  {
    id: 7,
    fileName: '会议摘要B.docx',
    fileType: 'Word文档',
    uploader: '李华',
    uploadTime: '2025-03-14 09:45:10',
    fileSize: '1.5 MB',
    status: '已完成',
    score: '12%',
  },
  {
    id: 8,
    fileName: '研究数据图像C.png',
    fileType: '图像',
    uploader: '王芳',
    uploadTime: '2025-03-12 16:20:35',
    fileSize: '3.8 MB',
    status: '处理中',
    score: '-',
  }
]);

// 过滤后的成员数据
const filteredMemberData = computed((): MemberItem[] => {
  return filterMemberData(membersData.value);
});

// 排序后的成员数据（管理员 > 申请加入的 > 其他）
const sortedMemberData = computed((): MemberItem[] => {
  return [...filteredMemberData.value].sort((a, b) => {
    // 首先按角色排序（管理员优先）
    if (a.role === '管理员' && b.role !== '管理员') return -1;
    if (a.role !== '管理员' && b.role === '管理员') return 1;
    
    // 然后按状态排序（申请加入的排在非管理员的前面）
    if (a.role !== '管理员' && b.role !== '管理员') {
      if (a.status === '申请加入组织中' && b.status !== '申请加入组织中') return -1;
      if (a.status !== '申请加入组织中' && b.status === '申请加入组织中') return 1;
    }
    
    return new Date(b.joinTime).getTime() - new Date(a.joinTime).getTime();
  });
});

const filteredResultsData = computed((): FileItem[] => {
  return filterResultData(resultsData.value);
});

// 数据总数
const membersDataTotal = computed((): number => filteredMemberData.value.length);
const resultsDataTotal = computed((): number => filteredResultsData.value.length);

async function fetchMembers(id:) { // 根据 组织的 id 获取所有 members
  memberTableLoading.value = true;
  try {
    // 替换成您的实际 API 端点
    const response = await axios.get('/api/group/members');
    if (response.data.code === 0) {
      // 假设您的 API 返回的数据结构包含 data 数组
      const backendMembers = response.data.data || [];
      
      // 将后端数据映射为前端所需的格式
      membersData.value = backendMembers.map(item => ({
        id: item.id,
        username: item.username || `用户${item.userId}`,
        email: item.email || `user${item.userId}@example.com`,
        role: item.role === 'leader' ? '管理员' : '成员',
        status: mapMemberStatus(item.status),
        joinTime: item.createdAt
      }));
    } else {
      ElMessage.error(response.data.message || '获取成员数据失败');
    }
  } catch (error) {
    console.error('获取成员数据失败:', error);
    ElMessage.error('获取成员数据失败，请检查网络连接');
  } finally {
    memberTableLoading.value = false;
  }
}

onMounted((): void => {
  console.log('组织管理界面已加载');
  fetch
});

// 成员数据过滤函数
function filterMemberData(data: MemberItem[]): MemberItem[] {
  let filtered = [...data];
  
  // 关键词搜索
  if (memberSearchKeyword.value) {
    const keyword = memberSearchKeyword.value.toLowerCase();
    filtered = filtered.filter(item => 
      item.username.toLowerCase().includes(keyword) ||
      item.email.toLowerCase().includes(keyword)
    );
  }
  
  return filtered;
}

// 结果数据过滤函数
function filterResultData(data: FileItem[]): FileItem[] {
  let filtered = [...data];
  
  // 关键词搜索
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase();
    filtered = filtered.filter(item => 
      item.fileName.toLowerCase().includes(keyword) ||
      item.uploader.toLowerCase().includes(keyword)
    );
  }
  
  return filtered;
}

// 处理标签页切换
function handleTabChange(): void {
  // 切换标签页时清空搜索条件
  if (activeTab.value === 'members') {
    searchKeyword.value = '';
    currentPage.value = 1;
  } else {
    memberSearchKeyword.value = '';
    memberCurrentPage.value = 1;
  }
}

// 处理成员页码变化
function handleMemberCurrentChange(page: number): void {
  memberCurrentPage.value = page;
}

// 处理结果页码变化
function handleCurrentChange(page: number): void {
  currentPage.value = page;
}

// 获取成员状态类型（用于标签颜色）
function getMemberStatusType(status: MemberStatus): 'success' | 'warning' | 'info' | 'danger' {
  switch(status) {
    case '已上传文件': return 'success';
    case '未上传文件': return 'warning';
    case '管理员': return 'danger';
    case '申请加入组织中': return 'info';
    default: return 'info';
  }
}

// 获取文件状态类型（用于标签颜色）
function getStatusType(status: string): 'success' | 'warning' | 'info' | '' {
  switch(status) {
    case '已完成': return 'success';
    case '处理中': return 'warning';
    case '待检测': return 'info';
    default: return '';
  }
}

// 查看检测详情
function handleViewDetails(row: FileItem): void {
  ElMessage.info(`查看检测详情: ${row.fileName}`);
  // 实际项目中可以跳转到详情页面
  // router.push(`/result/detail/${row.id}`);
}

// 批准加入
function handleApproveJoin(member: MemberItem): void {
  // mock here 模拟！！！
  ElMessageBox.confirm(
    `确定批准"${member.username}"的加入申请吗？`,
    '批准申请',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    }
  ).then(() => {
    // 模拟API调用
    memberTableLoading.value = true;
    setTimeout(() => {
      const index = membersData.value.findIndex(item => item.id === member.id);
      if (index !== -1) {
        membersData.value[index].status = '未上传文件';
        ElMessage.success(`已批准"${member.username}"的加入申请`);
      }
      memberTableLoading.value = false;
    }, 1000);
  }).catch(() => {
    ElMessage.info('已取消操作');
  });
}

// 拒绝申请
function handleRejectJoin(member: MemberItem): void {
  // 模拟 here
  ElMessageBox.confirm(
    `确定拒绝"${member.username}"的加入申请吗？`,
    '拒绝申请',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    // 模拟API调用
    memberTableLoading.value = true;
    setTimeout(() => {
      membersData.value = membersData.value.filter(item => item.id !== member.id);
      ElMessage.success(`已拒绝"${member.username}"的加入申请`);
      memberTableLoading.value = false;
    }, 1000);
  }).catch(() => {
    ElMessage.info('已取消操作');
  });
}

// 邀请用户
function handleInviteUser(): void {
  inviteDialogVisible.value = true;
  inviteForm.value = {
    email: '',
    message: '诚挚邀请您加入我们的组织。'
  };
}

// 提交邀请
function submitInvite(): void {
  if (!inviteForm.value.email) {
    ElMessage.warning('请输入用户id');
    return;
  }
    
  inviteLoading.value = true;
  
  // 模拟API调用
  setTimeout(() => {
    ElMessage.success(`邀请已发送至 ${inviteForm.value.email}`);
    inviteLoading.value = false;
    inviteDialogVisible.value = false;
  }, 1500);
}
</script>

<style lang="scss" scoped>
.organization-manage-container {
  padding: 20px;
  
  .manage-card {
    margin-bottom: 20px;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      h2 {
        margin: 0;
        font-size: 20px;
        font-weight: 600;
      }
    }
  }
  
  .filter-container {
    display: flex;
    justify-content: space-between;
    margin-bottom: 16px;
    
    .filter-left {
      display: flex;
      gap: 15px;
      
      .search-input {
        width: 220px;
      }
    }
    
    .filter-right {
      display: flex;
      gap: 10px;
    }
  }
  
  .pagination-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }
}
</style>