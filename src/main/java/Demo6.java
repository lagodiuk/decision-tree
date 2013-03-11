import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.lagodiuk.decisiontree.DecisionTree;
import com.lagodiuk.decisiontree.Item;
import com.lagodiuk.decisiontree.Predicate;

public class Demo6 {

	public static enum Type {
		YELLOW, GREEN
	}

	public static void main(String[] args) {
		DecisionTree tree =
				DecisionTree
						.createBuilder()
						.setTrainingSet(trainingSet())
						.setDefaultPredicates(Predicate.GTE, Predicate.LTE)
						.createDecisionTree();

		display(tree.getSwingTree(), 300, 400);
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
