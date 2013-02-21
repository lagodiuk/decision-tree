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
		if (!item.hasAttribute(this.attribute)) {
			return false;
		}
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
	public String toString() {
		return this.predicate.getDescription(this.attribute, this.sampleValue);
	}
}
