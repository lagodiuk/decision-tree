package com.lagodiuk.decisiontree;

public class Rule {

	private final String attribute;

	private final Predicate condition;

	private final Object sampleValue;

	public Rule(String attribute, Predicate condition, Object sampleValue) {
		this.attribute = attribute;
		this.condition = condition;
		this.sampleValue = sampleValue;
	}

	public boolean match(Item item) {
		if (!item.hasAttribute(this.attribute)) {
			return false;
		}
		Object otherField = item.getFieldValue(this.attribute);
		return this.condition.eval(otherField, this.sampleValue);
	}

	@Override
	public String toString() {
		return String.format("%s %s %s", this.attribute, this.condition, this.sampleValue);
	}
}
