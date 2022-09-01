package cz.cvut.fel.pjv;

import java.util.Scanner;

/**
   Read input from user (stdin), and computes mean.
   Checks if input is valid, ignores chars.
**/

public class Lab02 {

   private Scanner sc = new Scanner(System.in);

   public void start(String[] args) {
      double[] arr = new double[10];
      int i = 0;
      int sum = 0;
      double mean = 0;
      while (sc.hasNextDouble()) {
         if (i == 9) {
            System.out.println("10 numbers have been read");
            mean = (double) sum / 9;
            System.out.printf("Mean = %.3f\n", mean);
            break;
         }
         arr[i] = sc.nextDouble();
         sum += arr[i];
         i++;

      }
      int count = 0;
      if (arr.length > 0) {
         for (double j : arr) {
            System.out.printf("arr[%d] = %f\n", count, j);
            count++;
         }
      }

   }
}

/* end of Lab02.java */
