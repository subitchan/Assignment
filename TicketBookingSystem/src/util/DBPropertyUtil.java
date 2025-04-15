package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

public class DBPropertyUtil {
   
       // return "jdbc:mysql://localhost:3306/transportmanagementsystem?user=root&password=root";
    	public static String getConnectionString(String fileName)throws IOException {
    		//fileName="db.properties"
    		String connStr=null;
    		Properties props=new Properties();
    		FileInputStream fis=new FileInputStream(fileName);
    		//InputStream fis = DBPropertyUtil.class.getClassLoader().getResourceAsStream("db.properties");

    	
    		props.load(fis);
    		String user=props.getProperty("user");
    		String password=props.getProperty("password");
    		String protocol=props.getProperty("protocol");
    		String system=props.getProperty("system");
    		String database=props.getProperty("database");
    		String port=props.getProperty("port");
    		connStr=protocol+"//"+system+":"+port+"/"+database+"?user="+user+"&password="+password;
    		return  connStr;
    	}

			

    	
    }

