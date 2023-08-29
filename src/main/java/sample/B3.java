package sample;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.ArrayList;
import java.util.List;

public class B3 {

    public static void main(String[] args) {
        // Đặt port cho webservice
        Spark.port(8080);

        // Định nghĩa route và handler cho API
        Spark.get("/prime", new PrimeNumberHandler());
    }

    // Implement handler cho API
    private static class PrimeNumberHandler implements Route {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            // Lấy giá trị param từ URL
            int n = Integer.parseInt(request.queryParams("n"));

            // Gọi hàm để tìm danh sách số nguyên tố
            List<Integer> primes = getPrimeNumbers(n);

            // Trả về danh sách số nguyên tố dưới dạng JSON
            return primes;
        }
    }

    // Hàm tìm danh sách số nguyên tố từ 0 đến n
    private static List<Integer> getPrimeNumbers(int n) {
        List<Integer> primes = new ArrayList<>();

        // Vòng lặp kiểm tra từng số từ 2 đến n
        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }
        return primes;
    }

    // Hàm kiểm tra số nguyên tố
    private static boolean isPrime(int number) {
        // Kiểm tra từ 2 đến căn bậc hai của số number
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}