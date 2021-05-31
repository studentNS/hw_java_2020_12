package homework;

import homework.dataprocessor.FileLoader;
import homework.dataprocessor.ProcessorAggregator;
import homework.model.Measurement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//просто тестовый класс для понимания теста
public class App16 {

    public static void main(String[] args) throws FileNotFoundException {

        //given
        var inputDataFileName = "inputData.json";

        var loader = new FileLoader(inputDataFileName);
        List measurementArrayList = new ArrayList<Measurement>();
        measurementArrayList = loader.load();

        for (int i = 0; i < measurementArrayList.size(); i++) {
            System.out.println(measurementArrayList.get(i));

        }

        ProcessorAggregator agg = new ProcessorAggregator();
        Map<String, Double> t = agg.process(measurementArrayList);

        for (Map.Entry<String, Double> entry : t.entrySet()) {
            System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());

        }

    }

}
