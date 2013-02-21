package com.lagodiuk.decisiontree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomForest {

	private static final Random random = new Random();

	private List<DecisionTree> trees = new ArrayList<DecisionTree>();

	public Map<Object, Integer> classify(Item item) {
		Map<Object, Integer> result = new HashMap<Object, Integer>();

		for (DecisionTree tree : this.trees) {
			Object category = tree.classify(item);

			Integer count = result.get(category);
			count = (count == null) ? 1 : count + 1;

			result.put(category, count);
		}
		return result;
	}

	public static RandomForest create(List<Item> items, DecisionTreeBuilder builder) {
		RandomForest forest = new RandomForest();

		for (int i = 0; i < 10; i++) {
			DecisionTree tree = builder.setTrainingSet(getRandomSubset(items)).createDecisionTree().margeRedundantRules();
			forest.trees.add(tree);
		}

		return forest;
	}

	private static List<Item> getRandomSubset(List<Item> items) {
		int itemsCount = items.size();

		List<Item> result = new LinkedList<Item>();
		for (int i = 0; i < (itemsCount * 0.8); i++) {
			result.add(items.get(random.nextInt(itemsCount)));
		}

		return result;
	}

}
