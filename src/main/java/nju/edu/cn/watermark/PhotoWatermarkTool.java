package nju.edu.cn.watermark;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片水印工具主类
 */
public class PhotoWatermarkTool {
    
    public static void main(String[] args) {
        System.out.println("=== 图片水印工具 ===");
        System.out.println("基于EXIF拍摄时间为图片添加日期水印\n");
        
        // 解析命令行参数
        CommandLineOptions options = new CommandLineOptions();
        if (!options.parseArguments(args)) {
            System.exit(1);
        }
        
        // 获取输入路径
        File inputPath = new File(options.getInputPath());
        if (!inputPath.exists()) {
            System.err.println("错误: 输入路径不存在: " + options.getInputPath());
            System.exit(1);
        }
        
        // 收集所有图片文件
        List<File> imageFiles = new ArrayList<>();
        collectImageFiles(inputPath, imageFiles);
        
        if (imageFiles.isEmpty()) {
            System.out.println("在指定路径中未找到支持的图片文件。");
            System.out.println("支持的格式: JPG, JPEG, PNG, TIFF, BMP");
            System.exit(0);
        }
        
        System.out.println("找到 " + imageFiles.size() + " 个图片文件");
        
        // 创建输出目录
        File outputDir = createOutputDirectory(inputPath);
        if (outputDir == null) {
            System.err.println("无法创建输出目录");
            System.exit(1);
        }
        
        System.out.println("输出目录: " + outputDir.getAbsolutePath());
        System.out.println("水印设置: 字体大小=" + options.getFontSize() + 
                         ", 颜色=" + getColorDescription(options.getColor()) + 
                         ", 位置=" + options.getPosition().getDescription());
        System.out.println();
        
        // 处理每个图片文件
        int successCount = 0;
        int skippedCount = 0;
        int errorCount = 0;
        
        for (File imageFile : imageFiles) {
            try {
                ProcessResult result = processImage(imageFile, outputDir, options);
                switch (result) {
                    case SUCCESS:
                        successCount++;
                        System.out.println("✓ " + imageFile.getName() + " - 处理成功");
                        break;
                    case SKIPPED_NO_EXIF:
                        skippedCount++;
                        System.out.println("⚠ " + imageFile.getName() + " - 跳过（无EXIF拍摄时间）");
                        break;
                    case ERROR:
                        errorCount++;
                        break;
                }
            } catch (Exception e) {
                errorCount++;
                System.err.println("✗ " + imageFile.getName() + " - 处理失败: " + e.getMessage());
            }
        }
        
        // 打印处理结果统计
        System.out.println("\n=== 处理完成 ===");
        System.out.println("总计: " + imageFiles.size() + " 个文件");
        System.out.println("成功: " + successCount + " 个");
        System.out.println("跳过: " + skippedCount + " 个");
        System.out.println("失败: " + errorCount + " 个");
        
        if (successCount > 0) {
            System.out.println("\n已处理的图片保存在: " + outputDir.getAbsolutePath());
        }
    }
    
    /**
     * 处理结果枚举
     */
    private enum ProcessResult {
        SUCCESS,
        SKIPPED_NO_EXIF,
        ERROR
    }
    
    /**
     * 递归收集所有图片文件
     */
    private static void collectImageFiles(File path, List<File> imageFiles) {
        if (path.isFile()) {
            if (ExifReader.isSupportedImageFile(path)) {
                imageFiles.add(path);
            }
        } else if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    collectImageFiles(file, imageFiles);
                }
            }
        }
    }
    
    /**
     * 创建输出目录
     */
    private static File createOutputDirectory(File inputPath) {
        File outputDir;
        
        if (inputPath.isFile()) {
            // 如果输入是单个文件，在其父目录下创建输出目录
            File parentDir = inputPath.getParentFile();
            String dirName = parentDir.getName() + "_watermark";
            outputDir = new File(parentDir, dirName);
        } else {
            // 如果输入是目录，在该目录下创建子目录
            String dirName = inputPath.getName() + "_watermark";
            outputDir = new File(inputPath, dirName);
        }
        
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            return null;
        }
        
        return outputDir;
    }
    
    /**
     * 处理单个图片文件
     */
    private static ProcessResult processImage(File imageFile, File outputDir, CommandLineOptions options) {
        try {
            // 读取EXIF拍摄时间
            String dateTaken = ExifReader.getDateTaken(imageFile);
            if (dateTaken == null) {
                return ProcessResult.SKIPPED_NO_EXIF;
            }
            
            // 创建输出文件
            File outputFile = new File(outputDir, imageFile.getName());
            
            // 添加水印
            WatermarkRenderer.addWatermark(
                imageFile,
                outputFile,
                dateTaken,
                options.getFontSize(),
                options.getColor(),
                options.getPosition()
            );
            
            return ProcessResult.SUCCESS;
            
        } catch (IOException e) {
            System.err.println("✗ " + imageFile.getName() + " - 处理失败: " + e.getMessage());
            return ProcessResult.ERROR;
        }
    }
    
    /**
     * 获取颜色描述
     */
    private static String getColorDescription(java.awt.Color color) {
        if (color.equals(java.awt.Color.WHITE)) return "白色";
        if (color.equals(java.awt.Color.BLACK)) return "黑色";
        if (color.equals(java.awt.Color.RED)) return "红色";
        if (color.equals(java.awt.Color.GREEN)) return "绿色";
        if (color.equals(java.awt.Color.BLUE)) return "蓝色";
        if (color.equals(java.awt.Color.YELLOW)) return "黄色";
        if (color.equals(java.awt.Color.ORANGE)) return "橙色";
        if (color.equals(java.awt.Color.PINK)) return "粉色";
        
        return String.format("RGB(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue());
    }
}
