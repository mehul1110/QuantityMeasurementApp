package com.bridgelabz.repository;

import com.bridgelabz.entity.QuantityMeasurementEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {
    private static final Logger log = LoggerFactory.getLogger(QuantityMeasurementCacheRepository.class);
    private static QuantityMeasurementCacheRepository instance;
    private final List<QuantityMeasurementEntity> cache;
    private static final String FILE_NAME = "quantity_measurements.ser";

    private QuantityMeasurementCacheRepository() {
        this.cache = new ArrayList<>();
        loadFromDisk();
    }

    public static synchronized QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository();
        }
        return instance;
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        cache.add(entity);
        saveToDisk(entity);
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return new ArrayList<>(cache);
    }

    private void saveToDisk(QuantityMeasurementEntity entity) {
        File file = new File(FILE_NAME);
        boolean append = file.exists();
        try (FileOutputStream fos = new FileOutputStream(file, append);
             ObjectOutputStream oos = append ? new AppendableObjectOutputStream(fos) : new ObjectOutputStream(fos)) {
            oos.writeObject(entity);
            log.info("Saved measurement to cache file: {}", entity.getOperationType());
        } catch (IOException e) {
            log.error("Could not save to disk: {}", e.getMessage());
        }
    }

    private void loadFromDisk() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                try {
                    QuantityMeasurementEntity entity = (QuantityMeasurementEntity) ois.readObject();
                    cache.add(entity);
                } catch (EOFException e) {
                    break;
                }
            }
            log.info("Loaded {} measurements from cache file", cache.size());
        } catch (IOException | ClassNotFoundException e) {
            log.warn("Could not load from disk. History might be incomplete: {}", e.getMessage());
        }
    }

    private static class AppendableObjectOutputStream extends ObjectOutputStream {
        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}
