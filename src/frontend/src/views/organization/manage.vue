<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search } from '@element-plus/icons-vue';
import { FileItem, MemberItem } from '@/types/organization'; // 假设你有一个类型定义文件
import { getAllGroupMember, inviteMember, kickMember } from '@/api/group';
import { useRoute, useRouter } from 'vue-router';

type TabType = 'members' | 'results';
const router = useRouter();
const activeTab = ref<TabType>('members');
const searchKeyword = ref<string>('');
const memberSearchKeyword = ref<string>('');
const tableLoading = ref<boolean>(false);
const memberTableLoading = ref<boolean>(false);
const currentPage = ref<number>(1);
const memberCurrentPage = ref<number>(1);
const isCurrentUserAdmin = ref<boolean>(true); // 假设当前用户是管理员
const pageSize = 10; // 每页显示的条数
const membersDataTotal = computed((): number => filteredMemberData.value.length); // 数据总数
const resultsDataTotal = computed((): number => filteredResultsData.value.length);
const roleFilter = ref<string>('all');
const fileFilter = ref<string>('all');
const statusFiler = ref<string>('all');
const idFilter = ref<number>(-1);
const mannulInput = ref<boolean>(true);
const groupId = ref<number>(1);; // 假设组织ID为1

const inviteDialogVisible = ref<boolean>(false);
const inviteLoading = ref<boolean>(false);
const inviteForm = ref({
  email: '',
  message: '诚挚邀请您加入我们的组织。'
});


// 成员数据
const membersData = ref<MemberItem[]>([]);

const resultsData = ref<FileItem[]>([]);

function handleRoleFilter(command: string): void {
  roleFilter.value = command;
}

// 成员关键词搜索
function filterMemberData(data: MemberItem[]): MemberItem[] {
  let filtered = [...data];
  if (memberSearchKeyword.value) {
    const keyword = memberSearchKeyword.value;
    filtered = filtered.filter(item =>
      String(item.username).includes(keyword) ||
      item.email.includes(keyword)
    );
  }
  if (roleFilter.value !== 'all') {
    filtered = filtered.filter(item => item.role === roleFilter.value);
  }
  if (idFilter.value !== -1) {
    filtered = filtered.filter(item => item.userId === idFilter.value);
  }
  return filtered;
}

// 过滤后的成员数据
const filteredMemberData = computed((): MemberItem[] => {
  return filterMemberData(membersData.value);
});

// 排序后的成员数据
const sortedMemberData = computed((): MemberItem[] => {
  return [...filteredMemberData.value].sort((a, b) => {
    // 首先按角色排序（管理员优先）
    if (a.role !== 'leader' && b.role === 'leader') return 1;
    return -1;
  });
});

// 分页后的成员数据
const paginatedMemberData = computed(() => {
  const start = (memberCurrentPage.value - 1) * pageSize;
  const end = start + pageSize;
  return sortedMemberData.value.slice(start, end);
});

function handleFileFilter(command: string): void {
  fileFilter.value = command;
}

function handleStatusFilter(command: string): void {
  statusFiler.value = command;
}

// 结果关键词搜索
function filterResultData(data: FileItem[]): FileItem[] {
  let filtered = [...data];
  if (searchKeyword.value) {
    const keyword = searchKeyword.value;
    filtered = filtered.filter(item =>
      item.fileName.toLowerCase().includes(keyword) ||
      item.uploader.toLowerCase().includes(keyword)
    );
  }
  if (fileFilter.value !== 'all') {
    filtered = filtered.filter(item => item.fileType === fileFilter.value);
  }
  if (statusFiler.value !== 'all') {
    filtered = filtered.filter(item => item.status === statusFiler.value);
  }
  return filtered;
}

// 过滤后的结果数据
const filteredResultsData = computed((): FileItem[] => {
  return filterResultData(resultsData.value);
});

// 分页后的结果数据
const paginatedResultsData = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  const end = start + pageSize;
  return filteredResultsData.value.slice(start, end);
});

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

// 获取文件状态类型（用于标签颜色）
function getStatusType(status: string): 'success' | 'warning' | 'info' {
  switch (status) {
    case '已完成': return 'success';
    case '处理中': return 'warning';
    case '待检测': return 'info';
    default: return 'info';
  }
}

// 计算文件大小
function parseFileSize(size: string): number {
  const units = { 'B': 1, 'KB': 1024, 'MB': 1024 ** 2, 'GB': 1024 ** 3 };
  const match = size.match(/^([\d.]+)\s*(B|KB|MB|GB)$/i);
  if (match) {
    const value = parseFloat(match[1]);
    const unit = match[2].toUpperCase();
    return value * (units[unit] || 1);
  }
  return 0;
}

