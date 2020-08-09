import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;

public class Main {
    private static final String URI = "https://api.nasa.gov/planetary/apod?api_key=3FZljgdRlY36R8QClKnFUB5apD7IxIqmL3O88Y5Y";

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        CloseableHttpResponse response = httpResponse(httpClient, URI);
        ObjectMapper mapper = new ObjectMapper();
        JSONData jsonData = mapper.readValue(response.getEntity().getContent(), JSONData.class);
        String imageURI = jsonData.getUrl();

        CloseableHttpResponse response2 = httpResponse(httpClient, imageURI);
        File image = new File("src\\main\\resources\\Nucleosynthesis2Image.jpg");
        if (!image.exists()) {
            image.createNewFile();
        }

        writeData(response2, image);
    }

    static CloseableHttpResponse httpResponse(CloseableHttpClient httpClient, String uri) {
        CloseableHttpResponse response = null;
        HttpGet requst = new HttpGet(uri);
        try {
            response = httpClient.execute(requst);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    static void writeData(CloseableHttpResponse response, File file) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
        FileOutputStream fos = new FileOutputStream(file);

        int ch;
        while ((ch = bis.read()) != -1) {
            fos.write(ch);
        }
        bis.close();
        fos.flush();
        fos.close();
    }
}
