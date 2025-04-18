<script setup lang="ts">
import { ListItem } from "@/layout/components/lay-notice/data";
import { ref, PropType } from "vue";
import { handleInvite } from "@/api/user";
import { ElMessage } from "element-plus";

const props = defineProps({
  noticeItem: {
    type: Object as PropType<ListItem>,
    default: () => ({
      avatar: "",
      title: "",
      datetime: "",
      type: "",
      description: "",
    })
  }
});
const isLoading = ref(false);

async function handleAccept() {
  if (isLoading.value) return;
  isLoading.value = true;
  try {
    const res = await handleInvite({
      groupLeaderId: String(props.noticeItem.userIdSent),
      groupId: String(props.noticeItem.groupId),
      isAgree: "1",
    })
    if (res.code === 0) {
      ElMessage.success('已接受邀请');
    } else {
      ElMessage.error(res.message || '接受邀请失败，请稍后重试');
    }
  } catch (error) {
    console.error("接受邀请失败:", error);
    ElMessage.error('处理邀请时发生网络错误');
  } finally {
    isLoading.value = false;
  }
}

async function handleReject() {
  if (isLoading.value) return;
  isLoading.value = true;
  try {
    const res = await handleInvite({
      groupLeaderId: String(props.noticeItem.userIdSent),
      groupId: String(props.noticeItem.groupId),
      isAgree: "0",
    })
    if (res.code === 0) {
      ElMessage.success('已拒绝邀请');
    } else {
      ElMessage.error(res.message || '拒接邀请失败，请稍后重试');
    }
  } catch (error) {
    console.error("拒绝邀请失败:", error);
    ElMessage.error('处理邀请时发生网络错误');
  } finally {
    isLoading.value = false;
  }
}
</script>

<template>
  <div class="notice-container border-b-[1px] border-solid border-[#f0f0f0] dark:border-[#303030]">
    <el-avatar v-if="noticeItem.avatar" :size="30" :src="noticeItem.avatar" class="notice-container-avatar" />
    <div class="notice-container-text">
      <div class="notice-text-title text-[#000000d9] dark:text-white">
        <div class="notice-title-content">
          {{ noticeItem.title }}
        </div>
        <el-tag v-if="noticeItem?.extra" :type="noticeItem?.status" class="notice-title-extra">
          {{ noticeItem?.extra }}
        </el-tag>
      </div>

      <div class="notice-text-description">
        {{ noticeItem.description }}
      </div>
      <div class="notice-footer">
        <!-- Actions first for left alignment -->
        <div v-if="noticeItem.templateId === 1" class="notice-actions">
          <el-button type="primary" size="small" link @click.stop="handleAccept" :loading="isLoading"
            :disabled="isLoading">
            接受
          </el-button>
          <el-button type="danger" size="small" link @click.stop="handleReject" :loading="isLoading"
            :disabled="isLoading">
            拒绝
          </el-button>
        </div>

        <!-- Placeholder for spacing if actions are not shown, or just rely on margin-left:auto on date -->
        <div v-else class="actions-placeholder" />

        <!-- Datetime second, will be pushed right -->
        <div class="notice-text-datetime text-[#00000073] dark:text-white">
          {{ noticeItem.datetime }}
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.notice-container {
  display: flex;
  align-items: flex-start;
  padding: 15px 0;

  .notice-container-avatar {
    margin-right: 20px;
    background: #fff;
    flex-shrink: 0; // Prevent shrinking
    align-self: flex-start; // Align avatar top
  }

  .notice-container-text {
    display: flex;
    flex: 1; // Take remaining space
    flex-direction: column;
    min-width: 0; // Prevent flex item overflow

    .notice-text-title {
      display: flex;
      align-items: flex-start; // Align items top for multi-line title
      margin-bottom: 10px;
      font-size: 17px;
      font-weight: 400;
      line-height: 1.6;

      .notice-title-content {
        flex: 1;
        min-width: 0; // Prevent flex item overflow
        white-space: normal; // Allow text wrapping
        word-break: break-word; // Allow long words to break
        margin-right: 10px;
      }

      .notice-title-extra {
        margin-left: auto; // Push tag right
        flex-shrink: 0; // Prevent shrinking
        align-self: center; // Center tag vertically relative to first line
      }
    }

    .notice-text-description {
      font-size: 15px;
      line-height: 1.6;
      white-space: normal;
      word-break: break-word;
      margin-bottom: 6px;
    }

    // Footer container for actions and date
    .notice-footer {
      display: flex;
      align-items: center; // Vertically align items
      margin-top: 10px;
      width: 100%;
    }

    .notice-actions {
      display: flex;
      gap: 12px;

      .el-button {
        font-size: 15px;
      }
    }

    .notice-text-datetime {
      font-size: 15px;
      line-height: 1.6;
      white-space: normal;
      word-break: break-word;
      color: #00000073;
      margin-left: auto; // Push date to the right

      // Dark mode color
      .dark & {
        color: white;
      }
    }
  }
}
</style>