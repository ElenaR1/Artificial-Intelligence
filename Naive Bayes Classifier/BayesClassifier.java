
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BayesClassifier {
	private static List<List<String>> dataFromFile = new LinkedList<>();
	static List<List<String>>[] subsets = new LinkedList[10];

	public BayesClassifier() throws NumberFormatException, IOException {
		File file = new File("votes.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String s;
		while ((s = br.readLine()) != null) {
			String[] array = s.split(",");
			boolean flag = true;
			for (String str : array) {
				if (str.equals("?")) {
					flag = false;
					;
				}
			}
			if (flag == false) {
				continue;
			}
			List<String> politicians = new LinkedList<String>();//puts each line from the textfile in politicians

			for (String str : array) {				
				politicians.add(str);
				//System.out.print(str+" ");
			}
			//System.out.println();
			dataFromFile.add(politicians);//puts all lines in dataFromFile
		}

		br.close();
	}

	public static void divideDataSet() {
		List<Integer> indexes = IntStream.rangeClosed(0, 231).boxed().collect(Collectors.toCollection(LinkedList::new));
		// System.out.println(indexes.size());
		// System.out.println(indexes);

		for (int i = 0; i < 10; i++) {

			subsets[i] = new LinkedList<>();

			for (int j = 0; j < 23; j++) {

				if (i == 9) {
					for (int index : indexes) {
						subsets[i].add(dataFromFile.get(index));
					}
					break;
				}
				int randomIndex = ThreadLocalRandom.current().nextInt(0, indexes.size());
				
				int randomNumber = indexes.get(randomIndex);
				indexes.remove(randomIndex);
				subsets[i].add(dataFromFile.get(randomNumber));

			}

		}

	}

	public static double computeChanceFor(int xi, String value, String politician, int excluded) {
		List<List<String>> politicans;
		if (politician.equals("democrat")) {
			politicans = getAllDemocrats(excluded);
		} else {
			politicans = getAllRepublicans(excluded);

		}
		double counter = 1;
		for (List<String> a : politicans) {
			if (a.get(xi).equals(value)) {
				counter++;
			}
		}

		return counter / politicans.size();
	}

	public static boolean algorithm(List<String> politician, int testingSetIndex) {
		double chanceForDemocrats = democratProb();
		double x = 1 * chanceForDemocrats;
		double chanceForRepublicans = republicanProb();
		double y = 1 * chanceForRepublicans;
		for (int i = 1; i <= 16; i++) {
			x = x * computeChanceFor(i, politician.get(i), "democrat", testingSetIndex);
			y = y * computeChanceFor(i, politician.get(i), "republican", testingSetIndex);
		}

		// System.out.println(x / (x + y) + "--" + "democrat" + " exclude" +
		// testingSetIndex);
		// System.out.println(y / (x + y) + "--" + "republican" + " exclude" +
		// testingSetIndex);
		String a;
		if ((x / (x + y)) > (y / (x + y))) {
			a = "democrat";
		} else {
			a = "republican";
		}
		if (politician.get(0).equals(a)) {

			return true;
		} else {

			return false;
		}
	}

	public static List<List<String>> getAllDemocrats(int excludedIndex) {
		List<List<String>> democratsOnly = new LinkedList<>();
		for (int i = 0; i < 10; i++) {
			if (i == excludedIndex) {
				continue;
			}
			for (List<String> a : subsets[i]) {
				if (a.get(0).equals("democrat")) {
					democratsOnly.add(a);
				}
			}
		}
		return democratsOnly;

	}

	public static List<List<String>> getAllRepublicans(int excludedIndex) {
		List<List<String>> republicansOnly = new LinkedList<>();
		for (int i = 0; i < 10; i++) {
			if (i == excludedIndex) {
				continue;
			}
			for (List<String> a : subsets[i]) {
				if (a.get(0).equals("republican")) {
					republicansOnly.add(a);
				}
			}
		}
		return republicansOnly;

	}

	public static List<List<String>> getAllDemocrats() {
		List<List<String>> democratsOnly = new LinkedList<>();
		for (List<String> a : dataFromFile) {
			if (a.get(0).equals("democrat")) {
				democratsOnly.add(a);
			}
		}
		return democratsOnly;
	}

	public static List<List<String>> getAllRepublicans() {
		List<List<String>> republicansOnly = new LinkedList<>();
		for (List<String> a : dataFromFile) {
			if (a.get(0).equals("republican")) {
				republicansOnly.add(a);
			}
		}
		return republicansOnly;
	}

	public static double democratProb() {
		double counter = 0;
		for (List<String> a : dataFromFile) {
			if (a.contains("democrat")) {
				counter++;
			}
		}
		return counter / 232;
	}

	public static double republicanProb() {
		double counter = 0;
		for (List<String> a : dataFromFile) {
			if (a.contains("republican")) {
				counter++;
			}
		}
		return counter / 232;
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		long start = System.currentTimeMillis();
		BayesClassifier object = new BayesClassifier();
		divideDataSet();

	
		double average = 0;
		for (int i = 0; i < 10; i++) {
			double counter = 0;
			for (List<String> a : subsets[i]) {
				if (algorithm(a, i)) {
					counter++;
				}
			}
			average += counter / subsets[i].size();
			System.out.println(counter / subsets[i].size());

		}
		System.out.println("average is " + average / 10);
		long stop = System.currentTimeMillis();
		System.out.println("Found in " + ((double) (stop - start)) / 1000 + "s.");
	}
}
