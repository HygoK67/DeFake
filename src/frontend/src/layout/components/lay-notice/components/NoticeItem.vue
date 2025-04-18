<script setup lang="ts">
import { ListItem } from "../data";
import { ref, PropType, nextTick, defineEmits } from "vue";
import { useNav } from "@/layout/hooks/useNav";
import { deviceDetection } from "@pureadmin/utils";
import { useRouter } from "vue-router";

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

const emit = defineEmits(['close-dropdown'])
const titleRef = ref(null);
const titleTooltip = ref(false);
const descriptionRef = ref(null);
const descriptionTooltip = ref(false);
const { tooltipEffect } = useNav();
const isMobile = deviceDetection();
const router = useRouter();


function hoverTitle() {
  nextTick(() => {
    titleRef.value?.scrollWidth > titleRef.value?.clientWidth
      ? (titleTooltip.value = true)
      : (titleTooltip.value = false);
  });
}

function hoverDescription(event, description) {
  // currentWidth 为文本在页面中所占的宽度，创建标签，加入到页面，获取currentWidth ,最后在移除
  const tempTag = document.createElement("span");
  tempTag.innerText = description;
  tempTag.className = "getDescriptionWidth";
  document.querySelector("body").appendChild(tempTag);
  const currentWidth = (
    document.querySelector(".getDescriptionWidth") as HTMLSpanElement
  ).offsetWidth;
  document.querySelector(".getDescriptionWidth").remove();

  // cellWidth为容器的宽度
  const cellWidth = event.target.offsetWidth;

  // 当文本宽度大于容器宽度两倍时，代表文本显示超过两行
  currentWidth > 2 * cellWidth
    ? (descriptionTooltip.value = true)
    : (descriptionTooltip.value = false);
}

function handleItemClick() {
  if (!props.noticeItem?.type) {
    console.error("无法导航：消息项缺少 type 属性。", props.noticeItem);
    return;
  }
  try {
    router.push({
      name: "NotificationCenter", // 确保这个路由名称与你的 router 配置一致
      params: {
        typeKey: props.noticeItem.type
      }
    });
    emit('close-dropdown');
  } catch (error) {
    console.error("路由跳转失败:", error);
  }
}
</script>

<template>
  <div class="notice-container border-b-[1px] border-solid border-[#f0f0f0] dark:border-[#303030]"
    @click="handleItemClick">
    <el-badge v-if="!props.noticeItem.readAt" is-dot class="unread-dot" />
    <el-avatar v-if="noticeItem.avatar" :size="30" :src="noticeItem.avatar" class="notice-container-avatar" />
    <div class="notice-container-text">
      <div class="notice-text-title text-[#000000d9] dark:text-white">
        <el-tooltip popper-class="notice-title-popper" :effect="tooltipEffect" :disabled="!titleTooltip"
          :content="noticeItem.title" placement="top-start" :enterable="!isMobile">
          <div ref="titleRef" class="notice-title-content" @mouseover="hoverTitle">
            {{ noticeItem.title }}
          </div>
        </el-tooltip>
        <el-tag v-if="noticeItem?.extra" :type="noticeItem?.status" size="small" class="notice-title-extra">
          {{ noticeItem?.extra }}
        </el-tag>
      </div>

      <el-tooltip popper-class="notice-title-popper" :effect="tooltipEffect" :disabled="!descriptionTooltip"
        :content="noticeItem.description" placement="top-start">
        <div ref="descriptionRef" class="notice-text-description"
          @mouseover="hoverDescription($event, noticeItem.description)">
          {{ noticeItem.description }}
        </div>
      </el-tooltip>
      <div class="notice-text-datetime text-[#00000073] dark:text-white">
        {{ noticeItem.datetime }}
      </div>
    </div>
  </div>
</template>

<style>
/* 全局样式保持不变 */
.notice-title-popper,
.notice-description-popper {
  max-width: 238px;
}

.getDescriptionWidth {
  position: absolute;
  top: -9999px;
  left: -9999px;
  z-index: -1;
  visibility: hidden;
  white-space: nowrap;
  font-size: 12px;
  line-height: 1.5715;
}
</style>

<style scoped lang="scss">
.notice-container {
  position: relative; // 父容器相对定位，为绝对定位的红点提供参照
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  padding: 12px 0; // 保持原有的内边距
  cursor: pointer;

  // 未读红点样式
  .unread-dot {
    position: absolute;
    top: 7px; // 调整 Y 坐标，更靠近顶部 padding 边缘
    left: -5px; // 调整 X 坐标，更靠近左侧 padding 边缘
    z-index: 1;
    // 红点本身不占文档流空间，不影响其他元素
  }

  .notice-container-avatar {
    // margin-left: 18px; // 移除这个 margin，让头像回到原始位置
    margin-right: 16px;
    background: #fff;
    flex-shrink: 0;
  }

  .notice-container-text {
    display: flex;
    flex: 1;
    flex-direction: column;
    justify-content: space-between;
    min-width: 0;

    // --- 内部文本样式保持和原来一致 ---
    .notice-text-title {
      display: flex;
      align-items: center;
      margin-bottom: 8px;
      font-size: 14px;
      font-weight: 400;
      line-height: 1.5715;

      .notice-title-content {
        flex: 1;
        min-width: 0;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        margin-right: 8px;
      }

      .notice-title-extra {
        margin-left: auto;
        font-weight: 400;
        flex-shrink: 0;
      }
    }

    .notice-text-description,
    .notice-text-datetime {
      font-size: 12px;
      line-height: 1.5715;
    }

    .notice-text-description {
      display: -webkit-box;
      overflow: hidden;
      text-overflow: ellipsis;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      word-break: break-word;
    }

    .notice-text-datetime {
      margin-top: 4px;
      white-space: nowrap;
    }
  }
}
</style>