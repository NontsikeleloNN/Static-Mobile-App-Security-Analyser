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
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class CodeInjectionUI extends JFrame {

	private JPanel contentPane;
	CodeInjection ci;
	public static ArrayList<String> tokens = new ArrayList<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CodeInjectionUI frame = new CodeInjectionUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static ArrayList<String> setTokens(ArrayList<String> tokens) {
		return tokens;
	}

	/**
	 * Create the frame.
	 */
	public CodeInjectionUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 913, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane spVulnerable = new JScrollPane();
		spVulnerable.setBounds(51, 96, 356, 233);
		contentPane.add(spVulnerable);
		
		JTextArea textArea = new JTextArea();
		spVulnerable.setViewportView(textArea);
		
		JScrollPane spVulnerable_1 = new JScrollPane();
		spVulnerable_1.setBounds(468, 96, 389, 233);
		contentPane.add(spVulnerable_1);
		
		JTextArea textArea_1 = new JTextArea();
		spVulnerable_1.setViewportView(textArea_1);
		
		JButton btnNewButton = new JButton("Scan");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ci = new CodeInjection(tokens);
				ci.inline(); 
				textArea.setLineWrap(true);
				ArrayList<String> list = ci.getFeedback();
				ArrayList<String> list2 = (ArrayList<String>) list.stream().distinct().collect(Collectors.toList());
				for(String line : list2) {
					textArea.append(line); 
					textArea.append("\n"); 
					textArea.append("\n");
					textArea.append("\n");
				}
				textArea_1.setLineWrap(true);
				ArrayList<String> listA = ci.getAdvice();
				ArrayList<String> list2B = (ArrayList<String>) listA.stream().distinct().collect(Collectors.toList());
				for(String line :list2B) {
					textArea_1.append(line); 
					textArea_1.append("\n");
					textArea_1.append("\n");
					textArea_1.append("\n");
				}
			
				
				
			}
		});
		btnNewButton.setBounds(318, 363, 89, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Vulnerable Statments");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel.setBounds(51, 71, 141, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Recommendations");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel_1.setBounds(468, 71, 122, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("SQL Injection Analyser");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblNewLabel_2.setBounds(261, 11, 182, 28);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Scan");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_3.setBounds(261, 357, 46, 33);
		contentPane.add(lblNewLabel_3);
	}
}
