import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;


public class CurrencyConverter implements ActionListener{

	// UI Components
	private JFrame frame;
	private JPanel panel;
	private JLabel title;
	
	private JLabel convertTitle;
	private JLabel fromTitle;
	private JLabel toTitle;
	
	private JTextField submitField;
	private JComboBox<Object> fromCombo;
	private JComboBox<Object> toCombo;
	
	private JLabel resultLabel;
	private JTextField resultField; 
	
	private JButton convertButton;
	
	// Build UI
	CurrencyConverter(){
		frame = new JFrame();
		panel = new JPanel();
		panel.setLayout(null);
		
		title = new JLabel("Currency Converter");
		title.setBounds(125,10,300,20);
		panel.add(title);
		
		convertTitle = new JLabel("Convert:");
		convertTitle.setBounds(20,50,60,20);
		panel.add(convertTitle);
		
		submitField = new JTextField("1");
		submitField.setBounds(20,75,60,20);
		panel.add(submitField);
		
		fromTitle = new JLabel("From:");
		fromTitle.setBounds(90,50,60,20);
		panel.add(fromTitle);
		
		toTitle = new JLabel("To:");
		toTitle.setBounds(230,50,60,20);
		panel.add(toTitle);
				
		String[] currencies = {"Pound [GBP]","Dollar [USD]","Euro [EUR]","Yen [JPY]"};
		fromCombo = new JComboBox<Object>(currencies);
		fromCombo.setBounds(90,75,130,20);
		fromCombo.setSelectedIndex(0);
		panel.add(fromCombo);
		
		toCombo = new JComboBox<Object>(currencies);
		toCombo.setBounds(230,75,130,20);
		toCombo.setSelectedIndex(1);
		panel.add(toCombo);

		resultLabel = new JLabel("Result:");
		resultLabel.setBounds(40,180,60,20);
		panel.add(resultLabel);
		
		resultField = new JTextField("");
		resultField.setEditable(false);
		resultField.setBounds(90,180,100,20);
		panel.add(resultField);

		convertButton = new JButton("Convert");
		convertButton.setBounds(280,180,80,20);
		convertButton.addActionListener(this);
		panel.add(convertButton);
		
		frame.add(panel);  
		frame.setTitle("Currency Converter");
        frame.setSize(400, 300);   
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setVisible(true);  
	}
	
	public static void main(String[] args) {
		new CurrencyConverter();
	}

	// When "Convert" button is pressed
	@Override
	public void actionPerformed(ActionEvent e) {
		resultField.setText("Please wait");
		float initialAmount = Float.parseFloat(submitField.getText());
		if (initialAmount < 0) {
			resultField.setText("Incorrect format");
			return;
		}
		
		
		System.out.println("Initial amount is: "+initialAmount);
		
		String fromChoice = (fromCombo.getSelectedItem().toString()).split("\\[")[1].split("\\]")[0];
		System.out.println("From: "+fromChoice);
		
		String toChoice = (toCombo.getSelectedItem().toString()).split("\\[")[1].split("\\]")[0];
		System.out.println("To: "+toChoice);
		
		// HTTP Stuff - Access currency rates with an API
		
		String GET_URL = "https://v6.exchangerate-api.com/v6/[MY API KEY]/latest/"+fromChoice;
		
		try {
			URL url = new URL(GET_URL);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.setRequestMethod("GET");
			int responseCode = request.getResponseCode();
		
			if (responseCode == HttpURLConnection.HTTP_OK) {
				System.out.println("OK");
				BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				
				while ((inputLine = in.readLine()) != null ){
					response.append(inputLine);
				} in.close();
			
				String[] values = response.toString().split(",");
				
				float toRate = 0;
				
				// Finding the currency rates
				for (int i = 0; i < values.length; i++) {
					if (values[i].startsWith(toChoice, 3)) {
						System.out.println(values[i]);
						toRate = Float.parseFloat(values[i].split(":")[1]);
					}
				}
				
				float result = initialAmount * toRate;
				resultField.setText(Float.toString(result));
				
			}
			
		} catch (Exception e1) {
			resultField.setText("Error");
		}
		
		
	}

}
