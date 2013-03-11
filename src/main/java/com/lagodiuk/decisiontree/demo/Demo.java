package com.lagodiuk.decisiontree.demo;
import java.awt.BorderLayout;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.lagodiuk.decisiontree.DecisionTree;
import com.lagodiuk.decisiontree.Item;
import com.lagodiuk.decisiontree.Predicate;

public class Demo {

	private static enum Result {
		WIN, LOOSE
	}

	public static void main(String[] args) {

		DecisionTree classifier =
				DecisionTree
						.createBuilder()
						.setDefaultPredicates(Predicate.EQUAL)
						.setTrainingSet(Arrays.asList(
								makeItem("Выше", "Дома", "На месте", "Да", Result.LOOSE),
								makeItem("Выше", "Дома", "На месте", "Нет", Result.WIN),
								makeItem("Выше", "Дома", "Пропускают", "Нет", Result.LOOSE),
								makeItem("Ниже", "Дома", "Пропускают", "Нет", Result.WIN),
								makeItem("Ниже", "В гостях", "Пропускают", "Нет", Result.LOOSE),
								makeItem("Ниже", "Дома", "Пропускают", "Да", Result.WIN),
								makeItem("Выше", "В гостях", "На месте", "Да", Result.LOOSE)))
						.createDecisionTree();

		display(classifier.getSwingTree(), 300, 300);

		System.out.println(classifier.classify(makeItem("Ниже", "Дома", "На месте", "Нет", null)));
	}

	private static Item makeItem(String sopernik, String igraem, String lideru, String dojd, Result category) {
		Item item = new Item()
				.setCategory(category)
				.setAttribute("Соперник", sopernik)
				.setAttribute("Играем", igraem)
				.setAttribute("Лидеры", lideru)
				.setAttribute("Дождь", dojd);

		return item;
	}

	public static JFrame display(DefaultMutableTreeNode root, int width, int height) {
		JTree tree = new JTree(root);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JScrollPane(tree), BorderLayout.CENTER);
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
		frame.setSize(width, height);
		// put frame at center of screen
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		return frame;
	}
}
