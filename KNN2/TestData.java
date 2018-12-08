public class TestData extends Data{
	int predictedLabel;
	
	TestData(double[] attributes, int classLabel) {
		super(attributes, classLabel);
	}
	public void print() {
		System.out.println("classLabel "+ this.classLabel);
		for (int i = 0; i < this.attributes.length; i++) {
			System.out.println(this.attributes[i]);
		}
	}
}