import java.util.ArrayList;
import java.util.Arrays;

public class OptimalSolutionFinder {

    TransportationTable TT;

    public OptimalSolutionFinder(TransportationTable TT) {
        this.TT = TT;
    }

    public double optimize() {

        double res = 0;

        // Анализируем опорный план методом потенциалов
        double[] retArr = this.potentialMethod();

        // Распаковываем полученные из метода данные
        double minElement = retArr[0]; // Минимальный элемент оценочной матрицы
        int minElementRow = (int)retArr[1]; // Его строка
        int minElementCol = (int)retArr[2]; // Его столбец

        // Цикл повторяется, пока в оценочной матрице есть отрицательные значения
        while (minElement < 0) {

            // Цикл пересчета
            res = this.steppingStoneMethod(minElementRow, minElementCol);

            // Перепроверяем полученный план
            retArr = this.potentialMethod();

            minElement = retArr[0];
            minElementRow = (int)retArr[1];
            minElementCol = (int)retArr[2];
        }

        return res;
    }

    public double[] potentialMethod() {

        double[][] potentialCostsMatrix = new double[TT.costsMatrix.length][TT.costsMatrix[0].length];

        double[] u = new double[TT.suppliers.length];
        double[] v = new double[TT.demanders.length];

        Arrays.fill(u, Double.MAX_VALUE);
        Arrays.fill(v, Double.MAX_VALUE);

        u[0] = 0;

        boolean updated;

        do {
            // Переменная, хранящая информацию о факте обновления потенциалов
            updated = false;

            for (int i = 0; i < TT.costsMatrix.length; ++i) {
                for (int j = 0; j < TT.costsMatrix[0].length; ++j) {

                    // Проверка ячейки на базисность
                    if (TT.shipmentMatrix[i][j] != 0) {
                        potentialCostsMatrix[i][j] = TT.costsMatrix[i][j];

                        // Double.MAX_VALUE - значит, что потенциал еще не рассчитан
                        if (u[i] != Double.MAX_VALUE && v[j] == Double.MAX_VALUE) {
                            v[j] = potentialCostsMatrix[i][j] - u[i];
                            updated = true;
                        }

                        else if (u[i] == Double.MAX_VALUE && v[j] != Double.MAX_VALUE) {
                            u[i] = potentialCostsMatrix[i][j] - v[j];
                            updated = true;
                        }
                    }
                }
            }
        } while (updated); // Цикл повторяется, пока есть нерассчитанные потенциалы

        for (int i = 0; i < TT.costsMatrix.length; i++) {
            for (int j = 0; j < TT.costsMatrix[0].length; j++) {
                if (TT.shipmentMatrix[i][j] == 0)
                    potentialCostsMatrix[i][j] = u[i] + v[j];
            }
        }

        double[][] plan = new double[TT.costsMatrix.length][TT.costsMatrix[0].length];

        double min = Double.MAX_VALUE;
        int minElementRow = -1;
        int minElementCol = -1;

        for (int i = 0; i < TT.costsMatrix.length; i++) {
            for (int j = 0; j < TT.costsMatrix[0].length; j++) {

                plan[i][j] = TT.costsMatrix[i][j] - potentialCostsMatrix[i][j];

                if (plan[i][j] < min) {
                    min = plan[i][j];
                    minElementRow = i;
                    minElementCol = j;
                }
            }
        }

        double[] retArr = new double[3];
        retArr[0] = min;
        retArr[1] = minElementRow;
        retArr[2] = minElementCol;

        return retArr;
    }

    public double steppingStoneMethod(int minRow, int minCol) {

        double totalCost = 0;

        ArrayList<Integer> rowIndices = new ArrayList<>();
        ArrayList<Integer> colIndices = new ArrayList<>();

        rowIndices.add(minRow);
        colIndices.add(minCol);

        ArrayList<Integer> falseRowIndices = new ArrayList<>();
        ArrayList<Integer> falseColIndices = new ArrayList<>();

        boolean pathFound = false;
        int counter = 2;

        while (!pathFound) {
            int lastRow = rowIndices.getLast();
            int lastCol = colIndices.getLast();

            boolean nextCellFound = false;

            if (counter % 2 == 0) {
                for (int j = 0; j < TT.costsMatrix[0].length; ++j) {
                    if (TT.shipmentMatrix[lastRow][j] != 0 && j != lastCol && !falseColIndices.contains(j)) {

                        rowIndices.add(lastRow);
                        colIndices.add(j);
                        nextCellFound = true;

                        break;
                    }
                }
                if (!nextCellFound) {
                    falseRowIndices.add(lastRow);
                    rowIndices.removeLast();
                    colIndices.removeLast();
                }
            }
            else {
                for (int i = 0; i < TT.costsMatrix.length; ++i) {
                    if (TT.shipmentMatrix[i][lastCol] != 0 && i != lastRow && !falseRowIndices.contains(i)) {

                        rowIndices.add(i);
                        colIndices.add(lastCol);
                        nextCellFound = true;

                        break;
                    }
                }
                if (!nextCellFound) {
                    falseColIndices.add(lastCol);
                    colIndices.removeLast();
                    rowIndices.removeLast();
                }
            }

            ++counter;
            TT.shipmentMatrix[minRow][minCol] = -1;

            if (!rowIndices.isEmpty() && rowIndices.size() != 1 && rowIndices.getFirst().equals(rowIndices.getLast())
                                                                && colIndices.getFirst().equals(colIndices.getLast()))
                pathFound = true;
        }

        TT.shipmentMatrix[minRow][minCol] = 0;

        rowIndices.removeLast();
        colIndices.removeLast();

        // Матрица знаков
        int[][] signMatrix = new int[TT.costsMatrix.length][TT.costsMatrix[0].length];
        int signValue = 1; // 1 - плюс, -1 - минус

        // Список "отрицательных" ячеек
        ArrayList<Double> negativeCells = new ArrayList<>();

        for (int i = 0; i < rowIndices.size(); ++i) {

            // Присваиваем ячейке матрицы знаков соответствующее значение
            signMatrix[rowIndices.get(i)][colIndices.get(i)] = signValue;

            // Меняем значение для следующей итерации
            if (signValue == 1)
                signValue = -1;

            else {
                // Добавляем "отрицательную ячейку" в список
                negativeCells.add(TT.shipmentMatrix[rowIndices.get(i)][colIndices.get(i)]);
                signValue = 1;
            }
        }

        double delta = Sorter.getMin(negativeCells);

        for (int i = 0; i < TT.costsMatrix.length; ++i) {
            for (int j = 0; j < TT.costsMatrix[0].length; ++j) {
                if (signMatrix[i][j] == -1)
                    TT.shipmentMatrix[i][j] -= delta;
                else if (signMatrix[i][j] == 1)
                    TT.shipmentMatrix[i][j] += delta;

                totalCost += TT.shipmentMatrix[i][j] * TT.costsMatrix[i][j];
            }
        }

        return totalCost;
    }
}
