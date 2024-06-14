import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static double[] weight = {0.5, 0.5};
    static double fitness = 0.0;
    static double f1 = 0.0;
    static double f2 = 0.0;
    static double[] X = new double[3];
    static int numberOfVariables = 1;
    static double lb = -1000;
    static double ub = 1000;
    static double number;
    Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        int chromosomeLength = 18;
        int populationSize = 100;
        double crossoverRate = 0.85;
        double mutationRate = 0.03;
        int maxGeneration = 1000;

        Object[][] population = new Object[populationSize][5];

        // Initialize the population with chromosomes values
        for (int row = 0; row < population.length; row++) {
            int[] chromosome1 = new int[chromosomeLength];
            for (int gene = 0; gene < chromosomeLength; gene++) {
                if (0.5 < Math.random()) {
                    chromosome1[gene] = 1;
                } else {
                    chromosome1[gene] = 0;
                }
            }

            // Initialize the population with fitness values to each chromosome
            X = calcFitness(toString(chromosome1));
            fitness = X[0];
            f1 = X[1];
            f2 = X[2];


            // Update the values into population
            population[row][0] = chromosome1;
            population[row][1] = fitness;
            population[row][2] = f1;
            population[row][3] = f2;
        }

        System.out.println("**********************");


        int generation = 0;


        while (generation < maxGeneration) {

            printFittest(population, generation);
//            System.out.println("**********************");
//            printPopulation(population);
//            System.out.println("**********************");

            Object[][] crossoverPopulation;
            crossoverPopulation = sirSinglePointCrossover(population, crossoverRate, populationSize, chromosomeLength);
            copyPopulation(crossoverPopulation, population);


            Object[][] mutatePopulation;
            mutatePopulation = sirMutate(population, mutationRate, populationSize, chromosomeLength);
            copyPopulation(mutatePopulation, population);

            generation++;

        }
        printFittest(population, generation);
