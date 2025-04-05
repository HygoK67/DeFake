<template>
    <div class="organization-manage-container">
      <el-card class="manage-card">
        <template #header>
          <div class="card-header">
            <h2>组织者管理界面</h2>
          </div>
        </template>
  
        <el-tabs v-model="activeTab" @tab-click="handleTabChange">
          <el-tab-pane label="待检测文件" name="pending">
            <!-- 搜索和筛选区域 -->
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
  
              <div class="filter-right">
                <el-button
                  type="primary"
                  :disabled="selectedItems.length === 0"
                  :loading="detectionLoading"
                  @click="handleBatchDetection"
                >
                  批量检测
                </el-button>
              </div>
            </div>
  
            <!-- 表格区域 -->
            <el-table
              v-loading="tableLoading"
              :data="filteredPendingData"
              border
              stripe
              style="width: 100%"
              @selection-change="handleSelectionChange"
            >
              <el-table-column type="selection" width="55" />
              <el-table-column prop="fileName" label="文件名" min-width="180" sortable>
                <template #default="scope">
                  <a href="javascript:void(0);">{{ scope.row.fileName }}</a>
                </template>
              </el-table-column>
              <el-table-column prop="fileType" label="文件类型" width="120" />
              <el-table-column prop="uploader" label="上传者" width="100" />
              <el-table-column prop="uploadTime" label="上传时间" width="170" />
              <el-table-column prop="fileSize" label="文件大小" width="100" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-tag :type="scope.row.status === '待检测' ? 'info' : 'warning'">
                    {{ scope.row.status }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template #default="scope">
                  <el-button size="small" type="primary" @click="handleStartDetection([scope.row])">
                    检测
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
  
            <!-- 分页器 -->
            <div class="pagination-container">
              <el-pagination
                background
                layout="total, prev, pager, next"
                :total="pendingDataTotal"
                :current-page="currentPage"
                @current-change="handleCurrentChange"
              />
            </div>
          </el-tab-pane>
  
          <el-tab-pane label="检测结果" name="results">
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
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, computed, onMounted } from 'vue';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { Search } from '@element-plus/icons-vue';
  
  // 定义类型
  type TabType = 'pending' | 'results';
  type FileStatus = '待检测' | '处理中' | '已完成';
  
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
  
  // 状态管理
  const activeTab = ref<TabType>('pending');
  const searchKeyword = ref<string>('');
  const selectedItems = ref<FileItem[]>([]);
  const tableLoading = ref<boolean>(false);
  const detectionLoading = ref<boolean>(false);
  const currentPage = ref<number>(1);
  
  // 待检测文件数据
  const pendingData = ref<FileItem[]>([
    {
      id: 1,
      fileName: '0000001-小明.pdf',
      fileType: 'PDF',
      uploader: '张明',
      uploadTime: '2025-03-20 10:30:22',
      fileSize: '2.5 MB',
      status: '待检测',
    },
    {
      id: 2,
      fileName: '0000002-小王.pdf',
      fileType: 'PDF',
      uploader: '李华',
      uploadTime: '2025-03-19 14:45:10',
      fileSize: '1.8 MB',
      status: '待检测',
    },

]);
  
  // 已检测文件数据
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
  
  // 过滤后的待检测数据
  const filteredPendingData = computed((): FileItem[] => {
    return filterData(pendingData.value);
  });
  
  // 过滤后的检测结果数据
  const filteredResultsData = computed((): FileItem[] => {
    return filterData(resultsData.value);
  });
  
  // 数据总数
  const pendingDataTotal = computed((): number => filteredPendingData.value.length);
  const resultsDataTotal = computed((): number => filteredResultsData.value.length);
  
  // 初始化
  onMounted((): void => {
    console.log('组织者管理界面已加载');
  });
  
  // 数据过滤函数
  function filterData(data: FileItem[]): FileItem[] {
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
  
  // 处理表格选择变化
  function handleSelectionChange(selection: FileItem[]): void {
    selectedItems.value = selection;
  }
  
  // 处理页码变化
  function handleCurrentChange(page: number): void {
    currentPage.value = page;
  }
  
  // 处理标签页切换
  function handleTabChange(): void {
    // 切换标签页时清空选择
    selectedItems.value = [];
    currentPage.value = 1;
    searchKeyword.value = '';
  }
  
  // 获取状态类型（用于标签颜色）
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
  
  // 单个文件开始检测
  function handleStartDetection(items: FileItem[]): void {
    if (!items || items.length === 0) return;
    
    const itemIds = items.map(item => item.id);
    
    ElMessageBox.confirm(
      `确定要开始检测${items.length > 1 ? '选中的' + items.length + '个' : ''}文件吗？`,
      '检测确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(() => {
      startDetectionProcess(itemIds);
    }).catch(() => {
      ElMessage.info('已取消检测');
    });
  }
  
  // 批量检测
  function handleBatchDetection(): void {
    if (selectedItems.value.length === 0) {
      ElMessage.warning('请选择要检测的文件');
      return;
    }
    
    const itemIds = selectedItems.value.map(item => item.id);
    
    ElMessageBox.confirm(
      `确定要检测选中的 ${selectedItems.value.length} 个文件吗？`,
      '批量检测确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(() => {
      startDetectionProcess(itemIds);
    }).catch(() => {
      ElMessage.info('已取消检测');
    });
  }
  
  // 执行检测过程
  function startDetectionProcess(itemIds: number[]): void {
    detectionLoading.value = true;
    tableLoading.value = true;
    
    // 模拟API调用
    setTimeout(() => {
      // 找出要检测的项目
      const itemsToDetect = pendingData.value.filter(item => itemIds.includes(item.id));
      
      // 从待检测列表移除
      pendingData.value = pendingData.value.filter(item => !itemIds.includes(item.id));
      
      // 添加到检测结果列表
      itemsToDetect.forEach(item => {
        const resultItem: FileItem = {
          ...item,
          status: Math.random() > 0.3 ? '已完成' : '处理中',
          score: Math.random() > 0.3 ? `${Math.floor(Math.random() * 100)}%` : '-'
        };
        resultsData.value.unshift(resultItem);
      });
      
      // 清空选择
      selectedItems.value = [];
      
      ElMessage.success(`已成功提交 ${itemsToDetect.length} 个文件进行检测`);
      
      detectionLoading.value = false;
      tableLoading.value = false;
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