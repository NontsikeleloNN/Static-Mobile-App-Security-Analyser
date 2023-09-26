package serialise;
import preprocess.LexicalAnalysis;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import javax.imageio.ImageIO;
import dataAnalysis.CodeInjection;
import dataAnalysis.DataGraph;
import dataAnalysis.Node;

public class RunAnalysis{
	DataGraph graph = new DataGraph();
	//private static ArrayList<File> selectedFiles;
	LexicalAnalysis lex;
	CodeInjection ci;
	InputStream is;
	BufferedImage bImage;
	private ArrayList<String> tokens;
	
	public RunAnalysis(ArrayList<File> files) {
		int fileCount = 0;
	//	go through each of the files and run the different analysis each time and output reports in PDF
		for (File file: files) {
			System.out.println("count for file: " + ++fileCount);
			uploadCode(file);
			taintedUI();
			codeInj();
			report(file);
			
		}
	}

	private void report(File file) {
		try (PDDocument document = new PDDocument()) {
			PDPage page = new PDPage(PDRectangle.A4);
			 document.addPage(page);
			 
			 PDPageContentStream contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true);
	            contentStream.beginText();
	            float x = 45; // Adjust as needed
			    float y = 750; // Adjust as needed
			    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_BOLD), 17);
				contentStream.newLineAtOffset(x, y);
				  contentStream.showText("Analysis Result for: "+ file.getName());;
				  contentStream.newLineAtOffset(0, -20); // Move down for content
				 
				  contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
				  contentStream.showText("Tainted Data Graph");
				  contentStream.newLineAtOffset(0, -20); // Move down for content
	            //contentStream.showText(reportContent);//content
	           // contentStream.endText();
	            // <<<<Formatting>>>
				  contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 14);
		            String text = "The root nodes in this structure represent the initial points of tainted input. " + "\n"
					  		+ "Descendants of these roots illustrate how the tainted data propagates to affect other "
					  		 + "\n"
					  		+ "variables.Isolated nodes in this structure are either clean or tainted, but they do not   " + "\n"
					  		+ "further taint any other variables.";
		             String texts [] = text.split("\n");
		             for (String line : texts) {
		            	 contentStream.showText(line);
					      contentStream.newLineAtOffset(0, -15);
		             }
		             DecimalFormat df = new DecimalFormat("###.###");
		       
		             contentStream.newLineAtOffset(0, -20);
		             contentStream.showText("The taint ratio of this particular file is: "+ df.format(calcTaintRatio()));
		             
		             contentStream.newLineAtOffset(0, -20);
		             String part = giveRecommendation(calcTaintRatio());
		             String parts [] = part.split("\n");
		             for (String port : parts) {
		            	 contentStream.showText(port);
					      contentStream.newLineAtOffset(0, -15);
		             }
					 contentStream.endText();
	            float pageWidth = PDRectangle.A4.getWidth();
	            float pageHeight = PDRectangle.A4.getHeight();

	            
	            // Define the relative positions (in percentages)
	            float relativeX = 10; // 10% from the left edge
	            float relativeY = 50; // 50% from the top edge
	            
	            PDRectangle pageSize = page.getMediaBox();
	            
	            // Convert relative positions to absolute coordinates
	            float absoluteX = (relativeX / 100) * pageWidth;
	            float absoluteY = (relativeY / 100) * pageHeight;
	            
	            
	            PDImageXObject image = PDImageXObject.createFromFile("./output.png", document); // <where do I find the path?>
	            
	            float imageWidth = image.getWidth();
	            float imageHeight = image.getHeight();
	            
	            float scale = Math.min(pageWidth / imageWidth, pageHeight / imageHeight);

	            float scaledWidth = imageWidth * scale;
	            float scaledHeight = imageHeight * scale;
	            
	            float x1 = (pageWidth - scaledWidth) / 2;
	            float y1 = (pageHeight - scaledHeight) / 2;
	            
	            contentStream.drawImage(image, x1, y1, scaledWidth, scaledHeight);
	            //contentStream.drawImage(image, absoluteX, absoluteY, image.getWidth(), image.getHeight()); // Adjust coordinates as needed
	            
	            // begin again
	            //contentStream.beginText();
	            //contentStream.newLineAtOffset(0, -10); // Move down for content
	            
	            PDPage page2 = new PDPage(PDRectangle.A4);
				 document.addPage(page2);
	            
				 PDPageContentStream contentStream1 = new PDPageContentStream(document, page2, AppendMode.APPEND, true);
		            contentStream1.beginText();
		            
	            contentStream1.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 14);
				  contentStream1.newLineAtOffset(45, 750); // Adjust position as needed
				  contentStream1.showText("Recommendations");
				  contentStream1.newLineAtOffset(0, -20); // Move down for content
				  
				  
				  contentStream1.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
				  for (String recommendation : ci.getAdvice()) {
					  ///////////////
					  String[] lines = recommendation.split("\n");
					  float q = -5; // Adjust startY as needed
					  for (String line : lines) {
					     
					     // contentStream1.newLineAtOffset(0, q);
					     // contentStream1.showText(line);
					     
					      y -= 5; // Adjust lineHeight as needed
					      contentStream1.showText(line);
					      contentStream1.newLineAtOffset(0, -15); 
					  }

					  ///////////////////
				     
					
				  }
	            contentStream1.endText();
	            contentStream.close();
	            contentStream1.close();
	            int time = LocalDateTime.now().getNano();
	            document.save("./"+time +"mobile_app_report.pdf"); // Save the PDF
	            
	            document.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	private String giveRecommendation(double val) {
		if(val < 0.2) {
			return "1. Implement input validation to ensure only expected data is processed. \n"
					+ "2. Use secure coding practices to prevent common coding mistakes. \n"
					+ "3. Regularly update dependencies to patch known vulnerabilities. \n"
					+ "4. Apply security headers and utilize HTTPS for secure communication";
		}else if (val >= 0.2 && val < 0.5) {
			return "1. Sanitize user inputs to protect against SQL injection attacks. \n"
					+ "2. Implement access controls to restrict unauthorized access to sensitive data. \n"
					+ "3. Use parameterized queries to prevent SQL injection vulnerabilities. \n"
					+ "4. Apply proper error handling to avoid leaking sensitive information.";
		}else {
			return "Immediately fix identified SQL injection vulnerabilities. \n"
					+ "Review and update access controls to prevent unauthorized access. \n"
					+ "Conduct a thorough security audit to identify and address critical issues. \n"
					+ "Apply patches or updates for known vulnerabilities without delay.";
		}
	}
	private void uploadCode(File file) {
		  lex = new LexicalAnalysis(file);
		  tokens = lex.makeLines();
		  graph.findTaintedInput(lex.makeLines(), lex.makeSpace());
			graph.connectVertices(lex.makeLines());
			graph.printGraph(graph.getGraph());
			try {
				is = graph.getGraphPrinter().things();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	
	public double calcTaintRatio() {
		Set<Node> nodes = graph.getVertices();
		int good = 0;
		int bad = 0;
		for (Node node : nodes) {
			if (node.isTainted()) {
				bad++;
			}else if(!node.isTainted()) {
				good++;
			}
		}
		if(good == 0) {
			return 0;
		}else {
			return bad/good;
		}
		
	}

	private void taintedUI() {
		
		try {
			bImage = ImageIO.read((is));
			//display image
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void codeInj() {
		ci = new CodeInjection(tokens);
		ci.inline();
		// I can directly get the feedback and advice after this
		
		
	}
	
	}
