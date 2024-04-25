package spiderman;

import java.util.ArrayList;
import java.util.HashMap;

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

    public HashMap<Integer, ArrayList<Integer>> table;

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

    public DimensionNode[] gra = new DimensionNode[65];

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

    private boolean isThere(DimensionNode[] grap, int dime){  //helper method

        for(int i = 0; i < grap.length;i++){
            if(grap[i] == null) return false;
            else if(grap[i].getData() == dime) return true;
        }
        return false;
    }

    private int locateIndex(DimensionNode[] grap,int dim){
        for(int i = 0; i < grap.length; i++){
            if(grap[i].getData() == dim) return i;
        }
        return -1;
    }

    public void graph(){

        int i = 0;

        for(ArrayList<Integer> list : table.values()){

            DimensionNode firstInLine = new DimensionNode(list.get(0));
           
            for(int dim : list){

                if(!isThere(gra, dim)){
                    DimensionNode newBit = new DimensionNode(dim);
                    
                    gra[i] = newBit;
                    gra[i].setNext(firstInLine);
                    i++;

                    if(dim != firstInLine.getData()){

                        DimensionNode ptr = gra[locateIndex(gra, firstInLine.getData())];

                        while(ptr.getNext() != null){
                            ptr = ptr.getNext();
                        }

                        ptr.setNext(newBit);

                        }
                }

                else if(dim == firstInLine.getData()){


                }
                
                else if(dim != firstInLine.getData()){

                    DimensionNode newNode = new DimensionNode(dim);

                    DimensionNode ptr = gra[locateIndex(gra, firstInLine.getData())];

                    while(ptr.getNext() != null){
                        ptr = ptr.getNext();
                    }

                    ptr.setNext(newNode);
                    
                }
            }
        }

    } 

    /*public void graph() {
        gra = new DimensionNode[table.size()]; // Initialize the gra array
    
        int index = 0; // Index for storing dimensions in the gra array
    
        for (ArrayList<Integer> list : table.values()) {
            // Get the first dimension in the cluster
            int firstDimension = list.get(0);
    
            // Create a new DimensionNode for the first dimension
            DimensionNode firstNode = new DimensionNode(firstDimension);
    
            // Add the firstNode to the adjacency list of the current dimension
            gra[index] = firstNode;
    
            // Create edges from every other dimension to the first dimension in the cluster
            for (int i = 1; i < list.size(); i++) {
                int dimension = list.get(i);
                DimensionNode newNode = new DimensionNode(dimension);
    
                // Add the newNode to the beginning of the adjacency list
                newNode.setNext(gra[index]);
                gra[index] = newNode;
            }
    
            // Create edges from the first dimension to every other dimension in the cluster
            for (int i = 1; i < list.size(); i++) {
                int dimension = list.get(i);
                DimensionNode newNode = new DimensionNode(dimension);
    
                // Add newNode to the beginning of the adjacency list of the first dimension
                newNode.setNext(firstNode.getNext());
                firstNode.setNext(newNode);
            }
    
            index++; // Move to the next index in the gra array
        }
    } */

    public void printGraph(String file) {

        StdOut.setFile(file);


        for (int i = 0; i < gra.length; i++) {
            DimensionNode currentNode = gra[i];
            if (currentNode != null) {
                StdOut.print(currentNode.getData() + ": ");
                DimensionNode nextNode = currentNode.getNext();
                while (nextNode != null) {
                    StdOut.print(nextNode.getData() + " ");
                    nextNode = nextNode.getNext();
                }
                StdOut.println();
            }
        }
    }





}