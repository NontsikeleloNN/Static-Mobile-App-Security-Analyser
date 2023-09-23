package gui;

import java.awt.EventQueue;
import AST.GraphPrinter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;

import dataAnalysis.*;
import preprocess.LexicalAnalysis;
import serialise.RunAnalysis;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.Color;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class Upload {

	private JFrame frame;
	private JTextField txtUpload;
	private JLabel lblTokenize;
	public static  File srcCode = null;
	private ArrayList<File> filesList;
	private ArrayList<String> fileNames;
	DataGraph graph = new DataGraph();
	
	LexicalAnalysis lex;
	CodeInjectionUI ciui;
	TaintedUI taint;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { // <<BACKEND>>
			public void run() {
				try {
					Upload window = new Upload(srcCode);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Upload(File code) {
		initialize(code);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(File srcCode) {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 642);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnOpenFile = new JButton("Select File");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				 JFileChooser fileChooser = new JFileChooser();
				 fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			        fileChooser.setMultiSelectionEnabled(true);
			        int returnValue = fileChooser.showOpenDialog(null);
			        filesList = new ArrayList<File>();
			        fileNames = new ArrayList<String>();
			        if (returnValue == JFileChooser.APPROVE_OPTION) {
			            File[] selectedFiles = fileChooser.getSelectedFiles();

			            for (File file : selectedFiles) {
			                System.out.println("Selected file: " + file.getName());
			                
			               filesList.add(file);
			               fileNames.add(file.getName());
			            }
			        }
			        
				 fileChooser.showOpenDialog(fileChooser);
				 fileChooser.setVisible(true);
			
				
				   // int result = fileChooser.showOpenDialog(btnOpenFile);
		/**trying to make singles*/		  // srcCode = fileChooser.getSelectedFile();
				 // lex = new LexicalAnalysis(srcCode);
				   // txtUpload.setText(srcCode.getAbsolutePath());
			}
		});
		btnOpenFile.setBounds(67, 54, 156, 23);
		frame.getContentPane().add(btnOpenFile);
		
		txtUpload = new JTextField();
		txtUpload.setEditable(false);
		txtUpload.setBounds(67, 88, 258, 20);
		frame.getContentPane().add(txtUpload);
		txtUpload.setColumns(10);
		
		JLabel lblUploadLabel = new JLabel("Upload your source code");
		lblUploadLabel.setForeground(Color.DARK_GRAY);
		lblUploadLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblUploadLabel.setBounds(67, 26, 156, 32);
		frame.getContentPane().add(lblUploadLabel);
		
		lblTokenize = new JLabel("Tokenize");
		lblTokenize.setForeground(Color.DARK_GRAY);
		lblTokenize.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTokenize.setBounds(67, 119, 61, 20);
		frame.getContentPane().add(lblTokenize);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(47, 220, 278, 336);
		frame.getContentPane().add(scrollPane);
		

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(367, 220, 278, 336);
		frame.getContentPane().add(scrollPane_1);
		
		JTextArea txtAfter = new JTextArea();
		scrollPane_1.setViewportView(txtAfter);
		
		
		JTextArea txtBefore = new JTextArea();
		scrollPane.setViewportView(txtBefore);
		
		JButton btnTokenize = new JButton("Tokenize");
		btnTokenize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// <<BACKEND>>
				List<String> list = lex.createString();
				txtBefore.setLineWrap(true);
				
				for(String token : fileNames) {
					txtBefore.append(token);
					txtBefore.append("\n");
				}
				
				List<String> listA = lex.tokenize();
				//CodeInjection ci = new CodeInjection(lex.tokenize());
				
				txtAfter.setLineWrap(true);
				
				for(String token : listA) {
					txtAfter.append(token);
					txtAfter.append("\n");
				}
				
				// <<BACKEND>>
				graph.findTaintedInput(lex.makeLines(), lex.makeSpace());
				graph.connectVertices(lex.makeLines());
				
				System.out.println("After graph call");
				

// Simple Example
			
				
			}
		});
		btnTokenize.setBounds(151, 119, 89, 23);
		frame.getContentPane().add(btnTokenize);
		
		JLabel lblMASA = new JLabel("Lexical Analyser");
		lblMASA.setForeground(Color.BLACK);
		lblMASA.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblMASA.setBounds(261, 0, 139, 25);
		frame.getContentPane().add(lblMASA);
		
		JLabel lblSrc = new JLabel("Source Code");
		lblSrc.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		lblSrc.setBounds(67, 195, 95, 14);
		frame.getContentPane().add(lblSrc);
		
		JLabel lblTokens = new JLabel("Tokens");
		lblTokens.setFont(new Font("Trebuchet MS", Font.BOLD | Font.ITALIC, 13));
		lblTokens.setBounds(389, 195, 46, 14);
		frame.getContentPane().add(lblTokens);
		
		JButton btnTaint = new JButton("Taint Analysis");
		btnTaint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {JOptionPane.showMessageDialog(null, "This is an information message. This is an information message.This is an information message." + "\n"+
					 "\n"+"This is an information message.This is an information message.This is an information message.This is an information message." + "\n"
					+ "This is an information message.This is an information message.This is an information message.This is an information message."+ "\n"
					+ "This is an information message.This is an information message.This is an information message.This is an information message.This is an information message." + "\n"
	                ,"Tainted Data Info", JOptionPane.INFORMATION_MESSAGE);}
		});
		btnTaint.setBounds(528, 567, 117, 23);
		frame.getContentPane().add(btnTaint);
		
		JButton btnSQLInj = new JButton("SQL Injection");
		btnSQLInj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "This is an information message. This is an information message.This is an information message." + "\n"+
						 "\n"+"This is an information message.This is an information message.This is an information message.This is an information message." + "\n"
						+ "This is an information message.This is an information message.This is an information message.This is an information message."+ "\n"
						+ "This is an information message.This is an information message.This is an information message.This is an information message.This is an information message." + "\n"
		                ,"SQL Ijection Info", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnSQLInj.setBounds(203, 567, 123, 23);
		frame.getContentPane().add(btnSQLInj);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RunAnalysis run = new RunAnalysis(filesList);
				
			}
		});
		btnNewButton.setBounds(39, 567, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
	
		
		
	}
}
