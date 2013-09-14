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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DecisionTreeBuilder {

	private List<Item> trainingSet;

	private List<Predicate> defaultPredicates = new LinkedList<Predicate>();

	private Map<String, List<Predicate>> attributesPredicates = new HashMap<String, List<Predicate>>();

	private Set<String> ignoredAttributes = new HashSet<String>();

	private int minimalNumberOfItems = 1;

	public DecisionTree createDecisionTree() {
		DecisionTree tree = DecisionTree.buildDecisionTree(
				this.trainingSet,
				this.minimalNumberOfItems,
				this.attributesPredicates,
				this.defaultPredicates,
				this.ignoredAttributes);

		return tree;
	}

	
	public RandomForest createRandomForest(int numberOfTrees) {
		return RandomForest.create(this, numberOfTrees);
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

	public DecisionTreeBuilder setDefaultPredicates(List<Predicate> defaultPredicates) {
		this.defaultPredicates = defaultPredicates;
		return this;
	}

	public DecisionTreeBuilder setDefaultPredicates(Predicate... defaultPredicates) {
		this.defaultPredicates = Arrays.asList(defaultPredicates);
		return this;
	}

	public Map<String, List<Predicate>> getAttributesPredicates() {
		return this.attributesPredicates;
	}

	public DecisionTreeBuilder setAttributePredicates(String attribute, List<Predicate> predicates) {
		this.attributesPredicates.put(attribute, predicates);
		return this;
	}

	public DecisionTreeBuilder setAttributePredicates(String attribute, Predicate... predicates) {
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
	
	public DecisionTreeBuilder setIgnoredAttributes(String... ignoredAttributes) {
		this.ignoredAttributes = new HashSet<String>(ignoredAttributes.length);
		for(String attr : ignoredAttributes) {
			this.ignoredAttributes.add(attr);
		}
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
