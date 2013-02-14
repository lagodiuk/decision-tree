import java.awt.BorderLayout;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.lagodiuk.decisiontree.BasicPredicates;
import com.lagodiuk.decisiontree.DecisionTree;
import com.lagodiuk.decisiontree.Item;

public class Demo {

	public static void main(String[] args) {

		DecisionTree.Config config =
				new DecisionTree.Config()
						.setDefaultPredicates(Arrays.asList(BasicPredicates.EQUAL))
						.setTrainingSet(Arrays.asList(
								makeItem("Выше", "Дома", "На месте", "Да", "Loose"),
								makeItem("Выше", "Дома", "На месте", "Нет", "Win"),
								makeItem("Выше", "Дома", "Пропускают", "Нет", "Loose"),
								makeItem("Ниже", "Дома", "Пропускают", "Нет", "Win"),
								makeItem("Ниже", "В гостях", "Пропускают", "Нет", "Loose"),
								makeItem("Ниже", "Дома", "Пропускают", "Да", "Win"),
								makeItem("Выше", "В гостях", "На месте", "Да", "Loose")));

		DecisionTree classifier = DecisionTree.build(config);

		display(classifier.getTree(), 300, 300);

		System.out.println(classifier.classify(makeItem("Ниже", "Дома", "На месте", "Нет", null)));
	}

	private static Item makeItem(String sopernik, String igraem, String lideru, String dojd, String win) {
		Item item = new Item();

		item.setCategory(win);

		item.setAttribute("Соперник", sopernik);
		item.setAttribute("Играем", igraem);
		item.setAttribute("Лидеры", lideru);
		item.setAttribute("Дождь", dojd);

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
