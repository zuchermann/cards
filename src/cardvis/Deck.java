
package cardvis;

import java.lang.Math;
import java.util.Arrays;

/**
 *
 * @author zachkondak
 */
public class Deck {

    static int getNumOfRotations(int deckSize){
        if (deckSize > 2){
            int adjustedSize = deckSize;
            if(deckSize % 2 == 0) adjustedSize = (deckSize - 1);
            int rotNumber = 1;
            int currentPosition = 2 % adjustedSize;
            while (currentPosition != 1) {
                rotNumber++;
                currentPosition = (currentPosition * 2) % adjustedSize;
            }
            return rotNumber;
        } 
        else return 1;
    }
    
    static int[][] simulate(int sizeOfDeck){
        int deckSize = sizeOfDeck;
        if(sizeOfDeck % 2 != 0) deckSize++;
        int rotationCount = getNumOfRotations(deckSize) + 1;
        int[][] result = new int[rotationCount][deckSize];
        for (int i = 0 ; i < deckSize ; i++){
            result[0][i] = i;
        }
        for(int i = 1; i < rotationCount; i++){
            int[] firstHalf = Arrays.copyOfRange(result[i-1], 0, deckSize / 2);
            int[] secondHalf = Arrays.copyOfRange(result[i-1], deckSize / 2, deckSize);
            //System.out.println("first: " + Arrays.toString(firstHalf));
            //System.out.println("second: " + Arrays.toString(secondHalf));
            for(int j = 0; j < deckSize / 2; j++){
                result[i][(j * 2)] = firstHalf[j];
                result[i][(j * 2) + 1] = secondHalf[j];
            }
        }
        if (deckSize > sizeOfDeck) {
            for(int i = 0; i < result.length; i++){
                result[i] = Arrays.copyOfRange(result[i], 0, sizeOfDeck);
            }
        }
        return result;
    }
    
}
