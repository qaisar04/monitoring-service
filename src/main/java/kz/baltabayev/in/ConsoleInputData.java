package kz.baltabayev.in;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Implementation of the {@link InputData} interface for reading input from the console.
 */
public class ConsoleInputData implements InputData {

    private final BufferedReader reader;

    /**
     * Constructs a new instance of {@code ConsoleInputData}.
     */
    public ConsoleInputData() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Reads a line of input from the console.
     *
     * @return The user input as a string.
     * @throws RuntimeException If an error occurs while reading input.
     */
    @Override
    public String input() {
        String readData;
        try {
            readData = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return readData;
    }

    /**
     * Closes the input stream associated with the console input.
     *
     * @throws RuntimeException If an error occurs while closing the input stream.
     */
    @Override
    public void closeInput() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
