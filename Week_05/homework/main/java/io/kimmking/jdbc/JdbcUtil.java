package io.kimmking.jdbc;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class JdbcUtil {
    //配置文件
    private final static String JDBC_CONFIG = "jdbc.properties";
    private static Properties properties = new Properties();
    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    static {
        try {
            Resource resource = new ClassPathResource(JDBC_CONFIG);
            properties.load(resource.getInputStream());
            Class.forName(properties.getProperty("jdbc.driverClassName"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //加载驱动 创建连接
    private static Connection getConnection() throws SQLException {
        Connection connection = connectionThreadLocal.get();
        if(connection == null) {
            connection = DriverManager.getConnection(properties.getProperty("jdbc.url"), properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));
            connectionThreadLocal.set(connection);
        }
        return connection;
    }

    /**
     * 查询
     * @param sql
     * @return
     */
    public static List select(String sql) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
       List result = new ArrayList<>();
        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            String[] columns = new String[resultSetMetaData.getColumnCount()];
            for (int i=0;i < columns.length;i++) {
                columns[i] = resultSetMetaData.getColumnName(i + 1);
            }
            while(resultSet.next()) {
                Map<String,Object> row = new LinkedHashMap<>();
                for (String column : columns) {
                    row.put(column,resultSet.getObject(column));
                }
                result.add(row);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            close(resultSet,statement,connection);
        }
        return result;
    }

    /**
     * 插入
     * @param sql
     * @return
     */
    public static boolean insert(String sql) {
        Connection connection = null;
        Statement statement = null;
        int result = 0;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            result = statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            close(statement,connection);
        }
        return result > 0;
    }
    public static boolean batchInsert(String sql,List<Object[]> data) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);//将自动提交关闭
            preparedStatement = connection.prepareStatement(sql);
            for(int i=0 ; i < data.size() ; i++) {
                Object[] row = data.get(i);
                for(int j=0;j < row.length;j++) {
                    preparedStatement.setString(j,row[j].toString());
                }
                if(i>0 && i%10==0){
                    preparedStatement.executeBatch();
                    //如果不想出错后，完全没保留数据，则可以每执行一次提交一次，但得保证数据不会重复
                    connection.commit();
                }
            }
            preparedStatement.executeBatch();//执行最后剩下不够500条的
            preparedStatement.close();
            connection.commit();//执行完后，手动提交事务
            connection.setAutoCommit(true);//再把自动提交打开，避免影响其他需要自动提交的操作
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            close(connection);
        }
        return result > 0;
    }

    /**
     * 删除
     * @param sql
     * @return
     */
    public static boolean delete(String sql) {
        Connection connection = null;
        Statement statement = null;
        int result = 0;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            result = statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            close(statement,connection);
        }
        return result > 0;
    }

    /**
     * 更新
     * @param sql
     * @return
     */
    public static boolean update(String sql) {
        Connection connection = null;
        Statement statement = null;
        int result = 0;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            result = statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            close(statement,connection);
        }
        return result > 0;
    }
    //关闭资源
    private static void close(Object obj) {
        if(obj instanceof AutoCloseable) {
            try {
                ((AutoCloseable) obj).close();
                if(obj instanceof Connection) {
                    connectionThreadLocal.remove();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static void close(Statement statement, Connection connection) {
        close(connection);
    }
    private static void close(ResultSet resultSet ,Statement statement, Connection connection) {
        close(resultSet);
        close(statement);
        close(connection);
    }
}
