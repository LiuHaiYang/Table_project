package hive;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.concurrent.Executors;  
import java.util.concurrent.ScheduledExecutorService;  
import java.util.concurrent.TimeUnit;  
public class HiveJdbcTest {
    private static final String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
    private static final String HOST = "172.16.100.100:10000";
    private static final String URL = "jdbc:hive://" + HOST + "/default";
  
    public static void main(String[] args) throws Exception {
    	
        Class.forName(driverName);	
    	Connection conn = DriverManager.getConnection(URL, "hive", "mysql");
        Connection connMysql = DriverManager.getConnection("jdbc:mysql://172.16.100.100:3306/TableDataResult","hive","mysql");
        
		String sql1 = "truncate table T_D_Result";
		PreparedStatement preStmt1 =connMysql.prepareStatement(sql1);
		preStmt1.executeUpdate();
        Statement stmt = conn.createStatement();
        String hql = "SELECT table_id FROM tabledata";
        ResultSet table_id_res = stmt.executeQuery(hql);
        
        List<String> list_hive=new ArrayList<String>();  
        list_hive.add("name");  
        list_hive.add("age");  
        list_hive.add("sex");  
        list_hive.add("class");     
   
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
        /**
         * 获取mysql 数据库字段名
         */
        /**
        DatabaseMetaData meta = connMysql.getMetaData();
        ResultSet resultSet = meta.getColumns("TableDataResult", null, "T_D_Result", "%");
        while (resultSet.next()) {
            System.out.println(resultSet.getString(4));
        }
        **/
        
        for(Integer i:list){
        	for (String j:list_hive){
			      List<String> list_res = new ArrayList<String>();
			      String hql1 = "SELECT "+ j +" FROM tabledata where table_id ="+ i;
			      System.out.println(hql1);
			      ResultSet tablres = stmt.executeQuery(hql1);
			      while(tablres.next()){
			    	  list_res.add(tablres.getString(1));
			      }
			//          System.out.println(list_res);
			      
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
				 System.out.println(result);
			  	String sql = "insert into T_D_Result(table_id,table_keys,result) values(?,?,?)";
			  	PreparedStatement preStmt =connMysql.prepareStatement(sql);
			  	preStmt.setInt(1,i);  
			    preStmt.setString(2,j);
			    preStmt.setString(3, result);
			    preStmt.executeUpdate();
        	}
          
        }        	 
        table_id_res.close();
        stmt.close();
        conn.close();

		
		//定时器
//		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();  
//        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间  
//        service.scheduleAtFixedRate(runnable, 10, 1, TimeUnit.SECONDS); 
		
    }
}
