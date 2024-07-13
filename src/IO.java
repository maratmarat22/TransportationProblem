import java.util.Scanner;

public class IO {

    public static double[] getArray(Scanner sc, String message) {

        System.out.println("\n" + message);

        int suppliersLength = sc.nextInt();
        double[] suppliers = new double[suppliersLength];

        for(int i = 0; i < suppliersLength; ++i)
            suppliers[i] = sc.nextDouble();

        return suppliers;
    }

    public static void getCosts(Scanner sc, double[][] costsMatrix) {

        for (int i = 0; i < costsMatrix.length; ++i) {
            for (int j = 0; j < costsMatrix[0].length; ++j) {
                System.out.printf("\nВведите эл-т матрицы стоимостей [%d][%d]: ", i, j);
                costsMatrix[i][j] = sc.nextDouble();
            }
        }
    }

    public static char returnMethodOption(Scanner sc) {

        System.out.println("\nВыберите метод нахождения опорного плана: ");
        System.out.println("1. Метод СЗ-угла");
        System.out.println("2. Метод минимальной стоимости");
        System.out.println("3. Метод аппроксимации Фогеля");

        return sc.next().charAt(0);
    }

    public static void showResult(TransportationTable TT, double res, String planType) {

        System.out.println("\n" + planType + " план:");

        for (int i = 0; i < TT.suppliers.length; ++i) {
            for (int j = 0; j < TT.demanders.length; ++j) {

                if (TT.costsMatrix[i][j] == Double.MAX_VALUE)
                    TT.costsMatrix[i][j] = 0;

                System.out.printf("%.1f\t", TT.costsMatrix[i][j]);
                System.out.printf("(%.1f)\t", TT.shipmentMatrix[i][j]);
            }

            System.out.println();
        }

        System.out.printf("Общая стоимость - %.1f\n", res);
    }
}