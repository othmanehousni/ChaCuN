package ch.epfl.chacun;


/**
 * Provides static utility methods to check method or constructor arguments.
 * This class serves as a facility to validate conditions (preconditions) for methods and constructors.
 * It is designed to support the enforcement of argument correctness across methods and constructors.
 */
public final class Preconditions {

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private Preconditions() {}

    /**
     * Ensures that an expression evaluating to a boolean value is true; otherwise throws an {@link IllegalArgumentException}.
     * This method is used to validate conditions for arguments passed to methods or constructors.
     *
     * @param shouldBeTrue The boolean expression that should be true to pass the check.
     * @throws IllegalArgumentException if {@code shouldBeTrue} is false, indicating that a method or constructor argument
     *                                  check failed.
     */
    public static void checkArgument (boolean shouldBeTrue) {
        if (!shouldBeTrue) {
            throw new IllegalArgumentException();
        }
    }
}
