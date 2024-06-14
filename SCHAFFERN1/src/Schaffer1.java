public class Schaffer1 {


    public static double normalize(int segmentValue, int segmentLength, double lb, double ub) {
        return lb + ((double) segmentValue / (Math.pow(2, segmentLength))) * (ub - lb);
    }

    public static double[] extractVariables(String chromosome, int numberOfVariables, double lb, double ub) {
        double[] x = new double[numberOfVariables];
        int segmentLength = chromosome.length() / numberOfVariables;

        // Iterate over segments and calculate fitness for each
        for (int i = 0; i < numberOfVariables; i++) {
            // Extract the segment from the chromosome
            int startIndex = i * segmentLength;
            int endIndex = (i + 1) * segmentLength;

            String segment = chromosome.substring(startIndex, endIndex);
            // Convert the segment to an integer value
            int segmentValue = Integer.parseInt(segment, 2);
            // Scaled value for each variable
            x[i] = normalize(segmentValue, segmentLength, lb, ub);
        }

        return x;
    }

    public static double[] extractVariablesMultiRange(String chromosome, int numberOfVariables, double[] lb, double[] ub) {
        double[] x = new double[numberOfVariables];
        int segmentLength = chromosome.length() / numberOfVariables;


        // Iterate over segments and calculate fitness for each
        for (int i = 0; i < numberOfVariables; i++) {
            // Extract the segment from the chromosome
            int startIndex = i * segmentLength;
            int endIndex = (i + 1) * segmentLength;

            String segment = chromosome.substring(startIndex, endIndex);
            // Convert the segment to an integer value
            int segmentValue = Integer.parseInt(segment, 2);
            // Scaled value for each variable
            x[i] = normalize(segmentValue, segmentLength, lb[i], ub[i]);
        }

        return x;
    }


    public static double[] Schaffer1(String chromosome, int numberOfVariables, double lb, double ub, double[] weight) {

        double result = 0.0;
        double f1 = 0.0;
        double f2 = 0.0;
        double x = extractVariables(chromosome, numberOfVariables, lb, ub)[0];


        f1 = Math.pow(x, 2);

        f2 = Math.pow((x - 2), 2);


        result = (weight[0] * f1) + (weight[1] * f2);

        return new double[]{result, f1, f2};


    }
}