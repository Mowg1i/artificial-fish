

public class Fish {

	// 0 for prey, 1 for predator; maybe worry about this later!!
	// int type;

	int size = Game.FSIZE;
	// int from 0 to 359 representing which direction the fish is facing
	int direction;
	int speed = 1;
	Brain brain;

	// representation as an int
	int intRep = 2;

	Fish parent;
	Fish child;
	
	int prevPos;
	int pos;
	int yPos;
	int xPos;
	int prevXpos;
	int prevYpos;
	
	int c1 =  150;
	int c2 = (int) (Math.random() * 100);
	int c3 = (int) (Math.random() * 50);
	
	int age = 0;

	// get 1d position given co-ordinates
	int getPos(int xPos, int yPos) {
		return yPos * Game.ROWLENGTH + xPos;
	}

	// get x position given 1d position
	int getXpos(int pos) {
		return pos % Game.ROWLENGTH;
	}

	// get y position given 1d position
	int getYpos(int pos) {
		return (int) pos / Game.ROWLENGTH;
	}

	// child fish constructor

	Fish(Fish parent) {
		direction = (int) Math.random() * 359 + 1;
		this.pos = parent.pos;
		this.prevXpos = parent.prevXpos;
		this.prevYpos = parent.prevYpos;
		
		this.prevPos = parent.prevPos;
		this.xPos = getXpos(pos);
		this.yPos = getYpos(pos);
		this.parent = parent;	
		
		this.c1 = parent.c1;
		this.c2 = parent.c2 + parent.c2*(int)(0.1*((Math.random() * 2) - 1));
		this.c3 = parent.c3 + parent.c3*(int)(0.1*((Math.random() * 2) - 1));
				
		this.changePos();
		
		//if (this.pos > Game.MAPSIZE-1) {this.pos = Game.MAPSIZE-1;}

		// TODO introduce error somehow?
		this.brain = new Brain(parent.brain);
		parent.child = this;
	}

	// new fish constructor
	Fish(int pos) {

		// random 0 or 1
		// int randomType = (int) Math.round(Math.random());
		// type = randomType;

		this.direction = (int) Math.random() * 359 + 1;
		this.pos = pos;
		this.prevPos = pos;
		this.xPos = getXpos(pos);
		this.yPos = getYpos(pos);
		this.prevXpos = getXpos(pos);
		this.prevYpos = getYpos(pos);
		this.brain = new Brain();
	}

	void print() {
		System.out.println("Fish pos:" + this.pos + " size:" + this.size + " age:" + this.age);
	}

	// change position of fish given its current direction
	void changePos() {

		this.prevPos = this.pos;
		this.prevXpos = this.xPos;
		this.prevYpos = this.yPos;

		if (this.direction < 45) {
			this.yPos -= 1;
		} else if (this.direction < 90) {
			this.yPos -= 1;
			this.xPos += 1;
		} else if (this.direction < 135) {
			this.xPos += 1;
		} else if (this.direction < 180) {
			this.yPos += 1;
			this.xPos += 1;
		} else if (this.direction < 225) {
			this.yPos += 1;
		} else if (this.direction < 270) {
			this.yPos += 1;
			this.xPos -= 1;
		} else if (this.direction < 315) {
			this.xPos -= 1;
		} else {
			this.yPos -= 1;
			this.xPos -= 1;
		}

		if (this.xPos < 0) {
			this.xPos = Game.ROWLENGTH-1;
		}
		if (this.xPos > Game.ROWLENGTH-1) {
			this.xPos = 0;
		}
		if (this.yPos < 0) {
			this.yPos = Game.ROWLENGTH-1;
		}
		if (this.yPos > Game.ROWLENGTH-1) {
			this.yPos = 0;
		}

		this.pos = this.getPos(this.xPos, this.yPos);
	}
}
