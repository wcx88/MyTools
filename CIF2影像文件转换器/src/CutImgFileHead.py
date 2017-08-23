# -*- coding:gbk -*- 
import os
#import sys
import shutil
# 去除图片文件头信息
def cutFileHead(sFileName):
    print(sFileName)
    srcFile = open(sFileName,  "rb")
    objFile = open(sFileName + ".tif",  "wb")
    try:
        # 跳过固定头APEX
        srcFile.seek(len("APEX"),  0)
        # 取出文件名长度
        b = srcFile.read(1)
        fileNameLen = ord(b)
        if (fileNameLen > 0):
            # 跳过文件名
            srcFile.seek(fileNameLen,  1)
        
        # 写图片文件流
        objFile.write(srcFile.read())
    except [Exception]:
        print(Exception)
    finally:
        srcFile.close()
        objFile.close()
    os.remove(sFileName)  
    return 0
    
# 遍历目录
def walk_dir(dir):
    for root,  dirs,  files in os.walk(dir,  True):
        for fileName in files:
            cutFileHead(root + "\\" + fileName) 
    return 0
    
# 主执行程序
print("正在处理中请候...\n")
#input("请将图片（或者整个图片目录）放到当前目录的imgDir下，并按回车开始！")
imgDir = "imgDir"
#if os.path.exists(imgDir):
#    shutil.rmtree(imgDir)
#os.mkdir(imgDir)
#if not os.path.exists(imgDir):
#    print("目录不存在！")
#    input("输入任意字符按回车退出！")
#    sys.exit(0)
    
newImgDir = "newImgDir"
if os.path.exists(newImgDir):
    shutil.rmtree(newImgDir)
# copy
shutil.copytree(imgDir, newImgDir)
# walk
try:
    walk_dir(newImgDir)
except [Exception]:
    print(Exception)

input("\n处理完成！按回车退出！")
