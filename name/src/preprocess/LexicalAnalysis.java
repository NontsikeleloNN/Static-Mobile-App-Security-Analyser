/**
 * 
 */
package preprocess;

import java.io.File;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.util.StringTokenizer;
/**
 * @author Nontsikelelo Ndumo
 *
 */
public class LexicalAnalysis {

	File code ;
	ArrayList<String> tokens;
	
	public LexicalAnalysis(File file) {
		this.code = file;
		this.tokens = new ArrayList<String>();
		
	}
	//unless in each line I remove the white space but keep the \n
	public String ignoreMultiline () {
	ArrayList<String> strings = createString(); // all file contents should be called before tokenisation
	StringBuilder stringBuilder = new StringBuilder();
	
	for(int i = 0; i < strings.size()-1; i++) {
		if (strings.get(i).contains("//")) {
			int arrIndex = strings.indexOf(strings.get(i));//index to set
			String before = strings.get(i).split("//")[0]; //things before
			if (before != null) {
				strings.set(arrIndex, before);
				
			}else {
				strings.remove(strings.get(i));
			}
		}else if (strings.get(i).strip().startsWith("/*")) { // we do a substring and take what is before the comment 
			// remove until or if there is a */ in the same line then we ...?
			if (strings.get(i).contains("*/")) {
				int start = strings.get(i).indexOf("/*");
				int end = strings.get(i).indexOf("*/");
				String extr = strings.get(i).substring(start, end);
				strings.get(i).replaceFirst(extr, " ");
			}else {
				int arrIndex = strings.indexOf(strings.get(i));//index to set
				String before = strings.get(i).split("/\\*")[0]; //things before
				if (before != "" | before != " " ) {
					strings.set(arrIndex, before);
					}
				}
				
	
		}else if (strings.get(i).strip().startsWith("*")) { // this whole line is a comment
			//strings.remove(strings.get(i));
			strings.set(i,"");
			
		}else if(strings.get(i).contains("*/")) { 
		
			int arrIndex = strings.indexOf(strings.get(i));//index to set
			if(strings.get(i).strip().startsWith("*/")) {
				strings.set(i,"");
				strings.remove(strings.get(i));
			}else {
			String after = strings.get(i).split("\\*/")[1]; //things after the comment
			if (after != null) {
				strings.set(arrIndex, after);
			}else {
				strings.remove(strings.get(i));
			
			} 
			}
			
			
		}else if (strings.get(i).strip().startsWith("//")) {
			strings.remove(strings.get(i)); // remove this line cause it is just a comment 
		}
	}
     // loop throug if it contains "//" grab the index? or do substring and then append substring before the // bnn 

	return strings.toString();	

	}
	// not using tokenizer because tokens are not what you asked for. Substring?
	
	
	/***
	 * take in the entire file and break everything up into components using white spaces and new line characters and .
	 * then how will they be in sequence?  
	 * @return
	 */
	public ArrayList<String> makeSpace(){
		
		ArrayList<String> list = new ArrayList<>();

		for (String piece: ignoreMultiline().split("\\s") ) {
			
			if(!piece.isEmpty() && !piece.isBlank() && !piece.equals(",") && !piece.equals("") ) {
			
				piece.trim();
				list.add(piece);
			}else {
				continue;
			}
				
			
			
		}
	
		return list;
	}
	
	
	public ArrayList<String> createString() {
		
		ArrayList<String> strings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(code))) {
            String line1;
            while ((line1 = reader.readLine()) != null) {
                strings.add(line1);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

            
            return strings;
            
        }
  
	public ArrayList<String> tokenize() {
		
		 String lines[] = ignoreMultiline().split("\\n"); // split the semi purified strings 
		
		for(String line : lines) {
			
			
			
					            
			
				tokens.add(line);
			 }
			
	
		return tokens;
		
	}
	
	public ArrayList<String> makeLines(){
		ArrayList<String> all = new ArrayList<>();
		String list = tokenize().toString();
		 String lines[] = list.split(",");
		 for(String line : lines) {
				all.add(line);
			 }
			
		return all;
	}
}
