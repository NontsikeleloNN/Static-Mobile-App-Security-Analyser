package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
public class TaintedUI extends JFrame {

	private JPanel contentPane;
	private static InputStream is;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) { 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TaintedUI frame = new TaintedUI(is);// <<BACKEND>>
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static InputStream setInputStream(InputStream is) {// <<BACKEND>>
		return is;
	}
	/**
	 * Create the frame.
	 * @param in 
	 */
	public TaintedUI(InputStream in) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 414);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.is = in;
		
		JButton btnNewButton = new JButton("Load Graph");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// <<BACKEND>>
				BufferedImage image;
				try {
					//issue is that is is null
					System.out.println("Input stream: "+is);
					image = ImageIO.read((is));
					ImageIcon imageIcon = new ImageIcon(image);
					System.out.println(imageIcon);
					JLabel label = new JLabel(imageIcon);
					  label.setBounds(50, 50, imageIcon.getIconWidth(), imageIcon.getIconHeight()); // Set your desired position
					getContentPane().add(label);
					pack();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // Load the image data
				
				
			}
		});
		btnNewButton.setBounds(164, 307, 139, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(185, 130, 46, 14);
		contentPane.add(lblNewLabel);
	}
}
