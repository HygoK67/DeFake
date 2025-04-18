<script setup lang="ts">
import { ref, computed, watch, PropType } from "vue";
import { noticesData, TabItem, ListItem } from "@/layout/components/lay-notice/data";
import NotificationItem from "./NotificationItem.vue";

const props = defineProps({
  typeKey: {
    type: String as PropType<string | null>, // 类型可以是字符串或 null
    required: true // 这个 prop 是必需的
  }
});

// 存储根据 prop 的 typeKey 加载到的具体标签页数据
const currentTabData = ref<TabItem | null>(null);
// 控制加载状态
const isLoading = ref(false); // 初始可以为 false，因为加载由 prop 变化触发

// 计算列表标题
const listTitle = computed(() => {
  return currentTabData.value ? currentTabData.value.name : "消息详情";
});

// 计算要显示的通知列表
const notificationList = computed<ListItem[]>(() => {
  return currentTabData.value?.list || [];
});

// 计算列表为空时的提示文本
const emptyText = computed<string>(() => {
  return currentTabData.value?.emptyText || "暂无内容";
});

// 根据传入的 key 加载通知数据
const loadNotificationsByKey = (keyToLoad: string | null) => {
  // 如果传入的 key 无效 (null 或空字符串)，则清空数据并返回
  if (!keyToLoad) {
    currentTabData.value = null;
    isLoading.value = false; // 确保加载状态关闭
    return;
  }
  isLoading.value = true; // 开始加载
  currentTabData.value = noticesData.find(tab => tab.key === keyToLoad) ?? null;
  isLoading.value = false; // 数据加载或处理完成
};

// --- 侦听器 ---
// 侦听 props.typeKey 的变化
watch(
  () => props.typeKey, // 侦听的源
  (newTypeKey) => {
    loadNotificationsByKey(newTypeKey);
  },
  { immediate: true }
);

</script>

<template>
  <!-- 子组件：显示具体类型消息列表的区域 -->
  <div class="notification-list-display">
    <div v-if="isLoading" class="text-center p-10">
      <p>加载中...</p>
    </div>

    <div v-else>
      <el-card shadow="never">
        <template #header>
          <div class="card-header">
            <span>{{ listTitle }} ({{ notificationList.length }} 条)</span>
          </div>
        </template>

        <el-empty v-if="!currentTabData || notificationList.length === 0" :description="emptyText" />

        <div v-else class="notification-list-items">
          <NotificationItem v-for="(item, index) in notificationList" :key="item.title + '-' + index" :noticeItem="item"
            class="list-page-item" />
        </div>
      </el-card>
    </div>
  </div>
</template>

<style scoped lang="scss">
/* 子组件的特定样式 */
.notification-list-display {
  margin-top: 1rem;
  /* 和父组件的选择器之间加点间距 */
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  /* 列表标题加粗 */
}

.notification-list-items {
  /* 列表容器的样式 */
}

.list-page-item {
  border-bottom: none;
  padding: 12px 0;

  &:not(:last-child) {
    border-bottom: 1px solid #f0f0f0;
  }
}

/* 调整 Element Plus 卡片组件的内边距 */
:deep(.el-card__body) {
  padding: 0 20px 20px 20px;
  /* 移除顶部，保留左右和底部 */
}

:deep(.el-card__header) {
  padding: 15px 20px;
  /* 设置头部内边距 */
}

/* 通用的辅助类样式 (如果你的项目没有全局定义) */
.text-center {
  text-align: center;
}

.p-10 {
  padding: 2.5rem;
}

.text-red-500 {
  color: #ef4444;
}

/* 红色文字 */
</style>