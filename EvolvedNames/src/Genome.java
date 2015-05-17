import java.util.List;
import java.util.Random;

public class Genome {

	private StringBuilder word;
	private double mutationRate;
	private List<Character> charList;
	Random random = new Random();

	private char[] phenotypes = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
			'W', 'X', 'Y', 'Z', '_', '-', '\'', ' ' };

	public Genome(double mutationRate) {

		word = new StringBuilder("A");
		this.mutationRate = mutationRate;
	}

	public Genome(Genome gene) {
		word = new StringBuilder();
		word.append(gene.getWord());
		mutationRate = gene.getMutationRate();
	}

	public void mutate() {

		// Randomly add char to random position
		if (random.nextDouble() < mutationRate) {
			int randomSpot = random.nextInt(word.length() + 1);
			int randomChar = random.nextInt(phenotypes.length);
			word.insert(randomSpot, phenotypes[randomChar]);
		}

		// Randomly delete single char, if length > 1
		if (random.nextDouble() < mutationRate) {
			if (word.length() > 1) {
				word.deleteCharAt(random.nextInt(word.length()));

			}
		}

		// Randomly replace a char
		for (int i = 0; i < word.length(); i++) {
			if (random.nextDouble() < mutationRate) {
				int randomChar = random.nextInt(phenotypes.length);
				word.setCharAt(i, phenotypes[randomChar]);

			}
		}

	}

	public void crossover(Genome other) {

		StringBuilder child = new StringBuilder();

		int childLength = Math.min(word.length(), other.getWord().length());

		for (int i = 0; i < childLength; i++) {
			if (random.nextBoolean()) {
				child.append(word.charAt(i));
			} else {
				child.append(other.getWord().charAt(i));
			}
		}

	}

	/**
	 * Determines the fitness of string, according to Wagner-Fischer algorithm.
	 * 
	 * @return Integer of fitness for current string.
	 */
	public Integer fitness() {

		// Length of current string
		int n = word.length();

		// Length of target string
		int m = Population.target.length();

		// Initialize 2D array of (testString + 1) x (targetString + 1)
		int fit2D[][] = new int[n + 1][m + 1];

		// Create column indices
		for (int i = 1; i <= m; i++) {
			fit2D[0][i] = i;
		}

		// Create row indices
		for (int i = 1; i <= n; i++) {
			fit2D[i][0] = i;
		}

		// Wagner-Fischer algorithm for calculating Levenshtein edit
		distance: for (int row = 1; row <= n; row++) {
			for (int col = 1; col <= m; col++) {

				if (row > m) {
					continue;
				}

				if (word.charAt(row - 1) == Population.target.charAt(row - 1)) {
					fit2D[row][col] = fit2D[row - 1][col - 1];
				} else {
					fit2D[row][col] = Math.min(fit2D[row - 1][col] + 1,
							fit2D[row][col - 1] + 1);
					fit2D[row][col] = Math.min(fit2D[row][col],
							fit2D[row - 1][col - 1] + 1);
				}
			}
		}
		return fit2D[n][m] + (Math.abs(n - m) + 1) / 2;

		// Regular algorithm:
		// int n = word.length();
		// int m = Population.target.length();
		// int l = Math.max(n, m);
		// int f = Math.abs(m - n);
		// for (int i = 1; i <= l; i++) {
		//
		// if (i <= m) {
		//
		// if (word.charAt(i) != Population.target.charAt(i)) {
		// f++;
		// }
		// }
		// }
		//
		// return f;

	}

	public String getWord() {
		return word.toString();
	}

	public double getMutationRate() {
		return mutationRate;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(\"" + word + "\", " + fitness() + ")");
		return sb.toString();

	}

}
