import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class KnapSack {
	private boolean mutation = false;
	private int numOfItems;
	private int crossoverCount;
	private int cloneCount;
	private int mutationCount = 0;
	private int sizeOfPopulation;
	private double knapsackCapacity;
	private double probabilityOfCrossover;
	private double probabilityOfMutation;
	private double totalGenerationFitness;
	private int maxGenerations;
	private int generationCounter;
	private ArrayList<Double> value = new ArrayList<Double>();
	private ArrayList<Double> weight = new ArrayList<Double>();
	private ArrayList<Double> fitness = new ArrayList<Double>();
	private ArrayList<Double> bestFitnesstOfGeneration = new ArrayList<Double>();
	private ArrayList<Double> meanFitnOfGeneretaion = new ArrayList<Double>();
	private ArrayList<String> population = new ArrayList<String>();
	private ArrayList<String> nextGeneration = new ArrayList<String>();
	private ArrayList<String> fittestChromosomeOfPopulation = new ArrayList<String>();
	private ArrayList<String> elements = new ArrayList<String>();
	private long s;
	private long e;

	public void readFromFile() {
		set();
		FileReader file;
		try {
			file = new FileReader("C:\\Users\\Toshiba\\eclipse-workspace\\81296KnapSackGeneticAlgorithm\\data1.txt");
			BufferedReader x = new BufferedReader(file);
			try {
				String line = x.readLine();
				while (line != null) {
					elements.add(line);
					line = x.readLine();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] el = elements.get(0).split(" ");
		numOfItems = Integer.parseInt(el[1]);
		knapsackCapacity = Double.parseDouble(el[0]);
		for (int i = 1; i < elements.size(); i++) {
			String element[] = elements.get(i).split(" ");
			Double w = Double.parseDouble(element[0]);
			Double val = Double.parseDouble(element[1]);
			value.add(val);
			weight.add(w);
		}
		System.out.println(knapsackCapacity);
		Scanner sc = new Scanner(System.in);

		System.out.println("size of population: ");
		sizeOfPopulation = sc.nextInt();

		System.out.println("crossover probability: ");
		probabilityOfCrossover = sc.nextDouble();

		System.out.println("mutation probability: ");
		probabilityOfMutation = sc.nextDouble();

		System.out.println("max generations: ");
		maxGenerations = sc.nextInt();
		this.genetics();
	}

	KnapSack() {
		//readFromFile();
		
		 set(); 
		// input(); 
		 
		 /*
		 numOfItems = 24;
		 
		 value.add((double) 150); weight.add((double) 90);
		 
		 value.add((double) 35); weight.add((double) 130);
		 
		 value.add((double) 200); weight.add((double) 1530);
		 
		 value.add((double) 160); weight.add((double) 500);
		 
		 value.add((double) 60); weight.add((double) 150);
		 
		 value.add((double) 45); weight.add((double) 680);
		 
		 value.add((double) 60); weight.add((double) 270);
		 
		 value.add((double) 40); weight.add((double) 390);
		 
		 value.add((double) 30); weight.add((double) 230);
		 
		 value.add((double) 10); weight.add((double) 520);
		 
		 value.add((double) 70); weight.add((double) 110);
		 
		 value.add((double) 30); weight.add((double) 320);
		 
		 value.add((double) 15); weight.add((double) 240);
		 
		 value.add((double) 10); weight.add((double) 480);
		 
		 value.add((double) 40); weight.add((double) 730);
		 
		 value.add((double) 70); weight.add((double) 420);
		 
		 value.add((double) 75); weight.add((double) 430);
		 
		 value.add((double) 80); weight.add((double) 220);
		 
		 value.add((double) 20); weight.add((double) 70);
		 
		 value.add((double) 12); weight.add((double) 180);
		 
		 value.add((double) 50); weight.add((double) 40);
		 
		 value.add((double) 10); weight.add((double) 300);
		 
		 value.add((double) 1); weight.add((double) 900);
		 
		 value.add((double) 150); weight.add((double) 2000);
		 
		 knapsackCapacity = 5000; sizeOfPopulation = 700; probabilityOfCrossover =
		 0.5; probabilityOfMutation = 0.2; maxGenerations = 30;
		*/

		
		 value.add((double) 6); weight.add((double) 2);
		 
		 value.add((double) 5); weight.add((double) 3);
		 
		 value.add((double) 8); weight.add((double) 6);
		 
		 value.add((double) 9); weight.add((double) 7);
		 
		 value.add((double) 6); weight.add((double) 5);
		 
		 value.add((double) 7); weight.add((double) 9);
		 
		 value.add((double) 3); weight.add((double) 4);
		 
		 numOfItems=7; knapsackCapacity = 9; sizeOfPopulation = 5;
		 probabilityOfCrossover = 0.5; probabilityOfMutation = 0.2; maxGenerations =
		 10;
		 
		 

		 this.genetics();
	}

	public void input() {
		Scanner sc = new Scanner(System.in);
		System.out.println("numOfItems: ");
		numOfItems = sc.nextInt();
		for (int i = 0; i < numOfItems; i++) {
			System.out.println("Enter the value of item " + (i + 1));
			value.add(sc.nextDouble());
			System.out.println("Enter the weigth of item " + (i + 1));
			weight.add(sc.nextDouble());
		}
		System.out.println("capacity: ");
		knapsackCapacity = sc.nextInt();

		System.out.println("size of population: ");
		sizeOfPopulation = sc.nextInt();

		System.out.println("crossover probability: ");
		probabilityOfCrossover = sc.nextDouble();

		System.out.println("mutation probability: ");
		probabilityOfMutation = sc.nextDouble();

		System.out.println("max generations: ");
		maxGenerations = sc.nextInt();
	}

	public void set() {
		crossoverCount = 0;
		cloneCount = 0;
		generationCounter = 1;
		totalGenerationFitness = 0;
	}

	public void genetics() {
		this.generatePopilation();
		System.out.println("\nInitial Generation:");
		System.out.println("----------------------------------");
		System.out.println("Population:");
		for (int i = 0; i < this.sizeOfPopulation; i++) {
			System.out.println((i + 1) + " : " + this.population.get(i));
		}

		this.evaluateFitnessOfPopulation();

		System.out.println("\nFitness:");
		for (int i = 0; i < this.sizeOfPopulation; i++) {
			System.out.println((i + 1) + " - " + this.fitness.get(i));
		}
		this.fittestChromosome();
		this.meanFitnOfGeneretaion.add(this.getMeanFitness());
		System.out.println("Mean fitness of initial generation: " + this.meanFitnOfGeneretaion.get(0));
		this.createMoreGenerations();

	}

	public void createMoreGenerations() {
		for (int j = 1; j < this.maxGenerations; j++) {

			if ((this.maxGenerations > 4) && (j > 4)) {

				double a = this.meanFitnOfGeneretaion.get(j - 1);
				double b = this.meanFitnOfGeneretaion.get(j - 2);
				double c = this.meanFitnOfGeneretaion.get(j - 3);

				if (a == b && b == c) {
					System.out.println("\nStop");
					maxGenerations = j;
					break;
				}
			}

			this.crossoverCount = 0;
			this.cloneCount = 0;
			this.mutationCount = 0;
			this.mutation = false;

			for (int i = 0; i < this.sizeOfPopulation / 2; i++) {
				if (sizeOfPopulation % 2 == 1) {
					//System.out.println(generationCounter);
					nextGeneration.add(fittestChromosomeOfPopulation.get(generationCounter - 1));
				}
				int indexOfParent1 = selectParents();
				int indexOfParent2 = selectParents();

				String parent1 = population.get(indexOfParent1);
				String parent2 = population.get(indexOfParent2);
				
				crossover(parent1, parent2);

			}

			this.fitness.clear();
			evaluateNextGenerationFitness();
			// change the population to the next generation
			for (int k = 0; k < this.sizeOfPopulation; k++) {
				this.population.set(k, this.nextGeneration.get(k));
			}

			System.out.println("\nGeneration " + (j + 1) + ":");
			System.out.println("--------------------");
			System.out.println("Population:");
			for (int l = 0; l < this.sizeOfPopulation; l++) {
				System.out.println("#" + (l + 1) + " - " + this.population.get(l));
			}
			System.out.println("\nFitness:");
			for (int m = 0; m < this.sizeOfPopulation; m++) {
				System.out.println((m + 1) + " - " + this.fitness.get(m));
			}

			this.nextGeneration.clear();

			// adds the fittest chromosome
			this.fittestChromosomeOfPopulation.add(population.get(this.getFittestChromosome()));
			generationCounter += 1;
			// prints it
			System.out.println("\nFittest chromosome of  generation: " + (j + 1) + ": "
					+ this.fittestChromosomeOfPopulation.get(j));

			// add the best fitness
			this.bestFitnesstOfGeneration
					.add(this.evaluateFitnessOfChromosome(this.population.get(this.getFittestChromosome())));
			// prints it
			System.out.println("Fitness of  best chromosome of generation: " + (j + 1) + ": "
					+ this.bestFitnesstOfGeneration.get(j));

			this.meanFitnOfGeneretaion.add(this.getMeanFitness());
			System.out.println("Mean fitness of initial generation: " + this.meanFitnOfGeneretaion.get(j));

			System.out.println("Crossover occurred " + this.crossoverCount + " times");
			System.out.println("Cloning occurred " + this.cloneCount + " times");
			if (this.mutation == false) {
				System.out.println("Mutation did not occur");
			}
			if (this.mutation == true) {
				System.out.println("Mutation occurred " + this.mutationCount + " times");
			}
		}
	}

	public void evaluateNextGenerationFitness() {
		totalGenerationFitness = 0;
		for (int i = 0; i < sizeOfPopulation; i++) {
			double fitnessOfChromosome = evaluateFitnessOfChromosome(nextGeneration.get(i));
			fitness.add(fitnessOfChromosome);
			totalGenerationFitness = totalGenerationFitness + fitnessOfChromosome;
		}
	}

	public int selectParents() {
		double randomNumber = Math.random() * totalGenerationFitness;
		System.out.println("randomNumber: "+randomNumber);
		for (int i = 0; i < sizeOfPopulation; i++) {
			double fitnessOfElem = fitness.get(i);
			if (fitnessOfElem >= randomNumber) {
				return i;
			} else {
				randomNumber = randomNumber - fitness.get(i);
			}
		}
		return 0;
	}

	public void crossover(String parent1, String parent2) {
		String child1;
		String child2;

		double pc = Math.random();
		Random ran = new Random();
		int crossoverPoint = ran.nextInt(numOfItems) + 1;
		System.out.println("parent1- "+parent1+" parent2- "+parent2+" crossover point "+crossoverPoint);
		if (pc <= probabilityOfCrossover) {
			crossoverCount = crossoverCount + 1;
			child1 = parent1.substring(0, crossoverPoint) + parent2.substring(crossoverPoint);
			child2 = parent2.substring(0, crossoverPoint) + parent1.substring(crossoverPoint);
			System.out.println("child1-"+child2+" child2- "+child2);
			nextGeneration.add(child1);
			nextGeneration.add(child2);
		} else {
			cloneCount = cloneCount + 1;
			nextGeneration.add(parent1);
			nextGeneration.add(parent2);
		}
		double mut = Math.random();
		if (mut <= probabilityOfMutation) {
			mutation = true;
			mutationCount += 1;
			BitFlipMutation();
		} // else
			// System.out.println("no mutation");
	}

	public void BitFlipMutation() {
		// e = System.currentTimeMillis();
		// System.out.println("Found in " + ((double)(e-s))/1000 + "s.");
		double pm = Math.random();
		Random ran = new Random();
		int geneToChange;
		String oldChromosome;
		String newMutatedChromosome;
		if (pm <= 0.5) {
			oldChromosome = nextGeneration.get(nextGeneration.size() - 1);
			geneToChange = ran.nextInt(numOfItems);
			System.out.println("genetochange: "+geneToChange);
			if (oldChromosome.charAt(geneToChange) == '1') {
				newMutatedChromosome = oldChromosome.substring(0, geneToChange) + "0"
						+ oldChromosome.substring(geneToChange+1);
				nextGeneration.set(nextGeneration.size() - 1, newMutatedChromosome);
				System.out.println("old chormosome :"+oldChromosome+" new chromosome "+newMutatedChromosome);
			}
			if (oldChromosome.charAt(geneToChange) == '0') {
				newMutatedChromosome = oldChromosome.substring(0, geneToChange) + "1"
						+ oldChromosome.substring(geneToChange+1);
				nextGeneration.set(nextGeneration.size() - 1, newMutatedChromosome);
				System.out.println("old chormosome :"+oldChromosome+" new chromosome "+newMutatedChromosome);
			}
		}
		if (pm >= 0.5) {
			oldChromosome = nextGeneration.get(nextGeneration.size() - 2);
			geneToChange = ran.nextInt(numOfItems);
			if (oldChromosome.charAt(geneToChange) == '1') {
				newMutatedChromosome = oldChromosome.substring(0, geneToChange) + "0"
						+ oldChromosome.substring(geneToChange+1);
				nextGeneration.set(nextGeneration.size() - 2, newMutatedChromosome);
				System.out.println("old chormosome :"+oldChromosome+" new chromosome "+newMutatedChromosome);
			}
			if (oldChromosome.charAt(geneToChange) == '0') {
				newMutatedChromosome = oldChromosome.substring(0, geneToChange) + "1"
						+ oldChromosome.substring(geneToChange+1);
				nextGeneration.set(nextGeneration.size() - 2, newMutatedChromosome);
				System.out.println("old chormosome :"+oldChromosome+" new chromosome "+newMutatedChromosome);
			}
		}
	}

	public void fittestChromosome() {
		// adds the fittest chromosome
		this.fittestChromosomeOfPopulation.add(population.get(this.getFittestChromosome()));
		// prints it
		System.out.println("\nFittest chromosome of initial generation: " + this.fittestChromosomeOfPopulation.get(0));

		// add the best fitness
		this.bestFitnesstOfGeneration
				.add(this.evaluateFitnessOfChromosome(this.population.get(this.getFittestChromosome())));
		// prints it
		System.out
				.println("Fitness of  best chromosome of initial generation: " + this.bestFitnesstOfGeneration.get(0));
	}

	public void generatePopilation() {
		for (int i = 0; i < this.sizeOfPopulation; i++) {
			population.add(createChromosome());
		}
	}

	// create a chromosome of genes(0s and 1s)
	public String createChromosome() {
		StringBuilder chromosome = new StringBuilder(numOfItems);

		char gene;

		for (int i = 0; i < this.numOfItems; i++) {

			double randomNum = Math.random();
			if (numOfItems < 100) {
				if (randomNum > 0.5) {
					gene = '1';
				} else {
					gene = '0';
				}			
			chromosome.append(gene);
			}
			else {
				if (randomNum > 0.99) {
					gene = '1';
				} else {
					gene = '0';
				}			
			chromosome.append(gene);
			}				
		}

		return chromosome.toString();
	}

	public void evaluateFitnessOfPopulation() {
		for (int i = 0; i < this.sizeOfPopulation; i++) {
			double fitnessOfChromosome = evaluateFitnessOfChromosome(population.get(i));
			// System.out.println("fitnessOfChromosome: "+fitnessOfChromosome);
			fitness.add(fitnessOfChromosome);
			totalGenerationFitness = totalGenerationFitness + fitnessOfChromosome;
		}
	}

	public double evaluateFitnessOfChromosome(String chromosome) {
		double valueOfFtitness = 0;
		double totalWeight = 0;
		double totalValue = 0;
		char gene;

		for (int j = 0; j < this.numOfItems; j++) {
			gene = chromosome.charAt(j);
			if (gene == '1') {
				totalValue = totalValue + value.get(j);
				totalWeight = totalWeight + weight.get(j);
				//System.out.println(numOfItems);
				// System.out.println("totalWeight: "+totalWeight+" "+(j+1)+"...");
			}
		}
		double difference;
		difference = knapsackCapacity - totalWeight;
		if (difference >= 0) {
			valueOfFtitness = totalValue;
		}
		return valueOfFtitness;
	}

	public double getMeanFitness() {
		double total_fitness = 0;
		double mean_fitness = 0;
		for (int i = 0; i < sizeOfPopulation; i++) {
			total_fitness = total_fitness + fitness.get(i);
		}
		mean_fitness = total_fitness / sizeOfPopulation;
		return mean_fitness;
	}

	public int getFittestChromosome() {
		double fittest = 0;
		int indexOfFittestChromosome = 0;
		double valueOfFtitness = 0;
		for (int i = 0; i < this.sizeOfPopulation; i++) {
			double fitnessOfChromosome = evaluateFitnessOfChromosome(population.get(i));
			if (fitnessOfChromosome > fittest) {
				fittest = fitnessOfChromosome;
				indexOfFittestChromosome = i;
			}
		}
		return indexOfFittestChromosome;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList<String> a = new ArrayList<String>();
		a.add("1000001");
		a.add("1000001");
		a.add("1100001");
		a.add("0000001");
		a.add("0000001");
		String newMutatedChromosome = "0000001".substring(0, 3) + "1"
				+ "0000001".substring(3+1);
		a.set(a.size() - 1, newMutatedChromosome);
		System.out.println(a.get(4));
		
		long start = System.currentTimeMillis();
		KnapSack obj = new KnapSack();
		long stop = System.currentTimeMillis();
		System.out.println("Found in " + ((double) (stop - start)) / 1000 + "s.");

		// Readfile r=new Readfile();
		// r.openFile();

	}

}
