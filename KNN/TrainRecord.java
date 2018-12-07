public class TrainRecord extends Record {
	double distance;
	
	TrainRecord(double[] attributes, int classLabel) {
		super(attributes, classLabel);
	}
	public void print() {
		System.out.println("classLabel "+ this.classLabel);
		for (int i = 0; i < this.attributes.length; i++) {
			System.out.println(this.attributes[i]);
		}
	}
}