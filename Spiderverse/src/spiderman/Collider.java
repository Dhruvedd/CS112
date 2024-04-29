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
 * ColliderOutputFile name is passed in through the command line as args[2]
 * Output to ColliderOutputFile with the format:
 * 1. e lines, each with a different dimension number, then listing
 *       all of the dimension numbers connected to that dimension (space separated)
 * 
 * @author Seth Kelley
 */

public class Collider {

    public HashMap<Integer, ArrayList<Dimension>> table;
    public HashMap<Integer, ArrayList<Dimension>> gra;
    public ArrayList<Dimension> dimList = new ArrayList<>();

    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
                return;
        }

        Collider col = new Collider();
        col.DimensionsCreate(args[0]);
        String str = args[1];
        col.graph();
        col.printGraph(args[2]);
        
        


        // WRITE YOUR CODE HERE
        
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

            table.get(dime.getDimensionNum() % tableSize).add(0, dime);
            currDim++;

            if((double)((currDim)/(table.size())) >= threshold ){
                tableSize *= 2;
                rehash(tableSize);
            }
        }
        Connect();
    }

    public void rehash(int newTableSize) {

        HashMap<Integer, ArrayList<Dimension>> tempTable = new HashMap<>(newTableSize);

        for(ArrayList<Dimension> list : table.values()){

            for(Dimension dim : list){

                if(!tempTable.containsKey(dim.getDimensionNum() % newTableSize)){
                    tempTable.put(dim.getDimensionNum() % newTableSize, new ArrayList<>());
                }

                tempTable.get(dim.getDimensionNum() % newTableSize).add(0, dim);
            }
        }

        table.clear();
        table.putAll(tempTable);
    }

    public void Connect(){

        for(int i=0 ; i<table.size() ; i++){

            Dimension conn1;
            Dimension conn2;

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

    private boolean isThere(ArrayList<Dimension> grap, Dimension dime){  //helper method
        return grap.contains(dime);
    }

    public void graph() {
    
        // First pass to initialize the gra array
        for (ArrayList<Dimension> list : table.values()) {
            for (Dimension dim : list) {
                if (!isThere(dimList, dim)) {
                    dimList.add(dim);
                }
            }
        }

        int graphSize = dimList.size();

        gra = new HashMap<>(graphSize);

        for(Dimension dim : dimList){

                gra.put(dim.getDimensionNum(), new ArrayList<>());
                gra.get(dim.getDimensionNum()).add(0,dim);
        
        }

        for (ArrayList<Dimension> list : table.values()) {

            Dimension headDim = list.get(0);

            for (Dimension dim : list) {

                if(!gra.get(headDim.getDimensionNum()).contains(dim)) gra.get(headDim.getDimensionNum()).add(dim);
                if(!gra.get(dim.getDimensionNum()).contains(headDim)) gra.get(dim.getDimensionNum()).add(headDim);

            }
        }
    }
    


    public void printGraph(String file) {

        StdOut.setFile(file);

        for(ArrayList<Dimension> list : gra.values())
        {
            for(Dimension dim : list){
                StdOut.print(dim.getDimensionNum() + " ");
            }
            StdOut.println();
        }
    }





}