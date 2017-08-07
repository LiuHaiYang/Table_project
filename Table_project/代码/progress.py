import sys
sys.path.append('/usr/local/python3/lib/python3.5/site-packages/hbase/')
import time
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from hbase import Hbase
import pymysql
from hbase.ttypes import *
from flask import Flask,request
from flask_restful import Api
from flask_restful import Resource
# from  product import *
from itertools import product
app = Flask(__name__)
api = Api(app)

class TrackAPI(Resource):
    def post(self):
        #连接mysql数据库
        conn =pymysql.connect(host="172.16.100.56",user="root",passwd="wuyunDB|116",db="ifilltables",charset='utf8')
        cur = conn.cursor()
        #连接HIVE
        transport = TSocket.TSocket('172.16.100.200', 9090)
        transport = TTransport.TBufferedTransport(transport)
        protocol = TBinaryProtocol.TBinaryProtocol(transport)
        client = Hbase.Client(protocol)
        transport.open()
        tableName = 'tabledata'
        
        #接收参数
        res_data = request.json
        print(res_data)
        
        #分割参数，
        try:
            list_rowKey = []
            user_id = res_data["user_id"]
            task_id = res_data["task_id"]
            print(user_id,task_id)
            #通过参数获取hbase数据
            resuser_id = int(int(user_id)*1000)
            restask_id = int(task_id)
            rowKey = str(resuser_id + restask_id+1000000)
            list_rowKey.append(rowKey)
        except Exception as e:
            list_rowKey = []
            task_id = res_data["task_id"]
            group_id = res_data["group_id"]
            print(task_id,group_id)

            #通过参数获取mysql数据
            SQL= "select user_id from userandgroup where group_id = "+ str(group_id)
            print(SQL)
            cur.execute(SQL)
            group_user = []
            for i in cur.fetchall():
                # print(i[0])
                group_user.append(i[0])
                #通过参数获取hbase数据
                resuser_id = int(i[0])*1000
                restask_id = task_id
                print(restask_id,resuser_id)
                rowKey = str(resuser_id + restask_id+1000000)
                print(rowKey)
                list_rowKey.append(rowKey)
        print(len(list_rowKey),"本组共有用户")

        if len(list_rowKey) ==1:
            result = client.getRow(tableName, rowKey, None)
            #处理数据
            la = {}
            li=[]
            if result:
                for (k,v) in result[0].columns.items():
                    kk =str("%-20s:%s" % (k,v.value))
                    ll = []
                    for i in kk.split(":"):
                        ll.append(i)
                    la[ll[1]]=ll[2:]
                    li.append(ll[2:])
                # print(li)
                kong_list = []
                for i in li:
                    # print(i)
                    if i == ['']:
                        kong_list.append(i)
                    else:
                        pass
                # print(kong_list)
                z_num = (len(li)-3)
                n_num = len(kong_list)
                t_num = (len(li)-3) - (len(kong_list))
                print(z_num,n_num,t_num)
                #进行计算
                if n_num == z_num:
                    result_shuju = 0
                elif z_num < 1:
                    result_shuju = 0
                elif t_num == z_num:
                    result_shuju =100
                else:
                    result_shuju = (round(t_num/z_num,2)*100)
                
                try:

                    result_progress = {user_id:result_shuju }
                except Exception as e:
                    result_progress = {group_user[0]:result_shuju }
                #返回结果
                print(result_progress)
                return result_progress
            else:
                result_progress = {user_id:0 }
                print(result_progress)
                return result_progress

        else:
            print(list_rowKey)
            all_list_shuju = []
            for i in list_rowKey:
                
                result = client.getRow(tableName, i, None)
                #处理数据
                la = {}
                li=[]
                kong_list = []
                if result:
                    for (k,v) in result[0].columns.items():
                        kk =str("%-20s:%s" % (k,v.value))
                        ll = []
                        for i in kk.split(":"):
                            ll.append(i)
                        la[ll[1]]=ll[2:]
                        li.append(ll[2:])
                    print(li)
                    
                    for i in li:
                        # print(i)
                        if i == ['']:
                            kong_list.append(i)
                        else:
                            pass
                    # print(kong_list)
                # else:
                #     all_progress = {}
                #     for i in range(len(group_user)):
                #         all_progress[group_user[i]]=0
                #     return all_progress
                z_num = (len(li)-3)
                n_num = len(kong_list)
                t_num = (len(li)-1) - (len(kong_list))
                print(z_num,n_num)
                if z_num < 1:
                    result_shuju = 0
                elif n_num == z_num:
                    result_shuju = 0
                elif n_num == 0:
                    result_shuju = 100
                #进行计算
                else:
                    result_shuju = (round(t_num/z_num,2)*100)

                print(result_shuju)
                #返回结果
                all_list_shuju.append(result_shuju)

            all_progress = {}
            for i in range(len(group_user)):
                all_progress[group_user[i]]=all_list_shuju[i]
            return all_progress 

        
        cur.close()
        conn.close()


api.add_resource(TrackAPI,'/tabledataprogess')
if __name__ == '__main__':
    app.run(host='0.0.0.0',port=5003)
