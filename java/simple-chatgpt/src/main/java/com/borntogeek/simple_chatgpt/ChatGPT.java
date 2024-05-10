package com.borntogeek.simple_chatgpt;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import dev.langchain4j.model.openai.OpenAiChatModel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ChatGPT extends JFrame {

	private static final String CHAT_GPT_API_KEY = "demo";

	private JTextArea promptTextArea;
	private JTextArea responseTextArea;

	public ChatGPT() {
		setTitle("ChatGPT");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initLayout();
		pack();

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - getHeight()) / 2);
		setLocation(x, y);
	}

	private void initLayout() {
		Container container = getContentPane();
		container.setLayout(new MigLayout());

		container.add(new JLabel("Prompt"), "wrap");
		promptTextArea = new JTextArea(5, 50);
		promptTextArea.setFont(promptTextArea.getFont().deriveFont(18f));
		container.add(promptTextArea, "wrap");
		container.add(createConfirmButton(), "wrap");

		container.add(new JLabel("Response"), "wrap");
		responseTextArea = new JTextArea(15, 50);
		responseTextArea.setFont(responseTextArea.getFont().deriveFont(18f));
		responseTextArea.setLineWrap(true);
		responseTextArea.setWrapStyleWord(true);
		JScrollPane responseTextAreaScrollPane = new JScrollPane(responseTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		container.add(responseTextAreaScrollPane, "wrap");
	}

	private JButton createConfirmButton() {
		JButton confirmButton = new JButton("Confirm");
		
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String response = getResponseFromPrompt(promptTextArea.getText());
				responseTextArea.setText(response);
				promptTextArea.setText("");
				promptTextArea.grabFocus();
			}
		});

		return confirmButton;
	}

	private String getResponseFromPrompt(String prompt) {
		OpenAiChatModel model = OpenAiChatModel.withApiKey(CHAT_GPT_API_KEY);
		return model.generate(prompt);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ChatGPT chatGpt = new ChatGPT();
				chatGpt.setVisible(true);
			}
		});
	}

}
