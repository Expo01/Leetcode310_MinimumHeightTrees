import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}

class Solution {
    List<Integer> ans = new ArrayList<>();
    PriorityQueue<int[]> rootDepthPairs = new PriorityQueue<>();
    Map<Integer, ArrayList<Integer>> adjacencyMap = new HashMap<>();


    public List<Integer> findMinHeightTrees(int n, int[][] edges) {

         // loops all vertex pairs to create arraylist
        for(int[] edge: edges){ // value of all vertices current vertex shares an edge with
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
        System.out.println(root);
        ArrayList<Integer> neighbors = adjacencyMap.get(root);
        visited.put(root,true);
        int maxH = height;


        for(int neighbor: neighbors){
            if(!visited.containsKey(neighbor)){
                int curH = DFS(root,height+1,visited);
                maxH = Math.max(curH, maxH);
            }
        }
        return maxH;

                // may actually have to compare max depth among all neighbors, and keep max height since it only takes
        // one node in a new row to change the tree height

    }
}

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