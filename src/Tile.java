public class Tile{

private int x;
private int y; //x y coordinates
private boolean has_push; //if contains a push box
private boolean has_pull; //if contains a pull box
private boolean has_player; //if contains player
private boolean is_inactive; //if is obstacle
private final boolean has_push_dest; // if is push destination
private final boolean has_pull_dest;// if is pull destination

public Tile(int x, int y, boolean push_dest, boolean pull_dest, boolean is_inactive) {
  this.x = x;
  this.y = y;
  this.has_push = false;
  this.has_pull = false;
  this.has_player = false;
  this.has_push_dest = push_dest;
  this. has_pull_dest = pull_dest;
  this.is_inactive = is_inactive;
}

public void inactivate() {
	this.is_inactive = true;
}

//Set coordinates
public void setCoords(int x, int y){
  this.x = x;
  this.y = y;
}

//Get x coordinate
public int getX() {
  return this.x;
}

//Get y coordinate
public int getY() {
  return this.y;
}

//sets the Tile to have a push-box
public void setPushBox(boolean has_push) {
  if (this.is_inactive || this.has_player || this.has_pull) {
	//  System.out.println("actv"+this.is_inactive + " ply"+this.has_player + " pus" + this.has_push);
	  //  throw new RuntimeException("Cannot set push Box!\n");
  }
  this.has_push = has_push;
}

public boolean getPushBox() {
  return this.has_push;
}

//sets the Tile to have a pull-box
public void setPullBox(boolean has_pull) {
  if (this.is_inactive || this.has_player || this.has_push) {
    throw new RuntimeException("Cannot set pull Box!\n");
  }
  this.has_pull = has_pull;
} 

public boolean getPullBox() {
  return this.has_pull;
}

//sets the Tile to have a player
public void setPlayer(boolean has_player) {
  if (this.is_inactive || this.has_push || this.has_pull) {
    throw new RuntimeException("Cannot set player!\n");
  }
  this.has_player = has_player;
} 

public boolean getPlayer() {
  return this.has_player;
}

public boolean isInactive() {
  return this.is_inactive;
}

public boolean isPushDest() {
  return this.has_push_dest;
}

public boolean isPullDest() {
  return this.has_pull_dest;
}

public int getColor() {
	if (this.has_player = true) {return 5;}
	return 0; //to be implemented
}

//remove all boxes or player from a given tile
public void clearTile(){
  this.has_push = false;
  this.has_pull = false;
  this.has_player = false;
} 


}
