package homework.dataprocessor;

import homework.model.Measurement;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileLoader implements Loader {

    public String fileName;

    public FileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() throws FileNotFoundException {
        //читает файл, парсит и возвращает результат
        File path = new File(getClass().getClassLoader().getResource(fileName).getFile());
        String filePathStr = path.getAbsolutePath();

        List measurementArrayList = new ArrayList<Measurement>();

        FileInputStream fis = new FileInputStream(filePathStr);
        JsonReader jsonReader = Json.createReader(fis);

        JsonArray jsonArray = jsonReader.readArray();

        for (JsonObject result : jsonArray.getValuesAs(JsonObject.class)) {
            String name = result.getString("name");
            double value = Double.parseDouble(result.get("value").toString());
            Measurement measurement = new Measurement(name, value);
            measurementArrayList.add(measurement);
        }

        return measurementArrayList;
    }
}
