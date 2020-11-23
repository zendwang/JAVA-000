package io.kimmking.jdbc;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Data
@AllArgsConstructor
public class CrudBean {

    private DataSource dataSource;

    /**
     * 查询
     * @param sql
     * @return
     */
    public  List<Map<String, Object>> select(String sql) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            String[] columns = new String[resultSetMetaData.getColumnCount()];
            for (int i = 0; i < columns.length; i++) {
                columns[i] = resultSetMetaData.getColumnName(i + 1);
            }
            while (resultSet.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (String column : columns) {
                    row.put(column, resultSet.getObject(column));
                }
                result.add(row);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            close(resultSet, statement, connection);
        }
        return result;
    }
    /**
     * 插入
     * @param sql
     * @return
     */
    public boolean insert(String sql) {
        Connection connection = null;
        Statement statement = null;
        int result = 0;
        try {
            connection =  dataSource.getConnection();
            statement = connection.createStatement();
            result = statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            close(statement,connection);
        }
        return result > 0;
    }
    public boolean delete(String sql) {
        return this.insert(sql);
    }
    public boolean update(String sql) {
        return this.insert(sql);
    }
    //关闭资源
    private static void close(Object obj) {
        if(obj instanceof AutoCloseable) {
            try {
                ((AutoCloseable) obj).close();
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
