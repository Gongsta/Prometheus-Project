
public class Autonomous implements WorldObject{
	private String name;
	private char token = 'A';
	private int x, y;
	
	public void setX(int newX){
		this.x = newX;
	}
	
	public void setY(int newY){	//setting the coordinates
		this.y = newY;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){			//getting the coordinates 
		return this.y;
	}
	
	public char getToken(){		//getting the token
		return this.token;
	}
	
	public int step(){											//Randomly picks a direction out of the 8 possible moves				
		int x = 0;				
		double direction = Math.random();
		if (direction <= 0.124){
			x = 1;												//Will translate to up
		}
		else if (direction > 0.124 & direction <= 0.249){
			x = 2; 												//Will translate to up-right
		}
		else if (direction > 0.249 & direction <= 0.374){
			x = 3; 												//Will translate to right
		}
		else if (direction > 0.374 & direction <= 0.499){
			x = 4; 												//Will translate to down-right
		}
		else if (direction > 0.499 & direction <= 0.624){
			x = 5; 												//Will translate to down
		}
		else if (direction > 0.624 & direction <= 0.749){
			x = 6;												//Will translate to down-left
		}
		else if (direction > 0.749 & direction <= 0.874){
			x = 7;												//Will translate to left
		}
		else if (direction > 0.874){
			x = 8;												//Will translate to up-left
		}
		return x;
	}
}
