#!/bin/bash

echo "========================================"
echo "图片水印工具 - 构建脚本"
echo "========================================"

echo
echo "正在编译项目..."
mvn clean compile
if [ $? -ne 0 ]; then
    echo "编译失败！"
    exit 1
fi

echo
echo "正在打包项目..."
mvn package
if [ $? -ne 0 ]; then
    echo "打包失败！"
    exit 1
fi

echo
echo "========================================"
echo "构建完成！"
echo "========================================"
echo
echo "可执行文件位置: target/photo-watermark-1.0-SNAPSHOT.jar"
echo
echo "使用示例:"
echo "java -jar target/photo-watermark-1.0-SNAPSHOT.jar -i \"/path/to/photos\" -s 30 -c red -p br"
echo
echo "查看帮助:"
echo "java -jar target/photo-watermark-1.0-SNAPSHOT.jar -h"
echo
