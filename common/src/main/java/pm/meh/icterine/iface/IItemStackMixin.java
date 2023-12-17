package pm.meh.icterine.iface;

/**
 * Interface for accessing custom fields of ItemStack
 */
public interface IItemStackMixin {
    /**
     * Save the previous stack size, before inventory change.
     * @param value previous value
     */
    void icterine$setPreviousStackSize(int value);

    /**
     * Get the previous stack size, before inventory change.
     * @return previous value
     */
    int icterine$getPreviousStackSize();
}
