package cn.sexycode.myjpa.transaction;

import org.apache.ibatis.transaction.Transaction;

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
    public void commit() {
        try {
            mybatisTransaction.commit();
        } catch (SQLException e) {
            e.printStackTrace();
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
