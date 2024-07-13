import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        double res = 0;

        double[] suppliers = IO.getArray(sc, "Введите кол-во складов и груз в них соответственно:");
        double[] demanders = IO.getArray(sc, "Введите кол-во клиентов и их спрос соответственно:");
        double[][] costsMatrix = new double[suppliers.length][demanders.length];
        IO.getCosts(sc, costsMatrix);

        TransportationTable TT = new TransportationTable(suppliers, demanders, costsMatrix);
        TT.balance();
        TT.addEpsilon(0.01);

        FeasibleSolutionFinder FSF = new FeasibleSolutionFinder(TT);

        loop:
        switch (IO.returnMethodOption(sc)) {
            case '1':
                res = FSF.northWestCornerMethod();
                break;
            case '2':
                res = FSF.leastCostMethod();
                break;
            case '3':
                res = FSF.VogelsApproximationMethod();
            default:
                break loop;
        }

        IO.showResult(TT, res, "Опорный");


        OptimalSolutionFinder OSF = new OptimalSolutionFinder(TT);

        double res2 = OSF.optimize();
        if (res2 == 0)
            res2 = res;

        IO.showResult(TT, res2, "Оптимальный");

        sc.nextLine();

        main(args);
    }
}