from flask import Flask,request
from flask_restful import Api
from flask_restful import Resource
from collections import Counter
import pymysql 
app = Flask(__name__)
api = Api(app)
#conn =pymysql.connect(host="172.16.100.200",user="hive",passwd="mysql",db="TableDataResult",charset='utf8')
class TrackAPI1(Resource):
	def post(self):
		conn = pymysql.connect(host="172.16.100.200",user="hive",passwd="mysql",db="dataresult",charset='utf8')
		cur = conn.cursor()
		kk_a=[]
		kk=[]
		task_id = request.form['task_id']
		table_keys = request.form['table_keys']

		sql = 'select result from result where task_id = ' + task_id +' and table_keys =' + "'"+table_keys+"'"
		print(sql)
		cur.execute(sql)
		null ='null'
		null1 = 'null '
		null2='null }'
		data_error= {"back":"无数据推荐！"}
		for i in cur.fetchall():
			i = str(i)
			for ii in i.split(","):
				kk_a.append(ii)
			for i in kk_a:
				if i != "null" or "null ":
					kk.append(i)
			if len(kk) == 2:
				print("1")
				kk.pop()
				kk_b=[]
				kkk = kk[0][3:]
				kkkk = kkk[:-2]
				
				res = []
				if kkkk == "null":
					return data_error
				elif kkkk == "null ":
					return data_error
				elif kkkk == "null }":
					return data_error
				else:
					results = kkkk,"1"
					print(results)
					
					res.append(results)
					return res
				
			elif len(kk) == 3:
				print("2")
				kk_b=[]
				lll= []
				kk.pop()
				kkk = kk[0][3:]
				kkkk = kk[1][:-2]
				lll.append(kkk)
				lll.append(kkkk)
				# print(lll)
				for  i in lll:
					if i != null:
						if i != null1:
							if i != null2:
								kk_b.append(i)
			
				if len(kk_b)==0:
					return data_error
				else:
					values_counts = Counter(kk_b)  
					results = values_counts.most_common(4)  
					return results
			else:
				print("3")
				kk.pop()
				kk_b=[]
				kk.append(kk[0][3:])
				kk.append(kk[-2][0:-2])
				
				del kk[0]
				if len(kk) > 3:
					del kk[-3]
				

				for  i in kk:
					if i != null:
						if i != null1:
							if i != null2:
								kk_b.append(i)
					else:
						pass
				
				if len(kk_b)==0:
					return data_error
				else:
					values_counts = Counter(kk_b)  
					results = values_counts.most_common(4)
					return results
		cur.close()
		conn.close()


api.add_resource(TrackAPI1,'/tabledatareult')

if __name__ == '__main__':
    app.run(host='0.0.0.0',port=5002)

