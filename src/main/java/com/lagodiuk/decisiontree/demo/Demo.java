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

public class Demo {

	private static enum Result {
		WIN, LOOSE
	}

	public static void main(String[] args) {

		DecisionTree classifier =
				DecisionTree
						.createBuilder()
						.setDefaultPredicates( Predicate.EQUAL )
						.setTrainingSet(
                                Arrays.asList(
                                        makeItem( "Выше", "Дома", "На месте", "Да", Result.LOOSE ),
                                        makeItem( "Выше", "Дома", "На месте", "Нет", Result.WIN ),
                                        makeItem( "Выше", "Дома", "Пропускают", "Нет", Result.LOOSE ),
                                        makeItem( "Ниже", "Дома", "Пропускают", "Нет", Result.WIN ),
                                        makeItem( "Ниже", "В гостях", "Пропускают", "Нет", Result.LOOSE ),
                                        makeItem( "Ниже", "Дома", "Пропускают", "Да", Result.WIN ),
                                        makeItem( "Выше", "В гостях", "На месте", "Да", Result.LOOSE )
                                )
                        )
						.createDecisionTree();

        display( SwingTreeVisitor.buildSwingTree( classifier ), 300, 300);

		System.out.println(classifier.classify(makeItem("Ниже", "Дома", "На месте", "Нет", null)));
	}

	private static Item makeItem(String sopernik, String igraem, String lideru, String dojd, Result category) {
		Item item = new Item()
				.setCategory( category )
				.setAttribute( "Соперник", sopernik )
				.setAttribute( "Играем", igraem )
				.setAttribute( "Лидеры", lideru )
				.setAttribute( "Дождь", dojd );

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
