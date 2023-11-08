package dataAnalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class CodeInjection {
	
	//Map<String,String> tokens; // line and the feedback 
	ArrayList<String> feedback;
	ArrayList<String> advice;
	ArrayList<String> clean;
	ArrayList<String> arrTokens;
	int badCount= 0;
	public CodeInjection(ArrayList<String> token) {
		this.arrTokens = token;// this won't help 
		this.feedback = new ArrayList<>();
		this.advice = new ArrayList<>();
	}
	
	public ArrayList<String> getFeedback(){
		return feedback;
	}
	/***
	 * check for inline sql statements that grab directly from parameters
	 * @return
	 */
	
	public int calcRatio() {
		return 0;
	}
	
	public void inline() {
			Pattern inlineQuery = Pattern.compile("(\"\\s+SELECT | \"\\s+select|\"SELECT |\"select)(\\w+|\\s+)+([\\s$&+,:;=?@#|\"'<>.^*()%!-]+)+(\\w+)+(\\s+)");
		for(int i = 0; i <= arrTokens.size() -1; i++) {
			if(!arrTokens.get(i).isBlank() && !arrTokens.get(i).isBlank()) {

				System.out.println(i); //store line untill you reach a ;
				String examine = makeLine(arrTokens.get(i), arrTokens, i);
				System.out.println("Examine this line: "+examine);
				if((examine.toLowerCase()).contains("select") && (!examine.isBlank() && (examine.toLowerCase()).contains("+"))) { 
					// there is concentanation of SQL queries
					
					if(examine.contains("@") || examine.toLowerCase().contains(".encoder()")) {
						continue; // clean
					}else if(examine.toLowerCase().contains(".getparameter()")|| !examine.toLowerCase().contains(".encoder()")) {
						badCount++;
						System.out.println("line considered dirty: " + examine);
						feedback.add(examine); //dirty
						advice.add("At least one of your SQL querys are concatenated with directly unsanitised input from a parameter,"+"\n"+ " process the parameters before using them in queries");
						continue;
					}else if( variableResolver(examine)){
						System.out.println("line considered dirty: " + examine);
						badCount++;
						advice.add("Your SQL query is concatenated with variable derived from unsanitised input,"+"\n"+" ensure all external input is processed before intergrating it into your system");
						feedback.add(examine);
						continue;
					}else {
						continue; // we'll assume you're clean
					}
				}
		System.out.println(i);
			
			}else {
				continue;
			}
		}
		
	}
	public ArrayList<String> getAdvice(){
		Set<String> set = new HashSet<>(advice);
		advice.clear();
		advice.addAll(set);
		return advice;
	}
	public int getSQLCount() {
		return badCount;
	}
	private boolean variableResolver(String exam) {
		String name = null;
		ArrayList<String> names = new ArrayList<>();
		int size = arrTokens.size();
		int i = 0;
		while ( i <= size) {
			if(arrTokens.get(i).contains("=") && arrTokens.get(i).toLowerCase().contains(".getparameter()") && !arrTokens.get(i).toLowerCase().contains("select")) { //should be variable assignment
				String before = exam.split("=")[0];
				//isolate var
				name = before.split(" ")[1];
				System.out.println("Names added to tainted in CI: "+ name);
				names.add(name); //add to list of tainted names 
				i++;;
		}
			i++;;
			}
		//if the stament contains the tainted name 
			for(int x = 0; x < names.size(); x++) {
				if(exam.contains(names.get(x))) {
					name = names.get(x);
					return true;
				}
				
			}
		
		return false;
	}
	/**
	 * will concat the next line if the line does not end with a :
	 * @param line
	 * @param allLines
	 * @param index
	 * @return
	 */
	private String makeLine(String line, ArrayList<String> allLines, int index) {
		String newString = line;
		int count = index;
		while (!newString.contains(";")) { // while the line in question doesn't have a ;
			count+=1;
			String x = allLines.get(count%allLines.size());
			if(!x.isBlank()) {
				newString+= " "+x;
				System.out.println("New string: "+newString);
			}else {
				continue;
			}
			
			
			
		}
		
		return newString.trim();
	}
	public Boolean Concat() {
		return null;
		
	}
}
