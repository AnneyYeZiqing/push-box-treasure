import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


class KeyInput extends KeyAdapter{
	
public void keyPressed(KeyEvent e){
	
	int key = e.getKeyCode();
	char dir;

//different cases for each possibility : up down left right space 
//switch statement 
//case 1 : VK_LEFT
	switch(key) {
	case KeyEvent.VK_LEFT:
		dir = 'l';
		if(is_inactive, isPulling) {
			return;
		}
		Tile.playerMove(dir);
		break;
	
	case KeyEvent.VK_RIGHT:
		dir = 'r';
		if(is_inactive) {
			return;
		}
		Tile.playerMove(dir);
		break;
	case KeyEvent.VK_UP:
		dir = 'u';
		if(is_inactive) {
			return;
		}
		Tile.playerMove(dir);
		break;

	case KeyEvent.VK_DOWN:
		dir = 'd';
		if(is_inactive) {
			return;
		}
		Tile.playerMove(dir);
		break;
	

	case KeyEvent.VK_R:
		resetLv(); 
		break;
	}
	
}

