import java.util.ArrayList;
import com.phidgets.*;
import org.encog.Encog;
import org.encog.ml.data.basic.BasicMLData;

import processing.core.PApplet;

public class Game extends PApplet {

	// 9x9 map of area surrounding fish
	static int LOCALMAPSIZE = 9;
	static int FSIZE = 40;
	static int ROWLENGTH = 40;
	static int MAPSIZE = 1600;
	static int DRAWDELAY = 1;
	static int NUMOFFISH = 50;
	static int NUMOFFOOD = 150;
	static int CLONETHRESHOLD = 100;
	static int MAPSCALE = 25;
	// create array of food at random positions and place in world
	static ArrayList<Food> foodArray = new ArrayList<Food>();
	static ArrayList<Fish> fishArray = new ArrayList<Fish>();
	static InterfaceKitPhidget IKP;
	static int timeStep;
	static World world;
	static int JINCREMENT = 45;
	static int FOODBASE = 250;

	// sensors

	// sensor 0 is rotation
	int rotation;

	// sensor 1 is sound
	int sound;

	// sensor 2 is light
	int light;

	// sensor 3 is joystick y
	int jy;

	// sensor 4 is joystick x
	int jx;

	// set up world
	public static void main(String args[]) {
		PApplet.main("Game");

		timeStep = -1;

		world = new World();

		world.clearMap();

		for (int i = 0; i < NUMOFFOOD; i++) {

			int random = (int) (Math.random() * (MAPSIZE - 1) + 1);
			Food fd = new Food(random);
			foodArray.add(fd);
		}

		// create fish in random positions and place in world
		// creating numOfFish fish with random start positions
		for (int i = 0; i < NUMOFFISH; i++) {
			// random is position of fish
			int random = (int) (Math.random() * (MAPSIZE));
			// create new fish with random position (1d position)
			Fish f = new Fish(random);
			fishArray.add(f);
		}

	}

	public void settings() {
		size(ROWLENGTH * MAPSCALE, ROWLENGTH * MAPSCALE);
	}

	public void setup() {

		try {
			IKP = new InterfaceKitPhidget();
			IKP.openAny();
			println("Waiting for Phidget");
			IKP.waitForAttachment();
			println("OK ready to go");
		} catch (Exception e) {
			println("ERROR");
			System.out.println(e);
		}

	}

