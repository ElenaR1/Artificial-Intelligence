import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class KNN {
	private Vector<String> v = new Vector<String>();
	private Vector<String> traindataVector = new Vector<String>();
	private Vector<String> vec1 = new Vector<String>();
	private Vector<String> vec2 = new Vector<String>();

	KNN() {		
	}
	public void help(Vector<String> v1,Vector<String> v2,int K) {
		TestData[] testingSet=helper2(v1);
		TrainData[] trainingSet=helper(v2);
		/*
		for (int i = 0; i < testingSet.length; i++) {
			testingSet[i].print();
		}
		*/
		/*
		for (int i = 0; i < trainingSet.length; i++) {
			trainingSet[i].print();
		}*/

		EuclideanDistance metric = new EuclideanDistance();
		//test the TestDatas one by one
		int numOfTestingRecord = testingSet.length;
		for(int i = 0; i < numOfTestingRecord; i ++){
			TrainData[] neighbors = findKNearestNeighbors(trainingSet, testingSet[i], K, metric);
			int classLabel = classify(neighbors);
			testingSet[i].predictedLabel = classLabel; //assign the predicted label to TestData
		}
		//calculate the accuracy
		int correctPrediction = 0;
		for(int j = 0; j < numOfTestingRecord; j ++){
			if(testingSet[j].predictedLabel == testingSet[j].classLabel)
				correctPrediction ++;
		}
		System.out.println("The accuracy is "+((double)correctPrediction / numOfTestingRecord)*100+"%");
		
		for(int j = 0; j < numOfTestingRecord; j ++){
			if(testingSet[j].predictedLabel == testingSet[j].classLabel)
				System.out.println("EQUAL "+testingSet[j].predictedLabel+ " = "+testingSet[j].classLabel);
			else
				System.out.println("NOT EQUAL "+testingSet[j].predictedLabel+ " != "+testingSet[j].classLabel);
		}
		
		
	}
	public static TrainData[] helper(Vector<String> v2) {
		int NumOfAttributes = 4;
		int NumOfSamples = v2.size();
		TrainData[] records = new TrainData[NumOfSamples];
		int index = 0;
		for (int j = 0; j < NumOfSamples; j++) {
			double[] attributes = new double[NumOfAttributes];
			int classLabel = -1;
			String[] arr = v2.elementAt(j).split(" ");  
			for(int i = 0; i < NumOfAttributes; i ++){
				  attributes[i]=Double.parseDouble(arr[i]);
			}
			classLabel = Integer.parseInt(arr[4]);
			assert classLabel != -1 : "Reading class label is wrong!";
			
			records[index] = new TrainData(attributes, classLabel);
			index ++;
		}
		
		return records;
		
	}
	
	public static TestData[] helper2(Vector<String> v2) {
		int NumOfAttributes = 4;
		int NumOfSamples = v2.size();
		TestData[] records = new TestData[NumOfSamples];
		int index = 0;
		for (int j = 0; j < NumOfSamples; j++) {
			double[] attributes = new double[NumOfAttributes];
			int classLabel = -1;
			String[] arr = v2.elementAt(j).split(" ");  
			for(int i = 0; i < NumOfAttributes; i ++){
				  attributes[i]=Double.parseDouble(arr[i]);
			}
			classLabel = Integer.parseInt(arr[4]);
			assert classLabel != -1 : "Reading class label is wrong!";
			
			records[index] = new TestData(attributes, classLabel);
			index ++;
		}
		
		return records;
		
	}
	

	
	static TrainData[] findKNearestNeighbors(TrainData[] trainingSet, TestData testRecord,int K, EuclideanDistance metric){
		int NumOfTrainingSet = trainingSet.length;
		assert K <= NumOfTrainingSet : "K is lager than the length of trainingSet!";
		
		TrainData[] neighbors = new TrainData[K];
		
		int index;
		for(index = 0; index < K; index++){
			trainingSet[index].distance = metric.getDistance(trainingSet[index], testRecord);
			neighbors[index] = trainingSet[index];
		}
		//System.out.println("-----------");
		for(index = K; index < NumOfTrainingSet; index ++){
			trainingSet[index].distance = metric.getDistance(trainingSet[index], testRecord);//the distance from trainingSet[index] to testRecord
			// index of the neighbor with the largest distance to testRecord
			int maxIndex = 0;
			for(int i = 1; i < K; i ++){
				if(neighbors[i].distance > neighbors[maxIndex].distance) {
					maxIndex = i;		
				}
			}
			
			//if trainingSet[index] has less distance than the neighbor with max distance form the testRecord add into neighbors 
			if(neighbors[maxIndex].distance > trainingSet[index].distance) {
				//System.out.println("neighbors["+maxIndex+"].distance: "+neighbors[maxIndex].distance+" trainingSet["+index+"].distance "+trainingSet[index].distance );
				neighbors[maxIndex] = trainingSet[index];
				//System.out.println("neighbors["+maxIndex+"].distance: "+neighbors[maxIndex].distance);
			}
		}
		
		return neighbors;
	}
	
			static int classify(TrainData[] neighbors){
				//construct a HashMap to store <classLabel, weight>
				HashMap<Integer, Double> map = new HashMap<Integer, Double>();
				int num = neighbors.length;
				
				for(int index = 0;index < num; index ++){
					TrainData temp = neighbors[index];
					int key = temp.classLabel;
					//if this classLabel does not exist we put it in the map
					if(!map.containsKey(key)) {
						map.put(key, 1 / temp.distance);
					}
					else{
						double value = map.get(key);
						value += 1 / temp.distance;
						map.put(key, value);
					}
				}
				double maxSimilarity = 0;
				int returnLabel = -1;
				Set<Integer> labelSet = map.keySet();
				Iterator<Integer> it = labelSet.iterator();
				
				//we search for the key with the highest value
				while(it.hasNext()){
					int label = it.next();
					double value = map.get(label);
					//System.out.println("label: "+label+" value "+value);
					if(value > maxSimilarity){
						maxSimilarity = value;
						returnLabel = label;
					}
				}
				
				return returnLabel;
			}
	public String generate(String data) {

		int totalLines = 0;
		File file = new File(data);

		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(file));

			while ((br.readLine()) != null) {
				totalLines++;
			}
			br.close();

			br = new BufferedReader(new FileReader(file));
			int n = 0;
			while (n < 20) {
				Random random = new Random();
				int randomInt = random.nextInt(totalLines);
				int count = 0;
				String icaocode;
				while ((icaocode = br.readLine()) != null) {
					if (count == randomInt) {
						br.close();
						v.add(icaocode);
						return icaocode;
					}
					count++;
				}
				n++;
			}
			br.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file.toString());
		} catch (IOException e) {
			System.out.println("Unable to read file: " + file.toString());
		}

		return "error";

	}

	public String generateTrainData(String data) {

		int totalLines = 0;
		File file = new File(data);

		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(file));

			while ((br.readLine()) != null) {
				totalLines++;
			}
			br.close();

			br = new BufferedReader(new FileReader(file));
			int n = 0;
			Random random = new Random();
			int randomInt = random.nextInt(totalLines);
			int count = 0;
			String icaocode;
			while ((icaocode = br.readLine()) != null) {
				if (!v.contains(icaocode)) {
					traindataVector.add(icaocode);
				}
				count++;
			}
			br.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file.toString());
		} catch (IOException e) {
			System.out.println("Unable to read file: " + file.toString());
		}

		return "error";

	}

	public void write(Vector<String> vec) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("data\\test_data.txt"));
		// Loop over the elements in the string array and write each line.
		for (String line : vec) {
			writer.write(line);
			writer.newLine();
		}
		writer.close();
	}

	public void writeTrainData(Vector<String> vec) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("data\\train_data.txt"));
		// Loop over the elements in the string array and write each line.
		for (String line : vec) {
			writer.write(line);
			writer.newLine();
		}
		writer.close();
	}

	public void printVector(Vector<String> v) {
		for (int i = 0; i < v.size(); i++) {
			System.out.println(v.elementAt(i));
		}
	}

	public String transform(String data) {

		int totalLines = 0;
		File file = new File(data);

		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(file));

			while ((br.readLine()) != null) {
				totalLines++;
			}
			br.close();

			br = new BufferedReader(new FileReader(file));
			int n = 0;
			while (n < 20) {
				Random random = new Random();
				int randomInt = random.nextInt(totalLines);
				int count = 0;
				String icaocode;
				while ((icaocode = br.readLine()) != null) {
					if (count == randomInt) {
						br.close();
						v.add(icaocode);
						return icaocode;
					}
					count++;
				}
				n++;
			}
			br.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file.toString());
		} catch (IOException e) {
			System.out.println("Unable to read file: " + file.toString());
		}

		return "error";

	}

	public void transform(Vector<String> vec) {
		for (int i = 0; i < vec.size(); i++) {
			String a = vec.elementAt(i).replaceAll(",", " ");
			// System.out.println(a)
			vec.setElementAt(a, i);
		}
	}

	public void transform2(Vector<String> vec) {
		for (int i = 0; i < vec.size(); i++) {
			String a = vec.elementAt(i);
			if (a.contains("Iris-setosa")) {
				String b = vec.elementAt(i).replaceAll("Iris-setosa", "1");
				vec.setElementAt(b, i);
			}
			if (a.contains("Iris-virginica")) {
				String b = vec.elementAt(i).replaceAll("Iris-virginica", "3");
				vec.setElementAt(b, i);
			}
			if (a.contains("Iris-versicolor")) {
				String b = vec.elementAt(i).replaceAll("Iris-versicolor", "2");
				vec.setElementAt(b, i);
			}
		}
	}
	public void putInVectors(String data,Vector<String> vec) {
		int totalLines = 0;
		File file = new File(data);

		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(file));

			while ((br.readLine()) != null) {
				totalLines++;
			}
			br.close();

			br = new BufferedReader(new FileReader(file));
			int n = 0;
				Random random = new Random();
				int randomInt = random.nextInt(totalLines);
				int count = 0;
				String icaocode;
				while ((icaocode = br.readLine()) != null) {
						vec.add(icaocode);
				}
			
			br.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file.toString());
		} catch (IOException e) {
			System.out.println("Unable to read file: " + file.toString());
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		KNN o = new KNN();
		for (int i = 0; i < 20; i++) {
			o.generate("data\\data.txt");
		}
		o.generateTrainData("data\\data.txt");

		o.transform(o.v);
		o.transform(o.traindataVector);
		o.transform2(o.v);
		o.transform2(o.traindataVector);
		

		o.write(o.v);
		o.writeTrainData(o.traindataVector);
		  
		 
		Vector<String> v1=new Vector<String>();
		Vector<String> v2=new Vector<String>();
		
		KNN ob=new KNN();
		ob.putInVectors("data\\test_data.txt", v1);
		ob.putInVectors("data\\train_data.txt", v2);
		//ob.printVector(v2);
		ob.help(v1, v2,1     );
	}
}