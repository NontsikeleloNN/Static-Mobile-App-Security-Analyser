package serialise;
import gui.Upload;
import preprocess.LexicalAnalysis;
import gui.TaintedUI;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dataAnalysis.DataGraph;
import gui.AbstractSyntax;
import gui.SQLInspection;
import gui.CodeInjectionUI;

public class RunAnalysis implements Runnable{
	private File filePath;
	DataGraph graph = new DataGraph();
	LexicalAnalysis lex;
	
	public RunAnalysis(File filePath) {
		this.filePath = filePath;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Upload.srcCode = this.filePath;// for starters
		
	}

	private void uploadCode(File file) {
		  lex = new LexicalAnalysis(file);
		  List<String> list = lex.createString();
		  graph.findTaintedInput(lex.makeLines(), lex.makeSpace());
			graph.connectVertices(lex.makeLines());
			graph.printGraph(graph.getGraph());
			try {
				InputStream in = graph.getGraphPrinter().things();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private void taintedUI() {
		
	}
	
	private void sqlInspection() {
		
	}
	
	private void codeInj() {
		
	}
	
	}
