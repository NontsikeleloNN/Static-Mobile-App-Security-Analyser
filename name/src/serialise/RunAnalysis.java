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
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import dataAnalysis.CodeInjection;
import dataAnalysis.DataGraph;

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
				 
				  contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 14);
				  contentStream.showText("Tainted Data Graph");
				  contentStream.newLineAtOffset(0, -20); // Move down for content
	            //contentStream.showText(reportContent);//content
	            contentStream.endText();
	            // <<<<Formatting>>>
	            
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
