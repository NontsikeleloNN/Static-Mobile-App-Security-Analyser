package dataAnalysis;

import java.util.List;

import java.util.Map;

import java.util.Set;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.lang.model.SourceVersion;

import AST.GraphPrinter;

public class DataGraph {

	private Map<Node, List<Node>> graph = new HashMap<>();
	private GraphPrinter gp;
	ArrayList<Node> tainted;
	int count = 0;
	int total = 0;
	
	public void addVertex(Node n) {
		graph.put(n, new LinkedList<Node>());
	}
	
	

	public Map<Node, List<Node>> getGraph() {
		return this.graph;
	}
	private int getNumTainted1 () {
		return this.tainted.size();
	}
	private int getNumTainted() {
		Set<Node> nodes = graph.keySet();
		int count = 0;
		for (Node node : nodes) {
			if(node.isTainted()) {
				count++;	
			}
			
		}
		return count;
	}
	public double ratio() {
		return getNumTainted()/getSize();
	}
	private int getSize() {
		return graph.keySet().size();
	}
	public void addEdge(Node from, Node to) {
		// do checks on my own if the node exists or not

		if (from != null && to != null) {
			//graph.get(from).add(to); // adding a link between from and to
			graph.get(to).add(from);// and to to from to make it bi-directional

		} else {
			System.out.println("cannot add edge because nulls");
		}
	}

	public Boolean isConnected(Node from, Node to) {

		if (graph.get(from).contains(to) || graph.get(to).contains(from)) {
			return true;
		} else {
			return false;
		}
	}

	public int numVertex() {
		return graph.keySet().size();
	}

	/***
	 * Will check if a node exists, if so, return an array containing all the
	 * elements in the entry's LL
	 * 
	 * @param n a node
	 * @return an array containing adjacent nodes
	 */
	public Node[] getAdjacent(Node n) {
		if (graph.containsKey(n)) {
			return (Node[]) graph.get(n).toArray();
		}
		return null;
	}

	int myAbs(int x) {
		if (x >= 0) {
			return x;
		} else {
			return 0;
		}
	}

	/***
	 * this will be used to create class nodes in the graph
	 * 
	 * @param lines
	 * @param words
	 * @return
	 */
	public ArrayList<Node> findClass(ArrayList<String> lines, ArrayList<String> words) {
		ArrayList<Node> classes = new ArrayList<>();
		for (int i = 0; i < words.size() - 1; i++) {
			int val1 = myAbs(i - 1);
			int val2 = myAbs(i - 2);
			// checks if it is a word, the tokens to the ;eft are an identifier, it is not a
			// keyword and returns true
			if (words.get(i).matches("[a-zA-Z]+")
					&& (words.get(val1).toLowerCase() == "class" || words.get(val2).toLowerCase() == "class")
					&& SourceVersion.isKeyword(words.get(i))) { // is a word
				classes.add(new Node(words.get(i), "class", false)); // make a new class
			}
		}
		return classes;
	}

	private Boolean isMethodKeyword(String word) {

		if (word.toLowerCase().equals("final") || word.toLowerCase().equals("main")
				|| word.toLowerCase().equals("public") || word.toLowerCase().equals("static")
				|| word.toLowerCase().equals("protected") || word.toLowerCase().equals("private")) {
			return true;
		}
		return false;
	}

	/***
	 * check for nulls
	 * 
	 * @param lines
	 * @param words
	 * @return
	 */
	ArrayList<Node> findMethods(ArrayList<String> lines, ArrayList<String> words) {
		ArrayList<Node> methods = new ArrayList<>();
		for (int i = 0; i < words.size() - 1; i++) {
			int val1 = myAbs(i - 1);
			int val2 = myAbs(i - 2);

			// if this token is a word and the previous 2 tokens are one of the function
			// identifiers, it is a function
			if (words.get(i).matches("[a-zA-Z]+")
					&& (isMethodKeyword(words.get(val1)) || isMethodKeyword(words.get(val2)))) {
				methods.add(new Node(words.get(i), "method", false)); // make a new class
			}

		}
		return methods;
	}

	public Set<Node> getVertices() {
		return graph.keySet();
	}
// editText
	private Boolean isVarKeyword(String word) {
		if (word.toLowerCase().equals("boolean") || word.toLowerCase().equals("string")
				|| word.toLowerCase().equals("byte") || word.toLowerCase().equals("char")
				|| word.toLowerCase().equals("short") || word.toLowerCase().equals("int")
				|| word.toLowerCase().equals("float") || word.toLowerCase().equals("double")
				|| word.toLowerCase().equals("long") || word.toLowerCase().equals("integer")
				|| word.toLowerCase().equals("edittext")) {
			return true;
		}
		return false;
	}

