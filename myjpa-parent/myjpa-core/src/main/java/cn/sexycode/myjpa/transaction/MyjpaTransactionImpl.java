package cn.sexycode.myjpa.transaction;

import org.apache.ibatis.transaction.Transaction;

import java.sql.SQLException;

/**
 * @author qzz
 */
public class MyjpaTransactionImpl implements MyjpaTransaction {
    private final Transaction transaction;

    public MyjpaTransactionImpl(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void begin() {
    }

    @Override
    public void commit() {
        try {
            transaction.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollback() {
        try {
            transaction.rollback();
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
}
