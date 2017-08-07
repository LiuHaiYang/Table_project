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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import java.util.concurrent.Executors;  
import java.util.concurrent.ScheduledExecutorService;  
import java.util.concurrent.TimeUnit;  
public class HiveJdbc_4{
    private static final String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
    private static final String HOST = "172.16.100.200:10000";
    private static final String URL = "jdbc:hive://" + HOST + "/default";
    public static void main(String[] args) throws Exception {
    	
    	while(true){
    	
        Class.forName(driverName);	
    	final Connection conn = DriverManager.getConnection(URL, "hive", "mysql");
    	System.out.println("hive连接成功");
    	final Connection connMysql = DriverManager.getConnection("jdbc:mysql://172.16.100.200:3306/dataresult?useUnicode=true&characterEncoding=utf8&&useSSL=false","hive","mysql");  
		System.out.println("mysql连接成功");
    	final Connection connMysql_116 = DriverManager.getConnection("jdbc:mysql://172.16.100.56:3306/ifilltables?useUnicode=true&characterEncoding=utf8&&useSSL=false","root","wuyunDB|116");  
		System.out.println("116mysql连接成功");
		
		String sql1 = "truncate table result";
//		System.out.println(sql1);
		PreparedStatement preStmt1 =connMysql.prepareStatement(sql1);
		preStmt1.executeUpdate();
		
		System.out.println("清空数据");
		Statement stmt = conn.createStatement();
		String hql = "SELECT info['task_id'] FROM tabledata";
		ResultSet table_id_res = stmt.executeQuery(hql);
//		System.out.println(hql);
		
		List<Integer> list=new ArrayList<Integer>();
		while (table_id_res.next()){
			 int table_hive_id = table_id_res.getInt(1);
			 list.add(table_hive_id); 
			 }
//		System.out.println(list);
		HashSet has  =   new  HashSet(list);
		list.clear();
		list.addAll(has);
	    System.out.println(list);
		
	    for (Integer t_id:list){
	    
		String sql116 = "select name from form_key_table where t_id = "+ t_id;
//		System.out.println(sql116);
		PreparedStatement preStmt116 =(PreparedStatement)connMysql_116.prepareStatement(sql116);
		ResultSet   tables_keys = preStmt116.executeQuery(sql116);
			
		List<String> list_hive=new ArrayList<String>();  
		
		while(tables_keys.next()){
			String name = tables_keys.getString(1);
			list_hive.add(name);
			
		}
		HashSet h  =   new  HashSet(list_hive);
		list_hive.clear();
		list_hive.addAll(h);
	    System.out.println(list_hive);	
	    
			for (String j:list_hive){
			    List<String> list_res;
				list_res = new ArrayList<String>();
				String hql1 = "SELECT  info['"+ j +"']  FROM tabledata where info['task_id'] ="+ t_id;
//				System.out.println(hql1);
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
			  	String sql = "insert into result(task_id,table_keys,result) values(?,?,?)";
//			  	System.out.println(sql);
			  	PreparedStatement preStmt =connMysql.prepareStatement(sql);
			  	preStmt.setInt(1,t_id);  
			    preStmt.setString(2,j);
			    preStmt.setString(3, result);
			    preStmt.executeUpdate();
			    preStmt.close();

			}
		  
		tables_keys.close();
		preStmt116.close();
	    
	    }
	    

	    

		System.out.println("ok");

		preStmt1.close();
		table_id_res.close();
		stmt.close();
		connMysql_116.close();
    	connMysql.close();
    	conn.close();
    	System.out.println("关闭所有资源");
    	Thread.sleep(1*60*1000);
        
    	}
		
    }
}
