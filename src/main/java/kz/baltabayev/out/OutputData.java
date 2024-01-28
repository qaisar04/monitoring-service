package kz.baltabayev.out;

/**
 * The interface Output data.
 */
public interface OutputData {

    /**
     * Output.
     *
     * @param data the data
     */
    void output(Object data);

    /**
     * Err output.
     *
     * @param data the data
     */
    void errOutput(Object data);
}
