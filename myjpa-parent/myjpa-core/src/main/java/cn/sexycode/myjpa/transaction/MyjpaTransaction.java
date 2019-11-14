package cn.sexycode.myjpa.transaction;

import org.apache.ibatis.transaction.Transaction;

import javax.persistence.EntityTransaction;

/**
 * @author qzz
 */
public interface MyjpaTransaction extends EntityTransaction, Transaction {

}