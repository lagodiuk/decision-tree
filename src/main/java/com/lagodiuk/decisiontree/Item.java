/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Yurii Lahodiuk (yura_lagodiuk@ukr.net)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
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
