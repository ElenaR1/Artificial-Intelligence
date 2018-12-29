
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BayesClassifier {
	private static List<List<String>> dataFromFile = new LinkedList<>();
	static List<List<String>>[] subsets = new LinkedList[10];

	public BayesClassifier() {	}
	public void readFromFile() throws NumberFormatException,IOException {
		File file = new File("votes1.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String s;
		while ((s = br.readLine()) != null) {
			String[] array = s.split(",");
			boolean flag = true;
			for (String str : array) {
				if (str.equals("?")) {
					flag = false;		
				}
			}
			if (flag == false)//we don't add the line if there is a question mark in it
			{
				continue;
			}
			List<String> politicians = new LinkedList<String>();//puts each line from the textfile in politicians

			for (String str : array) {				
				politicians.add(str);
				//System.out.print(str+" ");
				//System.out.print(politicians.get(politicians.size()-1)+" "); //same as above
			}
			//System.out.println();
			dataFromFile.add(politicians);//puts all lines in dataFromFile
			/*
			List<String> elementFromdataFromFile=dataFromFile.get(dataFromFile.size()-1);
			for (String s1:elementFromdataFromFile) {
				System.out.print(s1+" ");//prints the elements in dataFromFile
			}
			System.out.println();
			*/
		}

		br.close();
	}
	public static void randomDivision() {
		//System.out.println(dataFromFile.size()); =232
		List<Integer> indexes = IntStream.rangeClosed(0, 231).boxed().collect(Collectors.toCollection(LinkedList::new));
		// System.out.println(indexes.size());//232
		// System.out.println(indexes);

		 //10 mn-va po 23 elementa
		for (int i = 0; i < 10; i++) {

			subsets[i] = new LinkedList<>();

			for (int j = 0; j < 23; j++) {
				
				//v 9toto mn-vo sas vsichki ostanali index-i koito ne sa bili zaeti- 25 na broi
				if (i == 9) {
					for (int index : indexes) {
						subsets[i].add(dataFromFile.get(index));
					}
					break;
				}
				
				Random rand = new Random();
				int randomIndex = rand.nextInt(indexes.size());
				//System.out.println(randomIndex);
				
				int randomNumber = indexes.get(randomIndex);
				indexes.remove(randomIndex);
				subsets[i].add(dataFromFile.get(randomNumber));
				/*
				System.out.println("i="+i+" j="+j);
				List<String> el=dataFromFile.get(randomNumber);
				for (String s1:el) {
					System.out.print(s1+" ");//prints the elements in dataFromFile
				}
				System.out.println();*/
			}		
		}
		/*for (List<String> a : subsets[0]) {
			System.out.println(a);
		}
		
			List<List<String>> el=subsets[0];
			System.out.println(el.size());
			for (List<String> s1:el) {
				System.out.print(s1+" ");//prints the 23 elements in subsets[0]
			}
			System.out.println();
			
			List<List<String>> el1=subsets[9];
			System.out.println(el1.size());
			for (List<String> s1:el1) {
				System.out.print(s1+" ");//prints the 23 elements in subsets[0]
			}
			System.out.println();*/
	}

	public static double computeChanceFor(int xi, String value, String politician, int excluded) {
		List<List<String>> politicans;
		if (politician.equals("democrat")) {
			politicans = getAllDemocrats(excluded);//vrushta vsichki elementi na subsets kudeto imame demokrat osven demokratite v subsets(excluded) zshtoto tova e testingSet
		} else {
			politicans = getAllRepublicans(excluded);

		}
		double counter = 1;
		for (List<String> a : politicans) {
			if (a.get(xi).equals(value)) 
			{
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
			x = x * computeChanceFor(i, politician.get(i), "democrat", testingSetIndex);// politician.get(i)=vseki ot atributite bez democrat/republican; kakuv e shansa tozi element da e democrat ako ne znaem kakuv e
			y = y * computeChanceFor(i, politician.get(i), "republican", testingSetIndex);
		}
		if(testingSetIndex==0) {
			System.out.println("democratProb()= "+democratProb()+" x="+x);
			System.out.println("republicanProb()= "+republicanProb()+" y="+y);
		}

		String a;
		if ((x / (x + y)) > (y / (x + y))) {
			a = "democrat";
		} else {
			a = "republican";
		}
		if (politician.get(0).equals(a)) {
			if(testingSetIndex==0) {
			System.out.println("true");}
			return true;
		} else {
			if(testingSetIndex==0) {
			System.out.println("false");}
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
		/*List<Integer> ints=new LinkedList<Integer>();
		ints.add(1);
		ints.add(2);
		System.out.println(ints); //[1,2]*/
		long start = System.currentTimeMillis();
		BayesClassifier object = new BayesClassifier();
		object.readFromFile();
		randomDivision();

	
		double average = 0;
		for (int i = 0; i < 10; i++) {
			double counter = 0;
			for (List<String> a : subsets[i]) {
				if(i==0) {System.out.println(a);}
				if (algorithm(a, i)) {
					counter++;
				}
			}
			System.out.print("counter="+counter+"subsets["+i+"].size(): "+subsets[i].size()+"    ");
			average += counter / subsets[i].size();
			System.out.println(counter / subsets[i].size());

		}
		System.out.println("average is " + average / 10);
		long stop = System.currentTimeMillis();
		System.out.println("Found in " + ((double) (stop - start)) / 1000 + "s.");
	}
}
