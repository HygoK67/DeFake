<script setup lang="ts">
import { ref, computed } from "vue";
import { noticesData } from "./data";
import type { DropdownInstance } from 'element-plus';
import NoticeList from "./components/NoticeList.vue";
import BellIcon from "@iconify-icons/ep/bell";

const dropdownRef = ref<DropdownInstance>();
const notices = ref(noticesData);
const activeKey = ref(noticesData[0]?.key);


//todo 联调后删掉
noticesData[0].list.sort((a, b) => {
  const aIsRead = a.readAt != null;
  const bIsRead = b.readAt != null;
  if (aIsRead !== bIsRead) {
    return Number(aIsRead) - Number(bIsRead); // (0 - 1 = -1 -> a first), (1 - 0 = 1 -> b first)
  }
  const dateA = a.datetime ? new Date(a.datetime).getTime() : 0;
  const dateB = b.datetime ? new Date(b.datetime).getTime() : 0;

  if (isNaN(dateA) && isNaN(dateB)) return 0; // Both invalid, treat as equal
  if (isNaN(dateA)) return 1; // Invalid date A should likely come after valid date B
  if (isNaN(dateB)) return -1; // Invalid date B should likely come after valid date A
  return dateB - dateA;
});
noticesData[1].list.sort((a, b) => {
  const aIsRead = a.readAt != null;
  const bIsRead = b.readAt != null;
  if (aIsRead !== bIsRead) {
    return Number(aIsRead) - Number(bIsRead); // (0 - 1 = -1 -> a first), (1 - 0 = 1 -> b first)
  }
  const dateA = a.datetime ? new Date(a.datetime).getTime() : 0;
  const dateB = b.datetime ? new Date(b.datetime).getTime() : 0;

  if (isNaN(dateA) && isNaN(dateB)) return 0; // Both invalid, treat as equal
  if (isNaN(dateA)) return 1; // Invalid date A should likely come after valid date B
  if (isNaN(dateB)) return -1; // Invalid date B should likely come after valid date A
  return dateB - dateA;
});


const noticesNum = computed(() => {
  let unreadCount = 0;
  for (const tab of notices.value) {
    for (const item of tab.list) {
      if (item.readAt == null) {
        unreadCount++;
      }
    }
  }
  return unreadCount;
});
const getLabel = computed(
  () => item =>
    item.name + (item.list.length > 0 ? `(${item.list.length})` : "")
);

const closeDropdownMenu = () => {
  dropdownRef.value?.handleClose(); // 使用 ElDropdown 的方法关闭
};
</script>

<template>
  <el-dropdown ref="dropdownRef" trigger="click" placement="bottom-end">
    <span :class="[
      'dropdown-badge',
      'navbar-bg-hover',
      'select-none',
      Number(noticesNum) !== 0 && 'mr-[10px]'
    ]">
      <el-badge :value="Number(noticesNum) === 0 ? '' : noticesNum" :max="99">
        <span class="header-notice-icon">
          <IconifyIconOffline :icon="BellIcon" />
        </span>
      </el-badge>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-tabs v-model="activeKey" :stretch="true" class="dropdown-tabs"
          :style="{ width: notices.length === 0 ? '200px' : '330px' }">
          <el-empty v-if="notices.length === 0" description="暂无消息" :image-size="60" />
          <span v-else>
            <template v-for="item in notices" :key="item.key">
              <el-tab-pane :label="getLabel(item)" :name="`${item.key}`">
                <el-scrollbar max-height="330px">
                  <div class="noticeList-container">
                    <NoticeList :list="item.list" :emptyText="item.emptyText" @close-dropdown="closeDropdownMenu" />
                  </div>
                </el-scrollbar>
              </el-tab-pane>
            </template>
          </span>
        </el-tabs>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<style lang="scss" scoped>
.dropdown-badge {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 48px;
  cursor: pointer;

  .header-notice-icon {
    font-size: 18px;
  }
}

.dropdown-tabs {
  .noticeList-container {
    padding: 15px 24px 0;
  }

  :deep(.el-tabs__header) {
    margin: 0;
  }

  :deep(.el-tabs__nav-wrap)::after {
    height: 1px;
  }

  :deep(.el-tabs__nav-wrap) {
    padding: 0 36px;
  }
}
</style>
