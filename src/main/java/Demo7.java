import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import com.lagodiuk.decisiontree.DecisionTree;
import com.lagodiuk.decisiontree.DecisionTreeBuilder;
import com.lagodiuk.decisiontree.Item;
import com.lagodiuk.decisiontree.Predicate;
import com.lagodiuk.decisiontree.RandomForest;

public class Demo7 {

	private static final int TRAINING_SET_SIZE = 1000;

	private static final int WIDTH = 150;

	private static final int HEIGHT = 150;

	private static final int RADIUS = 40;

	private static Random random = new Random();

	private static enum Type {
		RED, GREEN
	}

	public static void main(String[] args) throws Exception {
		List<Item> trainingSet = trainingSet();

		DecisionTreeBuilder tbuilder =
				DecisionTree
						.createBuilder()
						.setTrainingSet(trainingSet)
						.setDefaultPredicates(Predicate.GTE, Predicate.LTE)
						.setMinimalNumberOfItems(1);

		DecisionTree tree = tbuilder.createDecisionTree();

		RandomForest forest = RandomForest.create(tbuilder, 7);

		BufferedImage bi = new BufferedImage((WIDTH * 3) + 2, HEIGHT, BufferedImage.TYPE_INT_RGB);
		displayTrainingSet(trainingSet, bi);
		displayDecisionTree(tree, bi);
		displayForest(forest, bi);
		ImageIO.write(bi, "png", new File("./img/test.png"));
	}

	private static void displayForest(RandomForest forest, BufferedImage bi) {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				Item item = item(transformX(x), transformY(y));

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

				bi.setRGB(x + (WIDTH * 2) + 2, y, new Color(((float) red) / (red + green), ((float) green) / (red + green), 0).getRGB());
			}
		}
	}

	private static void displayDecisionTree(DecisionTree tree, BufferedImage bi) {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				Item item = item(transformX(x), transformY(y));

				switch ((Type) tree.classify(item)) {
					case RED:
						bi.setRGB(x + WIDTH + 1, y, Color.RED.getRGB());
						break;
					case GREEN:
						bi.setRGB(x + WIDTH + 1, y, Color.GREEN.getRGB());
						break;
				}
			}
		}
	}

	private static void displayTrainingSet(List<Item> trainingSet, BufferedImage bi) {
		for (Item item : trainingSet) {
			int x = (Integer) item.getFieldValue("x");
			int y = (Integer) item.getFieldValue("y");

			x = antiTransformX(x);
			y = antiTransformY(y);

			switch (((Type) item.getCategory())) {
				case RED:
					bi.setRGB(x, y, Color.RED.getRGB());
					break;
				case GREEN:
					bi.setRGB(x, y, Color.GREEN.getRGB());
					break;
			}
		}
	}

	private static List<Item> trainingSet() {
		List<Item> items = new ArrayList<Item>(TRAINING_SET_SIZE);
		for (int i = 0; i < TRAINING_SET_SIZE; i++) {

			int x = random.nextInt(WIDTH);
			int y = random.nextInt(HEIGHT);

			x = transformX(x);
			y = transformX(y);

			Item item = item(x, y);

			if (isRed(x, y)) {
				item.setCategory(Type.RED);
			} else {
				item.setCategory(Type.GREEN);
			}

			items.add(item);
		}
		return items;
	}

	private static boolean isRed(int x, int y) {
		// return (random.nextDouble() < 0.9) && (((x * x) + (y * y)) <= (RADIUS
		// * RADIUS));
		return ((x * x) + (y * y)) <= (RADIUS * RADIUS);
	}

	private static int transformX(int x) {
		return x - (WIDTH / 2);
	}

	private static int transformY(int y) {
		return y - (HEIGHT / 2);
	}

	private static int antiTransformX(int x) {
		return x + (WIDTH / 2);
	}

	private static int antiTransformY(int y) {
		return y + (HEIGHT / 2);
	}

	private static Item item(int x, int y) {
		return new Item()
				.setAttribute("x", x)
				.setAttribute("y", y);
	}

}
