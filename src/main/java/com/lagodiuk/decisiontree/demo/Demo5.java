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

import com.lagodiuk.decisiontree.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Demo5 {

	public static void main(String[] args) {

		DecisionTreeBuilder builder =
				DecisionTree
						.createBuilder()
						.setTrainingSet( makeTrainingSet() )
						.setDefaultPredicates( Predicate.GTE, Predicate.LTE )
						.setMinimalNumberOfItems( 7 );

		RandomForest forest = RandomForest.create(builder, 10);

		JFrame f1 = new JFrame();
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

				Map<Object, Integer> result = forest.classify(item);

				int red = 0;
				int green = 0;

				for (Object colour : result.keySet()) {
					if ("red".equals(colour)) {
						red = result.get(colour);
					} else if ("green".equals(colour)) {
						green = result.get(colour);
					}
				}

				g.setColor(new Color(((float) red) / (red + green), ((float) green) / (red + green), 0));

				g.drawOval(x, y, 2, 2);
			}
		}

		while( true ) {
			p.getGraphics().drawImage(bi, 0, 0, null);
		}
	}

	private static List<Item> makeTrainingSet() {
		Random random = new Random();

		List<Item> items = new LinkedList<Item>();
		for (double i = 0; i <= 700; i++) {
			items.add(makeItem((random.nextDouble() - random.nextDouble()) * 14, (random.nextDouble() - random.nextDouble()) * 14));
		}

		return items;
	}

	private static Item makeItem(double x, double y) {
		Item item = new Item();

		if ((((x * x) + (y * y)) <= (10 * 10)) && (Math.random() < 0.9)) {
			item.setCategory("red");
			if ((((x * x) + (y * y)) <= (5 * 5)) && (Math.random() < 0.8)) {
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
}
