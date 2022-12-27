package net.sourceforge.plantuml.quantization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Uses k-means clustering for color quantization. This tends to yield good
 * results, but convergence can be slow. It is not recommended for large images.
 */
public final class KMeansQuantizer implements ColorQuantizer {
	public static final KMeansQuantizer INSTANCE = new KMeansQuantizer();

	private KMeansQuantizer() {
	}

	@Override
	public Set<QColor> quantize(Multiset<QColor> originalColors, int maxColorCount) {
		Map<QColor, Multiset<QColor>> clustersByCentroid = new LinkedHashMap<>();
		Set<QColor> centroidsToRecompute = getInitialCentroids(originalColors, maxColorCount);
		for (QColor centroid : centroidsToRecompute)
			clustersByCentroid.put(centroid, new HashMultiset<QColor>());

		for (QColor color : originalColors.getDistinctElements()) {
			final int count = originalColors.count(color);
			clustersByCentroid.get(color.getNearestColor(centroidsToRecompute)).add(color, count);
		}

		while (centroidsToRecompute.isEmpty() == false) {
			recomputeCentroids(clustersByCentroid, centroidsToRecompute);
			centroidsToRecompute.clear();

			Set<QColor> allCentroids = clustersByCentroid.keySet();
			for (QColor centroid : clustersByCentroid.keySet()) {
				Multiset<QColor> cluster = clustersByCentroid.get(centroid);
				for (QColor color : new ArrayList<>(cluster.getDistinctElements())) {
					QColor newCentroid = color.getNearestColor(allCentroids);
					if (newCentroid != centroid) {
						final int count = cluster.count(color);
						final Multiset<QColor> newCluster = clustersByCentroid.get(newCentroid);

						cluster.remove(color, count);
						newCluster.add(color, count);

						centroidsToRecompute.add(centroid);
						centroidsToRecompute.add(newCentroid);
					}
				}
			}
		}

		return clustersByCentroid.keySet();
	}

	private static void recomputeCentroids(Map<QColor, Multiset<QColor>> clustersByCentroid,
			Set<QColor> centroidsToRecompute) {
		for (QColor oldCentroid : centroidsToRecompute) {
			final Multiset<QColor> cluster = clustersByCentroid.get(oldCentroid);
			final QColor newCentroid = QColor.getCentroid(cluster);
			clustersByCentroid.remove(oldCentroid);
			clustersByCentroid.put(newCentroid, cluster);
		}
	}

	private static Set<QColor> getInitialCentroids(Multiset<QColor> originalColors, int maxColorCount) {
		// We use the Forgy initialization method: choose random colors as initial
		// cluster centroids.
		final List<QColor> colorList = new ArrayList<>(originalColors.getDistinctElements());
		Collections.shuffle(colorList);
		return new HashSet<>(colorList.subList(0, maxColorCount));
	}
}
