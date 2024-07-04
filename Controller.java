package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Controller {

    private GUI gui;
    private FileConverter fileConverter;

    public Controller(GUI gui) {
        this.gui = gui;
        this.fileConverter = new FileConverter();

        gui.getJButton1().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = gui.getJTextField1().getText().trim();


                if (filePath.startsWith("\"") && filePath.endsWith("\"")) {
                    filePath = filePath.substring(1, filePath.length() - 1);
                }

                if (isValidFile(filePath)) {
                    gui.getJProgressBar1().setVisible(true);
                    final String file = filePath;
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                        @Override
                        protected Void doInBackground() throws Exception {
                            try {
                                if (fileConverter.toCSVFile(file)) {
                                    NameGenderProcessor ngp = new NameGenderProcessor(file);
                                    if (ngp.processGender()) {
                                        JOptionPane.showMessageDialog(gui, "File cleaned!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(gui, "Error! Make sure you followed the instructions as stated", "Error!", JOptionPane.ERROR_MESSAGE);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(gui, "Please select a valid Excel file or make sure that the file is not open on Excel or make sure that the file is a valid csv file", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            gui.getJProgressBar1().setVisible(false);
                        }
                    };

                    worker.execute();
                } else {
                    JOptionPane.showMessageDialog(gui, "Invalid file path. Please enter a valid file path.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                gui.getJTextField1().setText("");
            }
        });
    }

    private boolean isValidFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
        Controller controller = new Controller(gui);
        gui.setVisible(true);
    }
}
