package dataAnalysis;

public class Node {

	String name;// name of the function or var
	String category;// is it a function
	Boolean tainted;
	
	public Node() {
		name = "";
		tainted = false;
		category = "none";
	}
	
	public Node(String name, String category,Boolean tainted) {
		this.name = name;
		this.tainted = tainted;
		this.category = category;
	}
	
	public void setNode(Boolean tainted) {
		this.tainted = tainted;
	}
	
	public Boolean isTainted() {
		return this.tainted;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	
	
	}
