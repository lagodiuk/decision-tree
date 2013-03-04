package com.lagodiuk.decisiontree;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Item {

	private Map<String, Object> fields = new HashMap<String, Object>();

	private Object category;

	public Object getCategory() {
		return this.category;
	}

	public Map<String, Object> getFields() {
		return Collections.unmodifiableMap(this.fields);
	}

	public Item setCategory(Object category) {
		this.category = category;
		return this;
	}

	public Item setAttribute(String name, Object value) {
		this.fields.put(name, value);
		return this;
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
