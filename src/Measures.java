import java.util.Arrays;
import java.util.Random;


public class Measures {
    private static Random random;
    private static int maxIN;
    private static int maxDL;
    private static double avgIN;
    private static double avgDL;

    public static void shuffle(int[] array) {
        if (random == null) random = new Random();
        int count = array.length;
        for (int i = count; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static void buildTree(int[] arr, WAVLTree tree) {
        int actionInsert = 0;
        int sumActions = 0;
        for (int item : arr) {
            actionInsert = tree.insert(item, Integer.toString(item));
            if (actionInsert > maxIN) {
                maxIN = actionInsert;
            }
            sumActions += actionInsert;
        }
        avgIN = (double) sumActions / arr.length;
    }

    private static void emptyTree(int[] arr, WAVLTree tree) {
        int actionDelete = 0;
        int sumActions = 0;
        for (int i = 0; i < arr.length; i++) {
            actionDelete = tree.delete(arr[i]);
            if (actionDelete > maxDL) {
                maxDL = actionDelete;
            }
            sumActions += actionDelete;
        }
        avgDL = (double) sumActions / arr.length;

    }

    private static void fillArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
    }

    public static void main(String[] args) {
        for (int i = 1; i < 11; i++) {
            int[] arr = new int[i * 10000];
            fillArray(arr);
            shuffle(arr);
            WAVLTree tree = new WAVLTree();
            buildTree(arr, tree);
            Arrays.sort(arr);
            emptyTree(arr, tree);
            System.out.println("Num of items: " + i * 10000);
            System.out.println("Average inserts: " + avgIN);
            System.out.println("Max inserts: " + maxIN);
            System.out.println("Average deletes: " + avgDL);
            System.out.println("Max deletes: " + maxDL);
            System.out.println();
        }
    }

}
