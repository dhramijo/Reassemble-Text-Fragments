package fragment.submissions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JonadDhrami {

    private static final String SEPARATOR = ";";

    public static void main(String[] args) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
            in.lines().map(JonadDhrami::reassemble).forEach(System.out::println);
        }

    }

    /**
     * Given a line of text which contains text fragments separated by a semicolon, ';'.
     * Look for the pair of fragments with the maximum overlap to merge them.
     * Repeat the process until only one fragment remains.
     */
    public static String reassemble(String text) {
        List<String> fragments = new ArrayList<String>(Arrays.asList(text.split(SEPARATOR)));

        int pendingFragments = fragments.size();

        while (pendingFragments > 1) {

            List<String> pairWithMaxOverlap = getPairWithMaxOverlap(fragments);

            if (pairWithMaxOverlap != null && pairWithMaxOverlap.size() == 2) {

                String reassemblyText = reassembleTwoFragments(pairWithMaxOverlap.get(0), pairWithMaxOverlap.get(1));
                fragments.removeAll(pairWithMaxOverlap);
                fragments.add(reassemblyText);

            }

            // infinite loops checking
            if (pendingFragments == fragments.size()) {
                break;
            }

            pendingFragments = fragments.size();
        }

        return fragments.get(0);
    }

    /**
     * Given a line of text which contains text fragments separated by a semicolon, ';'.
     * Return the pair of fragments with the maximum overlap to merge them.
     *
     * @param fragments to reassemble
     *
     * @return Pair of fragments with the maximum overlap
     *
     */
    public static List<String> getPairWithMaxOverlap(List<String> fragments) {

        HashMap<Integer, List<String>> overlapNumberPerFragment = new HashMap<Integer, List<String>>();

        for (String fragment : fragments) {
            // Scrolls through each item in the list, with unprocessed items
            for (int i = fragments.indexOf(fragment) + 1; i < fragments.size(); i++) {

                String nextFragment = fragments.get(i);
                overlapNumberPerFragment.put(calculateOverlapsNumberForTwoFragments(fragment, nextFragment), Arrays.asList(fragment, nextFragment));
            }
        }

        Integer maximumOverlap = overlapNumberPerFragment.keySet().stream().mapToInt(Integer::valueOf).max().orElse(0);

        return overlapNumberPerFragment.get(maximumOverlap);
    }

    /**
     * Given two text fragments calculates the maximum overlap to merge them.
     *
     * @param firstFragment to compare
     * @param secondFragment to compare
     *
     * @return Maximum overlap between both fragments
     *
     */
    public static int calculateOverlapsNumberForTwoFragments(String firstFragment, String secondFragment) {

        // Case "ABCDEF" and "BCDE"
        if (firstFragment.contains(secondFragment)) {
            return secondFragment.length();
        } else if (secondFragment.contains(firstFragment)) {
            return firstFragment.length();
        }

        boolean isFirstShortestThanSecond = firstFragment.length() < secondFragment.length();
        int shortestFragmentSize = isFirstShortestThanSecond ? firstFragment.length() : secondFragment.length();
        String shortestFragment = isFirstShortestThanSecond ? firstFragment : secondFragment;
        String largestFragment = !isFirstShortestThanSecond ? firstFragment : secondFragment;

        // Case "ABCDEF" - "DEFG" and "ABCDEF" - "XYZABC"
        for (int index = 0; index < shortestFragmentSize; index++) {

            if (largestFragment.startsWith(shortestFragment.substring(index))
                    || largestFragment.endsWith(shortestFragment.substring(0, shortestFragmentSize - index))) {

                return shortestFragmentSize - index;
            }

        }

        // Case "ABCDEF" and "XCDEZ"
        return 0;
    }

    /**
     * Given two text fragments, return reassembled text.
     */
    public static String reassembleTwoFragments(String firstFragment, String secondFragment) {

        // To reassemble the case "ABCDEF" and "BCDE"
        if (firstFragment.contains(secondFragment)) {
            return firstFragment;
        } else if (secondFragment.contains(firstFragment)) {
            return secondFragment;
        }

        boolean isFirstShortestThanSecond = firstFragment.length() < secondFragment.length();
        int shortestFragmentSize = isFirstShortestThanSecond ? firstFragment.length() : secondFragment.length();
        String shortestFragment = isFirstShortestThanSecond ? firstFragment : secondFragment;
        String largestFragment = !isFirstShortestThanSecond ? firstFragment : secondFragment;

        // To reassemble the case "ABCDEF" - "DEFG" and the case "ABCDEF" - "XYZABC"
        for (int index = 0; index < shortestFragmentSize; index++) {

            if (largestFragment.startsWith(shortestFragment.substring(index))) {
                return shortestFragment.substring(0, index) + largestFragment;
            }

            if (largestFragment.endsWith(shortestFragment.substring(0, shortestFragmentSize - index))) {
                return largestFragment + shortestFragment.substring(shortestFragmentSize - index, shortestFragmentSize);
            }

        }
        // To reassemble the case "ABCDEF" - "XCDEZ"
        return "";
    }

}