package com.express.database.dao;

import java.util.List;

import com.express.model.Coupon;
import com.express.model.CouponType;

public interface CouponTypeDao {

	double getCouponValue(String couponType);

	void deleteCoupon(String couponId);

	List<CouponType> getCouponType();

	boolean updateValue(String couponType, double value);

	List<Coupon> getUserCoupon(String mobile);

	void save(CouponType ct);

	void delectCoupon();

}
