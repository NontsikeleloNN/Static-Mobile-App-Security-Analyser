package gui;

import java.awt.EventQueue;
import javax.swing.border.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dataAnalysis.CodeInjection;

import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

public class ReportsUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	CodeInjection ci;
	double Value;
	public static ArrayList<String> tokens = new ArrayList<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {// <<BACKEND>>
					ReportsUI frame = new ReportsUI(0,false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	// <<BACKEND>>
	public static ArrayList<String> setTokens(ArrayList<String> tokens) {
		return tokens;
	}
	
	/**
	 * Create the frame.
	 */
	public ReportsUI(double value, boolean isVuln) {
		this.Value = value;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 574, 429);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 204, 255));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane spVulnerable = new JScrollPane();
		spVulnerable.setBounds(51, 96, 356, 233);
		contentPane.add(spVulnerable);
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Bookman Old Style", Font.PLAIN, 13));
		textArea.setEditable(false);
		spVulnerable.setViewportView(textArea);
		
		JButton btnNewButton = new JButton("Report");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 DecimalFormat df = new DecimalFormat("###.###");
				 
				textArea.setLineWrap(true); 
					textArea.append("Your mobile application has a combined taint ratio of: " + df.format(Value)); 
					textArea.append("\n");	
					textArea.append("\n");	
					textArea.append(feedback(Value));
					textArea.append("\n");	
					textArea.append("________________________________________________");
					textArea.append("\n");	
					textArea.append("The presence of vulnerable SQL queries has been" +"\n"+"found to be: "+"\n"+ isVuln);
					textArea.append("\n");	
					textArea.append("_________________________________________________");
					textArea.append("\n");	
					textArea.append("For individual details on each file, please look to the PDF reports generated");		
				
			}
		});
		btnNewButton.setBounds(224, 363, 89, 23);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setForeground(new Color(0, 0, 0));
		Border roundedBorder = new LineBorder(Color.BLACK, 2, true);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Overall Report Summary");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel.setBounds(51, 71, 193, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_2 = new JLabel("Mobile Application Security Analyser");
		lblNewLabel_2.setFont(new Font("Bookman Old Style", Font.ITALIC, 25));
		lblNewLabel_2.setBounds(38, 11, 459, 28);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Generate Report");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_3.setBounds(99, 357, 115, 33);
		contentPane.add(lblNewLabel_3);
		
		
	}
	
	String feedback(double x) {
		if(x <= 0.09) {
			return "This value is relatively low, mean your program has a few vulnerabilities to be addressed ";
		}else {
			return "This value is high which  means your program has many vulnerabilites that must be addressed "+"\n"+"immediately";
		}
		
	}
}
