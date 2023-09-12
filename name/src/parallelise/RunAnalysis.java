package parallelise;
import gui.Upload;
import gui.TaintedUI;

import java.io.File;

import gui.AbstractSyntax;
import gui.SQLInspection;
import gui.CodeInjectionUI;

public class RunAnalysis implements Runnable{
	private File filePath;
	
	public RunAnalysis(File filePath) {
		this.filePath = filePath;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Upload.srcCode = this.filePath;// for starters
		
	}

}
