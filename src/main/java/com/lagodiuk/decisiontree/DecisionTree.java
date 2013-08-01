package com.lagodiuk.decisiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
		if (this.isLeaf()) {
			return this.category;
		} else {
			if (this.rule.match(item)) {
				return this.matchSubTree.classify(item);
			} else {
				return this.notMatchSubTree.classify(item);
			}
		}
	}

	public DecisionTree mergeRedundantRules() {

		if (this.matchSubTree != null) {
			this.matchSubTree.mergeRedundantRules();
		}

		if (this.notMatchSubTree != null) {
			this.notMatchSubTree.mergeRedundantRules();
		}

		if ((this.rule != null)
				&& (this.matchSubTree.category != null)
				&& (this.notMatchSubTree.category != null)
				&& (this.matchSubTree.category.equals(this.notMatchSubTree.category))) {

			this.category = this.matchSubTree.category;
			this.rule = null;
			this.matchSubTree = null;
			this.notMatchSubTree = null;
		}

		return this;
	}

	public DefaultMutableTreeNode getSwingTree() {
		SwingTreeVisitor visitor = new SwingTreeVisitor();
		this.accept(visitor);
		return visitor.getRoot();
	}

	public void accept(DecisionTreeVisitor visitor) {
		visitor.visit(this);
	}

	public boolean isLeaf() {
		return this.rule == null;
	}

	public Object getCategory() {
		return this.category;
	}

	public Rule getRule() {
		return this.rule;
	}

	public DecisionTree getMatchSubTree() {
		return this.matchSubTree;
	}

	public DecisionTree getNotMatchSubTree() {
		return this.notMatchSubTree;
	}

	public static DecisionTreeBuilder createBuilder() {
		return new DecisionTreeBuilder();
	}

	public static DecisionTree buildDTree(
			List<Item> items,
			int minimalNumberOfItems,
			Map<String, List<Predicate>> attributesPredicates,
			List<Predicate> defaultPredicates,
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
		root.rule = splitResult.rule;
		root.matchSubTree = matchSubTree;
		root.notMatchSubTree = notMatchSubTree;
		return root;
	}

	private static DecisionTree makeDLeaf(List<Item> items) {
		List<Object> categories = getCategories(items);
		Object category = getMostFrequentCategory(categories);
		DecisionTree leaf = new DecisionTree();
		leaf.category = category;
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
			Map<String, List<Predicate>> attributesPredicates,
			List<Predicate> defaultPredicates,
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
			Map<String, List<Predicate>> attributesPredicates,
			List<Predicate> defaultPredicates) {

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

	private static class SwingTreeVisitor implements DecisionTreeVisitor {

		private DefaultMutableTreeNode root = new DefaultMutableTreeNode();

		@Override
		public void visit(DecisionTree tree) {
			if (tree.rule != null) {
				String description = null;

				Predicate predicate = tree.rule.getPredicate();
				String attribute = tree.rule.getAttribute();
				Object value = tree.rule.getSampleValue();
				switch (predicate) {
					case EQUAL:
						description = attribute + " == " + value;
						break;

					case EXISTS:
						description = "exists " + attribute;
						break;

					case GTE:
						description = attribute + " >= " + value;
						break;

					case LTE:
						description = attribute + " =< " + value;
						break;

					default:
						description = attribute + predicate.toString() + value;
						break;
				}

				this.root.setUserObject(description);
				this.root.add(tree.matchSubTree.getSwingTree());
				this.root.add(tree.notMatchSubTree.getSwingTree());
			} else {
				this.root.setUserObject(tree.category.toString());
			}
		}

		public DefaultMutableTreeNode getRoot() {
			return this.root;
		}
	}
}
