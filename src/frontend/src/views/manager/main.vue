<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search } from '@element-plus/icons-vue';
import { 
  getAllActivityLog, 
  deleteNotification,
  getNotificationInfo,
  getAllNotificationInfo,
  getAllUserInfo,
  UserActivityLogItem,
  NotificationInfoItem,
  UserInfoItem
} from '@/api/manager';

defineOptions({
  name: "ManagerMain"
});

// Tab management
type TabType = 'users' | 'logs' | 'notifications';
const activeTab = ref<TabType>('users');

// Search and filters
const userSearchKeyword = ref<string>('');
const logSearchKeyword = ref<string>('');
const notificationSearchKeyword = ref<string>('');
const roleFilter = ref<string>('all');

// Loading states
const usersLoading = ref<boolean>(false);
const logsLoading = ref<boolean>(false);
const notificationsLoading = ref<boolean>(false);

// Pagination
const userCurrentPage = ref<number>(1);
const logCurrentPage = ref<number>(1);
const notificationCurrentPage = ref<number>(1);
const pageSize = ref<number>(10);

// Data storage
const usersData = ref<UserInfoItem[]>([]);
const logsData = ref<UserActivityLogItem[]>([]);
const notificationsData = ref<NotificationInfoItem[]>([]);

// Notification detail dialog
const notificationDetailVisible = ref<boolean>(false);
const currentNotification = ref<NotificationInfoItem | null>(null);

// User detail dialog
const userDetailVisible = ref<boolean>(false);
const currentUser = ref<UserInfoItem | null>(null);

// Computed properties for filtered data
const filteredUsersData = computed(() => {
  let filtered = [...usersData.value];
  
  if (userSearchKeyword.value) {
    const keyword = userSearchKeyword.value.toLowerCase();
    filtered = filtered.filter(user => 
      user.username.toLowerCase().includes(keyword) || 
      user.email.toLowerCase().includes(keyword) ||
      user.phone.includes(keyword)
    );
  }
  
  if (roleFilter.value !== 'all') {
    filtered = filtered.filter(user => user.role === roleFilter.value);
  }
  
  return filtered;
});

const filteredLogsData = computed(() => {
  let filtered = [...logsData.value];
  
  if (logSearchKeyword.value) {
    const keyword = logSearchKeyword.value.toLowerCase();
    filtered = filtered.filter(log => 
      log.operationType.toLowerCase().includes(keyword) || 
      log.userEmail.toLowerCase().includes(keyword) ||
      log.userName.toLowerCase().includes(keyword) ||
      log.ipAddress.includes(keyword)
    );
  }
  
  return filtered;
});

const filteredNotificationsData = computed(() => {
  let filtered = [...notificationsData.value];
  
  if (notificationSearchKeyword.value) {
    const keyword = notificationSearchKeyword.value.toLowerCase();
    filtered = filtered.filter(notification => 
      notification.title.toLowerCase().includes(keyword) || 
      notification.content.toLowerCase().includes(keyword)
    );
  }
  
  return filtered;
});

