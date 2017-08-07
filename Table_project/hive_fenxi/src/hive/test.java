package hive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class test {

	 public static void main(String[] args) {
			
			try {
				final Connection connMysql_116 = DriverManager.getConnection("jdbc:mysql://172.16.100.56:3306/ifilltables?useUnicode=true&characterEncoding=utf8","root","wuyunDB|116");  

				String sql116 = "select name from form_key_table";
				System.out.println(sql116);
				PreparedStatement preStmt116 =(PreparedStatement)connMysql_116.prepareStatement(sql116);
				ResultSet   tables_keys = preStmt116.executeQuery(sql116);
					
				List<String> list_hive=new ArrayList<String>();  
				
				while(tables_keys.next()){
					String name = tables_keys.getString(1);
					System.out.println(name);
					list_hive.add(name);
					
				}
				System.out.println(list_hive);
				HashSet h  =   new  HashSet(list_hive);
				list_hive.clear();
				list_hive.addAll(h);
			    System.out.println(list_hive);
			    
				tables_keys.close();
				preStmt116.close();
				connMysql_116.close();
			} catch (SQLException e) {
		      System.out.println("kkkkkk");
			}
		
	}

}

