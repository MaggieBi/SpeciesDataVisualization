import javax.swing.JLabel;

public class A4GUI extends ComparisonGUI {

	private static Species[] s;// an array of Species objects
	private static int[][] distance;// a 2D array of distances between species
	private JLabel explaination;// an explaination of our coloring method

	/** An instance for n species. n>0 */
	public A4GUI(int n) {
		super(n);

	}

	/**
	 * An instance for n species from an Species array and a species distance
	 * matrix
	 * 
	 * @param species
	 *            n
	 * @param species
	 *            , which is sorted alphabetically
	 * @param matrix
	 *            , a 2D array of species distances
	 */
	public A4GUI(int n, Species[] species, int[][] matrix) {
		// call super's constructor
		super(n);
		// set the fields
		s = species;
		distance = matrix;

		String name;

		// set all cells' images
		for (int x = 0; x < species.length; x++) {
			name = "SpeciesData/" + setName(s[x].getName());
			setCellImage(x, name);

		}
	}

	/**
	 * Add to field comparisonBox the stuff that goes into the right panel, i.e.
	 * the label and image for the selected species and the label and image for
	 * its closest species.
	 */
	@Override
	public void fixComparisonBox() {
		// explaination of our coloring method
		explaination = new JLabel(
				"<html>What the background color means: <br> The selected species's "
						+ "cell is set to maroon and <br> its closest species's cell is set to"
						+ " navy blue. The <br> closer the species is to the selected species, "
						+ "the <br> blacker the background color gets. The farther <br> the "
						+ "species is from the selected species, the <br> whiter the background "
						+ "color is.<html>");

		// add selected species info and image
		comparisonBox.add(selectedLabel);
		comparisonBox.add(selectedImage);

		// add the closest species info and image
		comparisonBox.add(closestRelatedLabel);
		comparisonBox.add(closestRelatedImage);

		// add the explaination
		comparisonBox.add(explaination);
	}

	/**
	 * Place the image for species number i and the image for its closest
	 * relative in the east panel. Change the background colors of the species
	 * to indicate distance from species number i. (Override this method to
	 * accomplish that task.)
	 */
	@Override
	public void onSelectCell(int i) {

		String name = s[i].getName();

		// set selected species's info and image in the east comparisonBox
		setSelectedInfo(name, "SpeciesData/" + setName(name));

		// set the closest species of the selected species's info and image in
		// the east comparisonBox
		String cname = s[closestSpecies(i)].getName();
		setClosestRelativeInfo(cname, "SpeciesData/" + setName(cname));

		run();

		// keep track of species selected, their closest species and their
		// distances
		JLabel list = new JLabel("<html>" + s[i].getName() + " | "
				+ s[closestSpecies(i)].getName() + "  "
				+ distance[i][closestSpecies(i)] + "<html>");
		comparisonBox.add(list);

		try {
			// set the cells' background color according to their distances to
			// the selected species
			setBackground(i);
		} catch (InvalidCellNumberException e) {
			e.printStackTrace();
		} catch (InvalidColorException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Set the format of a species's name into its file path Replace the spaces
	 * with underscores
	 * 
	 * @param name
	 * @return the image's file path: String
	 */
	private String setName(String name) {
		int sub = name.indexOf(" ");

		// recursively call this method and replace the spaces with underscores
		if (sub == -1) {
			return name + ".png";
		} else {
			String first = name.substring(0, sub);
			String second = name.substring(sub + 1);
			return first + "_" + setName(second);
		}

	}

	/**
	 * Set the background color of every cell according to its distance with
	 * species i. The selected species's cell is set to maroon and its closest
	 * species's cell is set to navy blue. The closer the species is to the
	 * selected species, except for the closest species which is special
	 * colored, the blacker the background color gets. The farther the species
	 * is from the selected species, the whiter the background color is.
	 * 
	 * @param i
	 * @throws InvalidCellNumberException
	 * @throws InvalidColorException
	 */
	private void setBackground(int i) throws InvalidCellNumberException,
			InvalidColorException {
		// set the background color of selected cell to maroon
		setCellColor(i, 0.31, 0, 0);
		// set the background color of the closest species's cell to navy
		int close = closestSpecies(i);
		setCellColor(close, 0, 0, 0.53);

		// the difference between the largest distance and smallest distance of
		// one species
		double a = distance[i][findMax(i)] - distance[i][findMin(i)];

		double b;
		double score;

		for (int x = 0; x < 40; x++) {
			if (x != i && x != close) {
				// the difference between the distance of species x and the
				// smallest distance
				b = distance[i][x] - distance[i][findMin(i)];
				score = b / a;
				setCellColor(x, score, score, score);
			}
		}

	}

	/**
	 * Find the index of species i's closest relative
	 * 
	 * @param i
	 * @return species i's closest relative's index
	 */
	private int closestSpecies(int i) {

		int small = 8000;
		int index = 0;

		// iterate through the array and find the smallest value's index
		for (int x = 0; x < distance.length; x++) {
			if (distance[x][i] < small && distance[x][i] != 0) {
				small = distance[x][i];
				index = x;
			}
		}
		return index;
	}

	/**
	 * Find the largest distance from species i to other species
	 * 
	 * @param i
	 * @return the index of the species that has the largest distance
	 */
	private int findMax(int i) {
		int greatest = 0;
		int index = 0;
		for (int x = 0; x < distance.length; x++) {
			if (distance[x][i] > greatest) {
				greatest = distance[x][i];
				index = x;
			}
		}
		return index;
	}

	/**
	 * Find the smallest distance from species i to other species
	 * 
	 * @param i
	 * @return the index of the species that has the smallest distance
	 */
	private int findMin(int i) {
		int small = 8000;
		int index = 0;
		for (int x = 0; x < distance.length; x++) {
			if (distance[x][i] < small && (x != i)) {
				small = distance[x][i];
				index = x;
			}
		}
		return index;
	}

}
