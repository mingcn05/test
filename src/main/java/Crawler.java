import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Crawler {
    public static void main(String[] args) {
        String url = "http://dantri.com.vn";
        try {
            Document doc = Jsoup.connect(url).get();
            String html = doc.html();

            // Loại bỏ các thẻ html
            String text = Jsoup.parse(html).text();

            // Tạo tên file dựa trên thời gian hiện tại
            String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")) + ".txt";

            // Ghi nội dung vào file
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(text);
            writer.close();

            System.out.println("Dữ liệu đã được ghi vào file " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
