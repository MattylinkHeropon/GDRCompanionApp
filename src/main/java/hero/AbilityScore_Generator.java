package hero;


import java.util.Arrays;
import java.util.Random;

/**
 * Class with the method to generate generate a set of Ability Score.
 * Each method has a Name and a Description associated with it
 */
public  class AbilityScore_Generator {

    private static final Random random = new Random();

    private static final String[] methodName = new String[] {
            "Complete Random",
            "Classic",
            "Standard",
            "Point Buy",
            "Manual"
    };

    private static final String[] methodDescription = new String[]{
            "All value are picked via a random function by the machine, range 1 ~ 20",
            "Simulate the roll of 3D6 and sum the value of the rolls",
            "Simulate the roll of 4D6, remove the lower value, sum the remaining values",
            "Use the Point Buy method of your version. Only supported for D&D 3.x; 4; 5 and Pathfinder 1E",
            "Manually set your values"
    };

    public static String[] getMethodName() {
        return methodName;
    }

    public static String[] getMethodDescription() {
        return methodDescription;
    }


    /**
     * Generate an array of random int, each one in a range 1 to 20
     * @return The created array
     */
    public static int[] completeRandom(){
        int[] value = new int[6];
        for (int i = 0; i < 6; i++) {
            value[i] = random.nextInt(20) + 1;
        }
        Arrays.sort(value);
        return value;
    }

    /**
     * Generate an array of random int,
     * Every int is generated simulating the roll of 3 six-faced dice, and adding the result of the three roll
     * @return The created array
     */
    public static int[] classic(){
        int temp;
        int[] value = new int[6];

        for (int i = 0; i < 6; i++) {
            temp = 0;
            for (int j = 0; j < 3; j++) {
                temp = temp + random.nextInt(6) + 1;
            }
            value[i] = temp;
        }
        Arrays.sort(value);
        return value;
    }

    /**
     * Generate an array of random int,
     * Every int is generated simulating the roll of 4 six-faced dice, discard the lower value and sum the remaining ones
     * @return The created array
     */
    public static int[] standard(){
        int[] temp = new int[4];
        int[] value = new int[6];

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                temp[j] = random.nextInt(6) + 1;
            }
            Arrays.sort(temp);
            value[i] = temp[1] + temp[2] + temp[3];
        }
        Arrays.sort(value);
        return value;
    }

}
