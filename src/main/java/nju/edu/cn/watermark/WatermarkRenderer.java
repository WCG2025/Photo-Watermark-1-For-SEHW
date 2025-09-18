package nju.edu.cn.watermark;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 水印渲染器，用于在图片上绘制文本水印
 */
public class WatermarkRenderer {
    
    /**
     * 水印位置枚举
     */
    public enum WatermarkPosition {
        TOP_LEFT("左上角"),
        TOP_CENTER("顶部居中"),
        TOP_RIGHT("右上角"),
        CENTER_LEFT("左侧居中"),
        CENTER("居中"),
        CENTER_RIGHT("右侧居中"),
        BOTTOM_LEFT("左下角"),
        BOTTOM_CENTER("底部居中"),
        BOTTOM_RIGHT("右下角");
        
        private final String description;
        
        WatermarkPosition(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
        
        /**
         * 从字符串解析位置
         */
        public static WatermarkPosition fromString(String position) {
            switch (position.toLowerCase()) {
                case "topleft":
                case "tl":
                case "左上":
                case "左上角":
                    return TOP_LEFT;
                case "topcenter":
                case "tc":
                case "顶部居中":
                case "上中":
                    return TOP_CENTER;
                case "topright":
                case "tr":
                case "右上":
                case "右上角":
                    return TOP_RIGHT;
                case "centerleft":
                case "cl":
                case "左侧居中":
                case "左中":
                    return CENTER_LEFT;
                case "center":
                case "c":
                case "居中":
                case "中心":
                    return CENTER;
                case "centerright":
                case "cr":
                case "右侧居中":
                case "右中":
                    return CENTER_RIGHT;
                case "bottomleft":
                case "bl":
                case "左下":
                case "左下角":
                    return BOTTOM_LEFT;
                case "bottomcenter":
                case "bc":
                case "底部居中":
                case "下中":
                    return BOTTOM_CENTER;
                case "bottomright":
                case "br":
                case "右下":
                case "右下角":
                default:
                    return BOTTOM_RIGHT;
            }
        }
    }
    
    /**
     * 在图片上添加文本水印
     * 
     * @param inputFile 输入图片文件
     * @param outputFile 输出图片文件
     * @param watermarkText 水印文本
     * @param fontSize 字体大小
     * @param color 字体颜色
     * @param position 水印位置
     * @throws IOException 如果读写图片失败
     */
    public static void addWatermark(File inputFile, File outputFile, String watermarkText, 
                                  int fontSize, Color color, WatermarkPosition position) throws IOException {
        
        // 读取原始图片
        BufferedImage originalImage = ImageIO.read(inputFile);
        if (originalImage == null) {
            throw new IOException("无法读取图片文件: " + inputFile.getName());
        }
        
        // 创建新的BufferedImage用于绘制水印
        BufferedImage watermarkedImage = new BufferedImage(
            originalImage.getWidth(), 
            originalImage.getHeight(), 
            BufferedImage.TYPE_INT_RGB
        );
        
        // 获取Graphics2D对象
        Graphics2D g2d = watermarkedImage.createGraphics();
        
        // 绘制原始图片
        g2d.drawImage(originalImage, 0, 0, null);
        
        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // 设置字体
        Font font = new Font("微软雅黑", Font.BOLD, fontSize);
        g2d.setFont(font);
        
        // 获取字体度量
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(watermarkText);
        int textHeight = fm.getHeight();
        
        // 计算水印位置
        Point watermarkPosition = calculateWatermarkPosition(
            originalImage.getWidth(), 
            originalImage.getHeight(), 
            textWidth, 
            textHeight, 
            position
        );
        
        // 添加阴影效果（可选）
        g2d.setColor(new Color(0, 0, 0, 100)); // 半透明黑色阴影
        g2d.drawString(watermarkText, watermarkPosition.x + 2, watermarkPosition.y + 2);
        
        // 绘制主要文本
        g2d.setColor(color);
        g2d.drawString(watermarkText, watermarkPosition.x, watermarkPosition.y);
        
        // 释放Graphics2D资源
        g2d.dispose();
        
        // 保存图片
        String format = getImageFormat(outputFile.getName());
        ImageIO.write(watermarkedImage, format, outputFile);
    }
    
