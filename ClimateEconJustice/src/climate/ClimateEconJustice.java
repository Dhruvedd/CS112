package climate;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered 
 * linked list structure that contains USA communitie's Climate and Economic information.
 * 
 * @author Navya Sharma
 */

public class ClimateEconJustice {

    private StateNode firstState;
    
    /*
    * Constructor
    * 
    * **** DO NOT EDIT *****
    */
    public ClimateEconJustice() {
        firstState = null;
    }

    /*
    * Get method to retrieve instance variable firstState
    * 
    * @return firstState
    * 
    * **** DO NOT EDIT *****
    */ 
    public StateNode getFirstState () {
        // DO NOT EDIT THIS CODE
        return firstState;
    }

    /**
     * Creates 3-layered linked structure consisting of state, county, 
     * and community objects by reading in CSV file provided.
     * 
     * @param inputFile, the file read from the Driver to be used for
     * @return void
     * 
     * **** DO NOT EDIT *****
     */
    public void createLinkedStructure ( String inputFile ) {
        
        // DO NOT EDIT THIS CODE
        StdIn.setFile(inputFile);
        StdIn.readLine();
        
        // Reads the file one line at a time
        while ( StdIn.hasNextLine() ) {
            // Reads a single line from input file
            String line = StdIn.readLine();
            // IMPLEMENT these methods
            addToStateLevel(line);
            addToCountyLevel(line);
            addToCommunityLevel(line);
        }
    }

    /*
    * Adds a state to the first level of the linked structure.
    * Do nothing if the state is already present in the structure.
    * 
    * @param inputLine a line from the input file
    */
    public void addToStateLevel ( String inputLine ) {


        String[] data = inputLine.split(",");
        String state = data[2];
        StateNode newStateNode = new StateNode();
        newStateNode.setName(state);

        StateNode stateNode = firstState;

        while(stateNode != null){

            if(stateNode.getName().equals(state)){
                return;
            }

            stateNode = stateNode.getNext();
        }

        if(firstState == null){
            firstState = newStateNode;
        }

        else{

            stateNode = firstState;

            while(stateNode.getNext() != null){

                stateNode = stateNode.getNext();
                

            }
            stateNode.setNext(newStateNode);
        }

    }

