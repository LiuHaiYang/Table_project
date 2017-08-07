import sys
sys.path.append('/usr/local/python3/lib/python3.5/site-packages/hbase/')
import time
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from hbase import Hbase
from hbase.ttypes import *
from flask import Flask,request
from flask_restful import Api
from flask_restful import Resource

app = Flask(__name__)
api = Api(app)

class TrackAPI(Resource):
    def post(self):
        task_id = request.form['task_id']
        user_id = request.form['user_id']
        print(task_id,user_id,"===============")
        transport = TSocket.TSocket('172.16.100.200', 9090)
        transport = TTransport.TBufferedTransport(transport)
        protocol = TBinaryProtocol.TBinaryProtocol(transport)
        client = Hbase.Client(protocol)
        transport.open()
        tableName = 'tabledata'
        res_uid =int(user_id)*1000
        res_tid = int(task_id)
        res_num = res_tid + res_uid
        rowKey = str(res_num +1000000)
        print(rowKey)
        result = client.getRow(tableName, rowKey, None)
        la = {}
        li=[]
        if result:
            for (k,v) in result[0].columns.items():
                kk =str("%-20s:%s" % (k,v.value))
                ll = []
                for i in kk.split(" :"):
                    ll.append(i)
                # ll_b=str(ll[2:])[2:-2]
                # la[ll[1]]=ll_b
                # la[ll[1]]=str(ll[2:])
                la[ll[0][5:]]=str(ll[1])              
            return la
api.add_resource(TrackAPI,'/querytabledata')
if __name__ == '__main__':
	app.run(host='0.0.0.0',port=5001)
