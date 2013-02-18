package com.lagodiuk.decisiontree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DecisionTreeBuilder {

	private List<Item> trainingSet;

	private List<? extends Predicate> defaultPredicates = new LinkedList<Predicate>();

	private Map<String, List<? extends Predicate>> attributesPredicates = new HashMap<String, List<? extends Predicate>>();

	private Set<String> ignoredAttributes = new HashSet<String>();

	private int minimalNumberOfItems = 1;

	public DecisionTree createDecisionTree() {
		DecisionTree tree = DecisionTree.buildDTree(
				this.trainingSet,
				this.minimalNumberOfItems,
				this.attributesPredicates,
				this.defaultPredicates,
				this.ignoredAttributes);

		return tree;
	}

	public List<Item> getTrainingSet() {
		return this.trainingSet;
	}

	public DecisionTreeBuilder setTrainingSet(List<Item> trainingSet) {
		this.trainingSet = trainingSet;
		return this;
	}

	public List<? extends Predicate> getDefaultPredicates() {
		return this.defaultPredicates;
	}

	public DecisionTreeBuilder setDefaultPredicates(List<? extends Predicate> defaultPredicates) {
		this.defaultPredicates = defaultPredicates;
		return this;
	}

	public <T extends Predicate> DecisionTreeBuilder setDefaultPredicates(T... defaultPredicates) {
		this.defaultPredicates = Arrays.asList(defaultPredicates);
		return this;
	}

	public Map<String, List<? extends Predicate>> getAttributesPredicates() {
		return this.attributesPredicates;
	}

	public DecisionTreeBuilder setAttributePredicates(String attribute, List<? extends Predicate> predicates) {
		this.attributesPredicates.put(attribute, predicates);
		return this;
	}

	public <T extends Predicate> DecisionTreeBuilder setAttributePredicates(String attribute, T... predicates) {
		this.attributesPredicates.put(attribute, Arrays.asList(predicates));
		return this;
	}

	public Set<String> getIgnoredAttributes() {
		return this.ignoredAttributes;
	}

	public DecisionTreeBuilder setIgnoredAttributes(Set<String> ignoredAttributes) {
		this.ignoredAttributes = ignoredAttributes;
		return this;
	}

	public DecisionTreeBuilder setMinimalNumberOfItems(int minimalNumberOfItems) {
		this.minimalNumberOfItems = minimalNumberOfItems;
		return this;
	}

	public int getMinimalNumberOfItems() {
		return this.minimalNumberOfItems;
	}
}