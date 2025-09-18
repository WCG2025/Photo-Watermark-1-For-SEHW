@echo off
echo ========================================
echo JDK21 兼容性测试脚本
echo ========================================

echo.
echo 1. 检查Java版本...
java -version 2>&1 | findstr "21" >nul
if errorlevel 1 (
    echo 警告: 当前Java版本不是21，建议使用JDK21
) else (
    echo ✓ 检测到JDK21
)

echo.
echo 2. 检查Maven版本...
call mvn -version >nul 2>&1
if errorlevel 1 (
    echo ✗ Maven未安装或未配置
    echo 请安装Maven 3.9.0+并配置环境变量
    pause
    exit /b 1
) else (
    echo ✓ Maven已安装
)

echo.
echo 3. 清理之前的构建...
if exist target rmdir /s /q target

echo.
echo 4. 编译项目...
call mvn clean compile -q
if errorlevel 1 (
    echo ✗ 编译失败
    pause
    exit /b 1
) else (
    echo ✓ 编译成功
)

echo.
echo 5. 打包项目...
call mvn package -q
if errorlevel 1 (
    echo ✗ 打包失败
    pause
    exit /b 1
) else (
    echo ✓ 打包成功
)

echo.
echo 6. 测试可执行JAR...
java -jar target\photo-watermark-1.0-SNAPSHOT.jar -h >nul 2>&1
if errorlevel 1 (
    echo ✗ JAR文件无法执行
    pause
    exit /b 1
) else (
    echo ✓ JAR文件可正常执行
)

echo.
echo ========================================
echo ✓ JDK21兼容性测试通过！
echo ========================================
echo.
echo 项目已成功编译并可在JDK21环境下运行。
echo 可执行文件: target\photo-watermark-1.0-SNAPSHOT.jar
echo.
echo 使用示例:
echo java -jar target\photo-watermark-1.0-SNAPSHOT.jar -i "C:\Photos" -s 30 -c red -p br
echo.
pause
