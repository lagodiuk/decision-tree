package com.lagodiuk.decisiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

public class DecisionTree {

	private Rule rule;

	private Object category;

	private DecisionTree matchSubTree;

	private DecisionTree notMatchSubTree;

	public Object classify(Item item) {
		if (this.rule != null) {

			if (this.rule.match(item)) {
				return this.matchSubTree.classify(item);
			} else {
				return this.notMatchSubTree.classify(item);
			}
		} else {
			return this.category;
		}
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public void setCategory(Object category) {
		this.category = category;
	}

	public void setMatchSubTree(DecisionTree matchSubTree) {
		this.matchSubTree = matchSubTree;
	}

	public void setNotMatchSubTree(DecisionTree notMatchSubTree) {
		this.notMatchSubTree = notMatchSubTree;
	}

	public DefaultMutableTreeNode getTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		if (this.rule != null) {
			root.setUserObject(this.rule.toString());
			root.add(this.matchSubTree.getTree());
			root.add(this.notMatchSubTree.getTree());
		} else {
			root.setUserObject(this.category.toString());
		}

		return root;
	}

	public static Factory createFactory() {
		return new Factory();
	}

	private static DecisionTree buildDTree(
			List<Item> items,
			int minimalNumberOfItems,
			Map<String, List<? extends Predicate>> attributesPredicates,
			List<? extends Predicate> defaultPredicates,
			Set<String> ignoredAttributes) {

		if (items.size() <= minimalNumberOfItems) {
			return makeDLeaf(items);
		}

		double entropy = entropy(items);

		if (Double.compare(entropy, 0) == 0) {
			// all categories the same
			return makeDLeaf(items);
		}

		SplitResult splitResult = findBestSplit(items, attributesPredicates, defaultPredicates, ignoredAttributes);

		if (splitResult == null) {
			// can't find split which reduces entropy
			return makeDLeaf(items);
		}

		DecisionTree matchSubTree =
				buildDTree(splitResult.matched, minimalNumberOfItems, attributesPredicates, defaultPredicates, ignoredAttributes);

		DecisionTree notMatchSubTree =
				buildDTree(splitResult.notMatched, minimalNumberOfItems, attributesPredicates, defaultPredicates, ignoredAttributes);

		DecisionTree root = new DecisionTree();
		root.setRule(splitResult.rule);
		root.setMatchSubTree(matchSubTree);
		root.setNotMatchSubTree(notMatchSubTree);
		return root;
	}

	private static DecisionTree makeDLeaf(List<Item> items) {
		List<Object> categories = getCategories(items);
		Object category = getMostFrequentCategory(categories);
		DecisionTree leaf = new DecisionTree();
		leaf.setCategory(category);
		return leaf;
	}

	private static Object getMostFrequentCategory(List<Object> categories) {
		Map<Object, Integer> categoryCountMap = groupAndCount(categories);

		Object mostFrequentCategory = null;
		int mostFrequentCategoryCount = -1;

		for (Entry<Object, Integer> e : categoryCountMap.entrySet()) {
			Object category = e.getKey();
			int count = e.getValue();

			if (count >= mostFrequentCategoryCount) {
				mostFrequentCategoryCount = count;
				mostFrequentCategory = category;
			}
		}

		return mostFrequentCategory;
	}

	private static SplitResult findBestSplit(
			List<Item> items,
			Map<String, List<? extends Predicate>> attributesPredicates,
			List<? extends Predicate> defaultPredicates,
			Set<String> ignoredAttributes) {

		double initialEntropy = entropy(items);

		double bestGain = 0;

		SplitResult bestSplitResult = null;

		for (Item baseItem : new LinkedList<Item>(items)) {
			for (String attr : baseItem.getAttributeNames()) {

				if (ignoredAttributes.contains(attr)) {
					continue;
				}

				Object value = baseItem.getFieldValue(attr);

				List<Predicate> predicates = predicatesForAttribute(attr, attributesPredicates, defaultPredicates);

				for (Predicate pred : predicates) {
					Rule rule = new Rule(attr, pred, value);

					SplitResult splitResult = split(rule, items);

					double matchedEntropy = entropy(splitResult.matched);
					double notMatchedEntropy = entropy(splitResult.notMatched);

					double pMatched = (double) splitResult.matched.size() / items.size();
					double pNotMatched = (double) splitResult.notMatched.size() / items.size();

					double gain = initialEntropy - (pMatched * matchedEntropy) - (pNotMatched * notMatchedEntropy);
					if (gain > bestGain) {
						bestGain = gain;
						bestSplitResult = splitResult;
					}
				}
			}
		}
		return bestSplitResult;
	}

