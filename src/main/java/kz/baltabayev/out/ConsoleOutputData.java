package kz.baltabayev.out;

/**
 * Implementation of the {@link OutputData} interface for console output.
 */
public class ConsoleOutputData implements OutputData {
    /**
     * Outputs the provided data to the console.
     *
     * @param data the data to be printed to the console
     */
    @Override
    public void output(Object data) {
        System.out.println(data.toString());
    }

    /**
     * Outputs the provided data as an error message to the console.
     *
     * @param data the data to be printed as an error message
     */
    @Override
    public void errOutput(Object data) {
        System.err.println(data.toString());
    }
}
