# -*- coding:gbk -*- 
import os
#import sys
import shutil
# ȥ��ͼƬ�ļ�ͷ��Ϣ
def cutFileHead(sFileName):
    print(sFileName)
    srcFile = open(sFileName,  "rb")
    objFile = open(sFileName + ".tif",  "wb")
    try:
        # �����̶�ͷAPEX
        srcFile.seek(len("APEX"),  0)
        # ȡ���ļ�������
        b = srcFile.read(1)
        fileNameLen = ord(b)
        if (fileNameLen > 0):
            # �����ļ���
            srcFile.seek(fileNameLen,  1)
        
        # дͼƬ�ļ���
        objFile.write(srcFile.read())
    except [Exception]:
        print(Exception)
    finally:
        srcFile.close()
        objFile.close()
    os.remove(sFileName)  
    return 0
    
# ����Ŀ¼
def walk_dir(dir):
    for root,  dirs,  files in os.walk(dir,  True):
        for fileName in files:
            cutFileHead(root + "\\" + fileName) 
    return 0
    
# ��ִ�г���
print("���ڴ��������...\n")
#input("�뽫ͼƬ����������ͼƬĿ¼���ŵ���ǰĿ¼��imgDir�£������س���ʼ��")
imgDir = "imgDir"
#if os.path.exists(imgDir):
#    shutil.rmtree(imgDir)
#os.mkdir(imgDir)
#if not os.path.exists(imgDir):
#    print("Ŀ¼�����ڣ�")
#    input("���������ַ����س��˳���")
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

input("\n������ɣ����س��˳���")
