package homework.dataprocessor;

import homework.model.Measurement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value

        Map<String, Double> jsonInput = new HashMap<String, Double>();

        /*for(Measurement measurement : data){
            jsonInput.put(measurement.getName(), measurement.getValue());

        }*/

        for(Measurement element : data){
          /*  double d = 0.0;
            double newValue = jsonInput.getOrDefault(element.getName(), element.getValue()) + element.getValue();
            jsonInput.put(element.getName(), newValue);*/

            if(jsonInput.isEmpty())
                jsonInput.put(element.getName(), element.getValue());
            else {
                for (Map.Entry<String, Double> entry : jsonInput.entrySet()) {
                    //System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
                    double sum = entry.getValue();
                    if (entry.getKey().equals(element.getName())) {
                        sum += element.getValue();
                        jsonInput.put(entry.getKey(), sum);
                        break;
                    }else{
                        jsonInput.put(element.getName(), element.getValue());
                        break;
                    }


                }
            }
        }

        TreeMap<String, Double> sorted = new TreeMap<String, Double>();
        sorted.putAll(jsonInput);

        return sorted;
    }
}