	public void draw() {

		// update sensor values

		// sensor 0 is rotation
		try {
			rotation = (int) IKP.getSensorValue(0);//
		} catch (Exception e) {
			println(e.toString());
		}

		// sensor 1 is sound
		try {
			sound = (int) IKP.getSensorValue(1);//
		} catch (Exception e) {
			println(e.toString());
		}

		// sensor 2 is light
		try {
			light = (int) IKP.getSensorValue(2);//
		} catch (Exception e) {
			println(e.toString());
		}

		// sensor 3 is joystick y
		try {
			jy = (int) IKP.getSensorValue(3);//
		} catch (Exception e) {
			println(e.toString());
		}

		// sensor 4 is joystick x
		try {
			jx = (int) IKP.getSensorValue(4);//
		} catch (Exception e) {
			println(e.toString());
		}

		timeStep++;

		DRAWDELAY = getSpeed(rotation);

		if (timeStep % DRAWDELAY == 0) {
			

			// TODO add or remove food from food array according to light level
			
			int foodRate = getFoodRate(light);

			while (foodArray.size() < foodRate ) {
				int random = (int) (Math.random() * (MAPSIZE - 1) + 1);
				// and random new food is added, how much depends on light level
				foodArray.add(new Food(random));
		}


			// then for each fish
			for (int f = 0; f < fishArray.size(); f++) {

				Fish fish = fishArray.get(f);

				// get info about fish
				fish.print();

				// get input data
				// 3 by 3 map around the fish and 1 node for fish direction
				double[] inputs = new double[Game.LOCALMAPSIZE + 1];

				int[] localMap = world.getLocalMap(fish.pos);

				for (int i = 0; i < LOCALMAPSIZE; i++) {
					inputs[i] = localMap[i];
				}

				inputs[LOCALMAPSIZE] = fish.direction;

				// put input data into network
				BasicMLData dataInput = new BasicMLData(inputs);
				// compute inputs in fish's nn
				double[] output = fish.brain.network.compute(dataInput).getData();
				System.out.println("Fish brain output: " + output[0]);
				Encog.getInstance().shutdown();

				// convert output to a degree number - this will change the
				// direction the fish is moving in
				fish.direction = (int) (output[0] * 359 + 1);
		
				getjInput(jx, jy, fish);

				// update fish now their direction has been updated
				if (sound < 100 ) {fish.changePos(); }

				boolean canEat = false;
				// feed fish

				// for each food item
				for (int i = 0; i < foodArray.size(); i++) {

					// if food is at same position as fish
					if (foodArray.get(i).pos == fish.pos) {
						// fish can eat
						canEat = true;
						// fish size grows by food value
						fish.size += foodArray.get(i).value;
						// food is removed from array
						foodArray.remove(i);
						i--;
					}
				}	

				// else if fish is not eating, decrement fish size
				if (canEat == false) {
					fish.size--;
				}
				fish.age++;
			}
			

			// update fish

			// check for any dead ones and remove
			// check for any successful ones and clone (with mutation?)
			for (int i = 0; i < fishArray.size(); i++) {
				// if the fish has sadly withered away to nothing!
				if (fishArray.get(i).size <= 0) {
					fishArray.remove(i);
					i--;
				} else if (fishArray.get(i).size > CLONETHRESHOLD) {
					// or if the fish is very successful, clone it
					fishArray.add(new Fish(fishArray.get(i)));
					fishArray.get(i).size -= FSIZE;
				}
			}

			// if all the fish have died :( get some new fish!
			// TODO can this be more interesting?
			if (fishArray.size() == 0) {

				// creating numOfFish fish with random start positions
				for (int i = 0; i < NUMOFFISH; i++) {
					// random is position of fish
					int random = (int) (Math.random() * (MAPSIZE - 1) + 1);
					// create new fish with random position (1d position)
					Fish f = new Fish(random);
					fishArray.add(f);
				}
			}

			// update map
			world.clearMap();
			world.drawFish(fishArray);
			world.drawFood(foodArray);
			System.out.println("Number of fish: " + fishArray.size());
			System.out.println("Time step: " + timeStep);
			world.print();
			System.out.println();
		}

		// now just replicate all the print methods
		// clear the screen
		// 
		background(50, light, 100);

		// draw the food

		noStroke();

		fill(150, 200, 60, 160);
		for (Food f : foodArray) {
			ellipse(f.xPos * MAPSCALE, f.yPos * MAPSCALE, 5, 5);
		}

		// draw the fish
		for (int i = 0; i < fishArray.size(); i++) {

			Fish f = fishArray.get(i);

			int fishCol = color(f.c1, f.c2, f.c3);
			fill(fishCol, 200);
			ellipse(f.xPos * MAPSCALE, f.yPos * MAPSCALE, f.size, f.size);
		}

	}
	
	void getjInput(int jx,int jy, Fish fish) {
		if (jx > 600) {
			
			if (fish.direction <90) {				
				fish.direction += JINCREMENT;	
			}
			
			if (fish.direction >90) {				
				fish.direction -= JINCREMENT;	
			}
		} 
		
		if (jx > 900) { fish.direction = 90; } 
		
		
		if (jx < 400) {
			
			if (fish.direction <270) {				
				fish.direction += JINCREMENT;	
			}
			
			if (fish.direction >270) {				
				fish.direction -= JINCREMENT;	
			}
		} 
		
		if (jx < 100) { fish.direction = 270; } 
		
		
		
		if (jy > 600) {
			
			if (fish.direction >180) {				
				fish.direction += JINCREMENT;	
			}
			
			if (fish.direction <180) {				
				fish.direction -= JINCREMENT;	
			}
		} 
		
		if (jy > 900) { fish.direction = 0; } 

		
		if (jy < 400) {
			
			if (fish.direction <180) {				
				fish.direction += JINCREMENT;	
			}
			
			if (fish.direction >180) {				
				fish.direction -= JINCREMENT;	
			}
		} 
		
		if (jy < 200) { fish.direction = 180; } 
		
	}
	
	int getSpeed(int rotation) {
		
		int d = 0;
		if (rotation > 900) {

			d = 1;
		} else if (rotation > 800) {

			d = 2;
		} else if (rotation > 700) {

			d = 4;
		} else if (rotation > 600) {

			d = 8;
		} else if (rotation > 500) {

			d = 16;
		} else if (rotation > 400) {

			d = 32;
		} else if (rotation > 300) {

			d = 64;
		} else if (rotation > 200) {

			d = 128;
		} else if (rotation > 100) {

			d = 256;
		} else {

			d = 1000000000;
		}
		
		return d;
	}
	

	int getFoodRate(int light) {
		
		return FOODBASE - light;
		
	}
	
}
