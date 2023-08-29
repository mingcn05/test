//package sample.b1;
//
//import ch.qos.logback.classic.Level;
//import ch.qos.logback.classic.Logger;
//import ch.qos.logback.classic.LoggerContext;
//import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
//import ch.qos.logback.classic.spi.ILoggingEvent;
//import ch.qos.logback.core.FileAppender;
//import ch.qos.logback.core.rolling.RollingFileAppender;
//import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.slf4j.LoggerFactory;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class DataCompression {
//    private static final String API_URL = "http://news.admicro.vn:10002/api/realtime?domain=kenh14.vn";
//
//    private static final double THRESHOLD = 0.005; // 0.5%
//
//    private static final long CHECK_INTERVAL = 2000; // 2s
//    private static final long DEBUG_DELAY = 12000; // 12s
//    private static final long DEBUG_INTERVAL = 2000; // 2s
//
//    private static final long LOG_FILE_SIZE = 1024 * 1024; // 1MB
//    private static final int MAX_LOG_FILES = 20;
//    private static final long MAX_TOTAL_LOG_SIZE = 20 * LOG_FILE_SIZE; // 20MB
//
//    private static Timer timer;
//    private static volatile long lastUser;
//    private static volatile long currentTime;
//    private static volatile boolean debugMode;
//
//    public static void main(String[] args) {
//        setupLogger();
//
//        timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    long currentUser = getUserCount();
//                    if (currentUser > lastUser * (1 + THRESHOLD)) {
//                        logInfo(currentUser);
//                    } else {
//                        debugMode = true;
//                        lastUser = currentUser;
//                        scheduleDebugLog(currentUser);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 0, CHECK_INTERVAL);
//    }
//
//    private static void scheduleDebugLog(long userCount) {
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (debugMode) {
//                    logDebug(userCount);
//                    debugMode = false;
//                }
//            }
//        }, DEBUG_DELAY);
//    }
//
//    private static void logInfo(long userCount) {
//        Logger logger = (Logger) LoggerFactory.getLogger(DataCompression.class);
//        logger.setLevel(Level.INFO);
//        logger.info("User count: " + userCount);
//    }
//
//    private static void logDebug(long userCount) {
//        Logger logger = (Logger) LoggerFactory.getLogger(DataCompression.class);
//        logger.setLevel(Level.DEBUG);
//        logger.debug("User count: " + userCount);
//    }
//
//    private static long getUserCount() throws Exception {
//        URL url = new URL(API_URL);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//
//        int responseCode = connection.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//            reader.close();
//
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode jsonNode = mapper.readTree(response.toString());
//            return jsonNode.get("user").asLong();
//        }
//        return 0;
//    }
//
//    private static void setupLogger() {
//        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
//
//        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
//        encoder.setPattern("%date %level %msg%n");
//        encoder.setContext(loggerContext);
//        encoder.start();
//
//        FileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
//        appender.setFile("logs/compressed_data.log");
//
//        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<>();
//        rollingPolicy.setFileNamePattern("logs/compressed_data.%d{yyyy-MM-dd_HH}.log");
//        rollingPolicy.setParent(appender);
//        rollingPolicy.setMaxHistory(MAX_LOG_FILES);
//        rollingPolicy.setTotalSizeCap(MAX_TOTAL_LOG_SIZE);
//        rollingPolicy.setMaxFileSize(LOG_FILE_SIZE);
//        rollingPolicy.setContext(loggerContext);
//        rollingPolicy.start();
//
//        appender.setEncoder(encoder);
//        appender.setRollingPolicy(rollingPolicy);
//        appender.setContext(loggerContext);
//        appender.start();
//
//        Logger logger = (Logger) LoggerFactory.getLogger(DataCompression.class);
//        logger.setLevel(Level.INFO);
//        logger.addAppender(appender);
//    }
//}
