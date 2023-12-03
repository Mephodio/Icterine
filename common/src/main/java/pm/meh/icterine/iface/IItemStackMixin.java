package pm.meh.icterine.iface;

/**
 * Interface for accessing custom fields of ItemStack
 */
public interface IItemStackMixin {
    /**
     * Set the value of lastChangeDecreasedStack, which indicates if last stack
     * change decreased its size, for example item was thrown.
     * @param value true if last change decreased stack size
     */
    void icterine$setLastChangeDecreasedStack(boolean value);

    /**
     * Get the value of lastChangeDecreasedStack, which indicates if last stack
     * change decreased its size, for example item was thrown.
     * @return true if last change decreased stack size
     */
    boolean icterine$isLastChangeDecreasedStack();
}
