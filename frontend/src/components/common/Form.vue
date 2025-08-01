<template>
  <a-form
    v-bind="$attrs"
    class="common-form"
  >
    <slot></slot>
  </a-form>
</template>

<script setup lang="ts">
// 启用属性继承
defineOptions({
  inheritAttrs: false
})
</script>

<style scoped>
.common-form {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
}

.common-form :deep(.ant-form-item) {
  margin-bottom: 24px;
}

.common-form :deep(.ant-form-item-label) {
  font-weight: 600;
}

.common-form :deep(.ant-form-item-explain) {
  font-size: 12px;
}
</style>