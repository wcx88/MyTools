#!/bin/sh
# 从53复制开发环境文件到58测试环境
# @author 魏成煊 2016.12.19
#源机器ip 
src_address=10.189.145.53
#一柜通服务基础路径
ygt_svc_path=/home/ygt/ygtservice
#一柜通办理端tomcat基础路径
ygt_path=/home/ygt/LiveBOS_Tomcat/LiveBos/FormBuilder
#集中营运tomcat基础路径
jzyy_path=/home/ygt/Accept_Tomcat/webapps/qyyx

#停止tomcat服务器
#cd $ygt_path/../../bin
#./shutdown.sh
kill  `ps -ef | grep -E 'java.*catalina\.base=/home/ygt/LiveBOS_Tomcat' | grep -v grep | awk '{ print $2 }'`
cd $jzyy_path/../../bin
./shutdown.sh

#停止felix
cd /opt/felix
./shutdown.sh

#复制
echo "开始复制……"
scp -r root@$src_address:$ygt_svc_path/ygtservice.jar $ygt_svc_path/
scp -r root@$src_address:$ygt_svc_path/service/*.jar $ygt_svc_path/service/

#要复制的ygt目录数组，用空格分割
arr_file="plug-in WEB-INF/view WEB-INF/classes/com/apex/ygt WEB-INF/classes/com/apexsoft"
for i in $arr_file
do
	echo "开始复制 $ygt_path/$i"
	scp -r root@$src_address:$ygt_path/$i/* $ygt_path/$i
done
#要复制的jzyy文件数组，用空格分割
arr_file="WEB-INF/classes/menus.xml"
for i in $arr_file
do
	echo "开始复制 $jzyy_path/$i"
	scp -r root@$src_address:$jzyy_path/$i $jzyy_path/$i
done

#要复制的jzyy目录数组，用空格分割
arr_file="js css WEB-INF/jsp WEB-INF/include WEB-INF/tld WEB-INF/classes/com"
for i in $arr_file
do
	echo "开始复制 $jzyy_path/$i"
	scp -r root@$src_address:$jzyy_path/$i/* $jzyy_path/$i
done

echo "复制完毕"

#重起服务
cd $ygt_svc_path/
./start_service.sh
rm -rf $jzyy_path/../../work/*
cd $jzyy_path/../../bin
./startup.sh
#./restart.sh
rm -rf $ygt_path/../../work/*
cd $ygt_path/../../bin
./startup.sh
#./restart.sh
#重起felix
cd /opt/felix
#./shutdown.sh
./startup.sh

