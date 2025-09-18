package nju.edu.cn.watermark;

import org.apache.commons.cli.*;
import java.awt.Color;

/**
 * 命令行选项解析器
 */
public class CommandLineOptions {
    
    private String inputPath;
    private int fontSize = 24;
    private Color color = Color.WHITE;
    private WatermarkRenderer.WatermarkPosition position = WatermarkRenderer.WatermarkPosition.BOTTOM_RIGHT;
    
    public CommandLineOptions() {}
    
    /**
     * 解析命令行参数
     * 
     * @param args 命令行参数
     * @return 解析成功返回true，失败返回false
     */
    public boolean parseArguments(String[] args) {
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();
        
        try {
            CommandLine cmd = parser.parse(options, args);
            
            // 检查是否需要显示帮助
            if (cmd.hasOption("h")) {
                printHelp(options);
                return false;
            }
            
            // 输入路径（必需）
            if (!cmd.hasOption("i")) {
                System.err.println("错误: 必须指定输入路径 (-i 或 --input)");
                printHelp(options);
                return false;
            }
            inputPath = cmd.getOptionValue("i");
            
            // 字体大小
            if (cmd.hasOption("s")) {
                try {
                    fontSize = Integer.parseInt(cmd.getOptionValue("s"));
                    if (fontSize <= 0 || fontSize > 200) {
                        System.err.println("错误: 字体大小必须在1-200之间");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("错误: 无效的字体大小格式");
                    return false;
                }
            }
            
            // 颜色
            if (cmd.hasOption("c")) {
                color = WatermarkRenderer.parseColor(cmd.getOptionValue("c"));
            }
            
            // 位置
            if (cmd.hasOption("p")) {
                position = WatermarkRenderer.WatermarkPosition.fromString(cmd.getOptionValue("p"));
            }
            
            return true;
            
        } catch (ParseException e) {
            System.err.println("参数解析错误: " + e.getMessage());
            printHelp(options);
            return false;
        }
    }
    
    /**
     * 创建命令行选项
     */
    private Options createOptions() {
        Options options = new Options();
        
        options.addOption(Option.builder("i")
                .longOpt("input")
                .hasArg()
                .required()
                .desc("输入图片文件或目录路径（必需）")
                .build());
        
        options.addOption(Option.builder("s")
                .longOpt("size")
                .hasArg()
                .desc("字体大小 (默认: 24)")
                .build());
        
        options.addOption(Option.builder("c")
                .longOpt("color")
                .hasArg()
                .desc("字体颜色，支持格式：red, blue, white, #FF0000, 255,0,0 (默认: white)")
                .build());
        
        options.addOption(Option.builder("p")
                .longOpt("position")
                .hasArg()
                .desc("水印位置：tl(左上), tc(顶部居中), tr(右上), cl(左侧居中), c(居中), cr(右侧居中), bl(左下), bc(底部居中), br(右下) (默认: br)")
                .build());
        
        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("显示帮助信息")
                .build());
        
        return options;
    }
    
    /**
     * 打印帮助信息
     */
    private void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar photo-watermark.jar", 
                          "\n图片水印工具 - 基于EXIF拍摄时间为图片添加日期水印\n\n选项:", 
                          options, 
                          "\n示例:\n" +
                          "  java -jar photo-watermark.jar -i /path/to/photos\n" +
                          "  java -jar photo-watermark.jar -i /path/to/photos -s 30 -c red -p tl\n" +
                          "  java -jar photo-watermark.jar -i photo.jpg -s 24 -c \"255,255,255\" -p br\n\n" +
                          "支持的图片格式: JPG, JPEG, PNG, TIFF, BMP\n" +
                          "输出目录: 原目录下的 [原目录名]_watermark 子目录\n");
    }
    
    // Getters
    public String getInputPath() {
        return inputPath;
    }
    
    public int getFontSize() {
        return fontSize;
    }
    
    public Color getColor() {
        return color;
    }
    
    public WatermarkRenderer.WatermarkPosition getPosition() {
        return position;
    }
}
