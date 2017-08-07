package hive;

import java.io.UnsupportedEncodingException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.concurrent.Executors;  
import java.util.concurrent.ScheduledExecutorService;  
import java.util.concurrent.TimeUnit;  
public class HiveJdbc{
    private static final String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
    private static final String HOST = "172.16.100.200:10000";
    private static final String URL = "jdbc:hive://" + HOST + "/default";
    public static void main(String[] args) throws Exception {
    	
    	while(true){
    	
        Class.forName(driverName);	
    	final Connection conn = DriverManager.getConnection(URL, "hive", "mysql");
    	System.out.println("hive已连接");
    	final Connection connMysql = DriverManager.getConnection("jdbc:mysql://172.16.100.200:3306/TableDataResult?useUnicode=true&characterEncoding=utf8","hive","mysql");  
		System.out.println("mysql已连接");
		
		
		String sql1 = "truncate table T_D_Result";
		
		System.out.println(sql1);
		PreparedStatement preStmt1 =connMysql.prepareStatement(sql1);
		preStmt1.executeUpdate();
		
		System.out.println("已清空");
		Statement stmt = conn.createStatement();
		String hql = "SELECT info['task_id'] FROM tabledata_1";
		System.out.println(hql);
		
		ResultSet table_id_res = stmt.executeQuery(hql);
		List<String> list_hive=new ArrayList<String>();  
		list_hive.add("姓名");  
		list_hive.add("班级");  
		list_hive.add("籍贯");  
		list_hive.add("QQ");
		list_hive.add("手机");
		list_hive.add("进入时间");  
		list_hive.add("分组");  
		list_hive.add("Trello账号");  
		list_hive.add("邮箱");
		list_hive.add("参与项目");
		list_hive.add("离开时间");  
		list_hive.add("前往何处");  
		list_hive.add("书籍");  
		list_hive.add("办公用品");
		list_hive.add("门禁卡");
		list_hive.add("其他");  
		list_hive.add("签字");  
				   
		List<Integer> list=new ArrayList<Integer>();
		while (table_id_res.next()) {	 
			 list.add(table_id_res.getInt(1)); 
			 }  
		List<Integer> listTemp= new ArrayList<Integer>();  
		Iterator<Integer> it=list.iterator();  
		while(it.hasNext()){  
		 int a=it.next();  
		 if(listTemp.contains(a)){  
		  it.remove();  
		 }  
		 else{  
		  listTemp.add(a);  
		 }  
		}
		
		for(Integer i:list){
			for (String j:list_hive){
			    List<String> list_res;
				list_res = new ArrayList<String>();
				String hql1 = "SELECT  info['"+ j +"']  FROM tabledata_1 where info['task_id'] ="+ i;
				System.out.println(hql1);
				ResultSet tablres = stmt.executeQuery(hql1);
				while(tablres.next()){
				list_res.add(tablres.getString(1));
				}
			    StringBuilder sb = new StringBuilder();    
			    for (int ii = 0; ii < list_res.size(); ii++) {
			    	
				    if (ii == list_res.size() - 1) {            
					    sb.append(list_res.get(ii));        
				} else {            
				  sb.append(list_res.get(ii));            
				  sb.append(",");        
				  }    
				  }
				String result = "{" +sb +" }";
			  	String sql = "insert into T_D_Result(task_id,table_keys,result) values(?,?,?)";
			  	System.out.println(sql);
			  	PreparedStatement preStmt =connMysql.prepareStatement(sql);
			  	preStmt.setInt(1,i);  
			    preStmt.setString(2,j);
			    preStmt.setString(3, result);
			    preStmt.executeUpdate();
			    preStmt.close();

			}
		  
		}
		System.out.println("ok");
		preStmt1.close();
		table_id_res.close();
		stmt.close();
    	connMysql.close();
    	conn.close();
    	System.out.println("资源关闭");
    	Thread.sleep(5*60*1000);
        
    	}
		
    }
}
