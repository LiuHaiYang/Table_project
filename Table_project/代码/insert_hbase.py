import sys
sys.path.append('/usr/local/python3/lib/python3.5/site-packages/hbase/')
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
class TrackAPI1(Resource):
    def post(self):
        transport = TSocket.TSocket('172.16.100.200', 9090)
        res_data = request.json
        print(res_data)
        transport = TTransport.TBufferedTransport(transport)
        protocol = TBinaryProtocol.TBinaryProtocol(transport)
        client = Hbase.Client(protocol)
        transport.open()
        for k in res_data:
            print(res_data[k])
            row = (int(res_data["user_id"])*1000)+int(res_data["task_id"])+1000000
            rew = str(row)
            print(rew)
            mutations = [Mutation(column= 'info:%s'%k, value=res_data[k])]
            client.mutateRow('tabledata', rew, mutations, None)
        ok = {"put":"ok"}
        return ok
api.add_resource(TrackAPI1,'/puttabledata')
if __name__ == '__main__':
    app.run(host='0.0.0.0',port=5000)
