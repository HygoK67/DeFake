<script setup lang="ts">
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { noticesData, TabItem } from '@/layout/components/lay-notice/data'; // 确认路径
import NotificationList from './components/NotificationList.vue'; // 确认子组件路径

const notificationTypes = ref<TabItem[]>(noticesData);
const selectedTypeKey = ref<string | null>(null); // 用于驱动 UI (RadioButton) 和传递给子组件
const route = useRoute();

watch(
  () => route.params.typeKey,
  (newTypeKeyParam) => {
    const key = Array.isArray(newTypeKeyParam) ? newTypeKeyParam[0] : newTypeKeyParam;
    if (key && notificationTypes.value.some(t => t.key === key)) {
      selectedTypeKey.value = key;
    } else if (!key && notificationTypes.value.length > 0) {
      // 如果 URL 中没有参数
      // 设置一个默认选中的 key
      const defaultKey = notificationTypes.value[0].key;
      selectedTypeKey.value = defaultKey;
    } else {
      selectedTypeKey.value = null;
    }
  },
  { immediate: true } // 立即执行以处理初始 URL
);

</script>

<template>
  <div class="notifications-container">
    <div class="all-notifications-page p-4">
      <el-card shadow="hover">
        <template #header>
          <div class="clearfix">
            <span>消息中心</span>
          </div>
        </template>

        <div class="type-selection mb-4">
          <!-- RadioGroup v-model 绑定 selectedTypeKey 用于高亮 -->
          <el-radio-group v-model="selectedTypeKey">
            <el-radio-button v-for="type in notificationTypes" :key="type.key" :label="type.key">
              {{ type.name }}
            </el-radio-button>
          </el-radio-group>
        </div>

        <el-divider border-style="dashed" />

        <div class="content-display-area">
          <NotificationList v-if="selectedTypeKey" :type-key="selectedTypeKey" />
          <div v-else class="text-center p-10 text-gray-500">
            请选择或等待消息类型加载。
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<style scoped lang="scss">
.el-card :deep(.el-card__header) .clearfix span {
  font-size: 20px; // 将字体大小调整为你需要的值，例如 20px
  font-weight: 600; // 可以选择性地增加字重使其更突出
  color: #303133; // 可以确保颜色与 Element Plus 标题默认颜色一致或自定义
}

.notifications-container {
  display: flex; // Use flexbox for centering
  justify-content: center; // Center horizontally
  align-items: flex-start; // Align content to the top (adjust if needed e.g., center)
  min-height: calc(100vh - var(--v3-header-height, 50px)); // Make it fill at least the viewport height (minus header if applicable) Adjust '50px' or variable name as per your layout
  width: 100%;
  background-color: #f8faff; // Set the background color here
  margin: 0;

  height: 100%;
  box-sizing: border-box; // Include padding in height calculation
}

// Modified content style
.all-notifications-page {
  margin: 2rem auto; // REMOVED - centering is handled by parent
  max-width: 900px; // KEEP - limits the content width
  width: 100%; // Ensure it takes full width up to max-width
  // background-color: #f8faff; // REMOVED - background is on parent
}

.clearfix::after {
  content: "";
  display: table;
  clear: both;
}

.type-selection {
  margin-bottom: 1.5rem;
}

.content-display-area {
  /* 消息列表显示区域的样式 */
}

/* 通用的辅助类样式 (如果你的项目没有全局定义) */
.p-4 {
  padding: 1rem;
}

.mb-4 {
  margin-bottom: 1rem;
}

.text-center {
  text-align: center;
}

.p-10 {
  padding: 2.5rem;
}

.text-gray-500 {
  color: #6b7280;
}

/* 灰色文字 */
</style>