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
 * Read from the SpotInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * AnomaliesInputFile name is passed through the command line as args[3]
 * Read from the AnomaliesInputFile with the format:
 * 1. e (int): number of anomalies in the file
 * 2. e lines, each with:
 *      i.   The Name of the anomaly which will go from the hub dimension to their home dimension (String)
 *      ii.  The time allotted to return the anomaly home before a canon event is missed (int)
 * 
 * Step 5:
 * ReportOutputFile name is passed in through the command line as args[4]
 * Output to ReportOutputFile with the format:
 * 1. e Lines (one for each anomaly), listing on the same line:
 *      i.   The number of canon events at that anomalies home dimensionafter being returned
 *      ii.  Name of the anomaly being sent home
 *      iii. SUCCESS or FAILED in relation to whether that anomaly made it back in time
 *      iv.  The route the anomaly took to get home
 * 
 * @author Seth Kelley
 */

public class GoHomeMachine {

    public Clusters cl;
    public HashMap<String, Person> spiderverse;
    public int hub;
    public ArrayList<Anomaly> anomalies;
    ArrayList<Anomaly> anoms;
    public HashMap<Integer, ArrayList<Dimension>> table;
    public HashMap<Integer, ArrayList<Dimension>> gra;
    public ArrayList<Dimension> dimList = new ArrayList<>();

    
    public static void main(String[] args) {

        if ( args.length < 5 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.GoHomeMachine <dimension INput file> <spiderverse INput file> <hub INput file> <anomalies INput file> <report OUTput file>");
                return;
        }

        GoHomeMachine hm = new GoHomeMachine();
        String dims = args[0];
        hm.DimensionsCreate(dims);
        hm.graph();

        String spidey = args[1];
        String hub2 = args[2];
        String anom = args[3];
        String out = args[4];
        hm.defineHub(hub2);
        hm.createVerse(spidey);
        hm.collectAno();
        hm.goHome(anom);
        hm.Printr(out);

        //hm.DimensionsCreate("dimension.in");
        //hm.graph();
//
        //hm.defineHub("hub.in");
        //hm.createVerse("spiderverse.in");
        //hm.collectAno();
        //hm.goHome("anomalies.in");
        //hm.Printr("report.out");
        
        
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

    public void defineHub(String file){
        StdIn.setFile(file);
        hub = StdIn.readInt();
    }


    public void createVerse(String file){

        anomalies = new ArrayList<>();

        StdIn.setFile(file);

        int verseSize = StdIn.readInt();
        StdIn.readLine();

        spiderverse = new HashMap<>();

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

            spiderverse.put(toMark.Name, toMark);
        }
    }