	private static SplitResult split(Rule rule, List<Item> base) {
		List<Item> matched = new LinkedList<Item>();
		List<Item> notMatched = new LinkedList<Item>();

		for (Item otherItem : new LinkedList<Item>(base)) {
			if (rule.match(otherItem)) {
				matched.add(otherItem);
			} else {
				notMatched.add(otherItem);
			}
		}

		return new SplitResult(matched, notMatched, rule);
	}

	private static List<Predicate> predicatesForAttribute(
			String attr,
			Map<String, List<? extends Predicate>> attributesPredicates,
			List<? extends Predicate> defaultPredicates) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		List<? extends Predicate> attrPredicates = attributesPredicates.get(attr);
		if ((attrPredicates != null) && (!attrPredicates.isEmpty())) {
			predicates.addAll(attrPredicates);
		} else if ((defaultPredicates != null) && (!defaultPredicates.isEmpty())) {
			predicates.addAll(defaultPredicates);
		}

		return predicates;
	}

	private static double entropy(List<Item> items) {
		List<Object> categories = getCategories(items);
		Map<Object, Integer> categoryCount = groupAndCount(categories);
		return entropy(categoryCount.values());
	}

	private static double entropy(Collection<Integer> values) {
		double totalCount = 0;
		for (Integer count : values) {
			totalCount += count;
		}

		double entropy = 0;
		for (Integer count : values) {
			double x = count / totalCount;
			entropy += -x * Math.log(x);
		}
		return entropy;
	}

	private static List<Object> getCategories(List<Item> items) {
		List<Object> categories = new LinkedList<Object>();
		for (Item item : items) {
			categories.add(item.getCategory());
		}
		return categories;
	}

	private static Map<Object, Integer> groupAndCount(List<Object> categories) {
		Map<Object, Integer> categoryCount = new HashMap<Object, Integer>();
		for (Object cat : categories) {
			Integer count = categoryCount.get(cat);
			if (count == null) {
				count = 0;
			}
			categoryCount.put(cat, count + 1);
		}
		return categoryCount;
	}

	private static class SplitResult {

		public final Rule rule;

		public final List<Item> matched;

		public final List<Item> notMatched;

		public SplitResult(List<Item> matched, List<Item> notMatched, Rule rule) {
			this.rule = rule;
			this.matched = new ArrayList<Item>(matched);
			this.notMatched = new ArrayList<Item>(notMatched);
		}
	}

	public static class Factory {

		private List<Item> trainingSet;

		private List<? extends Predicate> defaultPredicates = new LinkedList<Predicate>();

		private Map<String, List<? extends Predicate>> attributesPredicates = new HashMap<String, List<? extends Predicate>>();

		private Set<String> ignoredAttributes = new HashSet<String>();

		private int minimalNumberOfItems = 1;

		private Factory() {
			// encapsulate
		}

		public DecisionTree createDecisionTree() {
			DecisionTree tree = buildDTree(
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

		public Factory setTrainingSet(List<Item> trainingSet) {
			this.trainingSet = trainingSet;
			return this;
		}

		public List<? extends Predicate> getDefaultPredicates() {
			return this.defaultPredicates;
		}

		public Factory setDefaultPredicates(List<? extends Predicate> defaultPredicates) {
			this.defaultPredicates = defaultPredicates;
			return this;
		}

		public Map<String, List<? extends Predicate>> getAttributesPredicates() {
			return this.attributesPredicates;
		}

		public Factory setAttributePredicates(String attribute, List<? extends Predicate> predicates) {
			this.attributesPredicates.put(attribute, predicates);
			return this;
		}

		public Set<String> getIgnoredAttributes() {
			return this.ignoredAttributes;
		}

		public Factory setIgnoredAttributes(Set<String> ignoredAttributes) {
			this.ignoredAttributes = ignoredAttributes;
			return this;
		}

		public Factory setMinimalNumberOfItems(int minimalNumberOfItems) {
			this.minimalNumberOfItems = minimalNumberOfItems;
			return this;
		}

		public int getMinimalNumberOfItems() {
			return this.minimalNumberOfItems;
		}
	}
}
