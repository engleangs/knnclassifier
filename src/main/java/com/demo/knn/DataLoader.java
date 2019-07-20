package com.demo.knn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Engleang
 *
 * Data laoder for loading text file of handwrinting into collection of SampleData object.
 * Using map to group its by folder name.
 *
 * **/
public class DataLoader {
    private String path;
    private final ConcurrentHashMap<String, List<SampleData>> data;


    public DataLoader(String path) {
        this.path = path;
        data = new ConcurrentHashMap<>();
    }

    private SampleData fromFileToData(String name, Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        if (lines.size() == 0) throw new RuntimeException("Could not find data in training file! " + path.toString());
        double[] training = new double[lines.size() * lines.get(0).length()];
        AtomicInteger atomicInteger = new AtomicInteger(0);
        lines.forEach(it -> {
            for (int i = 0; i < it.length(); i++) {
                training[atomicInteger.get() * it.length() + i] = Double.valueOf(String.valueOf(it.charAt(i)));
            }
            atomicInteger.incrementAndGet();
        });
        SampleData sampleData = new SampleData(name, training,path.getFileName().toString());
        return sampleData;


    }

    public void load() throws IOException {
        List<Path> directories = Files.list(Paths.get(path)).parallel()
                .filter(Files::isDirectory)
                .collect(Collectors.toList());
        directories. parallelStream().forEach(it -> {
            try {
                List<Path> files = Files.list(it).parallel().filter( Files::isRegularFile).collect(Collectors.toList());
                final String name = it.getFileName().toString();
                files.forEach(filePath -> {
                    try {
                        SampleData sampleData = fromFileToData(name, filePath);
                        if (!data.containsKey(name)) {
                            final List<SampleData> list = new ArrayList<>();
                            data.put(name, list);
                        }
                        data.get(name).add(sampleData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public ConcurrentHashMap<String, List<SampleData>> getData() {
        return data;
    }
}
