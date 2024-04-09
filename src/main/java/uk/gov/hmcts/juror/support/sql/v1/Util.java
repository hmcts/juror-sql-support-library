package uk.gov.hmcts.juror.support.sql.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class Util {

    public static <T> T getWeightedRandomItem(Map<T, Integer> weightMap) {
        int totalWeight = weightMap.values().stream().mapToInt(Integer::intValue).sum();
        if (totalWeight == 0) {
            return new RandomFromCollectionGeneratorImpl<>(weightMap.keySet()).generate();
        }

        int randomWeight = RandomGenerator.nextInt(1, totalWeight + 1);
        int currentWeight = 0;
        for (Map.Entry<T, Integer> entry : weightMap.entrySet()) {
            currentWeight += entry.getValue();
            if (randomWeight <= currentWeight) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Weight map is empty");
    }

    public static <T> List<List<T>> getBatches(List<T> collection, int batchSize) {
        List<List<T>> batches = new ArrayList<>();
        for (int i = 0; i < collection.size(); i += batchSize) {
            batches.add(collection.subList(i, Math.min(i + batchSize, collection.size())));
        }
        return batches;
    }

    public static <T> void batchSave(CrudRepository<T, ?> repository, List<T> items, int batchSize) {
        if (items.isEmpty()) {
            return;
        }
        List<List<T>> batches = Util.getBatches(items, batchSize);
        log.info("Saving {} items of type {} in {} batches of size {}",
            items.size(), items.get(0).getClass().getSimpleName(),
            batches.size(), batchSize);
        AtomicLong atomicLong = new AtomicLong(batches.size());
        batches
            .parallelStream()
            .forEach(ts -> {
                repository.saveAll(ts);
                log.info("Saved batch of {} items {} batches left", ts.size(), atomicLong.decrementAndGet());
            });
    }

    public static String getRandomItem(Collection<String> from) {
        return getRandomItem(from, List.of());
    }

    public static <T> T getRandomItem(Collection<T> from, Collection<T> excluding) {
        List<T> items = new ArrayList<>(from);
        items.removeAll(excluding);
        return new RandomFromCollectionGeneratorImpl<>(items).generate();
    }
}
