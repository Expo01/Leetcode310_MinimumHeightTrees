import java.util.*;

public class Main {
    public static void main(String[] args) {

        System.out.println(Arrays.toString(new int[1]));
    }
}


class Solution {
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {

        // edge cases
        if (n < 2) {
            ArrayList<Integer> centroids = new ArrayList<>();
                centroids.add(0); // this works becaus the nodes are labeled 0 - (n-1). so min height root must be 0 if
            return centroids; //  only 1 node (or zero?) exists
        }

        // Build the graph with the adjacency list
        ArrayList<Set<Integer>> neighbors = new ArrayList<>();
        for (int i = 0; i < n; i++)
            neighbors.add(new HashSet<Integer>());

        for (int[] edge : edges) {
            Integer start = edge[0], end = edge[1];
            neighbors.get(start).add(end); // gets arraylist at index of 'start' which is also the value of the vertex
            neighbors.get(end).add(start); // add reciprocal edge from end of start
        }

        // Initialize the first layer of leaves
        ArrayList<Integer> leaves = new ArrayList<>();
        for (int i = 0; i < n; i++)
            if (neighbors.get(i).size() == 1) // this is how minimum dependency/leaf is determined, but assessing size
                leaves.add(i); // of its arraylist which will state # of vertices is has an edge with

        // Trim the leaves until reaching the centroids
        int remainingNodes = n;
        while (remainingNodes > 2) {
            remainingNodes -= leaves.size();
            ArrayList<Integer> newLeaves = new ArrayList<>(); // will hold 'new leaves' that are next rung closer to centroid(s)

            // remove the current leaves along with the edges
            for (Integer leaf : leaves) {
                Integer neighbor = neighbors.get(leaf).iterator().next(); // gets the next vertex that is closer to centroid
                // stores its value
                neighbors.get(neighbor).remove(leaf); // removes current leaf
                if (neighbors.get(neighbor).size() == 1) // adds 'new leaf' to the 'queue' which is the arraylist with iterator
                    newLeaves.add(neighbor);
            }

            // prepare for the next round
            leaves = newLeaves;
        }

        // The remaining nodes are the centroids of the graph
        return leaves;
    }
}


// my solution passes 65/71 then time exceeded
class Solution {
    List<Integer> ans = new ArrayList<>();
    PriorityQueue<int[]> rootDepthPairs = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
    Map<Integer, ArrayList<Integer>> adjacencyMap = new HashMap<>();


    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        if (edges.length == 0){ // another weird edge case
            ans.add(0);
            return ans;
        }

        if(n == 1 || n == 2){ // edge case, depth must be 1 or 2. return those edges
            for(int[] edge: edges){
                ans.add(edge[0]);
                return ans;
            }
        }

        for(int[] edge: edges){
            if(!adjacencyMap.containsKey(edge[0])){
                adjacencyMap.put(edge[0],new ArrayList<>());
            }
            adjacencyMap.get(edge[0]).add(edge[1]);

            if(!adjacencyMap.containsKey(edge[1])){
                adjacencyMap.put(edge[1],new ArrayList<>());
            }
            adjacencyMap.get(edge[1]).add(edge[0]);

        }

        for(int root: adjacencyMap.keySet()){
            Map<Integer,Boolean> visited = new HashMap<>();
           int maxHeight = DFS(root,1, visited);
           // could forgo the whole PQ and just record root of least known height so far and if new lesser height foud, just
            // wipe ans by set ans to new arraylist

            int[] pair = {maxHeight,root};
            rootDepthPairs.add(pair);
        }

        if(rootDepthPairs.isEmpty()){
            return new ArrayList<>();
        }

        int minHeight = rootDepthPairs.peek()[0];
        while (rootDepthPairs.peek()[0] == minHeight){
            ans.add(rootDepthPairs.poll()[1]);
        }

        return ans;

    }


    private int DFS(int root, int height, Map<Integer,Boolean> visited){
        ArrayList<Integer> neighbors = adjacencyMap.get(root);
        visited.put(root,true);
        int maxH = height;

        for(int neighbor: neighbors){
            if(!visited.containsKey(neighbor)){
                int curH = DFS(neighbor,height+1,visited);
                maxH = Math.max(curH, maxH);
            }
        }
        return maxH;

    }
}


