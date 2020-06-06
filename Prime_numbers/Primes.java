/*
 * Check for prime numbers
 */

package primes;
/**
 * @author ccnelson 2020
 */
public class Primes
{
    public static void main(String[] args) 
    {
        for (int i = 0; i < 101; i++)
        {
            if (IsPrime(i))
            {
                System.out.println(i);
            }
        }
    }
    
    static boolean IsPrime(int n)
    {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        double boundary = java.lang.Math.sqrt(n);
        for (int i = 3; i <= boundary; i += 2)
        {
            if (n % i == 0) return false;
        }
        return true;
    }
}
