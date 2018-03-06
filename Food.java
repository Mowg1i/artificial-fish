public class Food {

	// representation as an int
	int intRep = 1;
	int value = 10;

	int pos;
	int yPos;
	int xPos;
	

	// get 1d position given co-ordinates
	int getPos(int xPos, int yPos) {
		return yPos * Game.ROWLENGTH + xPos;
	}

	// get x position given 1d position
	int getXpos(int pos) {
		return pos % Game.ROWLENGTH;
	}

	// get y positon given 1d position
	int getYpos(int pos) {
		return (int) pos / Game.ROWLENGTH;
	}

	Food(int pos) {
		this.pos = pos;
		this.xPos = this.getXpos(pos);
		this.yPos = this.getYpos(pos);
	}
}
