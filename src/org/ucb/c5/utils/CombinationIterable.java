package org.ucb.c5.utils;

/**
 * Created by Daniel on 4/6/2019.
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

//From https://codereview.stackexchange.com/questions/119887/combination-generator-in-java-2nd-iteration
public class CombinationIterable<T> implements Iterable<List<T>> {

    private final List<T> allElements;
    private final int combinationSize;

    public CombinationIterable(Collection<T> allElements, int combinationSize) {
        this.allElements = new ArrayList<>(allElements);
        this.combinationSize = combinationSize;
    }

    @Override
    public Iterator<List<T>> iterator() {
        return new CombinationIterator<>(allElements, combinationSize);
    }

    private static final class CombinationIterator<T>
            implements Iterator<List<T>> {

        private final List<T> allElements;
        private final int[] indices;
        private List<T> nextCombination;
        private int currentCombinationSize;

        CombinationIterator(List<T> allElements, int currentCombinationSize) {
            this.allElements = new ArrayList<>(allElements);
            this.indices = new int[allElements.size()];

            if (!allElements.isEmpty()) {
                // Create the first combination.
                this.currentCombinationSize = currentCombinationSize;
                nextCombination = new ArrayList<>(currentCombinationSize);
                for(int i = 0; i < currentCombinationSize; i++){
                    nextCombination.add(allElements.get(i));
                    indices[i] = i;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return nextCombination != null;
        }

        @Override
        public List<T> next() {
            if (nextCombination == null) {
                throw new NoSuchElementException("No combinations left.");
            }

            List<T> combination = nextCombination;
            generateNextCombination();
            return combination;
        }

        private void loadCombination() {
            List<T> combination = new ArrayList<>(currentCombinationSize);

            for (int i = 0; i < currentCombinationSize; ++i) {
                combination.add(allElements.get(indices[i]));
            }

            this.nextCombination = combination;
        }

        private void generateNextCombination() {
            if (indices[currentCombinationSize - 1] < indices.length - 1) {
                indices[currentCombinationSize - 1]++;
                loadCombination();
                return;
            }

            for (int i = currentCombinationSize - 2; i >= 0; --i) {
                if (indices[i] < indices[i + 1] - 1) {
                    indices[i]++;

                    for (int j = i + 1; j < currentCombinationSize; ++j) {
                        indices[j] = indices[j - 1] + 1;
                    }

                    loadCombination();
                    return;
                }
            }

            /*++currentCombinationSize;

            if (currentCombinationSize > indices.length) {
                this.nextCombination = null;
                return;
            }

            for (int i = 0; i < currentCombinationSize; ++i) {
                indices[i] = i;
            }

            loadCombination();*/
            this.nextCombination = null;
        }
    }

    public static void main(String[] args) {
        List<String> all = new ArrayList<>();

        all.add("A");
        all.add("B");
        all.add("C");
        all.add("D");
        all.add("E");
        all.add("F");
        all.add("G");

        int row = 1;

        for (List<String> combination : new CombinationIterable<>(all, 3)) {
            System.out.printf("%2d: %s\n", row++, combination);
        }
    }
}