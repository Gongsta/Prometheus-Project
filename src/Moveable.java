
public class Moveable implements WorldObject{
	private String name = "Moveable";
	private char token = 'M';
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
	public int step(){
		return 0;
	}							//step's only relevant for Autonomous
}
