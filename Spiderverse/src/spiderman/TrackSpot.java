package spiderman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.IntegerSyntax;

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
 * SpotInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * Two integers (line seperated)
 *      i.    Line one: The starting dimension of Spot (int)
 *      ii.   Line two: The dimension Spot wants to go to (int)
 * 
 * Step 4:
 * TrackSpotOutputFile name is passed in through the command line as args[3]
 * Output to TrackSpotOutputFile with the format:
 * 1. One line, listing the dimenstional number of each dimension Spot has visited (space separated)
 * 
 * @author Seth Kelley
 */

public class TrackSpot {
    
    public static void main(String[] args) {

       // if ( args.length < 4 ) {
       //     StdOut.println(
       //         "Execute: java -cp bin spiderman.TrackSpot <dimension INput file> <spiderverse INput file> <spot INput file> <trackspot OUTput file>");
       //         return;
       // }

        TrackSpot tr = new TrackSpot();
        
        tr.DimensionsCreate(args[0]);
        String rand = args[1];
        tr.readSpot(args[2]);
        tr.printPath(args[3]);
        
    }

    public HashMap<Integer, ArrayList<Integer>> table;
    public ArrayList<Integer> tracker = new ArrayList<>();
    public int start;
    public int end;

    public void readSpot(String path){

        StdIn.setFile(path);

        start = StdIn.readInt();
        StdIn.readLine();
        end = StdIn.readInt();
    }

    public void DimensionsCreate(String In){
 
 
        StdIn.setFile(In);
        // StdIn.setFile(inFile);
 
        int dimNum = StdIn.readInt();
 
        int tableSize = StdIn.readInt();
 
        double threshold = StdIn.readDouble();
        StdIn.readLine();

        table = new HashMap<>(tableSize);
 
        double currDim = 0; 
 
        for(int i = 0; i < dimNum; i++){

            Dimension dime = new Dimension();

            dime.setDimensionNum(StdIn.readInt());
            dime.setCanonNum(StdIn.readInt());
            dime.setWeight(StdIn.readInt());

            if(!table.containsKey(dime.DimensionNum % tableSize)){
                table.put(dime.getDimensionNum() % tableSize, new ArrayList<>());
            }

            table.get(dime.getDimensionNum() % tableSize).add(0, dime.getDimensionNum());
            currDim++;

            if((double)((currDim)/(table.size())) >= threshold ){
                tableSize *= 2;
                rehash(tableSize);
            }
        }
        Connect();
    }

    public void rehash(int newTableSize) {

        HashMap<Integer, ArrayList<Integer>> tempTable = new HashMap<>(newTableSize);

        for(ArrayList<Integer> list : table.values()){

            for(int dimNum : list){

                if(!tempTable.containsKey(dimNum % newTableSize)){
                    tempTable.put(dimNum % newTableSize, new ArrayList<>());
                }

                tempTable.get(dimNum % newTableSize).add(0, dimNum);
            }
        }

        table.clear();
        table.putAll(tempTable);
    }

    public void Connect(){

        for(int i=0 ; i<table.size() ; i++){

            int conn1 = 0;
            int conn2 = 0;

            if(i==0){

                conn1 = table.get(table.size()-1).get(0);
                conn2 = table.get(table.size()-2).get(0);
                
                table.get(i).add(conn1);
                table.get(i).add(conn2);
            }

            else if(i==1){

                conn1 = table.get(i-1).get(0);
                conn2 = table.get(table.size()-1).get(0);

                table.get(i).add(conn1);
                table.get(i).add(conn2);
            }

            else{

                conn1 = table.get(i-1).get(0);
                conn2 = table.get(i-2).get(0);

                table.get(i).add(conn1);
                table.get(i).add(conn2);
            }
        }
    }



    public ArrayList<Integer> listWithFirst(HashMap<Integer, ArrayList<Integer>> tab, int toFind){ //Helper for tracker

        for(ArrayList<Integer> list : table.values()){

            if(list.get(0) == toFind){
                return list;
            }
        
        }
        return null;
    }

    public void tracker(){

        tracker.add(start);
        boolean foundIt = false;

        ArrayList<Integer> currList = new ArrayList<>();


        for(ArrayList<Integer> list : table.values()){

            for(int dim : list){

                if(dim == start){

                    currList = list;

                }

            }
        }

        int i = 0;
        
        while(!foundIt){
            
            int dime = currList.get(i);
            
            if(dime == end){
                tracker.add(end);
                foundIt = true;
                return;
            }
            
            if(dime != start){
                tracker.add(dime);
            }

            if(currList.indexOf(dime) == (currList.size() - 1)){
                currList = listWithFirst(table, dime);
                i = 0;
            }
            
            i++;
        }

    }


    public void printPath(String file){

        StdOut.setFile(file);

        for(int dim : tracker){
            StdOut.print(dim + " ");
        }

    }


}
