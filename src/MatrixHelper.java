import java.util.Arrays;

public class MatrixHelper {

    public static double[] calcDiffsBetweenMinElementsInRows(double[][] matrix) {

        double[] rowDiffs = new double[matrix.length]; // Массив с разницами между мин. стоимостями для каждой из строк

        for (int i = 0; i < matrix.length; ++i) {
            // Массив со стоимостями из одной строки
            double[] rowElements = new double[matrix[0].length];

            // Записываем все стоимости из одной строки в массив
            System.arraycopy(matrix[i], 0, rowElements, 0, matrix[0].length);

            Sorter.bubbleSort(rowElements); // Сортируем стоимости по возрастанию
            rowElements = Sorter.deleteByValue(rowElements, 0);
            rowElements = Sorter.deleteByValue(rowElements, -1);

            if (rowElements.length == 0) {
                rowDiffs[i] = -1;
                continue;
            }
            else if (rowElements.length == 1) {
                rowDiffs[i] = 0;
                continue;
            }
            // Если в массиве разность между наименьшими двумя эл-тами равна 0, но не все элементы одинаковы, то...
            else if (rowElements[1] - rowElements[0] == 0 && Arrays.stream(rowElements).distinct().count() != 1)
                Sorter.deleteMinDuplicates(rowElements); // ...Удаляем повторяющиеся минимальные эл-ты

            rowDiffs[i] = rowElements[1] - rowElements[0]; // Находим штраф
        }

        return rowDiffs;
    }

    public static double[] calcDiffsBetweenMinElementsInCols(double[][] matrix) {

        double[][] transposedMatrix = MatrixHelper.transpose(matrix);

        return calcDiffsBetweenMinElementsInRows(transposedMatrix);
    }

    public static double[][] transpose(double[][] matrix) {

        double[][] transposedMatrix = new double[matrix[0].length][matrix.length];

        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[0].length; ++j)
                transposedMatrix[j][i] = matrix[i][j];
        }

        return transposedMatrix;
    }

    public static double[][] copy(double[][] matrix) {

        double[][] copiedMatrix = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[0].length; ++j)
                copiedMatrix[i][j] = matrix[i][j];
        }

        return copiedMatrix;
    }
}