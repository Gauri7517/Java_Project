package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TypingSpeedTest {

    static int timeLeft = 60;
    static javax.swing.Timer timer;
    static long startTime;

    static String[] paragraphs = {
            "Java is a powerful programming language used for building applications.",
            "Practice typing daily to improve your typing speed and accuracy.",
            "Software development requires logical thinking and problem solving skills.",
            "Consistency and patience are important for mastering programming."
    };

    public static void main(String[] args) {

        JFrame frame = new JFrame("Typing Speed Test - Pro");
        frame.setSize(850, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(18, 18, 18));
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Title
        JLabel title = new JLabel("Typing Speed Test", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(new Color(0, 255, 200));
        frame.add(title, gbc);

        // Paragraph Area
        gbc.gridy++;
        JTextArea paragraph = new JTextArea();
        paragraph.setFont(new Font("Consolas", Font.PLAIN, 18));
        paragraph.setLineWrap(true);
        paragraph.setWrapStyleWord(true);
        paragraph.setEditable(false);
        paragraph.setBackground(new Color(35, 35, 35));
        paragraph.setForeground(Color.WHITE);
        paragraph.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        paragraph.setPreferredSize(new Dimension(700, 100));
        frame.add(paragraph, gbc);

        // Input Area
        gbc.gridy++;
        JTextArea inputArea = new JTextArea();
        inputArea.setFont(new Font("Consolas", Font.PLAIN, 18));
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setBackground(new Color(50, 50, 50));
        inputArea.setForeground(Color.WHITE);
        inputArea.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        inputArea.setPreferredSize(new Dimension(700, 120));
        frame.add(inputArea, gbc);

        // Progress Bar
        gbc.gridy++;
        JProgressBar progressBar = new JProgressBar(0, 60);
        progressBar.setValue(60);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0, 200, 150));
        frame.add(progressBar, gbc);

        // Stats Panel
        gbc.gridy++;
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(new Color(18, 18, 18));

        JLabel timerLabel = new JLabel("Time: 60");
        timerLabel.setForeground(Color.ORANGE);

        JLabel wpmLabel = new JLabel("WPM: 0");
        wpmLabel.setForeground(Color.CYAN);

        JLabel accuracyLabel = new JLabel("Accuracy: 0%");
        accuracyLabel.setForeground(Color.GREEN);

        statsPanel.add(timerLabel);
        statsPanel.add(Box.createHorizontalStrut(40));
        statsPanel.add(wpmLabel);
        statsPanel.add(Box.createHorizontalStrut(40));
        statsPanel.add(accuracyLabel);

        frame.add(statsPanel, gbc);

        // Result Label
        gbc.gridy++;
        JLabel resultLabel = new JLabel("Result: ", SwingConstants.CENTER);
        resultLabel.setForeground(Color.YELLOW);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        frame.add(resultLabel, gbc);

        // Button Panel
        gbc.gridy++;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(18, 18, 18));

        JButton startBtn = new JButton("Start");
        JButton finishBtn = new JButton("Finish");
        JButton resetBtn = new JButton("Reset");

        startBtn.setBackground(new Color(0, 200, 150));
        finishBtn.setBackground(new Color(70,130,180));
        resetBtn.setBackground(new Color(200, 50, 50));
        resetBtn.setForeground(Color.WHITE);

        startBtn.setFocusPainted(false);
        finishBtn.setFocusPainted(false);
        resetBtn.setFocusPainted(false);

        buttonPanel.add(startBtn);
        buttonPanel.add(finishBtn);
        buttonPanel.add(resetBtn);

        frame.add(buttonPanel, gbc);

        // START BUTTON
        startBtn.addActionListener(e -> {

            if (timer != null) timer.stop();

            inputArea.setText("");
            resultLabel.setText("Result: ");
            timeLeft = 60;
            timerLabel.setText("Time: 60");
            progressBar.setValue(60);

            Random random = new Random();
            String selectedText = paragraphs[random.nextInt(paragraphs.length)];
            paragraph.setText(selectedText);

            startTime = System.currentTimeMillis();

            timer = new javax.swing.Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    timeLeft--;
                    timerLabel.setText("Time: " + timeLeft);
                    progressBar.setValue(timeLeft);

                    updateStats(selectedText, inputArea.getText(),
                            wpmLabel, accuracyLabel);

                    if (timeLeft <= 0) {
                        timer.stop();
                        showResultPopup(frame, wpmLabel.getText(),
                                accuracyLabel.getText());
                    }
                }
            });

            timer.start();
        });

        // FINISH BUTTON
        finishBtn.addActionListener(e -> {
            if (timer != null) timer.stop();
            showResultPopup(frame, wpmLabel.getText(),
                    accuracyLabel.getText());
        });

        // RESET
        resetBtn.addActionListener(e -> {
            if (timer != null) timer.stop();
            inputArea.setText("");
            paragraph.setText("");
            resultLabel.setText("Result: ");
            timerLabel.setText("Time: 60");
            wpmLabel.setText("WPM: 0");
            accuracyLabel.setText("Accuracy: 0%");
            progressBar.setValue(60);
        });

        frame.setVisible(true);
    }

    public static void updateStats(String original, String typed,
                                   JLabel wpmLabel, JLabel accuracyLabel) {

        long currentTime = System.currentTimeMillis();
        double minutes = (currentTime) / 60000.0;

        int words = typed.trim().isEmpty() ? 0 : typed.trim().split("\\s+").length;
        int wpm = minutes > 0 ? words : 0;

        int correctChars = 0;
        for (int i = 0; i < Math.min(original.length(), typed.length()); i++) {
            if (original.charAt(i) == typed.charAt(i)) {
                correctChars++;
            }
        }

        double accuracy = original.length() > 0 ?
                (correctChars * 100.0) / original.length() : 0;

        wpmLabel.setText("WPM: " + wpm);
        accuracyLabel.setText("Accuracy: " + (int) accuracy + "%");
    }

    public static void showResultPopup(JFrame frame, String wpm, String accuracy) {
        JOptionPane.showMessageDialog(frame,
                "🏆 Test Completed!\n\n" + wpm + "\n" + accuracy,
                "Final Result",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
