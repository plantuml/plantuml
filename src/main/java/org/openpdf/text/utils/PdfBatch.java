package org.openpdf.text.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 *  Utility class for executing collections of tasks concurrently using Java 21 virtual threads.
 */
public final class PdfBatch {

    private PdfBatch() {}

    public static final class BatchResult<T> {
        public final List<T> successes = new ArrayList<>();
        public final List<Throwable> failures = new ArrayList<>();

        public boolean isAllSuccessful() {
            return failures.isEmpty();
        }

        public int total() {
            return successes.size() + failures.size();
        }

        @Override
        public String toString() {
            return "BatchResult{" +
                    "successes=" + successes.size() +
                    ", failures=" + failures.size() +
                    ", total=" + total() +
                    '}';
        }
    }

    public static <T> BatchResult<T> run(Collection<? extends Callable<T>> tasks,
            Consumer<T> onSuccess,
            Consumer<Throwable> onFailure) {
           Objects.requireNonNull(tasks, "tasks");
        var result = new BatchResult<T>();
        if (tasks.isEmpty()) {
            return result;
        }

        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<T>> futures = tasks.stream().map(exec::submit).toList();
            for (Future<T> f : futures) {
                try {
                    T v = f.get();
                    result.successes.add(v);
                    if (onSuccess != null) {
                        onSuccess.accept(v);
                    }
                } catch (ExecutionException ee) {
                    Throwable cause = ee.getCause() != null ? ee.getCause() : ee;
                    result.failures.add(cause);
                    if (onFailure != null) {
                        onFailure.accept(cause);
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    result.failures.add(ie);
                    if (onFailure != null) {
                        onFailure.accept(ie);
                    }
                }
            }
        }
        return result;
    }
}