// RECORD OF DEBUGGING
//class Solution {
//    List<Integer> ans = new ArrayList<>();
//    PriorityQueue<int[]> rootDepthPairs = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
//    Map<Integer, ArrayList<Integer>> adjacencyMap = new HashMap<>();
//
//
//    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
//
//        // loops all vertex pairs to create arraylist
//        for(int[] edge: edges){ // value of all vertices current vertex shares an edge with
//            if(!adjacencyMap.containsKey(edge[0])){
//                adjacencyMap.put(edge[0],new ArrayList<>());
//            }
//            adjacencyMap.get(edge[0]).add(edge[1]);
////            System.out.println(adjacencyMap.get(edge[0])); adjacency list working as expected
//
//            if(!adjacencyMap.containsKey(edge[1])){
//                adjacencyMap.put(edge[1],new ArrayList<>());
//            }
//            adjacencyMap.get(edge[1]).add(edge[0]);
////            System.out.println(adjacencyMap.get(edge[1])); working here too
//
//        }
//
//
//
//        for(int root: adjacencyMap.keySet()){
//            Map<Integer,Boolean> visited = new HashMap<>();
//            int maxHeight = DFS(root,1, visited);
//            System.out.println(maxHeight);
//            // could forgo the whole PQ and just record root of least known height so far and if new lesser height foud, just
//            // wipe ans by set ans to new arraylist
//
//            int[] pair = {maxHeight,root};
//            rootDepthPairs.add(pair);
//        }
//
//        if(rootDepthPairs.isEmpty()){
//            return new ArrayList<>();
//        }
//
//        int minHeight = rootDepthPairs.peek()[0];
//        while (rootDepthPairs.peek()[0] == minHeight){
//            ans.add(rootDepthPairs.poll()[1]);
//        }
//
//        return ans;
//
//    }
//
//
//    private int DFS(int root, int height, Map<Integer,Boolean> visited){
//        System.out.println(root + " root from DFS first line"); // this is fine. prints root
//        ArrayList<Integer> neighbors = adjacencyMap.get(root);
//        System.out.println(neighbors + " neighbors arraylist from DFS"); // this is also fine, prints the arraylist
//        visited.put(root,true);
//        System.out.println(visited.get(root)); // also fine, prints true for root
//        int maxH = height;
//        System.out.println(maxH + " maxH from DFS"); // height is the only thing that is incrementing even though root same
//
//
//        for(int neighbor: neighbors){
//            if(!visited.containsKey(neighbor)){
////                System.out.println(neighbor + "neighbor from DFS for loop"); // prints same neighbor in infinite loop
//                // int curH = DFS(root,height+1,visited); // **ERROR** passing root instead of neighbor
//                int curH = DFS(neighbor,height+1,visited);
//                maxH = Math.max(curH, maxH);
//            }
//        }
//        return maxH;
//
//        // may actually have to compare max depth among all neighbors, and keep max height since it only takes
//        // one node in a new row to change the tree height
//
//    }
//}

// here is the concept: a graph that does not have cycles is just a tree. a node can have any number of children as long
// as it doesn't connect with other children, other parents, etc. that would cause a cycle
// goal is to determine which node(s) if graph configured in a tree layout would produce the shallowest depth

// this sounds like a good scenario to for loop through all nodes, DFS, reach base case and find depth. very similar to
// max height of a binary tree, but now just looping through all possible roots and comparing depths
// tricky part will be suppose 4 root possibilities, say 3/4 options have depth of 2. all of those orientations
// are all min height trees. but when 4th node has depth of 1, need to remove all other nodes from answer List.
// could create a PQ and store an int[] which would indicate (depth,root) and only add to findal collection at end where
// first item polled from PQ will be the least then continue polling while depth is == to first removed. add index 1 value
// from each int[] to return collection of roots

// problem two. we are given edges, not nodes...
// should create adjacency list looping through int[] and map each first index with arraylist of values. if key exists
// then add to existing arraylist. later will have to looop through keyset and DFS through adjacency list. one
// arraylist fully looped, will come to end of method. will actually want to pass root and pass +1 depth with eeach
// call so when end finally reached at a 'leaf' in DFS, that (root,depth) will be passed to the PQ.