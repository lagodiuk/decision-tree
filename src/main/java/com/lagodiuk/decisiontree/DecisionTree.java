package com.lagodiuk.decisiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

			if (rule.match(item)) {
				DefaultMutableTreeNode matchNode = (DefaultMutableTreeNode) node.getChildAt(0);
				return this.classify(item, matchNode);
			} else {
				DefaultMutableTreeNode notMatchNode = (DefaultMutableTreeNode) node.getChildAt(1);
				return this.classify(item, notMatchNode);
			}
		} else {
			return nodeData.toString();
		}
	}

	public DefaultMutableTreeNode getTree() {
		return this.tree;
	}

	public static DecisionTree build(
			List<Item> items,
			List<? extends Predicate> defaultPredicates) {

		return build(items, Collections.<String, List<? extends Predicate>> emptyMap(), defaultPredicates, Collections.<String> emptySet());
	}

	public static DecisionTree build(
			List<Item> items,
			Map<String, List<? extends Predicate>> attributesPredicates) {

		return build(items, attributesPredicates, Collections.<Predicate> emptyList(), Collections.<String> emptySet());
	}

	public static DecisionTree build(
			List<Item> items,
			Map<String, List<? extends Predicate>> attributesPredicates,
			List<? extends Predicate> defaultPredicates,
			Set<String> ignoredAttributes) {

		DefaultMutableTreeNode tree = buildTree(items, attributesPredicates, defaultPredicates, ignoredAttributes);
		return new DecisionTree(tree);
	}

	private static DefaultMutableTreeNode buildTree(
			List<Item> items,
			Map<String, List<? extends Predicate>> attributesPredicates,
			List<? extends Predicate> defaultPredicates,
			Set<String> ignoredAttributes) {

		double entropy = entropy(items);

		if (Double.compare(entropy, 0) == 0) {
			// all categories the same
			return makeLeaf(items);
		}

		SplitResult splitResult = findBestSplit(items, attributesPredicates, defaultPredicates, ignoredAttributes);

		if (splitResult == null) {
			// can't find split which reduces entropy
			return makeLeaf(items);
		}

		DefaultMutableTreeNode matchSubTree = buildTree(splitResult.matched, attributesPredicates, defaultPredicates, ignoredAttributes);

		DefaultMutableTreeNode notMatchSubTree = buildTree(splitResult.notMatched, attributesPredicates, defaultPredicates, ignoredAttributes);

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(splitResult.rule);
		root.add(matchSubTree);
		root.add(notMatchSubTree);
		return root;
	}

	private static DefaultMutableTreeNode makeLeaf(List<Item> items) {
		List<String> categories = getCategories(items);
		String category = getMostFrequentCategory(categories);
		return new DefaultMutableTreeNode(category);
	}

	private static String getMostFrequentCategory(List<String> categories) {
		Map<String, Integer> categoryCountMap = groupAndCount(categories);

		String mostFrequentCategory = null;
		int mostFrequentCategoryCount = -1;

		for (Entry<String, Integer> e : categoryCountMap.entrySet()) {
			String category = e.getKey();
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
		List<String> categories = getCategories(items);
		Map<String, Integer> categoryCount = groupAndCount(categories);
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
}
