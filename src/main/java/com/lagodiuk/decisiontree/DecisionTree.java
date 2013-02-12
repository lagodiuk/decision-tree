package com.lagodiuk.decisiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

public class DecisionTree {

	private final DefaultMutableTreeNode tree;

	private DecisionTree(DefaultMutableTreeNode tree) {
		this.tree = tree;
	}

	public String classify(Item item) {
		return this.classify(item, this.tree);
	}

	private String classify(Item item, DefaultMutableTreeNode node) {
		Object nodeData = node.getUserObject();

		if (Rule.class.isInstance(nodeData)) {
			Rule rule = (Rule) nodeData;

			// System.out.println(rule + "\t" + rule.match(item));

			if (rule.match(item)) {
				DefaultMutableTreeNode matchNode = (DefaultMutableTreeNode) node.getChildAt(0);
				return this.classify(item, matchNode);
			} else {
				DefaultMutableTreeNode notMatchNode = (DefaultMutableTreeNode) node.getChildAt(1);
				return this.classify(item, notMatchNode);
			}
		}

		return nodeData.toString();
	}

	public DefaultMutableTreeNode getTree() {
		return this.tree;
	}

	public static DecisionTree build(
			List<Item> items,
			List<? extends Predicate> defaultPredicates) {

		return build(items, Collections.<String, List<? extends Predicate>> emptyMap(), defaultPredicates);
	}

	public static DecisionTree build(
			List<Item> items,
			Map<String, List<? extends Predicate>> attributePredicates) {

		return build(items, attributePredicates, Collections.<Predicate> emptyList());
	}

	public static DecisionTree build(
			List<Item> items,
			Map<String, List<? extends Predicate>> attributePredicates,
			List<? extends Predicate> defaultPredicates) {

		DefaultMutableTreeNode tree = buildTree(items, attributePredicates, defaultPredicates);
		return new DecisionTree(tree);
	}

	private static DefaultMutableTreeNode buildTree(
			List<Item> items, Map<String,
			List<? extends Predicate>> attributeConditions,
			List<? extends Predicate> defaultPredicates) {

		double entropy = entropy(items);

		if (Double.compare(entropy, 0) == 0) {
			// entropy == 0
			// all categories the same
			List<String> categories = getCategories(items);
			String category = categories.get(0);
			return new DefaultMutableTreeNode(category);
		}

		Rule rule = divide(items, attributeConditions, defaultPredicates);

		if (rule == null) {
			// can't find rule which produces better division
			List<String> categories = getCategories(items);
			return new DefaultMutableTreeNode(categories);
		}

		List<Item> matched = new LinkedList<Item>();
		List<Item> notMatched = new LinkedList<Item>();

		for (Item otherItem : new LinkedList<Item>(items)) {
			if (rule.match(otherItem)) {
				matched.add(otherItem);
			} else {
				notMatched.add(otherItem);
			}
		}

		DefaultMutableTreeNode node = new DefaultMutableTreeNode(rule);

		DefaultMutableTreeNode matchNode = buildTree(matched, attributeConditions, defaultPredicates);
		DefaultMutableTreeNode notMatchNode = buildTree(notMatched, attributeConditions, defaultPredicates);

		node.add(matchNode);
		node.add(notMatchNode);

		return node;
	}

	private static Rule divide(
			List<Item> items,
			Map<String, List<? extends Predicate>> attributeConditions,
			List<? extends Predicate> defaultPredicates) {

		double initialEntropy = entropy(items);

		double bestGain = 0;
		Rule bestRule = null;

		for (Item baseItem : new LinkedList<Item>(items)) {
			for (String attr : baseItem.getAttributeNames()) {
				Object baseValue = baseItem.getFieldValue(attr);

				List<Predicate> predicates = new ArrayList<Predicate>();

				List<? extends Predicate> attributePredicates = attributeConditions.get(attr);
				if ((attributePredicates != null) && (!attributePredicates.isEmpty())) {
					predicates.addAll(attributePredicates);
				} else if ((defaultPredicates != null) && (!defaultPredicates.isEmpty())) {
					predicates.addAll(defaultPredicates);
				}

				for (Predicate cond : predicates) {
					Rule rule = new Rule(attr, cond, baseValue);

					List<Item> matched = new LinkedList<Item>();
					List<Item> notMatched = new LinkedList<Item>();

					for (Item otherItem : new LinkedList<Item>(items)) {
						if (rule.match(otherItem)) {
							matched.add(otherItem);
						} else {
							notMatched.add(otherItem);
						}
					}

					double matchedEntropy = entropy(matched);
					double notMatchedEntropy = entropy(notMatched);

					double pMatched = (double) matched.size() / items.size();
					double pNotMatched = 1 - pMatched;

					double gain = initialEntropy - (pMatched * matchedEntropy) - (pNotMatched * notMatchedEntropy);
					if (gain > bestGain) {
						bestGain = gain;
						bestRule = rule;
					}
				}
			}
		}
		return bestRule;
	}

	private static double entropy(List<Item> items) {
		List<String> categories = getCategories(items);
		Map<String, Integer> categoryCount = groupAndCount(categories);
		return entropy(categoryCount.values());
	}

	private static List<String> getCategories(List<Item> items) {
		List<String> categories = new LinkedList<String>();
		for (Item item : items) {
			categories.add(item.getCategory());
		}
		return categories;
	}

	private static Map<String, Integer> groupAndCount(List<String> categories) {
		Map<String, Integer> categoryCount = new HashMap<String, Integer>();
		for (String item : categories) {
			String cat = item;
			Integer count = categoryCount.get(cat);
			if (count == null) {
				count = 0;
			}
			categoryCount.put(cat, count + 1);
		}
		return categoryCount;
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
}
