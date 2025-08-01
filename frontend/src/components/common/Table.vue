<template>
  <a-table
    v-bind="$attrs"
    class="common-table"
  >
    <template #bodyCell="props">
      <slot name="bodyCell" v-bind="props"></slot>
    </template>
    
    <template #headerCell="props">
      <slot name="headerCell" v-bind="props"></slot>
    </template>
    
    <template #emptyText>
      <slot name="emptyText">
        <a-empty description="暂无数据" />
      </slot>
    </template>
  </a-table>
</template>

<script setup lang="ts">
// 启用属性继承
defineOptions({
  inheritAttrs: false
})
</script>

<style scoped>
.common-table {
  background: #fff;
}

.common-table :deep(.ant-table) {
  border-radius: 8px;
  overflow: hidden;
}

.common-table :deep(.ant-table-thead > tr > th) {
  background: #fafafa;
  font-weight: 600;
}

.common-table :deep(.ant-table-tbody > tr:hover) {
  background: #f5f5f5;
}
</style>