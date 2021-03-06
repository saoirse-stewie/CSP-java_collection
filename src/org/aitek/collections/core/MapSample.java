package org.aitek.collections.core;

import org.aitek.collections.gui.Main;
import org.aitek.collections.gui.StatsPanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapSample extends CollectionSample implements PropertyChangeListener {

	private IdentityHashMap<Integer,Integer> identityhashmap;
	private WeakHashMap<Integer,Integer> weakhashmap;
	private ConcurrentHashMap<Integer,Integer> concurrenthashmap;
	private AbstractMap<Integer,Integer> abstractMap;
	private TreeMap<Integer, Integer> treeMap;
	private Task task;

	public MapSample(StatsPanel statsPanel, Main main) {

		super(statsPanel, main);
		COLLECTION_TYPES = 4;
		times = new long[COLLECTION_TYPES];
		identityhashmap = new IdentityHashMap<Integer,Integer>();
		weakhashmap= new WeakHashMap<Integer, Integer>();
		concurrenthashmap = new ConcurrentHashMap<Integer,Integer>();
		treeMap = new TreeMap<Integer, Integer>();
	}

	public HashSet<OperationType> getSupportedOperations() {

		HashSet<OperationType> set = new HashSet<OperationType>();

		set.add(OperationType.POPULATE);
		set.add(OperationType.REMOVE);
		set.add(OperationType.INSERT);
		set.add(OperationType.ITERATE);

		return set;
	}

	public void execute(OperationType operation) {

		this.currentOperation = operation;

		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		statusBar.updateProgressBar(task.getProgress());
	}

	private class Task extends SwingWorker<Void, Void> {

		private double mult;

		@Override
		public Void doInBackground() {

			mult = 100d / iterations;
			switch (currentOperation) {
				case POPULATE:
					times = fillMaps();
					statsPanel.setTimes("Populating", times);
				break;
				case REMOVE:
					times = removeFromMaps();
					statsPanel.setTimes("Removing existing elements", times);
				break;
				case INSERT:
					times = insertIntoMaps();
					statsPanel.setTimes("Inserting new elements", times);
				break;
				case ITERATE:
					times = iterateOnMaps();
					statsPanel.setTimes("Iterating keys on map", times);
				break;
			}

			return null;
		}

		@Override
		public void done() {

			main.setButtonsState();
			main.setReady();
		}

		private long[] fillMaps() {

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Filling map with " + getListFormattedSize() + " elements...");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				weakhashmap.clear();
				concurrenthashmap.clear();
				treeMap.clear();

				int keys[] = new int[listSize];
				int values[] = new int[listSize];

				for (int j = 0; j < getListSize(); j++) {
					keys[j] = (int) (Math.random() * listSize);
					values[j] = (int) (Math.random() * listSize);
				}

				long startingTime = System.nanoTime();
				for (int j = 0; j < getListSize(); j++) {
					weakhashmap.put(keys[j], values[j]);
				}
				times[0] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < getListSize(); j++) {
					concurrenthashmap.put(keys[j], values[j]);
				}
				times[1] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < getListSize(); j++) {
					treeMap.put(keys[j], values[j]);
				}
				times[2] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < getListSize(); j++) {
					identityhashmap.put(keys[j], values[j]);
				}
				times[3] += System.nanoTime() - startingTime;

				setProgress((int) (z * mult));
			}
			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}

			return times;
		}

		private long[] removeFromMaps() {

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Removing elements from map...");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				long startingTime = System.nanoTime();
				int[] toBeRemoved = new int[50];
				for (int j = 0; j < 50; j++) {
					toBeRemoved[j] = (int) (Math.random() * listSize);
				}

				for (int j = 0; j < 50; j++)
					weakhashmap.remove(toBeRemoved[j]);
				times[0] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 50; j++)
					concurrenthashmap.remove(toBeRemoved[j]);
				times[1] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 50; j++)
					treeMap.remove(toBeRemoved[j]);
				times[2] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 50; j++)
					identityhashmap.remove(toBeRemoved[j]);
				times[3] += System.nanoTime() - startingTime;

				setProgress((int) (z * mult));
			}
			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}
			System.out.println(identityhashmap.size());
			return times;
		}

		private long[] insertIntoMaps() {

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Inserting elements in map...");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				int keys[] = new int[listSize];
				int values[] = new int[listSize];

				for (int j = 0; j < getListSize(); j++) {
					keys[j] = (int) (Math.random() * listSize);
					values[j] = (int) (Math.random() * listSize);
				}

				long startingTime = System.nanoTime();
				for (int j = 0; j < 50; j++)
					weakhashmap.put(keys[j], values[j]);
				times[0] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 50; j++)
					concurrenthashmap.put(keys[j], values[j]);
				times[1] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 50; j++)
					treeMap.put(keys[j], values[j]);
				times[2] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				for (int j = 0; j < 50; j++)
					identityhashmap.put(keys[j], values[j]);
				times[3] += System.nanoTime() - startingTime;

				setProgress((int) (z * mult));
			}
			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}

			return times;
		}

		private long[] iterateOnMaps() {

			long[] times = new long[COLLECTION_TYPES];
			main.setWorking("Iterating on keys..");
			setProgress(0);

			for (int z = 0; z <= iterations; z++) {

				long startingTime = System.nanoTime();
				Iterator<Integer> iterator = weakhashmap.keySet().iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				times[0] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				iterator = concurrenthashmap.keySet().iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				times[1] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				iterator = treeMap.keySet().iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				times[2] += System.nanoTime() - startingTime;

				startingTime = System.nanoTime();
				iterator = identityhashmap.keySet().iterator();
				while (iterator.hasNext()) {
					iterator.next();
				}
				times[3] += System.nanoTime() - startingTime;

				setProgress((int) (z * mult));
			}

			for (int z = 0; z < COLLECTION_TYPES; z++) {
				times[z] = times[z] / iterations / 1000;
			}

			setProgress(100);

			return times;
		}

	}

	@Override
	public boolean isPopulated() {

		return identityhashmap.keySet().size() > 0;
	}

}
