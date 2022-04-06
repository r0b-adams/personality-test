
// This application prompts for an input file given by the user,
// composed of names and their answers to the Keirsey Temperament Sorter.
// For each set of answers, the totals are tallied for either
// side of four dimensions, a percentage of answers toward one side
// is calculated, and a dimension of personality is determined.
// The name of the subject, the percentage of one side of the personality dimension,
// and the determined personality type are then printed to an output file given by the user

import java.util.*;
import java.io.*;

public class Personality {
   public static final int TEST_DIMENSIONS = 4;

   public static void main(String[] args) throws FileNotFoundException {
      Scanner console = new Scanner(System.in);

      intro();

      Scanner input = getInput(console);
      PrintStream output = getOutput(console);

      while (input.hasNext()) {
         int[] aCount = new int[TEST_DIMENSIONS];
         int[] bCount = new int[TEST_DIMENSIONS];
         String name = input.nextLine();
         String answers = input.nextLine();

         getCounts(answers, aCount, bCount);
         int[] bPercentage = getPercentage(aCount, bCount);
         char[] type = getPersonality(bPercentage);
         String personalityType = typeToString(type);
         printToFile(output, name, bPercentage, personalityType);
      }
   }

   // prints an introduction
   public static void intro() {
      System.out.println("This program processes a file of answers to the");
      System.out.println("Keirsey Temperament Sorter.  It converts the");
      System.out.println("various A and B answers for each person into");
      System.out.println("a sequence of B-percentages and then into a");
      System.out.println("four-letter personality type.");
      System.out.println();
   }

   // prompts user for the input file to scan
   // @param console : user input
   // @return        : new Scanner of file given by user
   public static Scanner getInput(Scanner console) throws FileNotFoundException {
      System.out.print("input file name? ");
      return new Scanner(new File(console.nextLine()));
   }

   // prompts user for output file to print to
   // @param console : user input
   // @return        : new PrintStream of file given by user
   public static PrintStream getOutput(Scanner console) throws FileNotFoundException {
      System.out.print("output file name? ");
      return new PrintStream(console.nextLine());
   }

   // tallies the number of A and B answers in each dimension
   // @param responses : answers from input file
   // @param aCounts   : total "A" answers in each dimension
   // @param bCounts   : total "B" answers in each dimension
   public static void getCounts(String responses, int[] aCounts, int[] bCounts) {
      responses = responses.toUpperCase();

      //examines responses in 10 groups of 7 questions each
      for (int group = 0; group < responses.length() / 7; group++) {
         for (int question = 0; question < 7; question++) {
            int responseIndex = 7 * group + question;
            char currentResponse = responses.charAt(responseIndex);

            // sets the index of the counter array
            int countIndex = (2 * question + 2) / TEST_DIMENSIONS;
            if (currentResponse == 'A') {
               aCounts[countIndex]++;
            } else if (currentResponse == 'B') {
               bCounts[countIndex]++;
            }
         }
      }
   }

   // calculates a percentage of 'B' answers out of total answers in each dimension
   // @param aCounts : 'A' answer total in a dimension
   // @param bCounts : 'B' answer total in a dimension
   // @return        : array of percentages of "B" answers per dimension
   public static int[] getPercentage (int[] aCounts, int[] bCounts) {
      int[] bPercentage = new int[TEST_DIMENSIONS];

      for (int i = 0; i < TEST_DIMENSIONS; i++) {
         double percentage = (double) (bCounts[i]) / (aCounts[i] + bCounts[i]) * 100;
         bPercentage[i] = (int) Math.round(percentage);
      }
      return bPercentage;
   }

   // determines personality type from percentages given
   // @param bPercent : percentage of b answers ine each dimension
   // @return         : array that represents personality type
   public static char[] getPersonality(int[] bPercent) {
      char[][] dimensionTypes = {{'E', 'S', 'T', 'J'}, {'I', 'N', 'F', 'P'}};
      char[] personalityType = new char[TEST_DIMENSIONS];

      for (int i = 0; i < TEST_DIMENSIONS; i++) {
         if (bPercent[i] < 50) {
            personalityType[i] = dimensionTypes[0][i];
         } else if (bPercent[i] > 50) {
            personalityType[i] = dimensionTypes[1][i];
         } else {
            personalityType[i] = 'X';
         }
      }
      return personalityType;
   }

   // reformats given personality type
   // @param type : array that represents personality type
   // @return     : personality type reformatted to string
   public static String typeToString(char[] type) {
      String personalityType = "";

      for (int i = 0; i < TEST_DIMENSIONS; i++) {
         personalityType += type[i];
      }
      return personalityType;
   }

   // prints data to output file
   // @param output   : the file to write to
   // @param name     : name of the current subject
   // @param bPercent : percentage of "B" answers per dimension
   // @param type     : representation of personality type
   public static void printToFile(PrintStream output, String name, int[] bPercent, String type)
           throws FileNotFoundException {
      output.println(name + ": " + bPercent + " = " + type);
   }
}