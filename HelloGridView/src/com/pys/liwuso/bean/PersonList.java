package com.pys.liwuso.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.liwuso.app.AppException;

public class PersonList extends Entity {

	public final static int CATALOG_ALL = 1;
	public final static int CATALOG_INTEGRATION = 2;
	public final static int CATALOG_SOFTWARE = 3;

	private int catalog;
	private int pageSize = 5;
	private int personCount;
	private List<Person> person_list = new ArrayList<Person>();

	public int getCatalog() {
		return catalog;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void Add(Person person) {
		person_list.add(person);
	}

	public int getPersonCount() {
		return person_list.size();
	}

	public List<Person> getPersonList() {
		return person_list;
	}

	public static PersonList parse(InputStream inputStream) throws IOException,
			AppException {
		PersonList personlist = new PersonList();
		return personlist;
	}
}
