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

public class Demo4 {

	public static void main(String[] args) {

		DecisionTree dt =
				DecisionTree
						.createBuilder()
						.setDefaultPredicates( Predicate.EQUAL, Predicate.GTE, Predicate.LTE )
						.setAttributePredicates( "Outlook", Predicate.EQUAL )
						.setAttributePredicates( "Windy", Predicate.EQUAL )
						.setTrainingSet( makeTrainingSet() )
						.createDecisionTree();

        display( SwingTreeVisitor.buildSwingTree( dt ), 400, 500);
	}

	private static List<Item> makeTrainingSet() {
		return Arrays.asList(
				makeItem("sunny", 85, 85, false, "Don't Play"),
				makeItem("sunny", 80, 90, true, "Don't Play"),
				makeItem("overcast", 83, 78, false, "Play"),
				makeItem("rain", 70, 96, false, "Play"),
				makeItem("rain", 68, 80, false, "Play"),
				makeItem("rain", 65, 70, true, "Don't Play"),
				makeItem("overcast", 64, 65, true, "Play"),
				makeItem("sunny", 72, 95, false, "Don't Play"),
				makeItem("sunny", 69, 70, false, "Play"),
				makeItem("rain", 75, 80, false, "Play"),
				makeItem("sunny", 75, 70, true, "Play"),
				makeItem("overcast", 72, 90, true, "Play"),
				makeItem("overcast", 81, 75, false, "Play"),
				makeItem("rain", 71, 80, true, "Don't Play"));
	}

	private static Item makeItem(String outlook, int temperature, int humidity, boolean windy, String decision) {
		Item item = new Item();

		item.setAttribute("Outlook", outlook);
		item.setAttribute("Temperature", temperature);
		item.setAttribute("Humidity", humidity);
		item.setAttribute("Windy", windy);

		item.setCategory(decision);

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
