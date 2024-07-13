public class TransportationTable {

    double[] suppliers; // Массив с запасами поставщиков
    double[] demanders; // Массив с требованиями потребителей
    double[][] costsMatrix; // Матрица стоимостей
    double[][] shipmentMatrix; // Матрица перевозок

    public TransportationTable(double[] suppliers, double[] demanders, double[][] costsMatrix) {

        this.suppliers = suppliers;
        this.demanders = demanders;
        this.costsMatrix = costsMatrix;
        this.shipmentMatrix = new double[costsMatrix.length][costsMatrix[1].length];
    }

    public void balance() {

        double supSum = 0;
        double demSum = 0;

        for (double supplier : suppliers)
            supSum += supplier;
        for (double demander : demanders)
            demSum += demander;

        if (supSum > demSum) {
            double[] newDemanders = new double[demanders.length + 1];
            System.arraycopy(demanders, 0, newDemanders, 0, demanders.length);
            newDemanders[newDemanders.length - 1] = supSum - demSum;

            this.demanders = newDemanders;
            expandCostsMatrix(0);
        }
        else if (supSum < demSum) {
            double[] newSuppliers = new double[suppliers.length + 1];
            System.arraycopy(suppliers, 0, newSuppliers, 0, suppliers.length);
            newSuppliers[newSuppliers.length - 1] = demSum - supSum;

            this.suppliers = newSuppliers;
            expandCostsMatrix(1);
        }
    }

    private void expandCostsMatrix(int arg) {

        double[][] newCostsMatrix = new double[suppliers.length][demanders.length];

        if (arg == 0) {
            for (int i = 0; i < newCostsMatrix.length; ++i) {
                for (int j = 0; j < newCostsMatrix[0].length - 1; ++j)
                    newCostsMatrix[i][j] = costsMatrix[i][j];
            }

            for (int i = 0; i < suppliers.length; i++)
                newCostsMatrix[i][demanders.length - 1] = 0;
        }
        else {
            for (int i = 0; i < newCostsMatrix.length - 1; ++i) {
                for (int j = 0; j < newCostsMatrix[0].length; ++j)
                    newCostsMatrix[i][j] = costsMatrix[i][j];
            }

            for (int j = 0; j < demanders.length; ++j)
                newCostsMatrix[suppliers.length - 1][j] = 0;
        }

        this.costsMatrix = newCostsMatrix;
        this.shipmentMatrix = new double[costsMatrix.length][costsMatrix[0].length];
    }

    public void addEpsilon(double epsilon) {

        suppliers[0] += epsilon;

        for (int i = 0; i < demanders.length; ++i)
            demanders[i] += epsilon/demanders.length;
    }
}