    /*
    * Adds a county to a state's list of counties.
    * 
    * Access the state's list of counties' using the down pointer from the State class.
    * Do nothing if the county is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCountyLevel ( String inputLine ) {

        StateNode MyState = new StateNode();

        String[] data = inputLine.split(",");
        String state = data[2];
        String county = data[1];

        for(StateNode ptr = firstState;ptr != null;ptr = ptr.next){

            if((ptr.name).equals(state)){
                MyState = ptr;
            }
        }


        CountyNode hehe = new CountyNode();
        hehe.name = county;

        if(MyState.down == null){
            MyState.down = hehe;
            return;
        }

        CountyNode ptr1 = MyState.down;

        while(ptr1 != null){

            if((ptr1.getName()).equals(county)){
                return;
            }

            ptr1 = ptr1.next;
        }

        ptr1 = MyState.down;

        while(ptr1.next != null){
            ptr1 = ptr1.next;
        }


        ptr1.next = hehe;
    }

    /*
    * Adds a community to a county's list of communities.
    * 
    * Access the county through its state
    *      - search for the state first, 
    *      - then search for the county.
    * Use the state name and the county name from the inputLine to search.
    * 
    * Access the state's list of counties using the down pointer from the StateNode class.
    * Access the county's list of communities using the down pointer from the CountyNode class.
    * Do nothing if the community is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCommunityLevel ( String inputLine ) {

        // WRITE YOUR CODE HERE

        StateNode meState = new StateNode();
        CountyNode meCounty = new CountyNode();

        String[] dat = inputLine.split(",");
        String state = dat[2];
        String county = dat[1];
        String Comm = dat[0];

        Data data = new Data();

        data.chanceOfFlood = Double.parseDouble(dat[37]);
        data.prcntPovertyLine = Double.parseDouble(dat[121]);
        data.prcntAfricanAmerican = Double.parseDouble(dat[3]);
        data.prcntNative = Double.parseDouble(dat[4]);
        data.prcntAsian = Double.parseDouble(dat[5]);
        data.prcntHispanic = Double.parseDouble(dat[9]);
        data.prcntWhite = Double.parseDouble(dat[8]);
        data.disadvantaged = dat[19];
        data.PMlevel = Double.parseDouble(dat[49]);


        for(StateNode ptr = firstState;ptr != null;ptr = ptr.next){

            if((ptr.name).equals(state)){
                meState = ptr;
            }
        }

        CountyNode firstCounty = meState.down;

        if(firstCounty == null){return;}

        for(CountyNode ptr1 = firstCounty; ptr1 != null;ptr1 = ptr1.next){
            if((ptr1.name).equals(county)){
                meCounty = ptr1;
            }
        }

        CommunityNode newComm = new CommunityNode();
        newComm.name = Comm;
        newComm.info = data;

        if(meCounty.down == null){
            meCounty.down = newComm;
            return;
        }

        for(CommunityNode ptr2 = meCounty.down; ptr2 != null; ptr2 = ptr2.next){
            if((ptr2.name).equals(Comm)){
                return;
            }
        }

        CommunityNode ptr3 = meCounty.down;

        while( ptr3.next != null){
            ptr3 = ptr3.next;
        }

        ptr3.next = newComm;

    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int disadvantagedCommunities ( double userPrcntage, String race ) {

        if((firstState == null) || (firstState.down == null)){
            return 0;
        }
        else if(firstState.down.down == null){
            return 0;
        }

        int lol = 0;


        for(StateNode ptr1 = firstState; ptr1 != null; ptr1 = ptr1.next){

            for(CountyNode ptr2 = ptr1.down; ptr2 != null; ptr2 = ptr2.next){

                for(CommunityNode ptr3 = ptr2.down; ptr3 != null; ptr3 = ptr3.next){

                    if(race.equals("African American")){
                        if((ptr3.info.prcntAfricanAmerican)*100 >= userPrcntage){
                            if(ptr3.info.getAdvantageStatus().equals("True")){
                                lol++;
                            }
                        }
                    }

                    else if(race.equals("Native American")){
                        if((ptr3.info.prcntNative)*100 >= userPrcntage){
                            if(ptr3.info.getAdvantageStatus().equals("True")){
                                lol++;
                            }
                        }
                    }

                    else if(race.equals("White American")){
                        if((ptr3.info.prcntWhite)*100 >= userPrcntage){
                            if(ptr3.info.getAdvantageStatus().equals("True")){
                                lol++;
                            }
                        }
                    }

                    else if(race.equals("Asian American")){
                        if((ptr3.info.prcntAsian)*100 >= userPrcntage){
                            if(ptr3.info.getAdvantageStatus().equals("True")){
                                lol++;
                            }
                        }
                    }

                    else if(race.equals("Hispanic American")){
                        if((ptr3.info.prcntWhite)*100 >= userPrcntage){
                            if(ptr3.info.getAdvantageStatus().equals("True")){
                                lol++;
                            }
                        }
                    }
                    
                }

            }

        }
        
        return lol; // replace this line
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as non disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int nonDisadvantagedCommunities ( double userPrcntage, String race ) {

        if((firstState == null) || (firstState.down == null)){
            return 0;
        }
        else if(firstState.down.down == null){
            return 0;
        }

        int gandu = 0;


        for(StateNode ptr1 = firstState; ptr1 != null; ptr1 = ptr1.next){

            for(CountyNode ptr2 = ptr1.down; ptr2 != null; ptr2 = ptr2.next){

                for(CommunityNode ptr3 = ptr2.down; ptr3 != null; ptr3 = ptr3.next){

                    if(race.equals("African American")){
                        if((ptr3.info.prcntAfricanAmerican)*100 >= userPrcntage){
                            if(ptr3.info.getAdvantageStatus().equals("False")){
                                gandu++;
                            }
                        }
                    }

                    else if(race.equals("Native American")){
                        if((ptr3.info.prcntNative)*100 >= userPrcntage){
                            if(ptr3.info.getAdvantageStatus().equals("False")){
                                gandu++;
                            }
                        }
                    }

                    else if(race.equals("White American")){
                        if((ptr3.info.prcntWhite)*100 >= userPrcntage){
                            if(ptr3.info.getAdvantageStatus().equals("False")){
                                gandu++;
                            }
                        }
                    }

                    else if(race.equals("Asian American")){
                        if((ptr3.info.prcntAsian)*100 >= userPrcntage){
                            if(ptr3.info.getAdvantageStatus().equals("False")){
                                gandu++;
                            }
                        }
                    }

                    else if(race.equals("Hispanic American")){
                        if((ptr3.info.prcntWhite)*100 >= userPrcntage){
                            if(ptr3.info.getAdvantageStatus().equals("False")){
                                gandu++;
                            }
                        }
                    }
                    
                }

            }

        }

        return gandu; // replace this line
    }
    
    /** 
     * Returns a list of states that have a PM (particulate matter) level
     * equal to or higher than value inputted by user.
     * 
     * @param PMlevel the level of particulate matter
     * @return the States which have or exceed that level
     */ 
    public ArrayList<StateNode> statesPMLevels ( double PMlevel ) {

        ArrayList<StateNode> rand = new ArrayList<>();

        if((firstState == null) || (firstState.down == null)){
            return null;
        }
        else if(firstState.down.down == null){
            return null;
        }


        for(StateNode ptr1 = firstState; ptr1 != null; ptr1 = ptr1.next){

            for(CountyNode ptr2 = ptr1.down; ptr2 != null; ptr2 = ptr2.next){

                for(CommunityNode ptr3 = ptr2.down; ptr3 != null; ptr3 = ptr3.next){

                    if((ptr3.info.PMlevel >= PMlevel) && (rand.contains(ptr1) == false)){
                        rand.add(ptr1);
                    }
                   
                    
                }

            }

        }


        return rand; // replace this line
    }

