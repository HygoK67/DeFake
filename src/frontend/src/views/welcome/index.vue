<script setup lang="ts">
import { ref, reactive } from "vue";

defineOptions({
  name: "ForensicsDetection"
});

// 支持的文件类型
const supportedFormats = [".jpg", ".jpeg", ".png", ".bmp", ".tiff", ".webp", ".doc", ".docx", ".pdf"];

// 文件上传状态
const fileList = ref<File[]>([]);
const dragging = ref(false);
const uploading = ref(false);
const uploadProgress = ref(0);
const selectedFile = ref<File | null>(null);

// 元数据表单
const metadata = reactive({
  title: "",
  author: "",
  institution: "",
  publishDate: "",
  keywords: "",
  description: ""
});

// 处理文件拖放
const handleDragOver = (e: DragEvent) => {
  e.preventDefault();
  dragging.value = true;
};

const handleDragLeave = (e: DragEvent) => {
  e.preventDefault();
  dragging.value = false;
};

const handleDrop = (e: DragEvent) => {
  e.preventDefault();
  dragging.value = false;
  
  if (e.dataTransfer?.files) {
    handleFiles(e.dataTransfer.files);
  }
};

// 处理文件选择
const handleFileChange = (e: Event) => {
  const input = e.target as HTMLInputElement;
  if (input.files) {
    handleFiles(input.files);
  }
};

// 处理文件
const handleFiles = (files: FileList) => {
  Array.from(files).forEach(file => {
    const extension = '.' + file.name.split('.').pop()?.toLowerCase();
    if (supportedFormats.includes(extension)) {
      fileList.value.push(file);
    } else {
      alert(`不支持的文件格式: ${extension}`);
    }
  });
};

// 删除文件
const removeFile = (index: number) => {
  if (selectedFile.value === fileList.value[index]) {
    selectedFile.value = null;
  }
  fileList.value.splice(index, 1);
};

// 选择文件进行元数据编辑
const selectFile = (file: File) => {
  selectedFile.value = file;
  // 清空元数据表单
  Object.keys(metadata).forEach(key => {
    metadata[key] = "";
  });
};

// 上传并检测文件
const uploadAndDetect = async () => {
  if (fileList.value.length === 0) return;
  
  uploading.value = true;
  uploadProgress.value = 0;
  
  // 模拟上传过程
  const progressInterval = setInterval(() => {
    uploadProgress.value += 5;
    if (uploadProgress.value >= 100) {
      clearInterval(progressInterval);
      setTimeout(() => {
        uploading.value = false;
        alert("所有文件已成功检测并上传!");
        fileList.value = [];
        selectedFile.value = null;
      }, 500);
    }
  }, 100);
};

// 清空
const clearAll = () => {
  fileList.value = [];
  selectedFile.value = null;
  uploadProgress.value = 0;
  Object.keys(metadata).forEach(key => {
    metadata[key] = "";
  });
};

// 生成文件大小显示
const formatFileSize = (size: number): string => {
  if (size < 1024) {
    return size + ' B';
  } else if (size < 1024 * 1024) {
    return (size / 1024).toFixed(2) + ' KB';
  } else {
    return (size / (1024 * 1024)).toFixed(2) + ' MB';
  }
};

// 获取文件图标
const getFileIcon = (filename: string): string => {
  const extension = filename.split('.').pop()?.toLowerCase();
  
  switch(extension) {
    case 'jpg':
    case 'jpeg':
    case 'png':
    case 'bmp':
    case 'tiff':
    case 'webp':
      return '🖼️';
    case 'doc':
    case 'docx':
      return '📄';
    case 'pdf':
      return '📑';
    default:
      return '📁';
  }
};

// 获取文件类型
const getFileType = (filename: string): string => {
  const extension = filename.split('.').pop()?.toLowerCase();
  
  switch(extension) {
    case 'jpg':
    case 'jpeg':
    case 'png':
    case 'bmp':
    case 'tiff':
    case 'webp':
      return '图像';
    case 'doc':
    case 'docx':
      return 'Word文档';
    case 'pdf':
      return 'PDF文档';
    default:
      return '未知类型';
  }
};
</script>

