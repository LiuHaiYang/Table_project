IFILLTABLE  分析模块总结：

一、搭建 hadoop  （所用版本1.2.1）
1. JAVA
2. 正常 启动5个进程，正常操作 hadoop分布式文件系统 
3. 注意分布式文件系统的权限 和  安全模式

二、配置 Zookeeper （最先启动 一定在Hbase前启动）
1. 配置 zoo.cfg
2. 启动zookeeper

三、安装 Hive  
1. 安装数据库  mysql 
2. 配置 hive的元数据库，用户名 密码  可远程登录及权限  注意编码
3. 启动hive远程 服务  nohup hive -service hiveserver &

四、安装 Hbase（hive与Hbase的整合）
1. 安装配置 Hbase 正常启动及操作
2. hive与Hbase的整合

五、安装 Python3
1. 正常使用

六、安装 thrift （python3 操作hbase的 python 库）
1.安装很多的库 ....  
http://www.cnblogs.com/yjf512/p/4334622.html   
http://www.pythontab.com/html/2013/pythonhexinbiancheng_0122/155.html

thrift -version
Thrift version 0.9.0
						  
http://www.aiuxian.com/article/p-2778755.html

3.需要和 hbase的 源码  编译  

 thrift --gen py ~（Hbase源码） /hbase-0.90.5/src/main/resources/org/apache/hadoop/hbase/thrift/Hbase.thrift
 
 cp -r gen-py/hbase/ /usr/local/python2.7.3/lib/python2.7/site-packages/
 
4.启动thrift 服务 在hbase/bin中 hbase-daemon.sh start thrift

七、代码编写 （restAPI）
1.录入
2.查询 （回显）
3.分析结果 归并  通过 JAVA 操作hive 结果存入 mysql ,直接获取mysql的数据
4.进度

