	int checkBounds(int val, int upper) {
		if (val <= upper - 1) {
			return val;
		}
		return 0;
	}

	/**
	 * 
	 * @param words the broken up words from the lexical analysis
	 * @return supposed to bring back all the variables in the code
	 */
	ArrayList<Node> findVars(ArrayList<String> words) {
		ArrayList<Node> variables = new ArrayList<>();
		for (int i = 0; i < words.size() - 1; i++) {
			int val1 = myAbs(i - 1);
			int val2 = myAbs(i - 2);
			int up1 = checkBounds(i + 1, words.size());
			int up2 = checkBounds(i + 2, words.size());
			// if this token is a word and the previous 2 tokens are one of the variable
			// identifiers, it is a variable

			if (words.get(i).matches("[a-zA-Z]+")
					&& (words.get(up1).contains("+") || words.get(up1).contains(";") || words.get(up1).contains("=")
							|| words.get(up2).contains("+") || words.get(up2).contains(";")
							|| words.get(up2).contains("="))
					&& (isVarKeyword(words.get(val1)) || isVarKeyword(words.get(val2)))) {
				variables.add(new Node(words.get(i), "variable", false)); // make a new class
				addVertex(new Node(words.get(i), "variable", false));
			}

		}
		return variables;
	}
	// finish and test

	Boolean isInput(String line) {
		if (line.toLowerCase().contains("args") || line.toLowerCase().contains("new scanner")
				|| line.toLowerCase().contains("scanner") || line.toLowerCase().contains("nextline")
				|| line.toLowerCase().contains("gettext") || line.toLowerCase().contains("nameedittext") || line.toLowerCase().contains("feedbackedittext")
				|| line.toLowerCase().contains("feedbacktextview") || line.toLowerCase().contains("getparameter")
				|| line.toLowerCase().contains("getstring")) { // add other http and file
																							// things
			return true;
		}
		return false;
	}

	/**
	 * I believe this was supposed to be for clean = dirty only otherwise it should also have
	 * the isInput check. wanna try for if RHS contains infected
	 * @param line
	 * @return
	 */
	private Boolean containsInfected(String line) {
		Set<Node> nodes = graph.keySet();
		for (Node node : nodes) {
			if (line.contains(node.getName()) && node.isTainted()) {
				return true;
			}
		}
		return false;
	}

	private Node findInfectedRHS(String RHS) {
		Set<Node> nodes = graph.keySet();
		for (Node node : nodes) {
			if (RHS.contains(node.getName()) && node.isTainted()) {
				return node;
			}
		}
		return null;
	}

	private Node findLHSNode(String LHS) {
		Set<Node> nodes = graph.keySet();
		for (Node node : nodes) {
			//System.out.println("Node in findLHSNode: "+ node.getName()+ " is tainted: " + node.isTainted());
			if (LHS.contains(node.getName()) && !node.isTainted()) {
				System.out.println(LHS + " IN find LHS node and is tainted: " + node.isTainted());
				
				return node;
			}
		}
		return null;
	}

	/**
	 * Helper function to remove the old node from the lists and replace it with new
	 * ones
	 * 
	 * @param potentials
	 * @param oldNode
	 * @param newNode
	 */
	private void replaceOld(List<Node> potentials, Node oldNode, Node newNode) {
		// for each vertex in the graph, remove this Node from your list
		// if this particular list contains the node we've modified, remove the old one
		// and add the new one
		if (potentials.contains(oldNode)) {
			potentials.remove(newNode);
			potentials.add(newNode);
		}
	}
	/**
	 * need to urgently find a way to eliminate text in quotation marks
	 * @param lines
	 */
	public void connectVertices(ArrayList<String> lines) {
		// vertices are already added to the graph
		// we'll maybe need to store the information and then replace it using some of
		// the old info
		// like the list of related edges
		// if I iterate through the set and find the entry then it'll be fine
		// loop through lines
		for (int l = 0; l <= lines.size() - 1; l++) {
			// for each line, loop through the nodes all the nodes

			if ((lines.get(l).contains("=")) && containsInfected(lines.get(l))) { // contains infected cause of variable!!
				
				String LHS = lines.get(l).split("=")[0];
				String RHS = lines.get(l).split("=")[1];
				if (containsInfected(RHS)) {
					System.out.println("RHS: "+RHS);
					Node nodeToInfect = findLHSNode(LHS); // finds clean variables. null if?
					Node infectedNode = findInfectedRHS(RHS); // will be null if rhs does not contain node and contains tainting thing
					System.out.println("infected node: "+infectedNode.getName());
					if (graph.containsKey(nodeToInfect) && infectedNode != null && nodeToInfect != null) { // I want to change it to infected
						// if the node is not yet tainted
						if (nodeToInfect.isTainted() == false && infectedNode.isTainted() == true) {
							// graph.get(nodeToInfect);
							String name = nodeToInfect.getName();
							System.out.println("new tainting is: "+ name );
							String cat = nodeToInfect.getCategory();
							Node newNode = new Node(name, cat, true);
							graph.remove(nodeToInfect); // remove the old, unifected node from the graph
							List<Node> d= new LinkedList<>();
							graph.put(newNode, d);
							// node
							addEdge(newNode, infectedNode);
							// if a vertex contains a list with element x, then replace remove x and add
							// this instead

						}
					}
					// we could later do checks if the thing is actually being purified
				}

			} else {
				//this was taking into account that the LHS contains the infected instead of the rhs
			}
		}

	}

