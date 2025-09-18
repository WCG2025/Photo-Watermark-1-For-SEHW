# 图片水印工具 (Photo Watermark Tool)

一个基于Java开发的命令行图片水印工具，能够读取图片文件的EXIF信息中的拍摄时间，并将日期作为水印添加到图片上。

## 功能特性

- 📸 自动读取图片EXIF信息中的拍摄时间
- 🖼️ 支持多种图片格式：JPG, JPEG, PNG, TIFF, BMP
- 🎨 可自定义字体大小、颜色和位置
- 📁 支持单个文件或整个目录的批量处理
- 🛡️ 自动创建输出目录，保持原文件不变
- 📊 提供详细的处理结果统计

## 快速开始

### 环境要求
- Java 11 或更高版本
- Maven 3.6+（用于编译）

### 编译项目

**Windows:**
```cmd
build.bat
```

**Linux/Mac:**
```bash
./build.sh
```

### 基本使用

```bash
# 处理单个文件
java -jar target/photo-watermark-1.0-SNAPSHOT.jar -i photo.jpg

# 处理整个目录
java -jar target/photo-watermark-1.0-SNAPSHOT.jar -i /path/to/photos

# 自定义样式
java -jar target/photo-watermark-1.0-SNAPSHOT.jar -i photos/ -s 30 -c red -p tl
```

## 项目结构

```
Photo-Watermark-1-For-SEHW/
├── src/main/java/nju/edu/cn/watermark/
│   ├── PhotoWatermarkTool.java      # 主程序入口
│   ├── ExifReader.java              # EXIF信息读取器
│   ├── WatermarkRenderer.java       # 水印渲染器
│   └── CommandLineOptions.java     # 命令行参数解析
├── pom.xml                          # Maven配置文件
├── build.bat                        # Windows构建脚本
├── build.sh                         # Linux/Mac构建脚本
├── 运行文档.md                      # 详细使用文档
├── 快速开始.md                      # 快速开始指南
└── README.md                        # 项目说明
```

## 文档

- [快速开始.md](快速开始.md) - 快速开始指南
- [运行文档.md](运行文档.md) - 详细的使用文档和API说明

## 开发信息

- **开发语言**: Java 11
- **构建工具**: Maven
- **包名**: nju.edu.cn.watermark
- **主要依赖**:
  - Metadata Extractor (EXIF读取)
  - Apache Commons CLI (命令行解析)

## 许可证

本项目仅供学习和研究使用。
