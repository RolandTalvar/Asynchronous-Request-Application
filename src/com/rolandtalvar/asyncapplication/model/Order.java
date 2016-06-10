package com.rolandtalvar.asyncapplication.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable{
	private static final long serialVersionUID = 1L;

	private long orderId = 5236234;

	public List<OrderRow> orderRows;
	
	public Order(){
		orderRows = new ArrayList<>();
	}
	
	public void addOrderRow(OrderRow orderRow){
		this.orderRows.add(orderRow);
	}
	
	public List<OrderRow> getOrderRows(){
		return orderRows;
	}

	public String toString() {
		return "Order number: " + orderId + " with " + getOrderRows().size() + " rows";
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
}