    public String isSpidey(Anomaly ano){ // Helper


        for(Person person : spiderverse.values()){

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


    public ArrayList<Integer> shortestPath(HashMap<Integer, ArrayList<Dimension>> graph, int start, int end) {
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
            for (Dimension neighbor : graph.get(currentNode)) {
                if (!visited.containsKey(neighbor.getDimensionNum()) || !visited.get(neighbor.getDimensionNum())) {
                    visited.put(neighbor.getDimensionNum(), true);
                    ArrayList<Integer> newPath = new ArrayList<>(currentPath);
                    newPath.add(neighbor.getDimensionNum());
                    queue.add(newPath);
                    parent.put(neighbor.getDimensionNum(), currentNode); // Save parent of the neighbor
                }
            }
        }
    
        // If no path exists between start and end nodes
        return null;
    }


    public void goHome(String file){

        anoms = new ArrayList<>();

        StdIn.setFile(file);

        int anomNum = StdIn.readInt();
        StdIn.readLine();

        while(StdIn.hasNextLine()){
            Anomaly ano = new Anomaly();
            String anoName = StdIn.readString();
            ano.anomaly = spiderverse.get(anoName);
            StdIn.readChar();
            ano.allotedTime = StdIn.readInt();
            StdIn.readLine();
            ano.anomaly.Name = anoName;
            anoms.add(ano);
        }

        for(Anomaly ano : anoms){
            ano.anomaly.currDim = hub;
        }

        for(Anomaly ano : anoms){

            System.out.println(ano.anomaly.Name);

            Dimension huben = gra.get(hub).get(0);
            Dimension home = gra.get(ano.anomaly.belong).get(0);

            ArrayList<Dimension> shortPath = shortestPathD(huben, home);

            int totalWeight = 0;

            for(Dimension dim : shortPath){

                totalWeight += dim.getWeight();

            }

            if(totalWeight > ano.allotedTime){
                ano.suc = false;
                int canNum = gra.get(ano.anomaly.belong).get(0).getCanonNum();
                gra.get(ano.anomaly.belong).get(0).setCanonNum(canNum - 1);
            }

            ArrayList<Integer> shortPath1 = new ArrayList<>();

            for(Dimension dim : shortPath){
                shortPath1.add(dim.getDimensionNum());
            }

            ano.path = shortPath1;
        }
    }

    public void Printr(String file){

        StdOut.setFile("report.out");

        for(Anomaly ano : anoms){

            int canNum = gra.get(ano.anomaly.belong).get(0).getCanonNum();

            StdOut.print(canNum + " ");
            StdOut.print(ano.anomaly.Name + " ");
            if(ano.suc){
                StdOut.print("SUCCESS" + " ");
            }
            else{
                StdOut.print("FAILED" + " ");
            }

            for(int i : ano.path){
                if(i == ano.path.get(ano.path.size() - 1)){
                    StdOut.print(i);
                }
                else{
                    StdOut.print(i + " ");
                }
                
            }

            StdOut.println();
        }

    }

    public ArrayList<Dimension> shortestPathD(Dimension start, Dimension end) {
        // Set to keep track of vertices with known optimal paths from source
        Set<Dimension> doneSet = new HashSet<>();
    
        // Map to store the shortest distance from source to each vertex
        HashMap<Dimension, Integer> dist = new HashMap<>();
    
        // Priority queue (fringe) to store vertices with potential optimal paths
        PriorityQueue<Dimension> fringe = new PriorityQueue<>(Comparator.comparingInt(dist::get));
    
        // Map to store the previous vertex on the optimal path from source to each vertex
        HashMap<Dimension, Dimension> pred = new HashMap<>();
    
        // Initialize distances and predecessors
        for (Dimension dimension : dimList) {
            if (dimension.equals(start)) {
                dist.put(dimension, 0);
            } else {
                dist.put(dimension, Integer.MAX_VALUE);
            }
            pred.put(dimension, null);
        }
    
        // Add start vertex to fringe
        fringe.add(start);
    
        // Dijkstra's algorithm
        while (!fringe.isEmpty()) {
            // Extract vertex with minimum distance from fringe
            Dimension current = fringe.poll();
            doneSet.add(current);
    
            // Explore neighbors of current vertex
            for (Dimension neighbor : gra.get(current.getDimensionNum())) {
                if (!doneSet.contains(neighbor)) {
                    int newDist = dist.get(current) + getWeightBetween(current, neighbor);
                    if (newDist < dist.get(neighbor)) {
                        dist.put(neighbor, newDist);
                        pred.put(neighbor, current);
                        fringe.add(neighbor);
                    }
                }
            }
        }
    
        // Reconstruct the shortest path
        ArrayList<Dimension> shortestPath = new ArrayList<>();
        Dimension current = end;
        while (current != null) {
            shortestPath.add(current);
            current = pred.get(current);
        }
        Collections.reverse(shortestPath);
    
        return shortestPath;
    }
    
    
    // Helper method to get the weight between two dimensions
    private int getWeightBetween(Dimension dim1, Dimension dim2) {
        int weight = Integer.MAX_VALUE; // Initialize weight to maximum value
    
        // Check if dim1 and dim2 are connected in the graph
        if (gra.containsKey(dim1.getDimensionNum()) && gra.containsKey(dim2.getDimensionNum())) {
            ArrayList<Dimension> connections = gra.get(dim1.getDimensionNum());
            // Iterate over the connections of dim1 to find dim2
            for (Dimension connection : connections) {
                if (connection.equals(dim2)) {
                    weight = connection.getWeight(); // Update weight if connection found
                    break;
                }
            }
        }
        
        return weight;
    }












}
