public class EuclideanDistance{

	
	//L2
	public double getDistance(Record s, Record e) {
		assert s.attributes.length == e.attributes.length : "s and e are different types of records!";
		int numOfAttributes = s.attributes.length;
		double sum2 = 0;
		
		for(int i = 0; i < numOfAttributes; i ++){
			//System.out.println("sum2 "+sum2);
			sum2 += Math.pow(s.attributes[i] - e.attributes[i], 2);
			//System.out.println(s.attributes[i]+" "+e.attributes[i]+" "+(s.attributes[i] - e.attributes[i]));
			
		}
		
		return Math.sqrt(sum2);
	}

}