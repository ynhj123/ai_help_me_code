<template>
  <div class="image-upload">
    <a-upload
      v-model:file-list="fileList"
      name="file"
      :action="uploadUrl"
      :headers="headers"
      :before-upload="beforeUpload"
      :onChange="handleChange"
      :showUploadList="false"
      :multiple="false"
      accept="image/*"
    >
      <div v-if="imageUrl" class="image-preview">
        <img :src="imageUrl" alt="Preview" class="preview-img" />
        <a-button
          type="primary"
          danger
          size="small"
          class="remove-btn"
          @click.stop="handleRemove"
        >
          删除
        </a-button>
      </div>
      
      <div v-else class="upload-area">
        <div class="upload-content">
          <UploadOutlined />
          <div class="upload-text">点击上传图片</div>
        </div>
      </div>
    </a-upload>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { message } from 'ant-design-vue'
import { UploadOutlined } from '@ant-design/icons-vue'
import type { UploadProps } from 'ant-design-vue'

// 定义属性
const props = defineProps<{
  modelValue?: string
  uploadUrl?: string
}>()

// 定义事件
const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
}>()

// 文件列表
const fileList = ref<any[]>([])

// 图片URL
const imageUrl = computed(() => props.modelValue)

// 请求头
const headers = computed(() => {
  const token = localStorage.getItem('token')
  return token ? { Authorization: `Bearer ${token}` } : {}
})

// 上传前检查
const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    message.error('只能上传图片文件!')
    return false
  }
  
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    message.error('图片大小不能超过2MB!')
    return false
  }
  
  return true
}

// 处理上传变化
const handleChange: UploadProps['onChange'] = ({ file, fileList: newFileList }) => {
  fileList.value = newFileList
  
  if (file.status === 'done') {
    const response = file.response
    if (response && response.code === 200) {
      emit('update:modelValue', response.data)
      message.success('上传成功')
    } else {
      message.error('上传失败')
    }
  } else if (file.status === 'error') {
    message.error('上传失败')
  }
}

// 处理删除
const handleRemove = () => {
  emit('update:modelValue', '')
  fileList.value = []
}
</script>

<style scoped>
.image-upload {
  display: inline-block;
}

.upload-area {
  width: 120px;
  height: 120px;
  border: 1px dashed #d9d9d9;
  border-radius: 8px;
  cursor: pointer;
  transition: border-color 0.3s;
}

.upload-area:hover {
  border-color: #1890ff;
}

.upload-content {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #999;
}

.upload-content .anticon {
  font-size: 24px;
  margin-bottom: 8px;
}

.upload-text {
  font-size: 12px;
}

.image-preview {
  position: relative;
  width: 120px;
  height: 120px;
}

.preview-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.remove-btn {
  position: absolute;
  bottom: 8px;
  left: 50%;
  transform: translateX(-50%);
  opacity: 0;
  transition: opacity 0.3s;
}

.image-preview:hover .remove-btn {
  opacity: 1;
}
</style>