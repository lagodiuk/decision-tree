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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.lagodiuk.decisiontree.Predicate;
import com.lagodiuk.decisiontree.DecisionTree;
import com.lagodiuk.decisiontree.Item;

public class Demo2 {

	public static void main(String[] args) {

		DecisionTree classifier =
				DecisionTree
						.createBuilder()
						.setDefaultPredicates(Predicate.EQUAL, Predicate.GTE, Predicate.LTE)
						.setTrainingSet(makeTrainingSet())
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
				Item item = makeItem((x - 100) / 5, (y - 100) / 5);

				if ("in a circle".equals(classifier.classify(item))) {
					g.setColor(Color.RED);
				} else {
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
		List<Item> items = new LinkedList<Item>();
		for (double i = -12; i <= 12; i += 1) {
			for (double j = -12; j <= 12; j += 1) {
				items.add(makeItem(i, j));
			}
		}
		return items;
	}

	private static Item makeItem(double x, double y) {
		Item item = new Item();

		if (((x * x) + (y * y)) <= (10 * 10)) {
			item.setCategory("in a circle");
		} else {
			item.setCategory("out of a circle");
		}

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
