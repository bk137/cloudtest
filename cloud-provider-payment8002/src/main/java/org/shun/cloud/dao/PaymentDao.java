package org.shun.cloud.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.shun.cloud.entities.Payment;

/**
 * 沒payment 拿 Dog頂替一下
 * @author Administrator
 */
@Mapper
public interface PaymentDao {

    public int create(Payment payment);

    public Payment getPaymentById(@Param("id") Long id);

}
