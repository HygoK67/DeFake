<script setup lang="ts">
import { ref, reactive, computed } from "vue";
import { FileWithMetadata, Metadata } from "@/types/document";
import { uploadFile, uploadFigure, getFigure, uploadPaper, getPaperFigures } from "@/api/document";
import { useRouter } from "vue-router";

defineOptions({
  name: "ForensicsDetection"
});

// 支持的文件类型
const supportedFormats = [
  ".jpg",
  ".jpeg",
  ".png",
  ".doc",
  ".docx",
  ".pdf"
];

// 文件上传状态
const fileList = ref<FileWithMetadata[]>([]);
const dragging = ref(false);
const uploading = ref(false);
const uploadProgress = ref(0);
const selectedFile = ref<FileWithMetadata | null>(null);
const validationError = ref<string | null>(null);

// 当前编辑的元数据
const currentMetadata = reactive<Metadata>({
  title: "",
  authorList: [],
  abstracT: "",
  doi: "",
  publishAt: ""
});

// 作者输入字段(用于UI展示)
const authorInput = ref("");

const metadataStorage = ref<WeakMap<File, Metadata>>(new WeakMap());

// 处理作者输入转换为数组
const processAuthorList = () => {
  return authorInput.value
    .split(',')
    .map(author => author.trim())
    .filter(author => author.length > 0);
};

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
    // 重置input值，确保相同文件可以再次选择
    input.value = '';
  }
};

