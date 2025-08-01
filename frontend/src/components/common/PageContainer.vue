<template>
  <div class="page-container">
    <a-page-header
      :title="title"
      :sub-title="subTitle"
      class="page-header"
    >
      <template #extra>
        <slot name="extra"></slot>
      </template>
      
      <template #footer>
        <slot name="footer"></slot>
      </template>
      
      <slot></slot>
    </a-page-header>
  </div>
</template>

<script setup lang="ts">
// 定义属性
defineProps<{
  title?: string
  subTitle?: string
}>()
</script>

<style scoped>
.page-container {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.page-header {
  padding: 24px;
  border-bottom: 1px solid #f0f0f0;
}

.page-header :deep(.ant-page-header-content) {
  padding-top: 16px;
}
</style>