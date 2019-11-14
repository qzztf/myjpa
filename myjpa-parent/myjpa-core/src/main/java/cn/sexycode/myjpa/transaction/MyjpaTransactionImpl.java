package cn.sexycode.myjpa.transaction;

import org.apache.ibatis.transaction.Transaction;

import javax.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author qzz
 */
public class MyjpaTransactionImpl implements MyjpaTransaction {
    private final Transaction mybatisTransaction;

    public MyjpaTransactionImpl(Transaction transaction) {
        this.mybatisTransaction = transaction;
    }

    @Override
    public void begin() {
    }

    @Override
    public Connection getConnection() throws SQLException {
        return mybatisTransaction.getConnection();
    }

    @Override
    public void commit() {
        try {
            mybatisTransaction.commit();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void rollback() {
        try {
            mybatisTransaction.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws SQLException {
        mybatisTransaction.close();
    }

    @Override
    public Integer getTimeout() throws SQLException {
        return mybatisTransaction.getTimeout();
    }

    @Override
    public void setRollbackOnly() {
    }

    @Override
    public boolean getRollbackOnly() {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    /**
     * 获取托管的mybatis事务对象
     *
     * @return Transaction
     */
    public Transaction getMybatisTransaction() {
        return mybatisTransaction;
    }
}
