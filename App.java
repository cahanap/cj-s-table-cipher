import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Program introduction
        System.out.println("=== CJ's Table Cipher ===");
        System.out.println("Type 'EXIT' at Plain Text prompt to quit.");

        while (true) {
            // Ask user for plain text
            System.out.print("\nPlain Text: ");
            String pt = scanner.nextLine();

            // Exit condition
            if (pt.equalsIgnoreCase("EXIT")) break;

            // Input validation: plain text must not contain numbers
            if (pt.matches(".*\\d.*")) {
                System.out.println("Error: Plaintext must not contain numbers (0-9).");
                continue;
            }

            // Ask user for key
            System.out.print("Chan's Auto Key: ");
            String keyString = scanner.nextLine().trim();

            // Exit condition
            if (keyString.equalsIgnoreCase("EXIT")) break;

            // Input validation: key must be digits 1–9 only (no 0, no letters)
            if (!keyString.matches("[1-9]+")) {
                System.out.println("Error: Key must be a positive integer composed of digits 1..9 (no 0).");
                continue;
            }

            // Number of columns is equal to the key length
            int numCols = keyString.length();

            // Calculate how many filler characters 'y' are needed
            int offsetCharLength = (pt.length() % numCols) == 0 ? 0 : numCols - (pt.length() % numCols);

            // Number of rows is total length divided by number of columns
            int numRows = (pt.length() + offsetCharLength) / numCols;

            // Show table size
            System.out.println("\nNumber of columns: " + numCols);
            System.out.println("Number of rows: " + numRows);

            // Perform encryption
            String encryptedText = encrypt(pt, keyString, numCols, numRows, offsetCharLength);
            System.out.println("\nEncrypted Text: " + encryptedText);

            // Perform decryption
            String decryptedText = decrypt(encryptedText, keyString, numCols, numRows, offsetCharLength);
            System.out.println("\nDecrypted Text: " + decryptedText);
        }

        // Goodbye message
        System.out.println("Thank You and GoodbyeE!");
        scanner.close();
    }

    // Encrypts the plain text using Table Cipher method
    public static String encrypt(String text, String keyString, int numCols, int numRows, int offsetCharLength) {
        // Add filler 'z' to complete the table if necessary
        for (int i = 0; i < offsetCharLength; i++) text += "y";

        // Fill table row by row
        char[][] td = new char[numRows][numCols];
        int ptIndex = 0;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                td[r][c] = text.charAt(ptIndex++);
            }
        }

        // Display encryption table
        System.out.println("\n====== Encryption Table ======");
        displayTable(td, numCols, numRows);

        // Read table column by column according to key order
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < keyString.length(); i++) {
            int col = Character.getNumericValue(keyString.charAt(i));
            if (col < 1 || col > numCols) {
                throw new IllegalArgumentException("Key digit out of range: " + col);
            }
            for (int r = 0; r < numRows; r++) {
                encrypted.append(td[r][col - 1]);
            }
        }
        return encrypted.toString();
    }

    // Decrypts the ciphertext back into the original message
    public static String decrypt(String text, String keyString, int numCols, int numRows, int offsetCharLength) {
        // Check if ciphertext length matches expected size
        if (text.length() != numCols * numRows) {
            System.out.println("Warning: ciphertext length mismatch; expected " + (numCols * numRows) + " got " + text.length());
        }

        System.out.println("\n====== Decryption Table ======");
        
        // Split ciphertext into groups (columns)
        String[] groups = new String[numCols];
        int idx = 0;
        for (int c = 0; c < numCols; c++) {
            StringBuilder sb = new StringBuilder();
            for (int r = 0; r < numRows && idx < text.length(); r++) {
                sb.append(text.charAt(idx++));
            }
            groups[c] = sb.toString();
            System.out.print("| " + groups[c] + " |  ");
        }
        System.out.println();

        // Reconstruct the table based on the key order
        char[][] td = new char[numRows][numCols];
        for (int i = 0; i < keyString.length(); i++) {
            int col = Character.getNumericValue(keyString.charAt(i));
            String g = groups[i];
            for (int r = 0; r < numRows; r++) {
                char ch = (r < g.length()) ? g.charAt(r) : 'y'; // Use filler 'y' if missing
                td[r][col - 1] = ch;
            }
        }

        // Display decryption table
        displayTable(td, numCols, numRows);

        // Read row by row to get back the original text
        StringBuilder decrypted = new StringBuilder();
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                decrypted.append(td[r][c]);
            }
        }

        // Remove filler characters at the end
        String result = decrypted.toString();
        if (offsetCharLength > 0 && offsetCharLength <= result.length()) {
            result = result.substring(0, result.length() - offsetCharLength);
        }
        return result;
    }

    // Displays a formatted table of the encryption/decryption process
    public static void displayTable(char[][] td, int numCols, int numRows) {
        // Print column headers
        System.out.print("   ");
        for (int c = 0; c < numCols; c++) System.out.print("  " + (c + 1) + " ");
        System.out.println();
        printHLineTable(numCols);

        // Print table contents row by row
        for (int r = 0; r < numRows; r++) {
            System.out.print("| " + (r + 1) + " ");
            for (int c = 0; c < numCols; c++) {
                System.out.print("| " + td[r][c] + " ");
            }
            System.out.println("|");
            printHLineTable(numCols);
        }
    }

    // Prints horizontal lines for the table formatting
    public static void printHLineTable(int numCols) {
        System.out.print("   ");
        for (int i = 0; i < numCols; i++) System.out.print("----");
        System.out.println("-");
    }
}
