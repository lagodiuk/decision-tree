package com.lagodiuk.decisiontree;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Item {

	private Map<String, Object> fields = new HashMap<String, Object>();

	private String category;

	public String getCategory() {
		return this.category;
	}

	public Map<String, Object> getFields() {
		return Collections.unmodifiableMap(this.fields);
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setAttribute(String name, Object value) {
		this.fields.put(name, value);
	}

	public Set<String> getAttributeNames() {
		return this.fields.keySet();
	}

	public Object getFieldValue(String attributeName) {
		return this.fields.get(attributeName);
	}

	public boolean hasAttribute(String attributeName) {
		return this.fields.get(attributeName) != null;
	}
}
