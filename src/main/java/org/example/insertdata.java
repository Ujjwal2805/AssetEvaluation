package org.example;

import au.com.bytecode.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static au.com.bytecode.opencsv.CSVParser.*;

public class insertdata {
     static INDArray loadAndPreprocessNewData() {
        // Load your raw new data from a CSV file (you can adapt this for other data formats)
//         String csvFilePath = "new_data.csv"; // Update with your file path
//         try (CSVParser csvParser = CSVParser.parse(new File(csvFilePath), Charset.defaultCharset(), CSVFormat.DEFAULT)) {
//             List<INDArray> inputDataList = new ArrayList<>();
//
//             for (CSVRecord record : csvParser) {
                 // Assuming your data has columns "feature1", "feature2", etc.
//                 double feature1 = Double.parseDouble(record.get("feature1"));
//                 double feature2 = Double.parseDouble(record.get("feature2"));
//                 // Add more feature columns as needed
//
//                 // Create an INDArray row for each data point
//                 INDArray dataPoint = Nd4j.create(new double[]{feature1, feature2 /* add more features */});
//                 inputDataList.add(dataPoint);
//             }
//
//            // Stack the rows into a 2D INDArray
//            return Nd4j.vstack(inputDataList);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null; // Handle loading error


         String csvFilePath = "resource/TestData.csv"; // Update with your file path

         try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(csvFilePath))
                 .withSkipLines(1) // Skip header row if present
                 .build()) {
             List<String[]> data = csvReader.readAll();

             // Convert the loaded data into an INDArray
             INDArray inputData = Nd4j.create(data.size(), data.get(0).length);

             for (int i = 0; i < data.size(); i++) {
                 String[] row = data.get(i);
                 for (int j = 0; j < row.length; j++) {
                     inputData.putScalar(i, j, Double.parseDouble(row[j]));
                 }
             }

             return inputData;
         } catch (IOException | CsvException e) {
             e.printStackTrace();
         }

         return null;
    }
}
