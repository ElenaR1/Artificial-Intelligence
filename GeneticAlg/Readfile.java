import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Readfile {
	private Scanner x;

	public void openFile() {
		try {
			FileReader file =new FileReader("C:\\Users\\Toshiba\\eclipse-workspace\\81296KnapSackGeneticAlgorithm\\data1.txt");
			BufferedReader  x = new BufferedReader(file);
			String text="";
			try {
   			 String line=x.readLine();
				while(line!=null) {
					/*text+=line;
					System.out.println(line);
					line=x.readLine();*/
					String[] parts = line.split(" ");
		            for (String part : parts) {
		                System.out.println(part);
		            }
		            System.out.println();
		            line=x.readLine();
				}
				x.close();
				//System.out.print(text);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readFile() {
		while (x.hasNext()) {
			String a = x.next();
			String b = x.next();
			System.out.println(a+" "+b);
		}
	}

	public void closeFile() {
		x.close();
	}

}