    /**
     * Given a percentage inputted by user, returns the number of communities 
     * that have a chance equal to or higher than said percentage of
     * experiencing a flood in the next 30 years.
     * 
     * @param userPercntage the percentage of interest/comparison
     * @return the amount of communities at risk of flooding
     */
    public int chanceOfFlood ( double userPercntage ) {

        
        if((firstState == null) || (firstState.down == null)){
            return 0;
        }
        else if(firstState.down.down == null){
            return 0;
        }

        int bsdn = 0;


        for(StateNode ptr1 = firstState; ptr1 != null; ptr1 = ptr1.next){

            for(CountyNode ptr2 = ptr1.down; ptr2 != null; ptr2 = ptr2.next){

                for(CommunityNode ptr3 = ptr2.down; ptr3 != null; ptr3 = ptr3.next){

                    if(ptr3.info.chanceOfFlood >= userPercntage){
                        bsdn++;
                    }
                   
                    
                }

            }

        }


        return bsdn; // replace this line
    }

    /** 
     * Given a state inputted by user, returns the communities with 
     * the 10 lowest incomes within said state.
     * 
     *  @param stateName the State to be analyzed
     *  @return the top 10 lowest income communities in the State, with no particular order
    */

    private CommunityNode ameer(ArrayList<CommunityNode> gareeba){

        CommunityNode dani = new CommunityNode();
        dani = gareeba.get(0);
        
        for(int i = 0; i < 10; i++){
            if(gareeba.get(i).info.prcntPovertyLine < dani.info.prcntPovertyLine){
                dani = gareeba.get(i);
                System.out.println(0);
            }
        }

        return dani;
    }



    public ArrayList<CommunityNode> lowestIncomeCommunities ( String stateName ) {

        
        StateNode meState = new StateNode();
        ArrayList<CommunityNode> gareeb = new ArrayList<>();

        for(StateNode ptr1 = firstState;ptr1 != null;ptr1 = ptr1.next){

            if((ptr1.name).equals(stateName)){
                meState = ptr1;
            }
        }

        for(CountyNode ptr2 = meState.down; ptr2 != null; ptr2 = ptr2.next){

            for(CommunityNode ptr3 = ptr2.down; ptr3 != null; ptr3 = ptr3.next){

                if(gareeb.size() < 10){
                    gareeb.add(ptr3);
                }

                else{

                    int j = 0;

                    for(int i = 0;i < 10;i++){
                        if(gareeb.get(i) == ameer(gareeb)){
                            j = i;
                            break;
                        }
                    }

                    if(gareeb.get(j).info.prcntPovertyLine < ptr3.info.prcntPovertyLine){
                        gareeb.set(j, ptr3);
                        System.out.println(2);
                    }



                }

            }

        }


        return gareeb; // replace this line
    }
}