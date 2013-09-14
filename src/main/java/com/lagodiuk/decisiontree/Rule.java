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

public class Rule {

	private final String attribute;

	private final Predicate predicate;

	private final Object sampleValue;

	public Rule(String attribute, Predicate predicate, Object sampleValue) {
		this.attribute = attribute;
		this.predicate = predicate;
		this.sampleValue = sampleValue;
	}

	public boolean match(Item item) {
		Object otherField = item.getFieldValue(this.attribute);
		return this.predicate.eval(otherField, this.sampleValue);
	}

	public String getAttribute() {
		return this.attribute;
	}

	public Predicate getPredicate() {
		return this.predicate;
	}

	public Object getSampleValue() {
		return this.sampleValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attribute == null) ? 0 : attribute.hashCode());
		result = prime * result
				+ ((predicate == null) ? 0 : predicate.hashCode());
		result = prime * result
				+ ((sampleValue == null) ? 0 : sampleValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (attribute == null) {
			if (other.attribute != null)
				return false;
		} else if (!attribute.equals(other.attribute))
			return false;
		if (predicate != other.predicate)
			return false;
		if (sampleValue == null) {
			if (other.sampleValue != null)
				return false;
		} else if (!sampleValue.equals(other.sampleValue))
			return false;
		return true;
	}
}