// 处理文件
const handleFiles = (files: FileList) => {
  Array.from(files).forEach(file => {
    const extension = "." + file.name.split(".").pop()?.toLowerCase();
    if (supportedFormats.includes(extension)) {
      const fileWithMetadata: FileWithMetadata = {
        file,
        metadata: {
          title: "",
          authorList: [],
          abstracT: "",
          doi: "",
          publishAt: ""
        }
      };
      fileList.value.push(fileWithMetadata);
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
const selectFile = (fileWithMetadata: FileWithMetadata) => {
  selectedFile.value = fileWithMetadata;
  const savedMetadata = metadataStorage.value.get(fileWithMetadata.file);
  if (savedMetadata) {
    fileWithMetadata.metadata = { ...savedMetadata };
    Object.assign(currentMetadata, savedMetadata);
    // 从作者列表获取逗号分隔的字符串用于输入框显示
    authorInput.value = savedMetadata.authorList.join(', ');
  } else {
    Object.assign(currentMetadata, fileWithMetadata.metadata);
    authorInput.value = '';
  }
};

// 验证元数据是否完整
const validateMetadata = (metadata: Metadata): boolean => {
  // 如果是图像文件，不需要验证元数据
  if (selectedFile.value && isImageFile(selectedFile.value.file.name)) {
    return true;
  }
  
  if (!metadata.title.trim()) return false;
  if (!metadata.authorList || metadata.authorList.length === 0) return false;
  if (!metadata.abstracT.trim()) return false;
  if (!metadata.doi.trim()) return false;
  if (!metadata.publishAt.trim()) return false;
  return true;
};

// 获取未完成元数据的文件
const getFilesWithIncompleteMetadata = (): FileWithMetadata[] => {
  return fileList.value.filter(
    fileItem => !isImageFile(fileItem.file.name) && !validateMetadata(fileItem.metadata)
  );
};

// 保存元数据
const saveMetadata = () => {
  if (!selectedFile.value) return;

  // 处理作者列表
  currentMetadata.authorList = processAuthorList();

  if (!validateMetadata(currentMetadata)) {
    validationError.value = "请完整填写所有元数据字段";
    return;
  }

  validationError.value = null;
  selectedFile.value.metadata = { ...currentMetadata };
  metadataStorage.value.set(selectedFile.value.file, { ...currentMetadata });
  alert("元数据已成功保存");
};

// 获取router实例
const router = useRouter();

// 判断是否为图片文件
const isImageFile = (filename: string): boolean => {
  const extension = filename.split(".").pop()?.toLowerCase();
  return ['jpg', 'jpeg', 'png'].includes(extension || '');
};

// 判断当前选中文件是否为图像文件
const isSelectedFileImage = computed(() => {
  return selectedFile.value && isImageFile(selectedFile.value.file.name);
});

// 文件是否需要元数据
const fileNeedsMetadata = (filename: string): boolean => {
  return !isImageFile(filename);
};

// 文件是否有完整元数据或不需要元数据
const fileHasValidMetadata = (fileItem: FileWithMetadata): boolean => {
  // 图片文件不需要元数据
  if (isImageFile(fileItem.file.name)) {
    return true;
  }
  // 非图片文件需要验证元数据完整性
  return validateMetadata(fileItem.metadata);
};

// 上传并检测文件
const uploadAndDetect = async () => {
  if (fileList.value.length === 0) return;

  // 只检查需要元数据的文件
  const incompleteFiles = getFilesWithIncompleteMetadata();
  if (incompleteFiles.length > 0) {
    alert(
      `有${incompleteFiles.length}个文件缺少完整元数据信息，请完成所有文件的元数据填写。`
    );
    if (incompleteFiles[0] !== selectedFile.value) {
      selectFile(incompleteFiles[0]);
    }
    return;
  }

  uploading.value = true;
  uploadProgress.value = 0;

  try {
    uploadProgress.value = 5;
    console.log("开始顺序上传文件");
    
    const allExtractedImages = [];
    const totalFiles = fileList.value.length;
    
    for (let i = 0; i < fileList.value.length; i++) {
      const fileWithMeta = fileList.value[i];
      const file = fileWithMeta.file;
      
      const fileStartProgress = 5 + Math.floor((i / totalFiles) * 90);
      const fileEndProgress = 5 + Math.floor(((i + 1) / totalFiles) * 90);
      uploadProgress.value = fileStartProgress;
      
      // 根据不同文件类型执行不同的上传流程
      if (isImageFile(file.name)) {
        // 图片文件上传流程
        // 1. 上传文件获取URL
        const fileUrl = await uploadFile(file);
        
        // 2. 上传图片信息获取图片ID
        const figureResult = await uploadFigure({ filePath: fileUrl });
        if (figureResult.code === '200' && figureResult.data) {
          const figureId = figureResult.data;
          
          // 3. 获取图片URL
          const imageUrl = await getFigure(figureId);
          
          allExtractedImages.push({
            id: figureId,
            filePath: imageUrl,
            sourceFile: file.name,
            sourceType: "图像"
          });
        }
      } else {
        // 论文文件上传流程
        // 1. 上传文件获取URL
        const fileUrl = await uploadFile(file);
        
        // 2. 上传论文信息
        const paperData = {
          ...fileWithMeta.metadata,
          filePath: fileUrl
        };
        
        const paperResult = await uploadPaper(paperData);
        if (paperResult.code === '200' && paperResult.data) {
          const paperId = paperResult.data;
          
          // 3. 获取论文关联图片
          const figures = await getPaperFigures(paperId);
          
          // 添加文件来源信息到每张图片
          const imagesWithSource = figures.map(figure => ({
            ...figure,
            sourceFile: file.name,
            sourceType: getFileType(file.name)
          }));
          
          allExtractedImages.push(...imagesWithSource);
        }
      }
      
      uploadProgress.value = fileEndProgress;
    }
    
    console.log("所有文件上传完成，共提取图片:", allExtractedImages.length);
    uploadProgress.value = 100;
    
    setTimeout(() => {
      uploading.value = false;
      
      // 使用query参数传递数据，避免params的问题
      const imagesData = encodeURIComponent(JSON.stringify(allExtractedImages));
      
      // 同时在sessionStorage中存一份，作为备用
      sessionStorage.setItem('uploadedImages', JSON.stringify(allExtractedImages));
      
      router.push({
        name: 'ImageSelection',
        query: { images: imagesData }
      });
    }, 500);
  } catch (error) {
    console.error("上传失败:", error);
    alert("上传过程中发生错误，请重试!");
    uploading.value = false;
  }
};

// 清空
const clearAll = () => {
  fileList.value = [];
  selectedFile.value = null;
  uploadProgress.value = 0;
  Object.keys(currentMetadata).forEach(key => {
    currentMetadata[key] = "";
  });
  // 不清除元数据存储，因为可能之后还会用到
};

// 生成文件大小显示
const formatFileSize = (size: number): string => {
  if (size < 1024) {
    return size + " B";
  } else if (size < 1024 * 1024) {
    return (size / 1024).toFixed(2) + " KB";
  } else {
    return (size / (1024 * 1024)).toFixed(2) + " MB";
  }
};

// 获取文件图标
const getFileIcon = (filename: string): string => {
  const extension = filename.split(".").pop()?.toLowerCase();

  switch (extension) {
    case "jpg":
    case "jpeg":
    case "png":
      return "🖼️";
    case "doc":
    case "docx":
      return "📄";
    case "pdf":
      return "📑";
    default:
      return "📁";
  }
};

// 获取文件类型
const getFileType = (filename: string): string => {
  const extension = filename.split(".").pop()?.toLowerCase();

  switch (extension) {
    case "jpg":
    case "jpeg":
    case "png":
      return "图像";
    case "doc":
    case "docx":
      return "Word文档";
    case "pdf":
      return "PDF文档";
    default:
      return "未知类型";
  }
};

// 文件元数据状态计算属性
const fileHasCompleteMetadata = computed(() => {
  if (!selectedFile.value) return false;
  return validateMetadata(selectedFile.value.metadata);
});
</script>

<template>
  <div class="detection-container">
    <header class="app-header">
      <div class="header-content">
        <h1 class="app-title">DeFake学术图像造假检测</h1>
        <p class="app-subtitle">
          支持图像、PDF和Word文档的多维度造假检测和归档管理
        </p>
      </div>
    </header>

    <main class="main-content">
      <!-- 左侧上传区域 -->
      <section class="upload-section">
        <div class="drop-zone" :class="{ 'drop-zone-active': dragging }" @dragover="handleDragOver"
          @dragleave="handleDragLeave" @drop="handleDrop">
          <div v-if="fileList.length === 0" class="drop-zone-content">
            <div class="icon">📁</div>
            <div class="text">拖放文件到此处或</div>
            <label class="upload-btn">
              选择文件
              <input type="file" multiple :accept="supportedFormats.join(',')" class="hidden-input"
                @change="handleFileChange" />
            </label>
            <div class="supported-formats">
              支持的格式: {{ supportedFormats.join(", ") }}
            </div>
          </div>

          <div v-else class="file-list">
            <div class="file-list-header">
              <h3>待检测文件 ({{ fileList.length }})</h3>
              <button class="text-btn" @click="clearAll">清空</button>
            </div>

            <div v-for="(fileItem, index) in fileList" :key="index" class="file-item" :class="{
              'file-selected': selectedFile === fileItem,
              'file-incomplete': fileNeedsMetadata(fileItem.file.name) && !validateMetadata(fileItem.metadata)
            }" @click="selectFile(fileItem)">
              <div class="file-icon">{{ getFileIcon(fileItem.file.name) }}</div>
              <div class="file-info">
                <div class="file-name">{{ fileItem.file.name }}</div>
                <div class="file-meta">
                  <span class="file-type">{{
                    getFileType(fileItem.file.name)
                  }}</span>
                  <span class="file-size">{{
                    formatFileSize(fileItem.file.size)
                  }}</span>
                  
                  <!-- 修改元数据状态标记 -->
                  <span v-if="isImageFile(fileItem.file.name)" class="no-metadata-needed">无需元数据</span>
                  <span v-else-if="validateMetadata(fileItem.metadata)" class="has-metadata">元数据完整</span>
                  <span v-else class="missing-metadata">缺少元数据</span>
                </div>
              </div>
              <button class="remove-btn" @click.stop="removeFile(index)">
                ×
              </button>
            </div>

            <label class="add-more-btn">
              添加更多
              <input type="file" multiple :accept="supportedFormats.join(',')" class="hidden-input"
                @change="handleFileChange" />
            </label>
          </div>
        </div>

        <!-- 上传按钮 -->
        <div class="action-buttons">
          <button class="detect-btn" :disabled="fileList.length === 0 || uploading" @click="uploadAndDetect">
            {{ uploading ? "检测中..." : "开始检测" }}
          </button>

          <div v-if="uploading" class="upload-progress">
            <div class="progress-bar">
              <div class="progress" :style="{ width: `${uploadProgress}%` }" />
            </div>
            <div class="progress-text">{{ uploadProgress }}%</div>
          </div>
        </div>
      </section>

      <!-- 右侧元数据区域 - 根据文件类型条件显示 -->
      <section v-if="selectedFile" class="metadata-section">
        <h2 class="section-title">
          {{ isSelectedFileImage ? '图像预览' : '文件元数据（所有字段必填）' }}
        </h2>

        <div class="metadata-content">
          <div class="selected-file-info">
            <div class="file-icon large">
              {{ getFileIcon(selectedFile.file.name) }}
            </div>
            <div>
              <h3 class="file-name">{{ selectedFile.file.name }}</h3>
              <p class="file-details">
                {{ getFileType(selectedFile.file.name) }} •
                {{ formatFileSize(selectedFile.file.size) }}
              </p>
            </div>
          </div>

          <!-- 图像文件显示预览 -->
          <div v-if="isSelectedFileImage" class="image-preview">
            <img v-if="selectedFile.file" :src="URL.createObjectURL(selectedFile.file)" alt="图像预览" />
            <p class="image-note">图像文件无需填写元数据</p>
          </div>

          <!-- 非图像文件显示元数据编辑表单 -->
          <form v-else class="metadata-form" @submit.prevent="saveMetadata">
            <div v-if="validationError" class="validation-error">
              {{ validationError }}
            </div>

            <div class="form-group">
              <label for="title">文档标题 <span class="required">*</span></label>
              <input id="title" v-model="currentMetadata.title" type="text" placeholder="输入文档标题"
                :class="{ 'field-error': !currentMetadata.title.trim() }" />
            </div>

            <div class="form-group">
              <label for="author">作者 (用逗号分隔) <span class="required">*</span></label>
              <input id="author" v-model="authorInput" type="text" placeholder="作者1, 作者2..."
                :class="{ 'field-error': !authorInput.trim() }" />
            </div>

            <div class="form-group">
              <label for="abstract">摘要 <span class="required">*</span></label>
              <textarea id="abstract" v-model="currentMetadata.abstracT" placeholder="输入文档摘要" rows="4"
                :class="{ 'field-error': !currentMetadata.abstracT.trim() }" />
            </div>

            <div class="form-group">
              <label for="doi">DOI <span class="required">*</span></label>
              <input id="doi" v-model="currentMetadata.doi" type="text" placeholder="输入DOI"
                :class="{ 'field-error': !currentMetadata.doi.trim() }" />
            </div>

            <div class="form-group">
              <label for="publishAt">发布日期 <span class="required">*</span></label>
              <input id="publishAt" v-model="currentMetadata.publishAt" type="date"
                :class="{ 'field-error': !currentMetadata.publishAt.trim() }" />
            </div>

            <div class="form-actions">
              <button type="submit" class="save-btn">保存元数据</button>
            </div>
          </form>
        </div>
      </section>

      <!-- 未选择文件时的右侧区域 -->
      <section v-else class="metadata-section">
        <h2 class="section-title">文件元数据</h2>
        <div class="empty-metadata">
          <div class="empty-icon">📝</div>
          <p class="empty-text">请从左侧选择一个文件</p>
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
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
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
  content: "";
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle,
      rgba(255, 255, 255, 0.1) 0%,
      rgba(255, 255, 255, 0) 70%);
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

.file-incomplete {
  border-left: 4px solid #ff5722;
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

.form-group input,
.form-group textarea {
  padding: 0.75rem;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  font-size: 1rem;
  transition: border 0.2s;
}

.form-group input:focus,
.form-group textarea:focus {
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

.has-metadata {
  background-color: rgba(76, 175, 80, 0.1);
  color: #4caf50;
  padding: 0.15rem 0.5rem;
  border-radius: 100px;
  font-weight: 500;
}

.missing-metadata {
  background-color: rgba(255, 87, 34, 0.1);
  color: #ff5722;
  padding: 0.15rem 0.5rem;
  border-radius: 100px;
  font-weight: 500;
}

.required {
  color: #f44336;
  margin-left: 2px;
}

.field-error {
  border-color: #f44336 !important;
  background-color: rgba(244, 67, 54, 0.03);
}

.validation-error {
  background-color: #ffebee;
  color: #d32f2f;
  padding: 0.75rem;
  border-radius: 6px;
  margin-bottom: 1rem;
  font-size: 0.9rem;
  font-weight: 500;
  border-left: 4px solid #f44336;
}

/* 新增图像预览样式 */
.image-preview {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 1rem;
}

.image-preview img {
  max-width: 100%;
  max-height: 300px;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.image-note {
  margin-top: 1rem;
  color: #1976d2;
  font-style: italic;
  background-color: #e3f2fd;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  font-size: 0.9rem;
}

/* 添加无需元数据的样式 */
.no-metadata-needed {
  background-color: rgba(33, 150, 243, 0.1);
  color: #2196f3;
  padding: 0.15rem 0.5rem;
  border-radius: 100px;
  font-weight: 500;
}
</style>