// 查看检测详情
function handleViewDetails(row: FileItem): void {
  router.push(`/result/detail/${row.id}`);
};

function handleUploaderClick(row: FileItem): void {
  activeTab.value = 'members';
  mannulInput.value = false;
  memberSearchKeyword.value = row.uploader;
  idFilter.value = row.uploaderId;
}

// 私信
function privateMsg(member: MemberItem): void {

}

// 踢出
async function removeFromGroup(member: MemberItem): Promise<void> {
  // //todo 联调
  // try {
  //   const res = await kickMember({ userIdRec: String(member.userId), groupId: String(groupId.value) });
  //   if (res.code === 0) {
  //     ElMessage.success(`已踢出用户${member.username}`);
  //   } else {
  //     ElMessage.error(res.message);
  //   }
  // } catch (error) {
  //   ElMessage.error('踢出成员失败');
  // }
  ElMessage.error('踢出成员失败');
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
async function submitInvite(): Promise<void> {
  if (!inviteForm.value.email) {
    ElMessage.warning('请输入用户邮箱');
    return;
  }
  inviteLoading.value = true;
  // // todo: 联调
  // try {
  //   const res = await inviteMember({
  //     groupId: String(groupId.value),
  //     email: inviteForm.value.email,
  //     title: '邀请您加入组织',
  //     content: inviteForm.value.message
  //   })
  //   if (res.code === 0) {
  //     ElMessage.success('邀请已发送');
  //   } else {
  //     ElMessage.error(res.message);
  //   }
  //   inviteLoading.value = false;
  // } catch (error) {
  //   ElMessage.error("邀请用户失败")
  // }

  // 模拟API调用
  setTimeout(() => {
    ElMessage.success(`邀请已发送至 ${inviteForm.value.email}`);
    inviteLoading.value = false;
    inviteDialogVisible.value = false;
  }, 1500);
}

watch(memberSearchKeyword, () => {
  if (mannulInput.value) {
    idFilter.value = -1;
  }
  mannulInput.value = true;
});

onMounted(async (): Promise<any> => {
  // todo：与后端联调 
  // try { 
  //   const res = await getAllGroupMember({ groupId });
  //   if (res.code === 0) {
  //     membersData.value = res.data;
  //   } else {
  //     ElMessage.error(res.message);
  //   }
  // } catch (error) {
  //   ElMessage.error('获取成员数据失败');
  // }

  groupId.value = Number(useRoute().params.id)

  membersData.value = [
    {
      userId: 4,
      username: "张明",
      role: "leader",
      email: "2323@qq.com"
    },
    {
      userId: 5,
      username: "李华",
      role: "leader",
      email: "2323@qq.com"
    },
    {
      userId: 6,
      username: "王芳",
      role: "leader",
      email: "2323@qq.com"
    },
    {
      userId: 4,
      username: "张明",
      role: "leader",
      email: "2323@qq.com"
    },
    {
      userId: 5,
      username: "李华",
      role: "member",
      email: "2323@qq.com"
    },
    {
      userId: 6,
      username: "王芳",
      role: "member",
      email: "2323@qq.com"
    },
    {
      userId: 4,
      username: "张明",
      role: "member",
      email: "2323@qq.com"
    },
    {
      userId: 5,
      username: "李华",
      role: "member",
      email: "2323@qq.com"
    },
    {
      userId: 6,
      username: "王芳",
      role: "member",
      email: "2323@qq.com"
    },
    {
      userId: 4,
      username: "张明",
      role: "member",
      email: "2323@qq.com"
    },
    {
      userId: 11,
      username: "张明",
      role: "leader",
      email: "2323@qq.com"
    },
  ]

  resultsData.value = [
    {
      id: 6,
      fileName: '科研论文A.pdf',
      fileType: 'PDF文档',
      uploader: '张明',
      uploaderId: 11,
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
      uploaderId: 5,
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
      uploaderId: 6,
      uploadTime: '2025-03-12 16:20:35',
      fileSize: '3.8 MB',
      status: '处理中',
      score: '-',
    }
  ]
});
</script>

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
              <el-input v-model="memberSearchKeyword" placeholder="搜索成员名称或邮箱" clearable class="search-input">
                <template #prefix>
                  <el-icon>
                    <search />
                  </el-icon>
                </template>
              </el-input>
            </div>

            <div class="filter-right">
              <el-button type="primary" @click="handleInviteUser">
                邀请用户
              </el-button>
            </div>
          </div>

          <!-- 表格区域 -->
          <el-table v-loading="memberTableLoading" :data="paginatedMemberData" border stripe
            style="width: 100%;margin: 0 auto;">
            <el-table-column prop="username" label="成员名称" min-width="200" />
            <el-table-column prop="email" label="邮箱" min-width="500" />
            <el-table-column label="角色" width="150">
              <template #header>
                <el-dropdown trigger="hover" @command="handleRoleFilter">
                  <span class="el-dropdown-link">
                    角色筛选
                  </span>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="all">全部</el-dropdown-item>
                      <el-dropdown-item command="leader">管理员</el-dropdown-item>
                      <el-dropdown-item command="member">普通成员</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </template>
              <template #default="scope">
                <el-tag :type="scope.row.role === 'leader' ? 'danger' : 'primary'" disable-transitions>
                  {{ scope.row.role === 'leader' ? '管理员' : '普通成员' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="250">
              <template #default="scope">
                <el-button v-if="isCurrentUserAdmin" size="small" type="success" @click="privateMsg(scope.row)">
                  私信
                </el-button>
                <el-button v-if="isCurrentUserAdmin" size="small" type="danger" @click="removeFromGroup(scope.row)">
                  踢出组织
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页器 -->
          <div class="pagination-container">
            <el-pagination background layout="total, prev, pager, next" :total="membersDataTotal"
              :current-page="memberCurrentPage" :page-size="pageSize" @current-change="handleMemberCurrentChange" />
          </div>
        </el-tab-pane>

        <!----------- 结果管理栏目 --------------->
        <el-tab-pane label="结果管理" name="results">
          <!-- 搜索区域 -->
          <div class="filter-container">
            <div class="filter-left">
              <el-input v-model="searchKeyword" placeholder="搜索文件名或用户名" clearable class="search-input">
                <template #prefix>
                  <el-icon>
                    <search />
                  </el-icon>
                </template>
              </el-input>
            </div>
          </div>

          <!-- 表格区域 -->
          <el-table v-loading="tableLoading" :data="paginatedResultsData" border stripe style="width: 100%">
            <el-table-column prop="fileName" label="文件名" min-width="160">
              <template #default="scope">
                <a href="javascript:void(0);" @click="handleViewDetails(scope.row)">{{ scope.row.fileName }}</a>
              </template>
            </el-table-column>
            <el-table-column prop="fileType" label="文件类型" width="140">
              <template #header>
                <el-dropdown trigger="hover" @command="handleFileFilter">
                  <span class="el-dropdown-link">
                    文件类型
                  </span>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="all">全部</el-dropdown-item>
                      <el-dropdown-item command="PDF文档">PDF文档</el-dropdown-item>
                      <el-dropdown-item command="Word文档">Word文档</el-dropdown-item>
                      <el-dropdown-item command="图像">图像</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </template>
            </el-table-column>
            <el-table-column prop="uploader" label="上传者" width="120">
              <template #default="scope">
                <span @click="handleUploaderClick(scope.row)" style="cursor: pointer;">
                  {{ scope.row.uploader }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="uploadTime" label="上传时间" width="220" sortable
              :sort-method="(a, b) => new Date(a.uploadTime).getTime() - new Date(b.uploadTime).getTime()" />
            <el-table-column prop="fileSize" label="文件大小" width="120" sortable
              :sort-method="(a, b) => parseFileSize(a.fileSize) - parseFileSize(b.fileSize)" />
            <el-table-column prop="status" label="状态" width="120">
              <template #header>
                <el-dropdown trigger="hover" @command="handleStatusFilter">
                  <span class="el-dropdown-link">
                    状态
                  </span>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="all">全部</el-dropdown-item>
                      <el-dropdown-item command="已完成">已完成</el-dropdown-item>
                      <el-dropdown-item command="处理中">处理中</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </template>
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="score" label="检测结果" width="120" sortable
              :sort-method="(a, b) => parseFloat(a.score) - parseFloat(b.score)" />
          </el-table>

          <!-- 分页器 -->
          <div class="pagination-container">
            <el-pagination background layout="total, prev, pager, next" :total="resultsDataTotal"
              :current-page="currentPage" @current-change="handleCurrentChange" />
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
          <el-input type="textarea" v-model="inviteForm.message" placeholder="请输入邀请消息" :rows="4"></el-input>
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

<style lang="scss" scoped>
.organization-manage-container {
  display: flex;
  justify-content: center;
  padding: 20px;

  .manage-card {
    margin-bottom: 20px;
    margin-top: 0;
    width: 85%;

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

    .el-dropdown-link {
      margin-top: 5px;
      cursor: pointer;
      color: #999999;
      font-size: 14px;
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
    width: 80%;
    margin-left: auto;
    margin-right: auto;
  }
}
</style>