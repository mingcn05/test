package sample.b5;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainApp {

    private final Cache<Integer, List<Integer>> primeCache;

    public MainApp() {
        primeCache = CacheBuilder.newBuilder()
                .expireAfterWrite(20, TimeUnit.SECONDS)
                .removalListener(notification -> System.out.println("Cache expired: " + notification.getKey()))
                .build();
    }

    public void start() {
        Spark.get("/prime", (request, response) -> handlePrimeRequest(request, response));
    }

    private String handlePrimeRequest(Request request, Response response) {
        int n = Integer.parseInt(request.queryParams("n"));

        List<Integer> primes = primeCache.getIfPresent(n);
        if (primes == null) {
            primes = generatePrimes(n);
            primeCache.put(n, primes);
        }

        return primes.toString();
    }

    private List<Integer> generatePrimes(int n) {
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }
        return primes;
    }

    private boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.start();
    }
}