// Paginated data
const paginatedUsersData = computed(() => {
  const start = (userCurrentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredUsersData.value.slice(start, end);
});

const paginatedLogsData = computed(() => {
  const start = (logCurrentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredLogsData.value.slice(start, end);
});

const paginatedNotificationsData = computed(() => {
  const start = (notificationCurrentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredNotificationsData.value.slice(start, end);
});

// Fetch data functions
const fetchAllUsers = async () => {
  usersLoading.value = true;
  try {
    const response = await getAllUserInfo();
    if (response.code === 0) {
      usersData.value = response.data;
    } else {
      ElMessage.error(response.message || '获取用户数据失败');
    }
  } catch (error) {
    console.error('获取用户数据失败:', error);
    ElMessage.error('获取用户数据失败，请检查网络连接');
  } finally {
    usersLoading.value = false;
  }
};

const fetchAllLogs = async () => {
  logsLoading.value = true;
  try {
    const response = await getAllActivityLog();
    if (response.code === 0) {
      logsData.value = response.data;
    } else {
      ElMessage.error(response.message || '获取日志数据失败');
    }
  } catch (error) {
    console.error('获取日志数据失败:', error);
    ElMessage.error('获取日志数据失败，请检查网络连接');
  } finally {
    logsLoading.value = false;
  }
};

const fetchAllNotifications = async () => {
  notificationsLoading.value = true;
  try {
    const response = await getAllNotificationInfo();
    if (response.code === 0) {
      notificationsData.value = response.data;
    } else {
      ElMessage.error(response.message || '获取通知数据失败');
    }
  } catch (error) {
    console.error('获取通知数据失败:', error);
    ElMessage.error('获取通知数据失败，请检查网络连接');
  } finally {
    notificationsLoading.value = false;
  }
};

// Action handlers
const handleTabChange = () => {
  // Reset search and page when changing tabs
  userSearchKeyword.value = '';
  logSearchKeyword.value = '';
  notificationSearchKeyword.value = '';
  userCurrentPage.value = 1;
  logCurrentPage.value = 1;
  notificationCurrentPage.value = 1;
};

const handleNotificationDetail = async (notificationId: number) => {
  try {
    const response = await getNotificationInfo({ notificationId: String(notificationId) });
    if (response.code === 0) {
      currentNotification.value = response.data;
      notificationDetailVisible.value = true;
    } else {
      ElMessage.error(response.message || '获取通知详情失败');
    }
  } catch (error) {
    console.error('获取通知详情失败:', error);
    ElMessage.error('获取通知详情失败，请检查网络连接');
  }
};

const handleDeleteNotification = (notificationId: number) => {
  ElMessageBox.confirm(
    '确定要删除这条通知吗？此操作不可逆。',
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const response = await deleteNotification({ notificationId: String(notificationId) });
      if (response.code === 0) {
        ElMessage.success('通知删除成功');
        // Remove from local data
        notificationsData.value = notificationsData.value.filter(n => n.id !== notificationId);
      } else {
        ElMessage.error(response.message || '删除通知失败');
      }
    } catch (error) {
      console.error('删除通知失败:', error);
      ElMessage.error('删除通知失败，请检查网络连接');
    }
  }).catch(() => {
    // User canceled
  });
};

const handleUserDetail = (user: UserInfoItem) => {
  currentUser.value = user;
  userDetailVisible.value = true;
};

const formatDateTime = (dateString: string) => {
  if (!dateString) return '未知';
  const date = new Date(dateString);
  return date.toLocaleString();
};

const formatOperationDetail = (detail: string | null, operationDetail: any) => {
  if (!detail) return '无详细信息';
  
  try {
    if (operationDetail) {
      return JSON.stringify(operationDetail, null, 2);
    }
    return JSON.stringify(JSON.parse(detail), null, 2);
  } catch (e) {
    return detail;
  }
};

// Role filter options
const handleRoleFilter = (command: string) => {
  roleFilter.value = command;
};

// Load data on mount
onMounted(() => {
  fetchAllUsers();
  fetchAllLogs();
  fetchAllNotifications();
});
</script>

<template>
  <div class="manager-container">
    <el-card class="manage-card">
      <template #header>
        <div class="card-header">
          <h2>平台管理</h2>
        </div>
      </template>

      <el-tabs v-model="activeTab" @tab-click="handleTabChange">
        <!-- 用户管理标签页 -->
        <el-tab-pane label="用户管理" name="users">
          <div class="filter-container">
            <div class="filter-left">
              <el-input v-model="userSearchKeyword" placeholder="搜索用户名/邮箱/电话" clearable class="search-input">
                <template #prefix>
                  <el-icon><search /></el-icon>
                </template>
              </el-input>
            </div>
          </div>

          <el-table v-loading="usersLoading" :data="paginatedUsersData" border stripe style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="username" label="用户名" min-width="120" />
            <el-table-column prop="email" label="邮箱" min-width="200" />
            <el-table-column prop="phone" label="电话" min-width="150" />
            <el-table-column label="角色" width="150">
              <template #header>
                <el-dropdown trigger="hover" @command="handleRoleFilter">
                  <span class="el-dropdown-link">
                    角色筛选
                  </span>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="all">全部</el-dropdown-item>
                      <el-dropdown-item command="admin">管理员</el-dropdown-item>
                      <el-dropdown-item command="user">普通用户</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </template>
              <template #default="scope">
                <el-tag :type="scope.row.role === 'admin' ? 'danger' : 'primary'" disable-transitions>
                  {{ scope.row.role === 'admin' ? '管理员' : '普通用户' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="注册时间" min-width="180">
              <template #default="scope">
                {{ formatDateTime(scope.row.createdAt) }}
              </template>
            </el-table-column>
            <el-table-column prop="lastLoginAt" label="最后登录" min-width="180">
              <template #default="scope">
                {{ scope.row.lastLoginAt ? formatDateTime(scope.row.lastLoginAt) : '未登录' }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="scope">
                <el-button size="small" type="primary" @click="handleUserDetail(scope.row)">
                  详情
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination-container">
            <el-pagination
              background
              layout="total, sizes, prev, pager, next"
              :total="filteredUsersData.length"
              :current-page="userCurrentPage"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="pageSize"
              @current-change="userCurrentPage = $event"
              @size-change="pageSize = $event"
            />
          </div>
        </el-tab-pane>

        <!-- 日志管理标签页 -->
        <el-tab-pane label="操作日志" name="logs">
          <div class="filter-container">
            <div class="filter-left">
              <el-input v-model="logSearchKeyword" placeholder="搜索操作类型/用户名/邮箱/IP" clearable class="search-input">
                <template #prefix>
                  <el-icon><search /></el-icon>
                </template>
              </el-input>
            </div>
          </div>

          <el-table v-loading="logsLoading" :data="paginatedLogsData" border stripe style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="userName" label="用户名" min-width="120" />
            <el-table-column prop="userEmail" label="邮箱" min-width="180" />
            <el-table-column prop="operationType" label="操作类型" min-width="150" />
            <el-table-column prop="ipAddress" label="IP地址" min-width="150" />
            <el-table-column label="操作详情" min-width="200">
              <template #default="scope">
                <el-tooltip 
                  class="box-item" 
                  placement="top" 
                  :content="formatOperationDetail(scope.row.operationDetailJsonString, scope.row.operationDetail)"
                  :show-after="500"
                >
                  <el-button link type="primary">查看详情</el-button>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column label="操作时间" min-width="180">
              <template #default="scope">
                {{ formatDateTime(scope.row.timestamp || scope.row.createdAt) }}
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination-container">
            <el-pagination
              background
              layout="total, sizes, prev, pager, next"
              :total="filteredLogsData.length"
              :current-page="logCurrentPage"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="pageSize"
              @current-change="logCurrentPage = $event"
              @size-change="pageSize = $event"
            />
          </div>
        </el-tab-pane>

        <!-- 通知管理标签页 -->
        <el-tab-pane label="通知管理" name="notifications">
          <div class="filter-container">
            <div class="filter-left">
              <el-input v-model="notificationSearchKeyword" placeholder="搜索通知标题/内容" clearable class="search-input">
                <template #prefix>
                  <el-icon><search /></el-icon>
                </template>
              </el-input>
            </div>
          </div>

          <el-table v-loading="notificationsLoading" :data="paginatedNotificationsData" border stripe style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="title" label="标题" min-width="200" />
            <el-table-column prop="content" label="内容" min-width="250">
              <template #default="scope">
                <div class="notification-content-preview">
                  {{ scope.row.content.length > 50 ? scope.row.content.substring(0, 50) + '...' : scope.row.content }}
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="user_id" label="用户ID" min-width="100" />
            <el-table-column prop="sentAt" label="发送时间" min-width="180">
              <template #default="scope">
                {{ formatDateTime(scope.row.sentAt) }}
              </template>
            </el-table-column>
            <el-table-column label="阅读状态" width="120">
              <template #default="scope">
                <el-tag :type="scope.row.readAt ? 'success' : 'info'">
                  {{ scope.row.readAt ? '已读' : '未读' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="220" fixed="right">
              <template #default="scope">
                <el-button size="small" type="primary" @click="handleNotificationDetail(scope.row.id)">
                  查看
                </el-button>
                <el-button size="small" type="danger" @click="handleDeleteNotification(scope.row.id)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination-container">
            <el-pagination
              background
              layout="total, sizes, prev, pager, next"
              :total="filteredNotificationsData.length"
              :current-page="notificationCurrentPage"
              :page-sizes="[10, 20, 50, 100]"
              :page-size="pageSize"
              @current-change="notificationCurrentPage = $event"
              @size-change="pageSize = $event"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 通知详情对话框 -->
    <el-dialog v-model="notificationDetailVisible" title="通知详情" width="600px">
      <div v-if="currentNotification" class="notification-detail">
        <div class="detail-item">
          <span class="detail-label">ID:</span>
          <span>{{ currentNotification.id }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">模板ID:</span>
          <span>{{ currentNotification.template_id }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">用户ID:</span>
          <span>{{ currentNotification.user_id }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">标题:</span>
          <span>{{ currentNotification.title }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">内容:</span>
          <div class="content-area">{{ currentNotification.content }}</div>
        </div>
        <div class="detail-item">
          <span class="detail-label">发送时间:</span>
          <span>{{ formatDateTime(currentNotification.sentAt) }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">阅读时间:</span>
          <span>{{ currentNotification.readAt ? formatDateTime(currentNotification.readAt) : '未读' }}</span>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="notificationDetailVisible = false">关闭</el-button>
          <el-button type="danger" @click="handleDeleteNotification(currentNotification?.id); notificationDetailVisible = false">
            删除
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 用户详情对话框 -->
    <el-dialog v-model="userDetailVisible" title="用户详情" width="600px">
      <div v-if="currentUser" class="user-detail">
        <div class="user-header">
          <el-avatar :size="64" :src="currentUser.avatarPath || ''" />
          <div class="user-title">
            <h3>{{ currentUser.username }}</h3>
            <el-tag :type="currentUser.role === 'admin' ? 'danger' : 'primary'">
              {{ currentUser.role === 'admin' ? '管理员' : '普通用户' }}
            </el-tag>
          </div>
        </div>
        
        <el-divider />
        
        <div class="detail-grid">
          <div class="detail-item">
            <span class="detail-label">用户ID:</span>
            <span>{{ currentUser.id }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">邮箱:</span>
            <span>{{ currentUser.email }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">电话:</span>
            <span>{{ currentUser.phone }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">注册时间:</span>
            <span>{{ formatDateTime(currentUser.createdAt) }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">上次更新:</span>
            <span>{{ currentUser.updatedAt ? formatDateTime(currentUser.updatedAt) : '未更新' }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">最后登录:</span>
            <span>{{ currentUser.lastLoginAt ? formatDateTime(currentUser.lastLoginAt) : '未登录' }}</span>
          </div>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="userDetailVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.manager-container {
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
        width: 320px;
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

  .el-dropdown-link {
    cursor: pointer;
    color: #999999;
    font-size: 14px;
  }

  .notification-content-preview {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 250px;
  }

  .notification-detail, .user-detail {
    padding: 10px;

    .detail-item {
      margin-bottom: 15px;

      .detail-label {
        font-weight: bold;
        margin-right: 10px;
        color: #606266;
        display: block;
        margin-bottom: 5px;
      }

      .content-area {
        padding: 10px;
        background-color: #f8f8f8;
        border-radius: 4px;
        white-space: pre-wrap;
        max-height: 200px;
        overflow-y: auto;
      }
    }

    .user-header {
      display: flex;
      align-items: center;
      margin-bottom: 20px;

      .user-title {
        margin-left: 15px;

        h3 {
          margin: 0 0 5px 0;
        }
      }
    }

    .detail-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 15px;
    }
  }
}
</style>