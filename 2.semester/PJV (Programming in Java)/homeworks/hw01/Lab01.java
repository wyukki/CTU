import java.text.DecimalFormat;
import java.util.Scanner;

/**
    Simple calculator, that takes input from user (operation and two numbers).
    Supports those operations: addition, substraciton, multiplication and division.
    Programm checks, there is an error, i.e. division by zero.
    In the end, user should give number of decimal places.
    Messages are in czech due to evaluator.
 **/

public class Lab01 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double num1, num2;
        double result;
        char operation;
        System.out.println("Vyber operaci (1-soucet, 2-rozdil, 3-soucin, 4-podil):"); // Choose the operation (1-addition, 2-substraction, 3-multiplication, 4-division)
        int oper = sc.nextInt();
        switch (oper) {
            case 1:
                operation = '+';
                System.out.println("Zadej scitanec: "); // Enter number
                num1 = sc.nextDouble();
                System.out.println("Zadej scitanec: "); // Enter number
                num2 = sc.nextDouble();
                result = num1 + num2;
                break;

            case 2:
                operation = '-';
                System.out.println("Zadej mensenec: "); // Enter minuend
                num1 = sc.nextDouble();
                System.out.println("Zadej mensitel: "); // Enter subtrahend
                num2 = sc.nextDouble();
                result = num1 - num2;
                break;

            case 3:
                operation = '*';
                System.out.println("Zadej cinitel: "); // Enter factor
                num1 = sc.nextDouble();
                System.out.println("Zadej cinitel: "); // Enter factor
                num2 = sc.nextDouble();
                result = num1 * num2;
                break;

            case 4:
                System.out.println("Zadej delenec: "); // Enter first number
                num1 = sc.nextDouble();
                System.out.println("Zadej delitel: "); // Enter second number
                num2 = sc.nextDouble();
                if (num2 == 0.0) {
                    System.err.println("Pokus o deleni nulou!"); // Division by zero
                    sc.close();
                    return;
                }
                operation = '/';
                result = num1 / num2;
                break;

            default:
                System.err.println("Chybna volba!"); // Wrong choice
                sc.close();
                return;
        }
        System.out.println("Zadej pocet desetinnych mist: "); // Enter count of decimal numbers
        int decimal_numb = sc.nextInt();

        if (decimal_numb < 0) {
            System.err.println("Chyba - musi byt zadane kladne cislo!"); // Error - positive number should be given
            sc.close();
            return;
        }

        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(decimal_numb);
        df.setMaximumFractionDigits(decimal_numb);
        System.out.println(
                df.format(num1) + " " + operation + " " + df.format(num2) + " = " + df.format(result));
        sc.close();
    }
}
