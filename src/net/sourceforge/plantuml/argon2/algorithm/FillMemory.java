/* 	This file is taken from
	https://github.com/andreas1327250/argon2-java

	Original Author: Andreas Gadermaier <up.gadermaier@gmail.com>
 */
package net.sourceforge.plantuml.argon2.algorithm;

import static net.sourceforge.plantuml.argon2.Constants.ARGON2_SYNC_POINTS;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.sourceforge.plantuml.argon2.model.Instance;
import net.sourceforge.plantuml.argon2.model.Position;

public class FillMemory {

    public static void fillMemoryBlocks(Instance instance) {
        if (instance.getLanes() == 1) {
            fillMemoryBlockSingleThreaded(instance);
        } else {
            fillMemoryBlockMultiThreaded(instance);
        }
    }

    private static void fillMemoryBlockSingleThreaded(Instance instance) {
        for (int i = 0; i < instance.getIterations(); i++) {
            for (int j = 0; j < ARGON2_SYNC_POINTS; j++) {
                Position position = new Position(i, 0, j, 0);
                FillSegment.fillSegment(instance, position);
            }
        }
    }

    private static void fillMemoryBlockMultiThreaded(final Instance instance) {

        ExecutorService service = Executors.newFixedThreadPool(instance.getLanes());
        List<Future<?>> futures = new ArrayList<Future<?>>();

        for (int i = 0; i < instance.getIterations(); i++) {
            for (int j = 0; j < ARGON2_SYNC_POINTS; j++) {
                for (int k = 0; k < instance.getLanes(); k++) {

                    final Position position = new Position(i, k, j, 0);

                    Future future = service.submit(new Runnable() {
                        @Override
                        public void run() {
                            FillSegment.fillSegment(instance, position);
                        }
                    });

                    futures.add(future);
                }

                joinThreads(instance, futures);
            }
        }

        service.shutdownNow();
    }

    private static void joinThreads(Instance instance, List<Future<?>> futures) {
        try {
            for (Future<?> f : futures) {
                f.get();
            }
        } catch (InterruptedException e) {
            instance.clear();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            instance.clear();
            throw new RuntimeException(e);
        }
    }
}
