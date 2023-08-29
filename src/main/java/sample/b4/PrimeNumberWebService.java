package sample.b4;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.List;

public class PrimeNumberWebService {

    private static final CacheTTL<Integer, List<Integer>> cache = new CacheTTL<>(60, 5);

    public static void main(String[] args) {
        port(8080);

        get("/prime", (request, response) -> {
            int n = Integer.parseInt(request.queryParams("n"));
            List<Integer> primes = cache.get(n);
            if (primes == null) {
                primes = calculatePrimes(n);
                cache.put(n, primes);
            }

            return primes;
        });
    }

    public static List<Integer> calculatePrimes(int n) {
        List<Integer> primes = new ArrayList<>();

        if (n >= 2) {
            primes.add(2);
        }
        for (int i = 3; i <= n; i += 2) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }

        return primes;
    }

    public static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}