<template>
  <div class="detection-container">
    <header class="app-header">
      <div class="header-content">
        <h1 class="app-title">DeFake学术图像造假检测</h1>
        <p class="app-subtitle">支持图像、PDF和Word文档的多维度造假检测和归档管理</p>
      </div>
    </header>
    
    <main class="main-content">
      <!-- 左侧上传区域 -->
      <section class="upload-section">
        <div 
          class="drop-zone" 
          :class="{ 'drop-zone-active': dragging }"
          @dragover="handleDragOver"
          @dragleave="handleDragLeave"
          @drop="handleDrop"
        >
          <div v-if="fileList.length === 0" class="drop-zone-content">
            <div class="icon">📁</div>
            <div class="text">拖放文件到此处或</div>
            <label class="upload-btn">
              选择文件
              <input 
                type="file" 
                multiple 
                :accept="supportedFormats.join(',')" 
                @change="handleFileChange"
                class="hidden-input"
              >
            </label>
            <div class="supported-formats">
              支持的格式: {{ supportedFormats.join(', ') }}
            </div>
          </div>
          
          <div v-else class="file-list">
            <div class="file-list-header">
              <h3>待检测文件 ({{ fileList.length }})</h3>
              <button class="text-btn" @click="clearAll">清空</button>
            </div>
            
            <div 
              class="file-item" 
              v-for="(file, index) in fileList" 
              :key="index"
              :class="{ 'file-selected': selectedFile === file }"
              @click="selectFile(file)"
            >
              <div class="file-icon">{{ getFileIcon(file.name) }}</div>
              <div class="file-info">
                <div class="file-name">{{ file.name }}</div>
                <div class="file-meta">
                  <span class="file-type">{{ getFileType(file.name) }}</span>
                  <span class="file-size">{{ formatFileSize(file.size) }}</span>
                </div>
              </div>
              <button class="remove-btn" @click.stop="removeFile(index)">×</button>
            </div>
            
            <label class="add-more-btn">
              添加更多
              <input 
                type="file" 
                multiple 
                :accept="supportedFormats.join(',')" 
                @change="handleFileChange"
                class="hidden-input"
              >
            </label>
          </div>
        </div>
        
        <!-- 上传按钮 -->
        <div class="action-buttons">
          <button 
            class="detect-btn" 
            :disabled="fileList.length === 0 || uploading" 
            @click="uploadAndDetect"
          >
            {{ uploading ? '检测中...' : '开始检测' }}
          </button>
          
          <div v-if="uploading" class="upload-progress">
            <div class="progress-bar">
              <div class="progress" :style="{ width: `${uploadProgress}%` }"></div>
            </div>
            <div class="progress-text">{{ uploadProgress }}%</div>
          </div>
        </div>
      </section>
      
      <!-- 右侧元数据区域 -->
      <section class="metadata-section">
        <h2 class="section-title">文件元数据</h2>
        
        <div v-if="selectedFile" class="metadata-content">
          <div class="selected-file-info">
            <div class="file-icon large">{{ getFileIcon(selectedFile.name) }}</div>
            <div>
              <h3 class="file-name">{{ selectedFile.name }}</h3>
              <p class="file-details">{{ getFileType(selectedFile.name) }} • {{ formatFileSize(selectedFile.size) }}</p>
            </div>
          </div>
          
          <form class="metadata-form">
            <div class="form-group">
              <label for="title">文档标题</label>
              <input 
                type="text" 
                id="title" 
                v-model="metadata.title" 
                placeholder="输入文档标题"
              >
            </div>
            
            <div class="form-group">
              <label for="author">作者</label>
              <input 
                type="text" 
                id="author" 
                v-model="metadata.author" 
                placeholder="输入作者姓名"
              >
            </div>
            
            <div class="form-group">
              <label for="institution">机构</label>
              <input 
                type="text" 
                id="institution" 
                v-model="metadata.institution" 
                placeholder="输入作者所属机构"
              >
            </div>
            
            <div class="form-group">
              <label for="publishDate">发布日期</label>
              <input 
                type="date" 
                id="publishDate" 
                v-model="metadata.publishDate"
              >
            </div>
            
            <div class="form-group">
              <label for="keywords">关键词</label>
              <input 
                type="text" 
                id="keywords" 
                v-model="metadata.keywords" 
                placeholder="用逗号分隔关键词"
              >
            </div>
            
            <div class="form-group">
              <label for="description">文档描述</label>
              <textarea 
                id="description" 
                v-model="metadata.description" 
                placeholder="简要描述文档内容"
                rows="4"
              ></textarea>
            </div>
            
            <div class="form-actions">
              <button type="button" class="save-btn">保存元数据</button>
            </div>
          </form>
        </div>
        
        <div v-else class="empty-metadata">
          <div class="empty-icon">📝</div>
          <p class="empty-text">请从左侧选择一个文件<br>添加元数据信息</p>
        </div>
      </section>
    </main>
    
    <footer class="app-footer">
      <p>© 2025 DeFake学术图像造假检测平台</p>
    </footer>
  </div>
</template>

<style scoped>
.detection-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f8faff;
  color: #2c3e50;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.app-header {
  background: linear-gradient(135deg, #1976d2, #1e88e5);
  color: white;
  padding: 2rem 0;
  position: relative;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.app-header::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 70%);
  pointer-events: none;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 2rem;
  position: relative;
  z-index: 10;
}

