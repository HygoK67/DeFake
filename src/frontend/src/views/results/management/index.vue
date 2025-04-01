<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";

defineOptions({
  name: "ResultManagement"
});

const router = useRouter();
const activeTab = ref("report");

// 表格数据
const tableData = ref([
  {
    id: 1,
    fileName: "科研论文A.pdf",
    fileType: "PDF文档",
    uploadTime: "2025-03-15 14:30:22",
    status: "已完成",
    score: "92%",
    uploader: "张明"
  },
  {
    id: 2,
    fileName: "会议摘要B.docx",
    fileType: "Word文档",
    uploadTime: "2025-03-14 09:45:10",
    status: "已完成",
    score: "12%",
    uploader: "李华"
  },
  {
    id: 3,
    fileName: "研究数据图像C.png",
    fileType: "图像",
    uploadTime: "2025-03-12 16:20:35",
    status: "处理中",
    score: "-",
    uploader: "王芳"
  }
]);

// 跳转到详情页
const viewDetail = (id: number) => {
  router.push(`/result/detail/${id}`);
};

// 导出报告
const exportReport = () => {
  // 导出报告逻辑实现
  console.log("导出报告");
};

// 图像排序
const sortImages = () => {
  // 图像排序逻辑实现
  console.log("图像排序");
};

// 结果反馈
const provideFeedback = () => {
  // 结果反馈逻辑实现
  console.log("结果反馈");
};

// 批量操作
const batchOperation = (operation: string) => {
  console.log(`批量操作: ${operation}`);
};
</script>

<template>
  <div class="result-management-container">
    <!-- 功能栏 -->
    <div class="function-bar">
      <div class="extra-functions">
        <!-- <el-dropdown @command="batchOperation">
          <el-button type="primary">
            更多操作
            <el-icon class="el-icon--right"><arrow-down /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="batchExport">批量导出</el-dropdown-item>
              <el-dropdown-item command="batchDelete">批量删除</el-dropdown-item>
              <el-dropdown-item command="batchShare">批量分享</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown> -->
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content-area">
      <el-table :data="tableData" border style="width: 100%" row-key="id">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="fileName" label="文件名" min-width="180" />
        <el-table-column prop="fileType" label="文件类型" width="120" />
        <el-table-column prop="uploader" label="上传人" width="120" />
        <el-table-column prop="uploadTime" label="上传时间" width="160" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === '已完成' ? 'success' : 'warning'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="检测结果" width="120" />
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button size="small" @click="viewDetail(scope.row.id)">查看详情</el-button>
            <el-button 
              size="small" 
              type="primary" 
              :disabled="scope.row.status !== '已完成'"
              @click="exportReport"
            >导出报告</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="tableData.length"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="10"
        />
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.result-management-container {
  padding: 20px;
  height: 100%;
  
  .function-bar {
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    gap: 15px;
    
    .extra-functions {
      margin-left: auto;
    }
  }
  
  .content-area {
    background-color: #fff;
    border-radius: 4px;
    padding: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  }
  
  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}
</style>