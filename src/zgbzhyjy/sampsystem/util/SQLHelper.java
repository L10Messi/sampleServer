package zgbzhyjy.sampsystem.util;
import java.sql.*;
import java.util.logging.*;



public class SQLHelper {
	public static String driver = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://localhost:3306/orcl";
	public static String user = "root";
	public static String password = "123456";
	
	public SQLHelper(){
		
	}
	
	public static Connection getConnection(){
       try{
    	   Class.forName(driver);
       }catch(ClassNotFoundException ex){
    	   Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE,null,ex);
       }
       try{
    	   return DriverManager.getConnection(url,user,password);
       }catch(SQLException ex){
    	   Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE,null,ex);
    	   return null;
       }
        
	}
	
	public static Statement getStatement(){
		Connection conn = getConnection();
		if(conn == null)
			return null;
		try{
			return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		}catch(SQLException ex){
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE,null,ex);
			close(conn);
		}
		return null;
	}
	
	public static void close(Connection conn){
		
	}
	
	public static ResultSet getResultSet(String cmdText){
		Connection conn = getConnection();
		Statement stmt = getStatement();
		if (stmt == null)
			{System.out.println("stmt==null");
			return null;}
		try{
			ResultSet rs=stmt.executeQuery(cmdText);			
			return rs;
			}catch(SQLException ex){
			Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE,null,ex);
			close(conn);
		}
		//System.out.println("执行到这一步，代表未查到数据");
		return null;
	} 
	
	public static int ExecSql(String cmdText)
    {
        Statement stmt = getStatement();
        if (stmt == null)
        {
            return -2;
        }
        int i;
        try
        {
            i = stmt.executeUpdate(cmdText);
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null,
                    ex);
            i = -1;
        }
        closeConnection(stmt);
        return i;
    }
	
	private static void closeConnection(Object obj)
    {
        if (obj == null)
        {
            return;
        }
        try
        {
            if (obj instanceof Statement)
            {
                ((Statement) obj).getConnection().close();
            } else if (obj instanceof PreparedStatement)
            {
                ((PreparedStatement) obj).getConnection().close();
            } else if (obj instanceof ResultSet)
            {
                ((ResultSet) obj).getStatement().getConnection().close();
            } else if (obj instanceof Connection)
            {
                ((Connection) obj).close();
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
