decision-tree
=============

Implementation of Decision tree engine from scratch

### simple demo ###
* Install [Java Runtime Environment](http://www.java.com/en/download/help/download_options.xml)
* Download <i>dec_tree_demo.jar</i> from https://github.com/lagodiuk/decision-tree/tree/master/bin
* Launch demo from command line: <i>java -jar dec_tree_demo.jar out.png</i>

### for developers ###
Language: Java <br/>
Build with: Maven <br/>
<ol>
<li> git clone git://github.com/lagodiuk/decision-tree.git </li>
<li> mvn -f decision-tree/pom.xml install </li>
</ol>
Add this project as a maven dependency to your project.

Simple demo, which trying to classify points inside of the circle:

![Demo](https://raw.github.com/lagodiuk/decision-tree/master/img/test.png)

Source:
```java
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.lagodiuk.decisiontree.DecisionTree;
import com.lagodiuk.decisiontree.DecisionTreeBuilder;
import com.lagodiuk.decisiontree.Item;
import com.lagodiuk.decisiontree.Predicate;
import com.lagodiuk.decisiontree.RandomForest;

public class MainDemo {

	private static final int TRAINING_SET_SIZE = 1000;

	private static final int WIDTH = 150;

	private static final int HEIGHT = 150;

	private static final int RADIUS = 40;

	private static Random random = new Random();

	private static enum Type {
		RED, GREEN
	}

	public static void main(String[] args) throws Exception {
		List<Item> trainingSet = createTrainingSet();

		DecisionTreeBuilder tbuilder =
				DecisionTree
						.createBuilder()
						.setTrainingSet(trainingSet)
						.setDefaultPredicates(Predicate.GTE, Predicate.LTE);

		DecisionTree tree = tbuilder.createDecisionTree();

		// Creating Random Forest with 7 trees
		RandomForest forest = RandomForest.create(tbuilder, 7);

		displayResults(args, trainingSet, tree, forest);
	}

	/**
	 * Training set contains red and green points <br/>
	 * Red points are actually inside of the circle
	 */
	private static List<Item> createTrainingSet() {
		List<Item> items = new ArrayList<Item>(TRAINING_SET_SIZE);

		for (int i = 0; i < TRAINING_SET_SIZE; i++) {
			int x = random.nextInt(WIDTH);
			int y = random.nextInt(HEIGHT);

			Item item = newItem(x, y);

			if (isRed(x, y)) {
				item.setCategory(Type.RED);
			} else {
				item.setCategory(Type.GREEN);
			}

			items.add(item);
		}
		return items;
	}

	/**
	 * Item is generic entity, which is recognized by decision tree classifier <br/>
	 * This method is just a wrapper for its constructor
	 */
	private static Item newItem(int x, int y) {
		return new Item()
				.setAttribute("x", x)
				.setAttribute("y", y);
	}

	/**
	 * Checking that point(x, y) is inside of the circle
	 */
	private static boolean isRed(int x, int y) {
		x = x - (WIDTH / 2);
		y = y - (HEIGHT / 2);
		return ((x * x) + (y * y)) <= (RADIUS * RADIUS);
	}

	/**
	 * Visualize training set, and results of using of decision tree and random forest to png image
	 */
	private static void displayResults(
				String[] args, 
				List<Item> trainingSet, 
				DecisionTree tree, 
				RandomForest forest) throws IOException {
				
		BufferedImage img = createBufferedImage();

		displayTrainingSet(trainingSet, img);

		displayDecisionTree(tree, img);

		displayForest(forest, img);

		saveToFile(args, img);
	}

	private static BufferedImage createBufferedImage() {
		return new BufferedImage((WIDTH * 3) + 2, HEIGHT, BufferedImage.TYPE_INT_RGB);
	}

	private static void displayTrainingSet(List<Item> trainingSet, BufferedImage img) {
		for (Item item : trainingSet) {
			int x = (Integer) item.getFieldValue("x");
			int y = (Integer) item.getFieldValue("y");

			switch (((Type) item.getCategory())) {
				case RED:
					img.setRGB(x, y, Color.RED.getRGB());
					break;
				case GREEN:
					img.setRGB(x, y, Color.GREEN.getRGB());
					break;
			}
		}
	}

	private static void displayDecisionTree(DecisionTree tree, BufferedImage img) {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {

				// creating item which we want to classify
				Item item = newItem(x, y);

				switch ((Type) tree.classify(item)) {
					case RED:
						img.setRGB(x + WIDTH + 1, y, Color.RED.getRGB());
						break;
					case GREEN:
						img.setRGB(x + WIDTH + 1, y, Color.GREEN.getRGB());
						break;
				}
			}
		}
	}

	private static void displayForest(RandomForest forest, BufferedImage img) {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {

				// creating item which we want to classify
				Item item = newItem(x, y);
				
				// result of classification by random forest is map,
				// which contains categories, that been suggested by each of decision trees,
				// and count of each suggestion
				Map<Object, Integer> result = forest.classify(item);

				int red = 0;
				int green = 0;

				for (Object color : result.keySet()) {
					if (Type.RED.equals(color)) {
						red = result.get(color);
					} else if (Type.GREEN.equals(color)) {
						green = result.get(color);
					}
				}

				float fRed = ((float) red) / (red + green);
				float fGreen = ((float) green) / (red + green);

				img.setRGB(x + (WIDTH * 2) + 2, y, new Color(fRed, fGreen, 0).getRGB());
			}
		}
	}

	private static void saveToFile(String[] args, BufferedImage bi) throws IOException {
		String outPath = "./img/test.png";
		if (args.length > 0) {
			outPath = args[0];
		}
		ImageIO.write(bi, "png", new File(outPath));
	}
}
```