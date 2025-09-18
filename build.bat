@echo off
echo ========================================
echo 图片水印工具 - 构建脚本
echo ========================================

echo.
echo 正在编译项目...
call mvn clean compile
if errorlevel 1 (
    echo 编译失败！
    pause
    exit /b 1
)

echo.
echo 正在打包项目...
call mvn package
if errorlevel 1 (
    echo 打包失败！
    pause
    exit /b 1
)

echo.
echo ========================================
echo 构建完成！
echo ========================================
echo.
echo 可执行文件位置: target\photo-watermark-1.0-SNAPSHOT.jar
echo.
echo 使用示例:
echo java -jar target\photo-watermark-1.0-SNAPSHOT.jar -i "C:\Photos" -s 30 -c red -p br
echo.
echo 查看帮助:
echo java -jar target\photo-watermark-1.0-SNAPSHOT.jar -h
echo.
pause