    /**
     * 计算水印文本的绘制位置
     */
    private static Point calculateWatermarkPosition(int imageWidth, int imageHeight, 
                                                  int textWidth, int textHeight, 
                                                  WatermarkPosition position) {
        int margin = 20; // 边距
        int x, y;
        
        switch (position) {
            case TOP_LEFT:
                x = margin;
                y = margin + textHeight;
                break;
            case TOP_CENTER:
                x = (imageWidth - textWidth) / 2;
                y = margin + textHeight;
                break;
            case TOP_RIGHT:
                x = imageWidth - textWidth - margin;
                y = margin + textHeight;
                break;
            case CENTER_LEFT:
                x = margin;
                y = (imageHeight + textHeight) / 2;
                break;
            case CENTER:
                x = (imageWidth - textWidth) / 2;
                y = (imageHeight + textHeight) / 2;
                break;
            case CENTER_RIGHT:
                x = imageWidth - textWidth - margin;
                y = (imageHeight + textHeight) / 2;
                break;
            case BOTTOM_LEFT:
                x = margin;
                y = imageHeight - margin;
                break;
            case BOTTOM_CENTER:
                x = (imageWidth - textWidth) / 2;
                y = imageHeight - margin;
                break;
            case BOTTOM_RIGHT:
            default:
                x = imageWidth - textWidth - margin;
                y = imageHeight - margin;
                break;
        }
        
        return new Point(x, y);
    }
    
    /**
     * 根据文件名获取图片格式
     */
    private static String getImageFormat(String fileName) {
        String name = fileName.toLowerCase();
        if (name.endsWith(".png")) {
            return "png";
        } else if (name.endsWith(".jpg") || name.endsWith(".jpeg")) {
            return "jpg";
        } else if (name.endsWith(".bmp")) {
            return "bmp";
        } else if (name.endsWith(".tiff") || name.endsWith(".tif")) {
            return "tiff";
        } else {
            return "jpg"; // 默认格式
        }
    }
    
    /**
     * 解析颜色字符串
     * 
     * @param colorStr 颜色字符串，支持格式：red, blue, #FF0000, 255,0,0
     * @return Color对象
     */
    public static Color parseColor(String colorStr) {
        if (colorStr == null || colorStr.trim().isEmpty()) {
            return Color.WHITE;
        }
        
        colorStr = colorStr.trim().toLowerCase();
        
        // 预定义颜色
        switch (colorStr) {
            case "red":
            case "红色":
                return Color.RED;
            case "green":
            case "绿色":
                return Color.GREEN;
            case "blue":
            case "蓝色":
                return Color.BLUE;
            case "white":
            case "白色":
                return Color.WHITE;
            case "black":
            case "黑色":
                return Color.BLACK;
            case "yellow":
            case "黄色":
                return Color.YELLOW;
            case "orange":
            case "橙色":
                return Color.ORANGE;
            case "pink":
            case "粉色":
                return Color.PINK;
        }
        
        // 十六进制颜色 #RRGGBB
        if (colorStr.startsWith("#") && colorStr.length() == 7) {
            try {
                return Color.decode(colorStr);
            } catch (NumberFormatException e) {
                System.err.println("无效的十六进制颜色格式: " + colorStr);
                return Color.WHITE;
            }
        }
        
        // RGB格式 r,g,b
        if (colorStr.contains(",")) {
            try {
                String[] rgb = colorStr.split(",");
                if (rgb.length == 3) {
                    int r = Integer.parseInt(rgb[0].trim());
                    int g = Integer.parseInt(rgb[1].trim());
                    int b = Integer.parseInt(rgb[2].trim());
                    return new Color(r, g, b);
                }
            } catch (NumberFormatException e) {
                System.err.println("无效的RGB颜色格式: " + colorStr);
                return Color.WHITE;
            }
        }
        
        System.err.println("无法识别的颜色格式: " + colorStr + "，使用默认白色");
        return Color.WHITE;
    }
}
