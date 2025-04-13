<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { submitSelectedImages } from '@/api/document';

const router = useRouter();
const route = useRoute();
const images = ref<Array<any>>([]);
const selectedImages = ref<Array<any>>([]);
const previewImage = ref<any>(null);
const isSubmitting = ref(false);

// 从路由参数中获取图片数据
onMounted(() => {
  try {
    if (route.query.images) {
      const imagesData = JSON.parse(decodeURIComponent(route.query.images as string));
      images.value = imagesData;
    } else if (sessionStorage.getItem('uploadedImages')) {
      // 从sessionStorage中获取数据作为备选
      images.value = JSON.parse(sessionStorage.getItem('uploadedImages') || '[]');
    } else {
      console.error('没有找到图片数据');
      alert('没有可用的图片数据');
      router.push('/');
    }
  } catch (error) {
    console.error('解析图片数据失败:', error);
    alert('解析图片数据失败');
    router.push('/');
  }
});

// 选择或取消选择图片
const toggleSelectImage = (image: any) => {
  const index = selectedImages.value.findIndex(img => img.url === image.url);
  if (index === -1) {
    selectedImages.value.push(image);
  } else {
    selectedImages.value.splice(index, 1);
  }
};

// 检查图片是否已选择
const isImageSelected = (image: any): boolean => {
  return selectedImages.value.some(img => img.url === image.url);
};

// 查看大图
const showPreview = (image: any) => {
  previewImage.value = image;
};

// 关闭预览
const closePreview = () => {
  previewImage.value = null;
};

// 提交选择的图片
const handleSubmit = async () => {
  if (selectedImages.value.length === 0) {
    alert('请至少选择一张图片');
    return;
  }

  try {
    isSubmitting.value = true;
    // 确保传递的数据格式与mock服务期望的一致
    const result = await submitSelectedImages(selectedImages.value);
    isSubmitting.value = false;
    alert('已成功提交选择的图片');
    router.push('/');
  } catch (error) {
    console.error('提交图片失败:', error);
    alert('提交图片失败，请重试');
    isSubmitting.value = false;
  }
};

// 返回上传页面
const goBack = () => {
  router.push('/');
};
</script>

<template>
  <div class="image-selection-page">
    <div class="page-header">
      <h1>检测结果与图片选择</h1>
      <div class="header-actions">
        <button class="back-btn" @click="goBack">
          <i class="back-icon">←</i>
          返回上传页
        </button>
        <button 
          class="submit-btn" 
          :disabled="selectedImages.length === 0 || isSubmitting" 
          @click="handleSubmit"
        >
          <i class="submit-icon">✓</i>
          {{ isSubmitting ? '提交中...' : `提交选择的图片 (${selectedImages.length})` }}
        </button>
      </div>
    </div>

    <div v-if="images.length === 0" class="no-images">
      <div class="empty-icon">📷</div>
      <p>没有找到图片数据</p>
    </div>

    <div v-else class="images-grid">
      <div 
        v-for="(image, index) in images" 
        :key="index"
        class="image-card"
        :class="{ 'selected': isImageSelected(image) }"
      >
        <div class="image-preview" @click="showPreview(image)">
          <img :src="image.url" :alt="image.name">
          <div class="image-overlay">
            <span class="view-icon">🔍</span>
          </div>
        </div>
        <div class="image-info">
          <div class="image-name" :title="image.name">{{ image.name }}</div>
          <div v-if="image.sourceFile" class="image-source">
            来源: {{ image.sourceFile }}
          </div>
          <label class="select-checkbox">
            <input 
              type="checkbox" 
              :checked="isImageSelected(image)"
              @change="toggleSelectImage(image)"
            >
            <span class="checkbox-custom"></span>
            选择
          </label>
        </div>
      </div>
    </div>

    <!-- 图片预览弹窗 -->
    <div v-if="previewImage" class="image-preview-modal" @click="closePreview">
      <div class="preview-content" @click.stop>
        <div class="preview-header">
          <h3>{{ previewImage.name }}</h3>
          <button class="close-btn" @click="closePreview">×</button>
        </div>
        <div class="preview-image">
          <img :src="previewImage.url" :alt="previewImage.name">
        </div>
        <div class="preview-actions">
          <label class="select-checkbox large">
            <input 
              type="checkbox" 
              :checked="isImageSelected(previewImage)"
              @change="toggleSelectImage(previewImage)"
            >
            <span class="checkbox-custom"></span>
            选择此图片
          </label>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.image-selection-page {
  padding: 30px;
  max-width: 1600px;
  margin: 0 auto;
  min-height: 90vh;
  background-color: #f9fbfd;
  border-radius: 12px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.03);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  border-bottom: 2px solid #e7f0fb;
  padding-bottom: 20px;
}

.page-header h1 {
  margin: 0;
  font-size: 28px;
  color: #1a5fb4;
  font-weight: 600;
  letter-spacing: -0.5px;
  position: relative;
}

