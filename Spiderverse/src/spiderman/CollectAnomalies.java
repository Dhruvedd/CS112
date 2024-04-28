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

    public Clusters cl;
    public Person[] spiderverse;
    public int hub;
    public ArrayList<Anomaly> anomalies;
    public HashMap<Integer, ArrayList<Integer>> table;
    public HashMap<Integer, ArrayList<Integer>> gra;
    public ArrayList<Integer> dimList = new ArrayList<>();
    
    public static void main(String[] args) {

      //  if ( args.length < 4 ) {
      //      StdOut.println(
      //          "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
      //          return;
      //  }

        CollectAnomalies ca = new CollectAnomalies();
        String dims = args[0];
        ca.DimensionsCreate(dims);
        ca.graph();

        String spidey = args[1];
        ca.defineHub(args[2]);
        ca.createVerse(spidey);
        ca.collectAno();
        ca.printAno(args[3]);



        //String dims = "dimension.in";
        //ca.DimensionsCreate(dims);
        //ca.graph();
//
        //String spidey = "spiderverse.in";
        //ca.defineHub("hub.in");
        //ca.createVerse(spidey);
        //ca.collectAno();
        //ca.printAno("collected.out");
        
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

    private boolean isThere(ArrayList<Integer> grap, int dime){  //helper method
        return grap.contains(dime);
    }

    private int locateIndex(DimensionNode[] grap,int dim){
        for(int i = 0; i < grap.length; i++){
            if(grap[i].getData() == dim) return i;
        }
        return -1;
    }

    public void graph() {
    
        // First pass to initialize the gra array
        for (ArrayList<Integer> list : table.values()) {
            for (int dim : list) {
                if (!isThere(dimList, dim)) {
                    dimList.add(dim);
                }
            }
        }

        int graphSize = dimList.size();

        gra = new HashMap<>(graphSize);

        for(int dim : dimList){

                gra.put(dim, new ArrayList<>());
                gra.get(dim).add(0,dim);
        
        }

        for (ArrayList<Integer> list : table.values()) {

            int headDim = list.get(0);

            for (int dim : list) {

                if(!gra.get(headDim).contains(dim)) gra.get(headDim).add(dim);
                if(!gra.get(dim).contains(headDim)) gra.get(dim).add(headDim);

            }
        }
    }

    public void defineHub(String file){
        StdIn.setFile(file);
        hub = StdIn.readInt();
    }


    public void createVerse(String file){

        anomalies = new ArrayList<>();

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
            i++;
        }
    }



    public String isSpidey(Anomaly ano){ // Helper


        for(Person person : spiderverse){

            if(person == null) return null;

            if((person.spider == true)&&(ano.anomaly.currDim == person.currDim)){
                return person.Name;
            }
        }
        return null;
    }



    public void collectAno(){

        for(Anomaly ano : anomalies){

            ArrayList<Integer> toPath = new ArrayList<>();
            ArrayList<Integer> fullPath = new ArrayList<>();

            int targetDim = ano.anomaly.currDim;

            boolean isSpider = false;

            if(isSpidey(ano) != null){
                isSpider = true;
                ano.anomaly.Name = ano.anomaly.Name + " " + isSpidey(ano);
            }

            toPath = shortestPath(gra, hub, targetDim);

            for(int dim : toPath){
                fullPath.add(dim);
            }

            if(!isSpider){

                Collections.reverse(toPath);
                for(int dim :toPath){
                    if(dim != targetDim){
                        fullPath.add(dim);
                    }
                }

            }

            else{
                Collections.reverse(fullPath);
            }
            ano.path = fullPath;
        }
    }

    public void printAno(String file){

        StdOut.setFile(file);

        for(Anomaly ano : anomalies){

            StdOut.print(ano.anomaly.Name + " ");

            for(int pathDim : ano.path){

                StdOut.print(pathDim + " ");

            }

            StdOut.println();
        }
    }


    public ArrayList<Integer> shortestPath(HashMap<Integer, ArrayList<Integer>> graph, int start, int end) {
        // Visited map to keep track of visited nodes
        HashMap<Integer, Boolean> visited = new HashMap<>();
    
        // Queue for BFS traversal
        Queue<ArrayList<Integer>> queue = new LinkedList<>();
    
        // Parent map to keep track of the parent of each node in the shortest path
        HashMap<Integer, Integer> parent = new HashMap<>();
    
        // Add start node to the queue and mark it as visited
        ArrayList<Integer> path = new ArrayList<>();
        path.add(start);
        queue.add(path);
        visited.put(start, true);
    
        while (!queue.isEmpty()) {
            ArrayList<Integer> currentPath = queue.poll();
            int currentNode = currentPath.get(currentPath.size() - 1);
    
            // If we reach the end node, reconstruct and return the shortest path
            if (currentNode == end) {
                ArrayList<Integer> shortestPath = new ArrayList<>();
                int node = end;
                while (node != start) {
                    shortestPath.add(0, node);
                    node = parent.get(node);
                }
                shortestPath.add(0, start);
                return shortestPath;
            }
    
            // Explore neighbors of the current node
            for (int neighbor : graph.get(currentNode)) {
                if (!visited.containsKey(neighbor) || !visited.get(neighbor)) {
                    visited.put(neighbor, true);
                    ArrayList<Integer> newPath = new ArrayList<>(currentPath);
                    newPath.add(neighbor);
                    queue.add(newPath);
                    parent.put(neighbor, currentNode); // Save parent of the neighbor
                }
            }
        }
    
        // If no path exists between start and end nodes
        return null;
    }
    
    










}