	/**
	 * Here I want to change the found variables to tainted if they are tainted. It
	 * would probably be profitable to make a rule during one the analysis that if
	 * the line does not end in a ;, then consider that one line so that I can catch
	 * the input that goes over * lines
	 * 
	 * @param lines
	 * @param words there are all the variables that I found in the java code
	 * @return
	 */
	public void findTaintedInput(ArrayList<String> lines, ArrayList<String> spaces) {
		// if the line contains one of the taint words and an = or + and is one of the
		// variables we've identified,
		// then mark that one as tainted.
		 tainted = new ArrayList<Node>();
		ArrayList<Node> words = findVars(spaces);
		System.out.println("find tainted/vars: " + words.toString());
		for (int i = 0; i <= lines.size() - 1; i++) {

			if (isInput(lines.get(i))) { // iff this line contains some sort of input
				// String line = lines.get(i).stripTrailing();
				for (int q = 0; q <= words.size() - 1; q++) {
					/**
					 * int count = i; while (!lines.get(i).endsWith(";")){ lines.get(i).concat(i+1)
					 * }
					 */
					// if the line contains some sort of input, a variable, a + or = then the
					// variable is tainted
					// I could use the absence of ; to make this better
					if (isInput(lines.get(i)) && lines.get(i).contains(words.get(q).getName())
							&& (lines.get(i).contains("+") || lines.get(i).contains("="))) { // if we get to a line that
																								// contains the word and
																								// ...
						System.out.println("line: " + lines.get(i) + " word looking for: " + words.get(q).getName()
								+ " concat: " + (lines.get(i).contains("+") || lines.get(i).contains("=")));
						Node node = new Node(words.get(q).getName(), "variable", true);
						tainted.add(node); // adding the tainted data
						graph.remove(words.get(q)); // remove the old vertex
						graph.put(node, new LinkedList<Node>()); // add a new one that shows that this one is tainted
						System.out.println("name: " + node.getName() + " is tainted: "
								+ (isInput(lines.get(i)) && lines.get(i).contains(words.get(q).getName())
										&& (lines.get(i).contains("+") || lines.get(i).contains("="))));
					}
				}

			}
		}

	}
public GraphPrinter getGraphPrinter () {
	return this.gp;
}

public InputStream setInputStream() {
	try {
		return gp.things();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}

public int getTainted() {
	return count;
}
public int size() {
	return total;
}

public float getRatio() {
	if(getTainted() != total) {
		return getTainted()/(total-getTainted());
	}
	return 0;
}
	public void printGraph(Map <Node, List<Node>> pGraph) {
		gp = new GraphPrinter();
		
		
		for(Node n : pGraph.keySet()) { // for each of the keys in here
			if(!pGraph.get(n).isEmpty()) {// if its corresponding list is not empty
				total = pGraph.keySet().size();
				for(Node k : pGraph.get(n)){ // for each element in the non empty list of the node 
					// I want to make connections via the graphviz
					if(n.isTainted()) {
						count ++;
						gp.addln(n.getName()+" [color=red]");
						gp.addln(n.getName() + " -> "+ k.getName() + "[color=\"red\"]" );
						
					}else {
						gp.addln(n.getName() + " -> "+ k.getName() + "[color=\"black\"]" );
						
					}
					
				}
				
			}else {
				if(n.isTainted()) {
					count++;
					gp.addln(n.getName()+" [color=red]");
				
					
				}else {
					gp.addln(n.getName());
					
				}
			}
			
		}
		
		gp.print();
	}
}