//        System.out.println("**********************");
//        printPopulation(population);
//        System.out.println("**********************");


    }

    public static double[] calcFitness(String chromosome) {

        return Schaffer1.Schaffer1(chromosome, numberOfVariables, lb, ub, weight);
    }

    /**
     * Single Point Cross Over Method Start
     **/
    public static Object[][] sirSinglePointCrossover(Object[][] population, double crossoverRate, int populationSize, int chromosomeLength) {
        Object[][] newPopulation = new Object[populationSize][4];

        Random rand = new Random();
        for (int populationIndex = 0; populationIndex < populationSize; populationIndex += 2) {
            int secondPopulationIndex = populationIndex + 1;

            int[] parent1 = sirTournamentSelectParent(population);
            int[] parent2 = sirTournamentSelectParent(population);

            int[] offspring1 = new int[chromosomeLength];
            int[] offspring2 = new int[chromosomeLength];

            int crossoverPoint = rand.nextInt(chromosomeLength); // Randomly select the crossover point

            if (crossoverRate > Math.random()) {
                for (int geneIndex = 0; geneIndex < chromosomeLength; geneIndex++) {
                    if (geneIndex < crossoverPoint) {

                        offspring1[geneIndex] = parent1[geneIndex];
                        offspring2[geneIndex] = parent2[geneIndex];
                    } else {
                        offspring1[geneIndex] = parent2[geneIndex];
                        offspring2[geneIndex] = parent1[geneIndex];
                    }
                }
            } else {
                offspring1 = parent1;
                offspring2 = parent2;
            }

            double[] x = calcFitness(toString(offspring1));


            double[] y = calcFitness(toString(offspring2));


            newPopulation[populationIndex][0] = offspring1;
            newPopulation[populationIndex][1] = x[0];
            newPopulation[populationIndex][2] = x[1];
            newPopulation[populationIndex][3] = x[2];

            newPopulation[secondPopulationIndex][0] = offspring2;
            newPopulation[secondPopulationIndex][1] = y[0];
            newPopulation[secondPopulationIndex][2] = y[1];
            newPopulation[secondPopulationIndex][3] = y[2];

        }

        return newPopulation;
    }

    /**END**/


    /**
     * Selecting Parent Using TournamentSelectParent Method start
     **/
    public static int[] sirTournamentSelectParent(Object[][] population) {

        Random rand = new Random();

        //rand.nextInt(population.length)
        int index1 = Math.abs((rand.nextInt() % (population.length - 1)));
        double parentFitness1 = (double) population[index1][1];
//        System.out.println(parentFitness1);
        int index2 = Math.abs((rand.nextInt() % (population.length - 1)));
        double parentFitness2 = (double) population[index2][1];
//        System.out.println(parentFitness2);
        if (parentFitness1 < parentFitness2) {
            return (int[]) population[index1][0];
        } else {
            return (int[]) population[index2][0];
        }
    }

    /**
     * END
     **/


    /**
     * Mutate Method start
     **/
    public static Object[][] sirMutate(Object[][] population, double mutationRate, int populationSize, int chromosomeLength) {
        Object[][] newPopulation = new Object[populationSize][4];

        Random rand = new Random();

        // Loop over current population
        for (int populationIndex = 0; populationIndex < populationSize; populationIndex++) {
            // Manually create a new array for the chromosome
            int[] originalChromosome = (int[]) population[populationIndex][0];
            int[] newChromosome = new int[chromosomeLength];

            // Copy the contents of the original chromosome to the new one using a loop
            for (int geneIndex = 0; geneIndex < chromosomeLength; geneIndex++) {
                newChromosome[geneIndex] = originalChromosome[geneIndex];
            }

            // Apply mutation
            for (int geneIndex = 0; geneIndex < chromosomeLength; geneIndex++) {
                // Does this gene need mutation?
                if (mutationRate > Math.random()) {
                    // Flip the gene
                    if (newChromosome[geneIndex] == 0) {
                        newChromosome[geneIndex] = 1;
                    } else {
                        newChromosome[geneIndex] = 0;
                    }
                }
            }

            // Calculate new fitness for the mutated chromosome
            double[] newFitness = calcFitness(toString(newChromosome));

            // Add the mutated individual to the new population
            newPopulation[populationIndex][0] = newChromosome;
            newPopulation[populationIndex][1] = newFitness[0];
            newPopulation[populationIndex][2] = newFitness[1];
            newPopulation[populationIndex][3] = newFitness[2];
        }

//        // Return mutated population
//        System.out.println("population after mutation");
//        printPopulation(newPopulation);
        return newPopulation;
    }


    /**
     * End
     **/


    // To print passed population
    public static void printPopulation(Object[][] population) {

        for (int i = 0; i < population.length; i++) {
            String x = toString((int[]) population[i][0]);
            System.out.print("x = ");
            System.out.print(Arrays.toString(Schaffer1.extractVariables(x, numberOfVariables, lb, ub)));

            System.out.print(", " + toString((int[]) population[i][0]));


            number = (double) population[i][2];
            System.out.print(", f1 = " + String.format("%.3f", number));
            number = (double) population[i][3];
            System.out.print(", f2 = " + String.format("%.3f", number));
            number = (double) population[i][1];
            System.out.println(", fitness: " + String.format("%.3f", number));

        }
    }

    public static void printFittest(Object[][] population, int generation) {
        int fittestIndex = 0;
        double bestFitness = (double) population[0][1];

        // Iterate through the population to find the fittest individual
        for (int i = 1; i < population.length; i++) {
            double currentFitness = (double) population[i][1];
            if (currentFitness < bestFitness) {
                fittestIndex = i;
                bestFitness = currentFitness;
            }
        }

        // Print the fittest individual
        System.out.print(generation + ") Best:");
        int[] fittestChromosome = (int[]) population[fittestIndex][0];

        System.out.print(" " + toString(fittestChromosome));
        String x = toString((int[]) population[fittestIndex][0]);
        System.out.print(", x = " + Arrays.toString(Schaffer1.extractVariables(x, numberOfVariables, lb, ub)));


        number = (double) population[fittestIndex][2];
        System.out.print(", f1: " + String.format("%.3f", number));
        number = (double) population[fittestIndex][3];
        System.out.print(", f2: " + String.format("%.3f", number));

        number = bestFitness;
        System.out.println(", Fitness: " + String.format("%.3f", number));
    }


    public static String getFittestString(Object[][] population, int generation) {
        int fittestIndex = 0;
        double bestFitness = (double) population[0][1];

        // Iterate through the population to find the fittest individual
        for (int i = 1; i < population.length; i++) {
            double currentFitness = (double) population[i][1];
            if (currentFitness < bestFitness) {
                fittestIndex = i;
                bestFitness = currentFitness;
            }
        }

        // Get the fittest individual
        int[] fittestChromosome = (int[]) population[fittestIndex][0];
        String fittestChromosomeString = toString(fittestChromosome);

        // Create the result string
        String result = generation + ") Best: ";
        result += fittestChromosomeString + ", ";
        number = bestFitness;
        result += "Fitness: " + String.format("%.3f", number);
        number = (double) population[fittestIndex][2];
        result += ", f1: " + String.format("%.3f", number);
        number = (double) population[fittestIndex][3];
        result += ", f2: " + String.format("%.3f", number);

        return result;
    }

    public static void copyPopulation(Object[][] source, Object[][] destination) {
        for (int i = 0; i < source.length; i++) {
            // Use the copyChromosome method to create a copy of the chromosome
            int[] chromosome = copyChromosome((int[]) source[i][0]);

            // Deep copy the fitness values
            double fitness1 = (double) source[i][1];
            double fitness2 = (double) source[i][2];
            double fitness3 = (double) source[i][3];

            // Copy the chromosome and fitness to the destination
            destination[i][0] = chromosome;
            destination[i][1] = fitness1;
            destination[i][2] = fitness2;
            destination[i][3] = fitness3;
        }
    }


    public static int[] copyChromosome(int[] original) {
        int[] copy = new int[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }


    public static String toString(int[] chromosome) {
        String output = "";
        for (int gene = 0; gene < chromosome.length; gene++) {
            output += chromosome[gene];
        }
        return output;
    }
}