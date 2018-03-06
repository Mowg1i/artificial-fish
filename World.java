import java.util.ArrayList;

public class World {

	static int MAPSIZE = 1600;
	static int ROWLENGTH = 40;
	static int LOCALMAPSIZE = 9;
	static int LOCALMAPROWLENGTH = 3;

	// 10x10 map
	// 0 is empty, 1 is food, 2 is fish
	int[] map = new int[MAPSIZE];
	int[][] map2d = new int[40][40];

	int[] localmap = new int[LOCALMAPSIZE];

	World() {

	}

	void print() {
		for (int i = 0; i < MAPSIZE; i++) {
			if (i % ROWLENGTH == 0) {
				System.out.println();
			}
			System.out.print(" " + map[i] + " ");

		}
		System.out.println();
	}

	void drawFish(ArrayList<Fish> fishArray) {

		for (int i = 0; i < fishArray.size(); i++) {

			Fish f = fishArray.get(i);

			this.map[f.prevPos] = 0;
			this.map[f.pos] = f.intRep;
			this.map2d[f.yPos][f.xPos] = f.intRep;
		}
	}

	void drawFood(ArrayList<Food> foodArray) {
		for (Food f : foodArray) {
			this.map[f.pos] = f.intRep;
			this.map2d[f.yPos][f.xPos] = f.intRep;
		}
	}

	void clearMap() {
		// clear map
		for (int i = 0; i < MAPSIZE; i++) {
			this.map[i] = 0;
		}

		// clear 2d map
		for (int j = 0; j < ROWLENGTH; j++) {
			for (int k = 0; k < ROWLENGTH; k++) {

				this.map2d[j][k] = 0;

			}
		}

	}

	int[] getLocalMap(int pos) {
		
		//TODO this is sooo ugly
		int[] localMap = new int[LOCALMAPSIZE];
		
		try {


		localMap[0] = this.map[pos-ROWLENGTH-1];
		localMap[1] = this.map[pos-ROWLENGTH];
		localMap[2] = this.map[pos-ROWLENGTH+1];
		localMap[3] = this.map[pos-1];
		
		// FISH POSITION
		localMap[4] = 3;
		
		localMap[5] = this.map[pos+1];
		localMap[6] = this.map[pos+ROWLENGTH-1];
		localMap[7] = this.map[pos+ROWLENGTH];
		localMap[8] = this.map[pos+ROWLENGTH+1];
		
		} catch (Exception e) {
			
		}
		
		return localMap;
		
	}

}
