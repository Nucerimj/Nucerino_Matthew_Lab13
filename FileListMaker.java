import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileListMaker {
    private static final ArrayList<String> itemList = new ArrayList<>();
    private static boolean needsToBeSaved = false;
    private static String currentListFilename = null;

    public static void main(String[] args) {
        boolean quit = false;

        while (!quit) {
            displayMenu();
            String choice = SafeInput.getRegExString("Enter your choice (A/D/V/O/S/C/Q): ", "[AaDdVvOoSsCcQq]");

            switch (choice.toUpperCase()) {
                case "A":
                    addItem();
                    break;
                case "D":
                    deleteItem();
                    break;
                case "V":
                    viewList();
                    break;
                case "O":
                    openList();
                    break;
                case "S":
                    saveList();
                    break;
                case "C":
                    clearList();
                    break;
                case "Q":
                    quit = quitConfirmation();
                    break;
            }
        }
    }

    private static void displayMenu() {
        System.out.println("Menu:");
        System.out.println("A - Add an item to the list");
        System.out.println("D - Delete an item from the list");
        System.out.println("V - View the list");
        System.out.println("O - Open a list from disk");
        System.out.println("S - Save the list to disk");
        System.out.println("C - Clear the list");
        System.out.println("Q - Quit the program");
    }

    private static void addItem() {
        String item = SafeInput.getRegExString("Enter the item to add: ", ".*");
        itemList.add(item);
        needsToBeSaved = true;
        System.out.println("Item added successfully!");
    }

    private static void deleteItem() {
        if (itemList.isEmpty()) {
            System.out.println("The list is empty. Nothing to delete.");
            return;
        }

        viewList();
        int itemNumber = SafeInput.getRangedInt("Enter the number of the item to delete: ", 1, itemList.size());
        itemList.remove(itemNumber - 1);
        needsToBeSaved = true;
        System.out.println("Item deleted successfully!");
    }

    private static void viewList() {
        if (itemList.isEmpty()) {
            System.out.println("The list is empty.");
        } else {
            System.out.println("Current items in the list:");
            for (int i = 0; i < itemList.size(); i++) {
                System.out.println((i + 1) + ". " + itemList.get(i));
            }
        }
    }

    private static void openList() {
        if (needsToBeSaved) {
            boolean saveList = SafeInput.getYesNoInput("Save current list before opening a new one (Y/N)? ");
            if (saveList) {
                saveList();
            }
        }

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            currentListFilename = selectedFile.getName();
            itemList.clear();
            readFile(selectedFile);
            needsToBeSaved = false;
        }
    }

    private static void saveList() {
        if (currentListFilename == null) {
            currentListFilename = SafeInput.getRegExString("Enter the filename to save the list: ", ".*");
            if (!currentListFilename.endsWith(".txt")) {
                currentListFilename += ".txt";
            }
        }

        try (PrintWriter writer = new PrintWriter(currentListFilename)) {
            for (String item : itemList) {
                writer.println(item);
            }
            System.out.println("List saved successfully!");
            needsToBeSaved = false;
        } catch (IOException e) {
            System.out.println("Error saving the list: " + e.getMessage());
        }
    }

    private static void clearList() {
        itemList.clear();
        System.out.println("List cleared.");
        needsToBeSaved = true;
    }

    private static void readFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                itemList.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    private static boolean quitConfirmation() {
        boolean quitConfirmed = SafeInput.getYesNoInput("Are you sure you want to quit? Unsaved changes will be lost (Y/N)? ");
        if (quitConfirmed) {
            if (needsToBeSaved) {
                boolean saveList = SafeInput.getYesNoInput("Do you want to save the current list before quitting (Y/N)? ");
                if (saveList) {
                    saveList();
                }
            }
            System.out.println("Goodbye!");
        }
        return quitConfirmed;
    }
}