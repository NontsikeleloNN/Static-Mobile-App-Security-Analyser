package gui;

import java.awt.EventQueue;

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
					ReportsUI frame = new ReportsUI(0);
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
	public ReportsUI(double value) {
		this.Value = value;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 574, 429);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane spVulnerable = new JScrollPane();
		spVulnerable.setBounds(51, 96, 356, 233);
		contentPane.add(spVulnerable);
		
		JTextArea textArea = new JTextArea();
		spVulnerable.setViewportView(textArea);
		
		JButton btnNewButton = new JButton("Report");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 DecimalFormat df = new DecimalFormat("###.###");
				 
				textArea.setLineWrap(true); 
					textArea.append("Your mobile application has a combined taint ratio of: " + df.format(Value)); 
					textArea.append("\n");		
					textArea.append("The presence of SQL injection vulnerability has been found to be: ");
					textArea.append("\n");		
					textArea.append("For individual details on each file, please look to PDF reports generated");		
				
			}
		});
		btnNewButton.setBounds(224, 363, 89, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Overall Report Summary");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel.setBounds(51, 71, 193, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_2 = new JLabel("Mobile Application Security Analyser");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblNewLabel_2.setBounds(142, 11, 327, 28);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Generate Report");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_3.setBounds(99, 357, 115, 33);
		contentPane.add(lblNewLabel_3);
	}
}
