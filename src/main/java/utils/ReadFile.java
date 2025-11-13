package utils;

import model.EmailData;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    String path = "datasets/train.csv";
    List<EmailData> dataList = new ArrayList<>();

    public void readFromPath() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
        String lines;
        while ((lines = bufferedReader.readLine()) != null){
            if (lines.trim().isEmpty()) continue;
            String[] parts = lines.split(",");
            try {
                int featureFree = Integer.parseInt(parts[0]);
                int featureStrangeLink = Integer.parseInt(parts[1]);
                int featureUpperCase = Integer.parseInt(parts[2]);
                String label = parts[parts.length - 1];
                dataList.add(new EmailData(label, featureFree, featureStrangeLink, featureUpperCase));
            } catch (NumberFormatException e){
                System.err.println("Lỗi parse số ở dòng: " + lines);
            }
        }
    }
}
