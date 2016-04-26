/*
 * Network Security Final Project: Anomaly Detection Algorithms
 * 
 * 
 */
import java.io.*;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Kmeans {
    private static int k;
    private static String filename;
    static ArrayList<Double> dataSet;
    private static ArrayList<Integer> timeStamps;
    private static ArrayList<Double> centroid;
    private ArrayList<Double> prevCentroid;
    private ArrayList<Double> temp;
    private ArrayList<ArrayList<Double>> groups;
    private ArrayList<ArrayList<Integer>> timeGroups;
   


    public Kmeans(int kVal, String fName) throws NumberFormatException, IOException {

    	
        k = kVal;
        filename = fName;
        dataSet = new ArrayList<>();
        centroid = new ArrayList<>();
        prevCentroid = new ArrayList<>();
        temp = new ArrayList<>();
        groups = new ArrayList<>();
        timeStamps=  new ArrayList<>();
        timeGroups= new ArrayList<>();

    	for (int i = 0; i < k; i++) {
            groups.add(new ArrayList<>());
            timeGroups.add( new ArrayList<>());
        }
    	
    	
    	readInData();
     
        int countIterations = 1;
        do {
            for (int i=0; i<dataSet.size();i++) {
                for (int j=0;j<centroid.size();j++) {
                    temp.add(abs(centroid.get(j) - dataSet.get(i)));
                }
                int minDifferenceIndex= temp.indexOf(Collections.min(temp));
                groups.get(minDifferenceIndex).add(dataSet.get(i));
                timeGroups.get(minDifferenceIndex).add(timeStamps.get(i));
                //timeGroups.get(temp.indexOf(Collections.min(temp))).add(timeStamps.get(i));
                temp.removeAll(temp);
            }
            for (int i = 0; i < k; i++) {
                if (countIterations == 1) {
                    prevCentroid.add(centroid.get(i));
                } else {
                    prevCentroid.set(i, centroid.get(i));
                }
                if (!groups.get(i).isEmpty()) {
                    centroid.set(i, average(groups.get(i)));
                }
            }
            if (!centroid.equals(prevCentroid)) {
                for (int i = 0; i < groups.size(); i++) {
                    groups.get(i).removeAll(groups.get(i));
                }
            }
            countIterations++;
        } while (!centroid.equals(prevCentroid));
        double highAvg=centroid.get(0);
        double lowAvg=centroid.get(0);
    
        for (int i = 0; i < centroid.size(); i++) {
            if (centroid.get(i)>highAvg)
            	highAvg=centroid.get(i);
            if (centroid.get(i)<lowAvg)
            	lowAvg=centroid.get(i);
        }
        int largeGroupIndex=0;
        int smallGroupIndex=0;
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).size()>largeGroupIndex)
            	largeGroupIndex=i;
            if (groups.get(i).size()<smallGroupIndex)
            	smallGroupIndex=i;
        }
        
        for (int i = 0; i < groups.size(); i++) {
            System.out.println("*****GROUP " + (i + 1)+"*****");
            System.out.println("Time Stamps:");
            System.out.println("\t"+timeGroups.get(i).toString());
            System.out.println("Values:");
            System.out.println("\t"+groups.get(i).toString());
         //   System.out.println("Time Stamps:");
           // System.out.println("\t"+timeGroups.get(i).toString());
            System.out.println("Mean Value (Centroid):\n\t"+centroid.get(i));  
            System.out.println("Size of Group:\n\t"+groups.get(i).size()+"\n");
        }
        
        System.out.println("\n*****General Information*****");       
        System.out.println("Size of Data Set: "+dataSet.size());
        int groupSizeDifference=(groups.get(largeGroupIndex).size())-(groups.get(smallGroupIndex).size());
        System.out.println("Group size difference = "+ groupSizeDifference);
        System.out.println("Group Average Difference: " + (highAvg-lowAvg)+"\n");
        System.out.println("\n\n\nNumber of Iterations: " + countIterations);
    }

    public static double average(ArrayList<Double> list) {
        double sum = 0;
         for (Double value : list) {
             sum = sum + value;
         }
         return sum / list.size();
     }
     public static void readInData() throws NumberFormatException, IOException{
     	FileReader inputData=new FileReader(filename);
         BufferedReader bf=new BufferedReader(inputData);
     	int count=0;
         String line;
          boolean readFirst=false;
            while ((line=bf.readLine())!= null){
            	if (!readFirst){
            		readFirst=true;
            	}
            	else{
            	
            		String dataRead[]=line.split(",");
            		
            		int time= Integer.parseInt(dataRead[0]);
  
            		double value=Double.parseDouble(dataRead[1]);
            		int isAnomoly=Integer.parseInt(dataRead[2]);
            		
            		timeStamps.add(time);
            		dataSet.add(value);
            		if (count < k) {
            			centroid.add(dataSet.get(count));
            		}
            		count++;
            	}
            }
     }
    
    
    public static void main(String[] args) throws NumberFormatException, IOException {
        Scanner kbd = new Scanner(System.in);
        String fname = null;
        System.out.println("Enter Value of K");
        int k = kbd.nextInt();
       	System.out.println("Enter Filename");
        fname = kbd.next();
        System.out.println("\n");
        Kmeans cluster = new Kmeans(k, fname);
       
    }
 

    
}
