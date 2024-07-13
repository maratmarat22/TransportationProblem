import java.util.ArrayList;
import java.util.Arrays;

public class Sorter {

    public static double[] bubbleSort(double[] arr) {

        boolean swapped;
        double buff;

        for (int i = 0; i < arr.length - 1; ++i) {
            swapped = false;

            for (int j = 0; j < arr.length - i - 1; ++j) {
                if (arr[j] > arr[j + 1]) {
                    buff = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = buff;

                    swapped = true;
                }
            }

            if (!swapped)
                break;
        }

        return arr;
    }

    public static double[] deleteByValue(double[] arr, double value) {

        int counter = 0;

        for (int i = 0; i < arr.length; ++i) {
            if (arr[0] == value) {
                for (int j = 0; j < arr.length - 1; ++j)
                    arr[j] = arr[j + 1];

                ++counter;
            }
        }

        double[] newArr = new double[arr.length - counter];
        System.arraycopy(arr, 0, newArr, 0, newArr.length);

        return newArr;
    }

    public static double[] deleteMinDuplicates(double[] arr) {

        int counter = 0;

        for (int i = 1; i < arr.length; ++i) {
            if (arr[i] == arr[0]) {
                for (int j = i; j < arr.length - 1; ++j)
                    arr[j] = arr[j + 1];

                ++counter;
            }
        }

        double[] newArr = new double[arr.length - counter];
        System.arraycopy(arr, 0, newArr, 0, newArr.length);

        return newArr;
    }

    public static int findIndex(double[] arr, double value) {

        int index = -1;

        for (int i = 0; i < arr.length; ++i) {
            if (arr[i] == value)
                index = i;
        }

        return index;
    }

    public static double getMax(double[] arr) {

        bubbleSort(arr);

        return arr[arr.length - 1];
    }

    public static double getMin(double[] arr) {

        bubbleSort(arr);

        double[] newArr = new double[arr.length - 1];

        // Проверка на -1, что значит, что данный ряд/столбец "истощён"
        if (arr[0] == -1) {
            System.arraycopy(arr, 1, newArr, 0, newArr.length);
            return getMin(newArr);
        }

        return arr[0];
    }

    public static double getMin(ArrayList<Double> list) {

        double[] array = new double[list.size()];

        for (int i = 0; i < list.size(); ++i)
            array[i] = list.get(i);

        return getMin(array);
    }
}