package com.wenx.demo.study.basis.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Wenx
 * @Description:
 * @Date: Created in 2020/3/21 19:55
 * @Modified By：
 */
public class JdbcDemo {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String sql = "select id, name, type from test001 where id > ?";
        String url = "jdbc:mysql://localhost:3306/mytest?user=root&password=1234@abcd&characterEncoding=utf8&serverTimezone=GMT%2B8&allowMultiQueries=true";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 5);
            rs = pstmt.executeQuery();
            try {
                List<Test001> list = getResultList(rs, Test001.class);
                System.out.println(list);
            } catch (Exception e) {
                e.printStackTrace();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> List<T> getResultList(ResultSet rs, Class<T> clazz)
            throws SQLException, IllegalAccessException, InstantiationException, InvocationTargetException {
        List<T> list = new ArrayList<T>();
        Map<String, Method> methodMap = getMethodMap(clazz);
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        while (rs.next()) {
            Object obj = clazz.newInstance();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = meta.getColumnName(i);
                Object columnValue = rs.getObject(i);
                Method m = methodMap.get("set" + columnName);
                if (m != null) {
                    m.invoke(obj, columnValue);
                }
            }
            list.add((T) obj);
        }
        return list;
    }

    public static Map<String, Method> getMethodMap(Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        Map<String, Method> methodMap = new HashMap<String, Method>(16);
        for (Method m : methods) {
            String methodName = m.getName().toLowerCase();
            if (!methodName.startsWith("set")) {
                continue;
            }
            int mod = m.getModifiers();
            boolean isInstancePublicSetter = Modifier.isPublic(mod) && !Modifier.isStatic(mod) && !Modifier.isAbstract(mod);
            if (isInstancePublicSetter) {
                methodMap.put(methodName, m);
            }
        }
        return methodMap;
    }

    public static class Test001 {
        private Integer id;
        private String name;
        private String type;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return String.format("{id:%d, name:%s, type:%s}", id, name, type);
        }
    }
}
