<script setup lang="ts">
import { PropType, defineEmits } from "vue";
import { ListItem } from "../data";
import NoticeItem from "./NoticeItem.vue";

defineProps({
  list: {
    type: Array as PropType<Array<ListItem>>,
    default: () => []
  },
  emptyText: {
    type: String,
    default: ""
  }
});
const emit = defineEmits(['close-dropdown']);
</script>

<template>
  <div v-if="list.length">
    <NoticeItem v-for="(item, index) in list" :key="index" :noticeItem="item"
      @close-dropdown="$emit('close-dropdown')" />
  </div>
  <el-empty v-else :description="emptyText" />
</template>
