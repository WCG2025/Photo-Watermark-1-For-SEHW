# JDK21 编译说明

## 环境确认

### 1. 检查Java版本

```bash
java -version
```

应该显示类似以下内容：
```
openjdk version "21.0.1" 2023-10-17
OpenJDK Runtime Environment (build 21.0.1+12-29)
OpenJDK 64-Bit Server VM (build 21.0.1+12-29, mixed mode, sharing)
```

### 2. 检查Maven版本

```bash
mvn -version
```

应该显示Maven 3.9.0或更高版本：
```
Apache Maven 3.9.5 (57804ffe001d7215b5e7bcb531cf83df38f93546)
Maven home: C:\Program Files\Apache\maven
Java version: 21.0.1, vendor: Eclipse Adoptium, runtime: C:\Program Files\Eclipse Adoptium\jdk-21.0.1.12-hotspot
```

## 编译步骤

### 方法1: 使用提供的构建脚本

**Windows:**
```cmd
build.bat
```

**Linux/Mac:**
```bash
./build.sh
```

### 方法2: 手动Maven命令

```bash
# 清理并编译
mvn clean compile

# 打包为可执行JAR
mvn clean package
```

## 可能遇到的问题及解决方案

### 问题1: Maven命令未找到

**错误信息:**
```
'mvn' 不是内部或外部命令，也不是可运行的程序或批处理文件。
```

**解决方案:**
1. 下载Maven: https://maven.apache.org/download.cgi
2. 解压到合适目录（如 `C:\Program Files\Apache\maven`）
3. 设置环境变量：
   - `MAVEN_HOME` = Maven安装目录
   - 在`PATH`中添加 `%MAVEN_HOME%\bin`
4. 重启命令提示符

### 问题2: Java版本不兼容

**错误信息:**
```
[ERROR] Source option 21 is not supported. Use 21 or later.
```

**解决方案:**
确保使用Java 21或更高版本，并且JAVA_HOME指向正确的JDK目录。

### 问题3: 依赖下载失败

**错误信息:**
```
[ERROR] Failed to execute goal ... Could not resolve dependencies
```

**解决方案:**
1. 检查网络连接
2. 尝试清理Maven本地仓库：
   ```bash
   mvn dependency:purge-local-repository
   ```
3. 或者删除 `~/.m2/repository` 目录重新下载

## 验证编译结果

编译成功后，应该在 `target/` 目录下看到：
- `photo-watermark-1.0-SNAPSHOT.jar` - 可执行JAR文件
- `classes/` - 编译后的class文件

## 运行测试

```bash
# 查看帮助信息
java -jar target/photo-watermark-1.0-SNAPSHOT.jar -h

# 测试基本功能（需要有包含EXIF信息的图片）
java -jar target/photo-watermark-1.0-SNAPSHOT.jar -i test.jpg -s 24 -c white -p br
```

## JDK21特性说明

本项目已针对JDK21进行优化，使用了以下配置：

- **编译器版本**: Maven Compiler Plugin 3.11.0
- **Shade插件版本**: Maven Shade Plugin 3.5.1  
- **源码和目标版本**: Java 21
- **兼容的依赖版本**: 所有依赖都经过JDK21兼容性测试

## 性能优化建议

使用JDK21时，可以启用以下JVM参数以获得更好性能：

```bash
java -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Xmx2g -jar target/photo-watermark-1.0-SNAPSHOT.jar -i photos/
```

参数说明：
- `-XX:+UseG1GC`: 使用G1垃圾收集器
- `-XX:MaxGCPauseMillis=200`: 设置GC暂停时间目标
- `-Xmx2g`: 设置最大堆内存为2GB
