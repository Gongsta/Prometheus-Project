import java.util.concurrent.TimeUnit;
import java.awt.*;// Basic java graphics library
import java.awt.event.*;// Basic java event handling library
import javax.swing.*;// The Swing library

public class Main {
	 
	public static void main(String[] args){
		
		TheWorld world = buildWorld(7,7);		//Building the world with our desired size
		world.display();						//Initialize the world
		int iterations = 10;					//Set the number of iterations we want of step (100 for now)
		boolean continueSimulation = true;		//Boolean deciding whether the program runs through another simulation
		
		while(continueSimulation){ 
			
			for(int i = 0; i< iterations; i++){
				world.display();				//Show the current state of the world
				world.step();					//Run a step throughout the world
				try{
					TimeUnit.SECONDS.sleep(1);
				}
				catch(InterruptedException e){
					System.out.println("Wait issue");
				}								//This try/catch delay is just so that the simulation doesn't go too fast
			}
			int userChoice = JOptionPane.showConfirmDialog(null, "Would you like to run the simulation again?", "Simulation Over", JOptionPane.YES_NO_OPTION);
			//User's choice (yes or no) in the option pane is saved as an integer
			if (userChoice== JOptionPane.YES_OPTION){
												//If yes, then we allow the loop to go again
			}
			else if(userChoice == JOptionPane.NO_OPTION){
				continueSimulation = false;		//otherwise, we end the simulation
			}
		}
		System.exit(0);							//End of program (use the syscall to close the window at the end as well)
	}
	
	private static TheWorld buildWorld(int rows, int columns){
		
		TheWorld w = new TheWorld(rows, columns);//Actually creating the world with the specified sizes
		
		/*
		Immovable i1 = new Immovable();
		Immovable i2 = new Immovable();
		Immovable i3 = new Immovable();
		Immovable i4 = new Immovable();
		Immovable i5 = new Immovable();			//Creating 5 immovable objects
		
		Moveable m1 = new Moveable();
		Moveable m2 = new Moveable();
		Moveable m3 = new Moveable();			//Creating 3 moveable objects
		Autonomous a2 = new Autonomous();
		*/
		
		Autonomous a1 = new Autonomous();		//Creating 2 autonomous objects
		
		
		w.add(a1, 4, 4);
		/*
		w.add(a2, 6, 7);
		
		w.add(m1, 3, 4);
		w.add(m2, 6, 3);
		w.add(m3, 1, 2);
		
		w.add(i1, 2, 1);
		w.add(i2, 9, 8);
		w.add(i3, 5, 6);
		w.add(i4, 7, 9);
		w.add(i5, 0, 0);						//Assign the objects to the world at different locations
		*/
		return w;
	}
	
}
