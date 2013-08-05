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
package com.lagodiuk.decisiontree.demo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.lagodiuk.decisiontree.Predicate;
import com.lagodiuk.decisiontree.DecisionTree;
import com.lagodiuk.decisiontree.Item;

public class Demo3 {

	public static void main(String[] args) {

		DecisionTree classifier =
				DecisionTree
						.createBuilder()
						.setDefaultPredicates(Predicate.GTE, Predicate.LTE)
						.setTrainingSet(makeTrainingSet())
						.setMinimalNumberOfItems(5)
						.createDecisionTree()
						.mergeRedundantRules();

		display(classifier.getSwingTree(), 300, 300);

		JFrame f1 = new JFrame();
		f1.setSize(300, 300);
		f1.setLocationRelativeTo(null);
		JPanel p = new JPanel();
		f1.add(p);
		f1.setVisible(true);

		BufferedImage bi = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();

		for (int x = 0; x < 200; x++) {
			for (int y = 0; y < 200; y++) {
				Item item = makeEmptyItem((x - 100) / 5, (y - 100) / 5);

				if ("red".equals(classifier.classify(item))) {
					g.setColor(Color.RED);
				}
				if ("green".equals(classifier.classify(item))) {
					g.setColor(Color.GREEN);
				}

				g.drawOval(x, y, 2, 2);
			}
		}

		for (;;) {
			p.getGraphics().drawImage(bi, 0, 0, null);
		}
	}

	private static List<Item> makeTrainingSet() {
		Random random = new Random();

		List<Item> items = new LinkedList<Item>();
		for (double i = 0; i <= 1000; i++) {
			items.add(makeItem((random.nextDouble() - random.nextDouble()) * 14, (random.nextDouble() - random.nextDouble()) * 14));
		}

		return items;
	}

	private static Item makeItem(double x, double y) {
		Item item = new Item();

		if (((x * x) + (y * y)) <= (10 * 10)) {
			item.setCategory("red");
			if (((x * x) + (y * y)) <= (5 * 5)) {
				item.setCategory("green");
			}
		} else {
			item.setCategory("green");
		}

		item.setAttribute("x", x);
		item.setAttribute("y", y);

		return item;
	}

	private static Item makeEmptyItem(double x, double y) {
		Item item = new Item();

		item.setAttribute("x", x);
		item.setAttribute("y", y);

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
