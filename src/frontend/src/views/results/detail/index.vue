<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

defineOptions({
  name: "ResultDetail"
});

const route = useRoute();
const router = useRouter();
const activeTab = ref("report");

const resultId = ref(route.params.id);
const reportData = ref({
  id: resultId.value,
  fileName: "科研论文A.pdf",
  fileType: "PDF文档",
  uploadTime: "2025-03-15 14:30:22",
  status: "已完成",
  score: 92,
  totalPages: 15,
  suspiciousPages: 3,
  author: "张三",
  institution: "高等研究院",
  analysisResults: [
    { page: 2, similarity: 45, source: "期刊文献A" },
    { page: 5, similarity: 68, source: "国际会议论文B" },
    { page: 12, similarity: 38, source: "学位论文C" }
  ]
});

// 返回列表页
const goBack = () => {
  router.go(-1);
};

// 导出报告
const exportReport = () => {
  console.log("导出报告");
};

// 图像排序
const sortImages = () => {
  console.log("图像排序");
};

// 结果反馈
const provideFeedback = () => {
  console.log("结果反馈");
};
</script>

<template>
  <div class="result-detail-container">
    <!-- 头部导航 -->
    <div class="detail-header">
      <el-page-header title="返回列表" @back="goBack">
        <template #content>
          <span class="header-title">检测报告详情</span>
        </template>
      </el-page-header>

      <div class="header-actions">
        <el-button type="primary" @click="exportReport">导出报告</el-button>
        <el-button type="primary" @click="sortImages">图像排序</el-button>
        <el-button type="primary" @click="provideFeedback">结果反馈</el-button>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="detail-content">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="检测报告" name="report">
          <div class="report-summary">
            <div class="summary-card">
              <div class="book-icon">
                <img src="" alt="Report" />
              </div>
              <div class="summary-info">
                <h3>{{ reportData.fileName }}</h3>
                <div class="info-row">
                  <span class="info-label">文件类型:</span>
                  <span>{{ reportData.fileType }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">作者:</span>
                  <span>{{ reportData.author }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">机构:</span>
                  <span>{{ reportData.institution }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">上传时间:</span>
                  <span>{{ reportData.uploadTime }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">检测状态:</span>
                  <el-tag :type="reportData.status === '已完成' ? 'success' : 'warning'
                    ">
                    {{ reportData.status }}
                  </el-tag>
                </div>
                <div class="info-row">
                  <span class="info-label">检测分数:</span>
                  <span class="score">{{ reportData.score }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">总页数:</span>
                  <span>{{ reportData.totalPages }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">疑似页数:</span>
                  <span class="suspicious">{{
                    reportData.suspiciousPages
                  }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 检测结果详情表格 -->
          <div class="result-details">
            <h3>检测结果详情</h3>
            <el-table :data="reportData.analysisResults" border style="width: 100%">
              <el-table-column prop="page" label="页码" width="100" />
              <el-table-column prop="similarity" label="相似度 (%)" width="150">
                <template #default="scope">
                  <el-progress :percentage="scope.row.similarity" :color="scope.row.similarity > 60
                      ? '#F56C6C'
                      : scope.row.similarity > 40
                        ? '#E6A23C'
                        : '#67C23A'
                    " />
                </template>
              </el-table-column>
              <el-table-column prop="source" label="可能来源" />
              <el-table-column label="操作" width="200">
                <template #default="scope">
                  <el-button size="small" type="primary">查看对比</el-button>
                  <el-button size="small">查看原文</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <el-tab-pane label="图像分析" name="images">
          <div class="image-analysis">
            <h3>图像分析结果</h3>
            <p>此处显示图像分析的详细内容...</p>

            <!-- 可以添加图像分析的相关内容 -->
          </div>
        </el-tab-pane>

        <el-tab-pane label="反馈历史" name="feedback">
          <div class="feedback-history">
            <h3>反馈历史记录</h3>
            <p>此处显示用户的反馈历史...</p>

            <!-- 可以添加反馈历史的相关内容 -->
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.result-detail-container {
  padding: 20px;

  .detail-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .header-title {
      font-size: 18px;
      font-weight: bold;
    }

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .detail-content {
    background-color: #fff;
    border-radius: 4px;
    padding: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);

    .report-summary {
      margin-bottom: 30px;

      .summary-card {
        display: flex;
        background-color: #f5f7fa;
        border-radius: 8px;
        padding: 20px;

        .book-icon {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 150px;
          height: 150px;
          margin-right: 30px;

          img {
            width: 100px;
            height: 100px;
          }
        }

        .summary-info {
          flex: 1;

          h3 {
            margin-top: 0;
            margin-bottom: 15px;
            font-size: 18px;
          }

          .info-row {
            margin-bottom: 8px;
            display: flex;
            align-items: center;

            .info-label {
              width: 80px;
              color: #606266;
            }

            .score {
              font-weight: bold;
              color: #409eff;
              font-size: 16px;
            }

            .suspicious {
              color: #f56c6c;
              font-weight: bold;
            }
          }
        }
      }
    }

    .result-details {
      h3 {
        margin-bottom: 15px;
      }
    }

    .image-analysis,
    .feedback-history {
      h3 {
        margin-bottom: 15px;
      }
    }
  }
}
</style>
