package spiderman;
import java.util.*;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * HubInputFile name is passed through the command line as args[2]
 * Read from the HubInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 *    is at the same Dimension (if one exists, space separated) followed by 
 *    the Dimension number for each Dimension in the route (space separated)
 * 
 * @author Seth Kelley
 */

public class CollectAnomalies {
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
                return;
        }

        CollectAnomalies ca = new CollectAnomalies();
        String dims = args[0];
        String spidey = args[1];
        ca.defineHub(args[2]);
        ca.createVerse(spidey);
        
    }

    public Person[] spiderverse;
    public int hub;
    public ArrayList<Anomaly> anomalies;

    public void defineHub(String file){
        StdIn.setFile(file);
        hub = StdIn.readInt();
    }


    public void createVerse(String file){

        StdIn.setFile(file);

        int verseSize = StdIn.readInt();
        StdIn.readLine();

        spiderverse = new Person[verseSize];

        int i = 0;

        while(StdIn.hasNextLine()){

            Person toMark = new Person();

            toMark.currDim = StdIn.readInt();
            StdIn.readChar();
            toMark.Name = StdIn.readString();
            StdIn.readChar();
            toMark.belong = StdIn.readInt();
            StdIn.readLine();

            if((toMark.currDim == toMark.belong) || (toMark.currDim == hub)) toMark.spider = true;
            else{
                toMark.spider = false;
                Anomaly newAno = new Anomaly();
                newAno.anomaly = toMark;
                anomalies.add(newAno);
            }

            spiderverse[i] = toMark;
        }
    }



    public String isSpidey(Anomaly ano){ // Helper


        for(Person person : spiderverse){

            if((person.spider == true)&&(ano.anomaly.currDim == person.currDim)){
                return person.Name;
            }
        }
        return null;
    }

    public void collectAno(){












    }










}
