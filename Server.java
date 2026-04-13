import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Properties;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class Server {
    public static void main(String[] args) throws IOException {
        // Load configuration
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
        }

        String env = props.getProperty("env", "development");
        int port = Integer.parseInt(props.getProperty("server.port", "8080"));

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHandler(env));
        server.setExecutor(null);
        System.out.println("Server started in " + env + " environment on port " + port);
        server.start();
    }

    static class MyHandler implements HttpHandler {
        private final String env;

        public MyHandler(String env) {
            this.env = env;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Hello from Java Server! Running in " + env + " environment.";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
