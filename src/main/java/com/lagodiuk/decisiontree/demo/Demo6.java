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

import com.lagodiuk.decisiontree.DecisionTree;
import com.lagodiuk.decisiontree.Item;
import com.lagodiuk.decisiontree.Predicate;
import com.lagodiuk.decisiontree.visitors.SwingTreeVisitor;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Demo6 {

	public static enum Type {
		YELLOW, GREEN
	}

	public static void main(String[] args) {
		DecisionTree tree =
				DecisionTree
						.createBuilder()
						.setTrainingSet( trainingSet() )
						.setDefaultPredicates( Predicate.GTE, Predicate.LTE )
						.createDecisionTree();

        display( SwingTreeVisitor.buildSwingTree( tree ), 300, 400);
	}

	private static List<Item> trainingSet() {
		return Arrays.asList(
				item(0, Type.YELLOW),
				item(1, Type.GREEN),
				item(2, Type.GREEN),
				item(3, Type.GREEN),
				item(4, Type.GREEN),
				item(5, Type.YELLOW),
				item(6, Type.YELLOW),
				item(7, Type.YELLOW),
				item(8, Type.YELLOW),
				item(9, Type.GREEN),
				item(10, Type.GREEN),
				item(11, Type.GREEN),
				item(12, Type.GREEN),
				item(13, Type.YELLOW),
				item(14, Type.YELLOW),
				item(15, Type.YELLOW),
				item(16, Type.YELLOW),
				item(17, Type.YELLOW),
				item(18, Type.YELLOW),
				item(19, Type.GREEN)
				);
	}

	private static Item item(int x, Type type) {
		Item item = new Item();
		item.setAttribute("x", x);
		item.setCategory(type);
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