.page-header h1:after {
  content: "";
  display: block;
  width: 50px;
  height: 3px;
  background: linear-gradient(90deg, #1a5fb4, #3584e4);
  margin-top: 8px;
  border-radius: 3px;
}

.header-actions {
  display: flex;
  gap: 16px;
}

.back-btn,
.submit-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 18px;
  border-radius: 8px;
  font-weight: 500;
  font-size: 15px;
  transition: all 0.3s ease;
  cursor: pointer;
  border: none;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
}

.back-btn {
  background: #f8f9fa;
  color: #495057;
  border: 1px solid #e9ecef;
}

.back-btn:hover {
  background: #e9ecef;
  transform: translateY(-1px);
}

.back-icon, .submit-icon {
  font-style: normal;
}

.submit-btn {
  background: linear-gradient(135deg, #1a5fb4, #3584e4);
  color: white;
}

.submit-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #164995, #1a73e8);
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(53, 132, 228, 0.3);
}

.submit-btn:disabled {
  background: #adb5bd;
  color: #e9ecef;
  cursor: not-allowed;
  box-shadow: none;
}

.no-images {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 400px;
  color: #868e96;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.04);
}

.empty-icon {
  font-size: 60px;
  margin-bottom: 24px;
  opacity: 0.7;
}

.images-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

.image-card {
  border-radius: 12px;
  overflow: hidden;
  background: white;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border: 1px solid #f1f3f5;
  position: relative;
}

.image-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.image-card.selected {
  border: 2px solid #3584e4;
  box-shadow: 0 8px 20px rgba(53, 132, 228, 0.15);
}

.image-preview {
  height: 200px;
  overflow: hidden;
  cursor: pointer;
  position: relative;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: all 0.3s ease;
}

.view-icon {
  font-size: 24px;
  color: white;
  background: rgba(0, 0, 0, 0.5);
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transform: scale(0.8);
  transition: transform 0.3s ease;
}

.image-preview:hover img {
  transform: scale(1.08);
}

.image-preview:hover .image-overlay {
  background: rgba(0, 0, 0, 0.3);
  opacity: 1;
}

.image-preview:hover .view-icon {
  transform: scale(1);
}

.image-info {
  padding: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid #f8f9fa;
}

.image-name {
  font-size: 15px;
  font-weight: 500;
  color: #495057;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 70%;
}

.image-source {
  font-size: 12px;
  color: #7a8b98;
  margin-top: 4px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
  max-width: 100%;
}

.select-checkbox {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  cursor: pointer;
  color: #495057;
  user-select: none;
}

.select-checkbox input {
  display: none;
}

.checkbox-custom {
  display: inline-block;
  width: 18px;
  height: 18px;
  border: 2px solid #ced4da;
  border-radius: 4px;
  position: relative;
  transition: all 0.2s ease;
}

.select-checkbox input:checked + .checkbox-custom {
  background-color: #3584e4;
  border-color: #3584e4;
}

.select-checkbox input:checked + .checkbox-custom::after {
  content: "";
  position: absolute;
  top: 2px;
  left: 6px;
  width: 4px;
  height: 9px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.select-checkbox.large {
  font-size: 16px;
}

.select-checkbox.large .checkbox-custom {
  width: 22px;
  height: 22px;
}

.select-checkbox.large input:checked + .checkbox-custom::after {
  top: 3px;
  left: 7px;
  width: 5px;
  height: 10px;
}

.image-preview-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.85);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.preview-content {
  background: white;
  border-radius: 16px;
  max-width: 90%;
  max-height: 90%;
  overflow: hidden;
  width: auto;
  display: flex;
  flex-direction: column;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: scaleIn 0.3s ease;
}

@keyframes scaleIn {
  from { transform: scale(0.95); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #e9ecef;
}

.preview-header h3 {
  margin: 0;
  font-size: 20px;
  color: #343a40;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  font-size: 28px;
  cursor: pointer;
  color: #868e96;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  transition: all 0.2s;
}

.close-btn:hover {
  background: #f1f3f5;
  color: #495057;
}

.preview-image {
  padding: 24px;
  overflow: auto;
  max-height: 70vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f8f9fa;
}

.preview-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  border-radius: 6px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.preview-actions {
  padding: 16px 24px;
  border-top: 1px solid #e9ecef;
  display: flex;
  justify-content: center;
  background: white;
}

@media (max-width: 768px) {
  .image-selection-page {
    padding: 20px;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .header-actions {
    width: 100%;
  }
  
  .submit-btn, .back-btn {
    flex: 1;
    justify-content: center;
  }
  
  .images-grid {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: 16px;
  }
  
  .image-preview {
    height: 140px;
  }
  
  .image-info {
    padding: 12px;
  }
  
  .preview-content {
    width: 95%;
  }
}
</style>
