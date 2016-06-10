package com.rolandtalvar.asyncapplication.model;

import java.io.Serializable;

public class OrderRow implements Serializable{
	
	private Product product;
	private Long quantity;
	
	public OrderRow(Product product, Long quantity){
		this.product = product;
		this.quantity = quantity;
	}
	
	public void setProduct(Product product){
		this.product = product;
	}
	
	public void setQuantity(Long quantity){
		this.quantity = quantity;
	}
	
	public Product getProduct(){
		return product;
	}
	
	public Long getQuantity(){
		return quantity;
	}
	
	public String toString() {
		return "Product: " + product + ", quantity: " + quantity;
	}
}
