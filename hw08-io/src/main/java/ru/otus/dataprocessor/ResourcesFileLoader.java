package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final URL url;

    public ResourcesFileLoader(String fileName) {
        this.url = ResourcesFileLoader.class.getClassLoader().getResource(fileName);
    }

    @Override
    public List<Measurement> load() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(url, mapper.getTypeFactory().constructCollectionType(List.class, Measurement.class));
    }
}
