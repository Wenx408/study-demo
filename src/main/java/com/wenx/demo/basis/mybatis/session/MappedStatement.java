package com.wenx.demo.basis.mybatis.session;

/**
 * 封装执行的SQL语句和结果类型的全限定类名
 *
 * @author Wenx
 * @date 2021/3/17
 */
public class MappedStatement {
    /**
     * 执行的SQL语句
     */
    private String sqlStatement;
    /**
     * 结果类型实体类的全限定类名
     */
    private String resultType;


    public String getSqlStatement() {
        return sqlStatement;
    }

    public void setSqlStatement(String sqlStatement) {
        this.sqlStatement = sqlStatement;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
