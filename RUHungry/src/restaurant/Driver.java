package restaurant;
/**
 * Use this class to test your Menu method. 
 * This class takes in two arguments:
 * - args[0] is the menu input file
 * - args[1] is the output file
 * 
 * This class:
 * - Reads the input and output file names from args
 * - Instantiates a new RUHungry object
 * - Calls the menu() method 
 * - Sets standard output to the output and prints the restaurant
 *   to that file
 * 
 * To run: java -cp bin restaurant.Menu menu.in menu.out
 * 
 */
public class Driver {
    public static void main(String[] args) {

	// 1. Read input files
	// Option to hardcode these values if you don't want to use the command line arguments
	   
        String inputFile = "Menu.in";

        String inputFile2 = "stock.in";
 
	
        // 2. Instantiate an RUHungry object
        RUHungry rh = new RUHungry();

	// 3. Call the menu() method to read the menu
        rh.menu(inputFile);//Testing the first menu method

        rh.createStockHashTable(inputFile2);// testing createStockHashTable and addStockNode methods

        rh.updateStock(null, 139, 90);//testing Update Stock

        rh.updatePriceAndProfit();//testing that method



        StdIn.setFile("order1.in");
        int amount = StdIn.readInt();
        StdIn.readLine();

        while(StdIn.hasNextLine()){
                
                int quant = StdIn.readInt();
                StdIn.readChar();
                String orderDish = StdIn.readLine();

                rh.order(orderDish, quant);
        } // Testing the order method




	// 4. Set output file
	// Option to remove this line if you want to print directly to the screen
        //StdOut.setFile(outputFile);

	// 5. Print restaurant
        rh.printRestaurant();

        System.out.println(rh.findStockNode(139).getIngredient().getName());//testing FindStockNode with ID
    }
}
