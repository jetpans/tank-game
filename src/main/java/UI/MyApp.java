package UI;

import proc.sketches.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MyApp {
    private JFrame frame;
    private JTextField textField;
    private File player1File;
    private File player2File;
    ArrayList<File> myFiles = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MyApp().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Game Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        textField = new JTextField(10);

        // Setup drag and drop
        setupDragAndDrop();

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Level:"));
        panel.add(textField);
        panel.add(startButton);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setVisible(true);
    }

    private void setupDragAndDrop() {

        JLabel first = new JLabel("Player 1");
        JLabel second = new JLabel("Player 2");
        this.frame.getContentPane().add(first, BorderLayout.WEST);

        this.frame.getContentPane().add(second, BorderLayout.EAST);
        first.setDropTarget(new DropTarget(frame, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    Transferable tr = dtde.getTransferable();
                    DataFlavor[] flavors = tr.getTransferDataFlavors();

                    for (DataFlavor flavor : flavors) {
                        if (flavor.isFlavorJavaFileListType()) {
                            dtde.acceptDrop(DnDConstants.ACTION_COPY);
                            @SuppressWarnings("unchecked")
                            List<File> files = (List<File>) tr.getTransferData(flavor);
                            myFiles.addAll(files);
                            first.setText(files.get(0).getName());
                            if (myFiles.size() == 2) {
                                handleDroppedFiles(myFiles);
                            }

                            dtde.dropComplete(true);
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dtde.rejectDrop();
                }
            }
        }));

        second.setDropTarget(new DropTarget(frame, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    Transferable tr = dtde.getTransferable();
                    DataFlavor[] flavors = tr.getTransferDataFlavors();

                    for (DataFlavor flavor : flavors) {
                        if (flavor.isFlavorJavaFileListType()) {
                            dtde.acceptDrop(DnDConstants.ACTION_COPY);
                            @SuppressWarnings("unchecked")
                            List<File> files = (List<File>) tr.getTransferData(flavor);
                            myFiles.addAll(files);
                            second.setText(files.get(0).getName());
                            if (myFiles.size() == 2) {
                                handleDroppedFiles(myFiles);
                            }

                            dtde.dropComplete(true);
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dtde.rejectDrop();
                }
            }
        }));
        ;
    }

    private void handleDroppedFiles(List<File> files) {
        if (files.size() >= 2) {
            player1File = files.get(0);
            player2File = files.get(1);
            System.out.println("Player 1 File: " + player1File.getAbsolutePath());
            System.out.println("Player 2 File: " + player2File.getAbsolutePath());
        } else {
            JOptionPane.showMessageDialog(frame, "Please drop two text files.");
        }
    }

    private void startGame() {
        if (textField.getText().isEmpty() || player1File == null || player2File == null) {
            JOptionPane.showMessageDialog(frame, "Please provide level and player files.");
            return;
        }

        String levelChoice = textField.getText();
        String[] arguments = {
                "--LevelChoice", levelChoice,
                "--Player1", player1File.getAbsolutePath(),
                "--Player2", player2File.getAbsolutePath(),
                "--TimeLimit", "10000"
        };

        // Run the Game class with the specified arguments
        Game.main(arguments);
    }
}