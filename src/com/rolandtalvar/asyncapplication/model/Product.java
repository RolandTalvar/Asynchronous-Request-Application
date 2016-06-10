package com.rolandtalvar.asyncapplication.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Serializable{

	private int id;
	private String name;
	private BigDecimal price;
	
	public Product(int id, String name, BigDecimal price){
		this.id = id;
		this.name = name;
		this.price = price;
	}
	
	public int getId(){
		return id;
	}
	
	public BigDecimal getPrice(){
		return price;
	}
	
	public String getName(){
		return name;
	}
	
	public String toString() {
		return "Product ID: " + id + ", name: " + name + ", price: " + price;
	}
}
