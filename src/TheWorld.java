import java.awt.*;// Basic java graphics library
import java.awt.event.*;// Basic java event handling library
import javax.swing.*;// The Swing library
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class TheWorld {
   
	private WorldObject [][] WorldArray;		//2D Array of the objects
	private ArrayList<WorldObject> AutonomousList = new ArrayList<WorldObject>();	
	//will use this to iterate through the autonomous objects (avoiding 2 moves per step error that can occur otherwise)
	private int displayCount = 0;				//Lets us know if the window's already been initialized
	private JLabel[][] LabelArray;				//2D array of JLabels, corresponding to the array of objects
	
	public TheWorld(int rows, int columns){		//Constructor method, where we set the size of World
		this.WorldArray = new WorldObject[rows][columns];
		this.LabelArray = new JLabel[rows][columns];		//Plugging the size into the 2D arrays
	}
	
	public static int getRandom(int[] array) { 		//Randomizes an array
	    int rnd = new Random().nextInt(array.length);
	    return array[rnd];
	}
	
	public int[] VerifyBorders(int i, int j){		//This method indicates to us which directions the object can navigate
		if(i == 0 && j == (this.WorldArray[i-1].length - 1)){    
			//if we're at the top of the world and at the most right side, the object can only use direction 5,6, or 7
			int values[] = {5,6,7};
			return values;
		} 
		else if(i == 0 && j == 0){    
			//if we're at the top of the world and at the most left side, the object can only use direction 3,4, or 5
			int values[] = {3,4,5};
			return values;
		}
		else if(i == (this.WorldArray.length - 1) && j == (this.WorldArray[i-1].length - 1)){    
			//if we're at the bottom of the world and at the most right side, the object can only use direction 1,7, or 8
			int values[] = {1,7,8};
			return values;
		}
			
		else if(i == (this.WorldArray.length - 1) && j == 0){    
			//if we're at the bottom of the world and at the most left side, the object can only use direction 1,2, or 3
			int values[] = {1,2,3};
			return values;
		}
		else if(j == 0){    
			//if we're at the most left side, the object can only use direction 1,2,3,4,5
			int values[] = {1,2,3,4,5};
			return values;
		
	}
		else if(j == (this.WorldArray[i-1].length - 1)){    
			//if we're at the most right side, the object can only use direction 1,5,6,7,8
			int values[] = {1,5,6,7,8};
			return values;
		
	}
		else if(i == 0){    
			//if we're at the top of the world, the object can only use direction 3,4,5,6,7
			int values[] = {3,4,5,6,7};
			return values;
		
	}
		else if(i == (this.WorldArray.length - 1)){    
			//if we're at the bottom of the world, the object can only use direction 1,2,3,7,8
			int values[] = {1,2,3,7,8};
			return values;
		
	}else {
		//if none of the others are True, it means the object is not in one of the borders, so it's safe to go in all directions
		int values[] = {1,2,3,4,5,6,7,8};
		
		return values;
	}
		}

	public void add(WorldObject item, int x, int y){
		
		if(this.WorldArray[x][y] == null){						//If the position is empty, we can add the object
			item.setX(x);						
			item.setY(y);										//Saving the coordinates of the object into itself
			this.WorldArray[x][y] = item;						//Placing it in the specified position within the world/array
			
			if(item instanceof Autonomous){						//If the item's autonomous
				AutonomousList.add(item);						//We place it in the list of autonomous items as well
			}
			
		}
		else{													//If the specified position is already filled with an object
			System.out.println("Add failed");					//Don't add it, and print a notice
		}
	}
	
	public void step(){											//Method moving the objects (most work done in helper method)
		int totalAutonomous = AutonomousList.size();			//Used for counter
		for(int i = 0; i < totalAutonomous; i++){				//Iterate through all of the autonomous objects to tell them to move
			
			WorldObject currentObject = AutonomousList.get(i);	//Take an autonomous object out of the list
			int x = currentObject.getX();
			int y = currentObject.getY();						//Going to need its current coordinates and desired direction 
			int direction = currentObject.step();				//To move properly
			moveIn(x, y, direction);
		}
	}
	
	private boolean moveIn(int i, int j, int direction){		//Recursive helper method that checks if a move's legal, and makes it, if it is
		boolean canDo = false;
		if (direction == 1){									//If we want to go up
			if(!(IntStream.of(VerifyBorders(i,j)).anyMatch(x -> x == 1))){ 	//If the direction is not authorized, try a new direction among the authorized directions
				int newDirection = getRandom(VerifyBorders(i,j));			//Generates an authorized direction
				moveIn(i,j,newDirection);									//Goes in that direction
			}													
			else if(this.WorldArray[i-1][j] == null){			//If the direction is authorized, proceed
				this.WorldArray[i-1][j] = this.WorldArray[i][j];//We can move, so move the object up one cell
				this.WorldArray[i][j] = null;					//And set it's old cell to null
				this.WorldArray[i-1][j].setX(i-1);				
				this.WorldArray[i-1][j].setY(j);				//Making sure to update the object's coordinates itself
				canDo = true;
			}
			else if(moveIn(i-1, j, direction)){					//If there's a moveable or autonomous above us, we use recursion
				//This is because we need to check if the object above us is hindered from moving, which means we shouldn't either
				//So we only move if the recursion tells us the above object is unhindered as well
				this.WorldArray[i-1][j] = this.WorldArray[i][j];
				this.WorldArray[i][j] = null;					//Move the object up, and free up it's old position
				this.WorldArray[i-1][j].setX(i-1);
				this.WorldArray[i-1][j].setY(j);				//Update it's new coordinates
				canDo = true;
			}
		}
		//This same if loop and its logic applies to all 7 other directions ()
		else if(direction == 2){								//For moving up-right
			if(!(IntStream.of(VerifyBorders(i,j)).anyMatch(x -> x == 2))){
			//need to check if we're at the top of the world, or at the far-right end
			//As it shouldn't move under any of those conditions
				int newDirection = getRandom(VerifyBorders(i,j));
				moveIn(i,j,newDirection);		
			}
			//Rest of this is the same as the first direction, (save for the actual position we're moving to)
			else if(this.WorldArray[i-1][j+1] == null){
				this.WorldArray[i-1][j+1] = this.WorldArray[i][j];
				this.WorldArray[i][j] = null;
				this.WorldArray[i-1][j+1].setX(i-1);
				this.WorldArray[i-1][j+1].setY(j+1);
				canDo = true;
			}
			else if(moveIn(i-1, j+1, direction)){
				this.WorldArray[i-1][j+1] = this.WorldArray[i][j];
				this.WorldArray[i][j] = null;
				this.WorldArray[i-1][j+1].setX(i-1);
				this.WorldArray[i-1][j+1].setY(j+1);
				canDo = true;
			}
		}
		
		else if(direction == 3){
			if(!(IntStream.of(VerifyBorders(i,j)).anyMatch(x -> x == 3))){
				int newDirection = getRandom(VerifyBorders(i,j));
				moveIn(i,j,newDirection);		
				
			}
			//Rest of this is the same as the first direction, (save for the actual position we're moving to)
			else if(this.WorldArray[i][j+1] == null){
				this.WorldArray[i][j+1] = this.WorldArray[i][j];
				this.WorldArray[i][j] = null;
				this.WorldArray[i][j+1].setX(i);
				this.WorldArray[i][j+1].setY(j+1);
				canDo = true;
			}
			else if(moveIn(i, j+1, direction)){
				this.WorldArray[i][j+1] = this.WorldArray[i][j];
				this.WorldArray[i][j] = null;
				this.WorldArray[i][j+1].setX(i);
				this.WorldArray[i][j+1].setY(j+1);
				canDo = true;
			}
		}
		
		else if(direction == 4){
			if(!(IntStream.of(VerifyBorders(i,j)).anyMatch(x -> x == 4))){
				int newDirection = getRandom(VerifyBorders(i,j));
				moveIn(i,j,newDirection);		
			}
			//Rest of this is the same as the first direction, (save for the actual position we're moving to)
			else if(this.WorldArray[i+1][j+1] == null){
				this.WorldArray[i+1][j+1] = this.WorldArray[i][j];
				this.WorldArray[i][j] = null;
				this.WorldArray[i+1][j+1].setX(i+1);
				this.WorldArray[i+1][j+1].setY(j+1);
				canDo = true;
			}
			else if(moveIn(i+1, j+1, direction)){
				this.WorldArray[i+1][j+1] = this.WorldArray[i][j];
				this.WorldArray[i][j] = null;
				this.WorldArray[i+1][j+1].setX(i+1);
				this.WorldArray[i+1][j+1].setY(j+1);
				canDo = true;
			}
		}
		
		else if(direction == 5){
			if(!(IntStream.of(VerifyBorders(i,j)).anyMatch(x -> x == 5))){
				int newDirection = getRandom(VerifyBorders(i,j));
				moveIn(i,j,newDirection);		
			}
			//Rest of this is the same as the first direction, (save for the actual position we're moving to)
			else if(this.WorldArray[i+1][j] == null){
				this.WorldArray[i+1][j] = this.WorldArray[i][j];
				this.WorldArray[i][j] = null;
				this.WorldArray[i+1][j].setX(i+1);
				this.WorldArray[i+1][j].setY(j);
				canDo = true;
			}
			else if(moveIn(i+1, j, direction)){
				this.WorldArray[i+1][j] = this.WorldArray[i][j];
				this.WorldArray[i][j] = null;
				this.WorldArray[i+1][j].setX(i+1);
				this.WorldArray[i+1][j].setY(j);
				canDo = true;
			}
		}
		
		else if(direction == 6){
			if(!(IntStream.of(VerifyBorders(i,j)).anyMatch(x -> x == 6))){
				int newDirection = getRandom(VerifyBorders(i,j));
				moveIn(i,j,newDirection);		
			}
			//Rest of this is the same as the first direction, (save for the actual position we're moving to)
			else if(this.WorldArray[i+1][j-1] == null){
				this.WorldArray[i+1][j-1] = this.WorldArray[i][j];
				this.WorldArray[i][j] = null;
				this.WorldArray[i+1][j-1].setX(i+1);
				this.WorldArray[i+1][j-1].setY(j-1);
				canDo = true;
			}
			else if(moveIn(i+1, j-1, direction)){
				this.WorldArray[i+1][j-1] = this.WorldArray[i][j];
				this.WorldArray[i][j] = null;
				this.WorldArray[i+1][j-1].setX(i+1);
				this.WorldArray[i+1][j-1].setY(j-1);
				canDo = true;
			}
		}
		
		else if(direction == 7){
			if(!(IntStream.of(VerifyBorders(i,j)).anyMatch(x -> x == 7))){
				int newDirection = getRandom(VerifyBorders(i,j));
				moveIn(i,j,newDirection);		
			}
			//Rest of this is the same as the first direction, (save for the actual position we're moving to)
			else if(this.WorldArray[i][j-1] == null){
				this.WorldArray[i][j-1] = this.WorldArray[i][j];
				this.WorldArray[i][j] = null;
				this.WorldArray[i][j-1].setX(i);
				this.WorldArray[i][j-1].setY(j-1);
				canDo = true;
			}
			else if(moveIn(i, j-1, direction)){
				this.WorldArray[i][j-1] = this.WorldArray[i][j];
				this.WorldArray[i][j] = null;
				this.WorldArray[i][j-1].setX(i);
				this.WorldArray[i][j-1].setY(j-1);
				canDo = true;
			}
		}
		
		else if(direction == 8){
			if(!(IntStream.of(VerifyBorders(i,j)).anyMatch(x -> x == 8))){
				int newDirection = getRandom(VerifyBorders(i,j));
				moveIn(i,j,newDirection);		
			}
			//Rest of this is the same as the first direction, (save for the actual position we're moving to)
			else if(this.WorldArray[i-1][j-1] == null){
				this.WorldArray[i-1][j-1] = this.WorldArray[i][j];
				this.WorldArray[i][j] = null;
				this.WorldArray[i-1][j-1].setX(i-1);
				this.WorldArray[i-1][j-1].setY(j-1);
				canDo = true;
			}
			else if(moveIn(i-1, j-1, direction)){
				this.WorldArray[i-1][j-1] = this.WorldArray[i][j];
				this.WorldArray[i][j] = null;
				this.WorldArray[i-1][j-1].setX(i-1);
				this.WorldArray[i-1][j-1].setY(j-1);
				canDo = true;
			}
		}
		else{	//Shouldn't happen but if for some reason, none of the 8 directions were registered
			System.out.println("Direction Error");
			canDo = false;
			//Then we print a notice than an error has occurred
		}
		return canDo;	//End of the helper method
	}
	
	public void display(){
		
		JFrame mainFrame = new JFrame("The World");
		mainFrame.setSize(1000,1000);
		mainFrame.setLayout(new GridLayout(1, 1));									//Initializing the window
		
		JPanel grid = new JPanel();													//Initializing the Jpanel
		grid.setLayout(new GridLayout(this.WorldArray.length, this.WorldArray[0].length));														//and placing it in the window
		mainFrame.add(grid);
		
		if(displayCount == 0){														//If this is the first call of the display() method
			for(int i = 0; i < this.WorldArray.length; i++){
				for(int j = 0; j < this.WorldArray[0].length; j++){					//Iterating through the 2D array of objects
					if (this.WorldArray[i][j] instanceof Immovable || this.WorldArray[i][j] instanceof Moveable || this.WorldArray[i][j] instanceof Autonomous){
						//If the current cell hosts any of our 3 objects
						char currentTokenC = this.WorldArray[i][j].getToken();
						String currentTokenS = Character.toString(currentTokenC);
						JLabel currentObject = new JLabel(currentTokenS, SwingConstants.CENTER);	//We take the token, turn it to a string, and place it in a JLabel
						currentObject.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
						currentObject.setBackground(Color.white);
						currentObject.setOpaque(true);								//Aesthetic additions to make the world more grid-like
						this.LabelArray[i][j] = currentObject;						//Insert the current JLabel in our 2D array of labels
						grid.add(this.LabelArray[i][j]);							//Then also place it into the cell of the JPanel corresponding with the cell in the 2D array of objects
					}
					else{															//If the current cell is empty
						JLabel currentObject = new JLabel("", SwingConstants.CENTER);//Then we create a JLabel in a similar manner as before
						currentObject.setBackground(Color.white);					//but the text is blank (no token)
						currentObject.setOpaque(true);
						currentObject.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5)); 
						this.LabelArray[i][j] = currentObject;						//Place the JLabel in the 2D array of Jlabels 
						grid.add(this.LabelArray[i][j]);							//(cell corresponding with current cell in 2D array of world objects)
					}
				}
			}
			mainFrame.setVisible(true);  // Activate window
		}
		
		else{																		//If this isn't the 1st time we're calling display (refreshing the window)
			for(int i = 0; i < this.WorldArray.length; i++){
				for(int j = 0; j < this.WorldArray[0].length; j++){					//Iterate through the entire 2D array of world objects
					if (this.WorldArray[i][j] instanceof Immovable || this.WorldArray[i][j] instanceof Moveable || this.WorldArray[i][j] instanceof Autonomous){
						//If the current cell hosts an object
						char currentTokenC = this.WorldArray[i][j].getToken();		//We get the the token of the object, change it to a string,
						String currentTokenS = Character.toString(currentTokenC);	//And update the already existing JLabel, that corresponds with the current cell in the 2D array
						this.LabelArray[i][j].setText(currentTokenS);				//of world objects, and change it's text with the token
					}
					else{															//If the current cell is empty
						this.LabelArray[i][j].setText("");							//We set the text of the corresponding JLabel cell with a blank
					}
				}
			}
		}
		
		this.displayCount++;	//Log the fact that the display method has been called
	}
}
