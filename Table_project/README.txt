IFILLTABLE  ����ģ���ܽ᣺

һ��� hadoop  �����ð汾1.2.1��
1. JAVA
2. ���� ����5�����̣��������� hadoop�ֲ�ʽ�ļ�ϵͳ 
3. ע��ֲ�ʽ�ļ�ϵͳ��Ȩ�� ��  ��ȫģʽ

�������� Zookeeper ���������� һ����Hbaseǰ������
1. ���� zoo.cfg
2. ����zookeeper

������װ Hive  
1. ��װ���ݿ�  mysql 
2. ���� hive��Ԫ���ݿ⣬�û��� ����  ��Զ�̵�¼��Ȩ��  ע�����
3. ����hiveԶ�� ����  nohup hive -service hiveserver &

�ġ���װ Hbase��hive��Hbase�����ϣ�
1. ��װ���� Hbase ��������������
2. hive��Hbase������

�塢��װ Python3
1. ����ʹ��

������װ thrift ��python3 ����hbase�� python �⣩
1.��װ�ܶ�Ŀ� ....  
http://www.cnblogs.com/yjf512/p/4334622.html   
http://www.pythontab.com/html/2013/pythonhexinbiancheng_0122/155.html

thrift -version
Thrift version 0.9.0
						  
http://www.aiuxian.com/article/p-2778755.html

3.��Ҫ�� hbase�� Դ��  ����  

 thrift --gen py ~��HbaseԴ�룩 /hbase-0.90.5/src/main/resources/org/apache/hadoop/hbase/thrift/Hbase.thrift
 
 cp -r gen-py/hbase/ /usr/local/python2.7.3/lib/python2.7/site-packages/
 
4.����thrift ���� ��hbase/bin�� hbase-daemon.sh start thrift

�ߡ������д ��restAPI��
1.¼��
2.��ѯ �����ԣ�
3.������� �鲢  ͨ�� JAVA ����hive ������� mysql ,ֱ�ӻ�ȡmysql������
4.����

















