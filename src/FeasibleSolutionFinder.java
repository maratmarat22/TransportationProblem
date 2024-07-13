import java.util.Arrays;

public class FeasibleSolutionFinder {

    TransportationTable TT;

    public FeasibleSolutionFinder(TransportationTable TT) {
        this.TT = TT;
    }

    public double northWestCornerMethod() {

        double totalCost = 0; // Общая стоимость перевозок

        int currentRow = 0; // Рассматриваемая строка
        int currentCol = 0; // Рассматриваемый столбец

        while (currentRow < TT.costsMatrix.length && currentCol < TT.costsMatrix[0].length) {

            // Осуществление перевозки
            totalCost += this.performShipment(currentRow, currentCol);

            // Проверка на истощение строки или столбца
            if (TT.suppliers[currentRow] == 0)
                currentRow++;
            else
                currentCol++;
        }

        return totalCost;
    }

    public double leastCostMethod() {

        double totalCost = 0;
        double totalSupply = -1; // Оставшееся количество груза. Значение -1 установлено для входа в цикл

        while (totalSupply != 0) {

            totalSupply = 0;

            double minElement = Double.MAX_VALUE; // Текущий минимальный элемент
            int minElementRow = -1; // Номер его строки
            int minElementCol = -1; // Номер его столбца

            // Алгоритм поиска наименьшего элемента матрицы стоимостей
            for (int i = 0; i < TT.costsMatrix.length; ++i) {
                for (int j = 0; j < TT.costsMatrix[0].length; ++j) {
                    if (TT.costsMatrix[i][j] < minElement && TT.suppliers[i] != 0 && TT.demanders[j] != 0) {
                        minElement = TT.costsMatrix[i][j];
                        minElementRow = i;
                        minElementCol = j;
                    }
                }
            }

            totalCost += this.performShipment(minElementRow, minElementCol);

            // Проверяем, остались ли у поставщиков запасы
            for (double supplier : TT.suppliers)
                totalSupply += supplier;
        }

        return totalCost;
    }

    public double VogelsApproximationMethod() {

        double totalCost = 0;
        double totalSupply = 1;

        double[][] copiedCostsMatrix = MatrixHelper.copy(TT.costsMatrix);

        while (totalSupply != 0) {

            totalSupply = 0;

            // Массивы для хранения штрафов по строкам и столбцам соответственно
            double[] rowDiffs = MatrixHelper.calcDiffsBetweenMinElementsInRows(copiedCostsMatrix);
            double[] colDiffs = MatrixHelper.calcDiffsBetweenMinElementsInCols(copiedCostsMatrix);

            // Максимальные штрафы по строкам и столбцам соответственно
            double rowMaxPenalty = Sorter.getMax(Arrays.copyOf(rowDiffs, rowDiffs.length));
            double colMaxPenalty = Sorter.getMax(Arrays.copyOf(colDiffs, colDiffs.length));

            int requiredRow;
            int requiredCol;

            if (rowMaxPenalty > colMaxPenalty) {
                // Находим индекс строки максимального штрафа
                requiredRow = Sorter.findIndex(rowDiffs, rowMaxPenalty);

                // Находим минимальную стоимость в этой строке
                double colMinCost = Sorter.getMin(Arrays.copyOf(copiedCostsMatrix[requiredRow],
                                                                copiedCostsMatrix[requiredRow].length));
                requiredCol = Sorter.findIndex(copiedCostsMatrix[requiredRow], colMinCost);
            }
            else {
                // Аналогично со столбцами
                requiredCol = Sorter.findIndex(colDiffs, colMaxPenalty);

                // Транспонируем матрицу для адекватной работы метода getMin
                double[][] transposedMatrix = MatrixHelper.transpose(copiedCostsMatrix);
                double rowMinCost = Sorter.getMin(Arrays.copyOf(transposedMatrix[requiredCol],
                                                                transposedMatrix[requiredCol].length));
                requiredRow = Sorter.findIndex(transposedMatrix[requiredCol], rowMinCost);
            }

            totalCost += this.performShipment(requiredRow, requiredCol);

            // Переопределяем элементы истощенных столбцов или строк матрицы как -1
            if (TT.demanders[requiredCol] == 0) {
                for (int i = 0; i < TT.suppliers.length; ++i)
                    copiedCostsMatrix[i][requiredCol] = -1;
            }
            else {
                for (int i = 0; i < TT.demanders.length; ++i)
                    copiedCostsMatrix[requiredRow][i] = -1;
            }

            for (double supplier : TT.suppliers)
                totalSupply += supplier;
        }

        return totalCost;
    }

    private double performShipment(int row, int col) {

        // Находим меньшее из спроса и предложения
        double min = Math.min(TT.suppliers[row], TT.demanders[col]);

        TT.shipmentMatrix[row][col] = min;
        TT.suppliers[row] -= min;
        TT.demanders[col] -= min;

        return min * TT.costsMatrix[row][col];
    }
}
