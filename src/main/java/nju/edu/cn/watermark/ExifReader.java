package nju.edu.cn.watermark;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * EXIF信息读取器，用于从图片文件中提取拍摄时间信息
 */
public class ExifReader {
    
    /**
     * 从图片文件中读取拍摄日期
     * 
     * @param imageFile 图片文件
     * @return 格式化的拍摄日期字符串（yyyy-MM-dd），如果无法读取则返回null
     */
    public static String getDateTaken(File imageFile) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
            ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            
            if (exifDirectory != null) {
                // 尝试获取原始拍摄日期时间
                if (exifDirectory.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
                    String dateTimeString = exifDirectory.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                    return parseDateFromExif(dateTimeString);
                }
                
                // 如果原始拍摄时间不存在，尝试获取修改时间
                if (exifDirectory.containsTag(ExifSubIFDDirectory.TAG_DATETIME)) {
                    String dateTimeString = exifDirectory.getString(ExifSubIFDDirectory.TAG_DATETIME);
                    return parseDateFromExif(dateTimeString);
                }
            }
        } catch (Exception e) {
            System.err.println("读取EXIF信息失败: " + imageFile.getName() + " - " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * 解析EXIF中的日期时间字符串
     * 
     * @param exifDateTime EXIF日期时间字符串，格式通常为 "yyyy:MM:dd HH:mm:ss"
     * @return 格式化的日期字符串 "yyyy-MM-dd"，解析失败返回null
     */
    private static String parseDateFromExif(String exifDateTime) {
        if (exifDateTime == null || exifDateTime.trim().isEmpty()) {
            return null;
        }
        
        try {
            // EXIF日期格式通常是 "yyyy:MM:dd HH:mm:ss"
            String datePart = exifDateTime.split(" ")[0]; // 只取日期部分
            String normalizedDate = datePart.replace(":", "-"); // 将冒号替换为短横线
            
            // 验证日期格式
            LocalDate.parse(normalizedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            return normalizedDate;
        } catch (DateTimeParseException | ArrayIndexOutOfBoundsException e) {
            System.err.println("日期解析失败: " + exifDateTime + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 检查文件是否为支持的图片格式
     * 
     * @param file 文件
     * @return 如果是支持的图片格式返回true
     */
    public static boolean isSupportedImageFile(File file) {
        if (!file.isFile()) {
            return false;
        }
        
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || 
               name.endsWith(".png") || name.endsWith(".tiff") || 
               name.endsWith(".tif") || name.endsWith(".bmp");
    }
}