.app-title {
  margin: 0;
  font-size: 2.5rem;
  font-weight: 700;
  letter-spacing: -0.5px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.app-subtitle {
  margin: 0.75rem 0 0;
  font-weight: 300;
  font-size: 1.1rem;
  opacity: 0.9;
  max-width: 600px;
}

.main-content {
  display: flex;
  flex: 1;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  padding: 2rem;
  gap: 2rem;
}

@media (max-width: 1024px) {
  .main-content {
    flex-direction: column;
  }
}

.section-title {
  color: #1976d2;
  border-bottom: 2px solid #e3f2fd;
  padding-bottom: 0.75rem;
  margin-top: 0;
  font-weight: 600;
  font-size: 1.5rem;
}

.upload-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.metadata-section {
  flex: 1;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  padding: 2rem;
  display: flex;
  flex-direction: column;
}

.drop-zone {
  background-color: white;
  border: 2px dashed #b0c4de;
  border-radius: 12px;
  padding: 2rem;
  text-align: center;
  transition: all 0.3s ease;
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
}

.drop-zone-active {
  border-color: #1976d2;
  background-color: #e3f2fd;
}

.drop-zone-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.icon {
  font-size: 3rem;
  color: #1976d2;
}

.text {
  color: #546e7a;
}

.upload-btn {
  background-color: #1976d2;
  color: white;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s;
  border: none;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.upload-btn:hover {
  background-color: #1565c0;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.hidden-input {
  display: none;
}

.supported-formats {
  font-size: 0.85rem;
  color: #90a4ae;
  margin-top: 0.75rem;
}

.file-list {
  width: 100%;
  text-align: left;
}

.file-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.25rem;
}

.file-list-header h3 {
  margin: 0;
  color: #1976d2;
  font-weight: 600;
}

.text-btn {
  background: none;
  border: none;
  color: #f44336;
  cursor: pointer;
  font-size: 0.875rem;
  font-weight: 500;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.text-btn:hover {
  background-color: #ffebee;
}

.file-item {
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem;
  border-radius: 8px;
  background-color: #f5f9ff;
  margin-bottom: 0.75rem;
  cursor: pointer;
  transition: all 0.2s;
  border: 2px solid transparent;
}

.file-item:hover {
  background-color: #e3f2fd;
}

.file-selected {
  border-color: #1976d2;
  background-color: #e3f2fd;
}

.file-icon {
  font-size: 1.75rem;
  margin-right: 1rem;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.file-icon.large {
  font-size: 2.5rem;
  width: 60px;
  height: 60px;
}

.file-info {
  flex: 1;
}

.file-name {
  font-weight: 500;
  margin-bottom: 0.25rem;
  color: #37474f;
  word-break: break-all;
}

.file-meta {
  display: flex;
  font-size: 0.75rem;
  color: #78909c;
  gap: 0.75rem;
}

.file-type {
  background-color: rgba(25, 118, 210, 0.1);
  color: #1976d2;
  padding: 0.15rem 0.5rem;
  border-radius: 100px;
  font-weight: 500;
}

.remove-btn {
  background: none;
  border: none;
  color: #e53935;
  font-size: 1.25rem;
  cursor: pointer;
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.2s;
}

.remove-btn:hover {
  background-color: #ffebee;
}

.add-more-btn {
  padding: 0.75rem;
  width: 100%;
  margin-top: 0.75rem;
  text-align: center;
  background-color: #e3f2fd;
  color: #1976d2;
  font-size: 0.875rem;
  border-radius: 8px;
  transition: all 0.2s;
  font-weight: 500;
}

.add-more-btn:hover {
  background-color: #bbdefb;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.detect-btn {
  background-color: #1976d2;
  color: white;
  padding: 1rem;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 600;
  border: none;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.detect-btn:hover:not(:disabled) {
  background-color: #1565c0;
  transform: translateY(-1px);
  box-shadow: 0 6px 10px rgba(0, 0, 0, 0.15);
}

.detect-btn:disabled {
  background-color: #b0bec5;
  cursor: not-allowed;
  box-shadow: none;
}

.upload-progress {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background-color: #e0e0e0;
  border-radius: 100px;
  overflow: hidden;
}

.progress {
  height: 100%;
  background-color: #1976d2;
  transition: width 0.3s ease;
}

.progress-text {
  text-align: center;
  color: #1976d2;
  font-weight: 500;
  font-size: 0.875rem;
}

.selected-file-info {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.25rem;
  background-color: #f5f9ff;
  border-radius: 10px;
  margin-bottom: 1.5rem;
}

.file-details {
  margin: 0.25rem 0 0;
  color: #78909c;
  font-size: 0.875rem;
}

.metadata-form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  font-weight: 500;
  color: #455a64;
  font-size: 0.9rem;
}

.form-group input, .form-group textarea {
  padding: 0.75rem;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  font-size: 1rem;
  transition: border 0.2s;
}

.form-group input:focus, .form-group textarea:focus {
  border-color: #1976d2;
  outline: none;
  box-shadow: 0 0 0 3px rgba(25, 118, 210, 0.1);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 1rem;
}

.save-btn {
  background-color: #1976d2;
  color: white;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 6px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.save-btn:hover {
  background-color: #1565c0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.empty-metadata {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #b0bec5;
  text-align: center;
  padding: 2rem;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.empty-text {
  font-size: 1.1rem;
  line-height: 1.6;
}

.app-footer {
  background-color: #1976d2;
  color: rgba(255, 255, 255, 0.8);
  text-align: center;
  padding: 1.25rem;
  font-size: 0.9rem;
}

@media (max-width: 768px) {
  .app-title {
    font-size: 2rem;
  }
  
  .main-content {
    padding: 1.5rem;
  }
}
</style>
