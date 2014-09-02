package com.pys.liwuso.bean;

public class SearchCatalog extends Entity {

	public final static String NODE_START = "item";
	public final static String NODE_ID = "id";
	public final static String NODE_NAME = "name";

	public String Name;

	public SearchCatalog(int id, String name) {
		this.id = id;
		this.Name = name;
	}

}