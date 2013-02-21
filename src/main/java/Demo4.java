import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.lagodiuk.decisiontree.Predicate;
import com.lagodiuk.decisiontree.DecisionTree;
import com.lagodiuk.decisiontree.Item;

public class Demo4 {

	public static void main(String[] args) {

		DecisionTree dt =
				DecisionTree
						.createBuilder()
						.setDefaultPredicates(Predicate.EQUAL, Predicate.GTE, Predicate.LTE)
						.setAttributePredicates("Outlook", Predicate.EQUAL)
						.setAttributePredicates("Windy", Predicate.EQUAL)
						.setTrainingSet(makeTrainingSet())
						.createDecisionTree();

		display(dt.getSwingTree(), 400, 500);
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
