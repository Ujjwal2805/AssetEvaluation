package org.example;



import au.com.bytecode.opencsv.CSVParser;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.example.insertdata.loadAndPreprocessNewData;

public class App
{
        public static void main(String[] args) throws Exception {
            // Define model hyperparameters
            int numInputs = 8;
            int numHidden = 64;
            int numOutputs = 1;
            double learningRate = 0.01;
            int numEpochs = 100;

            // Create and configure a neural network model
            MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                    .seed(123) // Random seed for reproducibility
                    .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                    .weightInit(WeightInit.XAVIER)
                    .updater(org.nd4j.linalg.learning.config.Sgd.builder().learningRate(learningRate).build())
                    .list()
                    .layer(0, new DenseLayer.Builder()
                            .nIn(numInputs)
                            .nOut(numHidden)
                            .activation(Activation.RELU)
                            .build())
                    .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                            .nIn(numHidden)
                            .nOut(numOutputs)
                            .activation(Activation.IDENTITY)
                            .build())
                    .build();

            // Create the model
            MultiLayerNetwork model = new MultiLayerNetwork(config);
            model.init();

            // Load and preprocess your training data
            RecordReader recordReader = new CSVRecordReader();
            recordReader.initialize(new FileSplit(new File("resource/Bengaluru_House_Data.csv")));
            int labelIndex=8,  batchSize = 110;

            int numClasses=3;
            DataSetIterator dataIterator = new RecordReaderDataSetIterator(recordReader, batchSize, labelIndex, numClasses);

            // Train the model
            for (int epoch = 0; epoch < numEpochs; epoch++) {
                model.fit(dataIterator);
            }

            // Save the trained model
            model.save(new File("NewModel_model.zip"));

            // Make predictions (you'll need to load and preprocess your test data)
            // INDArray input = /* Preprocessed input data for prediction */;
            // INDArray output = model.output(input);

            // You can use 'output' to make predictions based on your test data

            // Load your trained model
            MultiLayerNetwork models = new MultiLayerNetwork(model.getLayerWiseConfigurations());

           // String modelFilePath="C:\\Users\\Ujjwal\\AppData\\Local\\Temp\\archetypetmp\\AssetEvaluation\"



            // Load and preprocess the new data you want to predict
            //INDArray newData = new int[]{21, 23, 12, 31, 12, 31};

            insertdata obj=new insertdata();

        INDArray newData=insertdata.loadAndPreprocessNewData();

            // Make predictions
            INDArray predictions = model.output(newData);

            // Process the predictions as needed
            // For example, if it's regression, you have the predicted values in 'predictions'.
            // If it's classification, you might need to post-process the predictions to get class labels.

            // Print or use the predictions
            System.out.println("Predictions: " + predictions);

//            private static MultiLayerNetwork loadModel(String modelFilePath) {
//                try {
//                    // Load the model from the specified file
//                    return ModelSerializer.restoreMultiLayerNetwork(modelFilePath);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                   return; // Handle loading error
//                }
        }
}
