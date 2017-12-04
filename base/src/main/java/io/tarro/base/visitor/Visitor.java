package io.tarro.base.visitor;

/**
 * Visit an entity discovered by a parser.
 *
 * @author Victor Schappert
 * @since 20171009
 */
public interface Visitor {
    /**
     * Invoked by the parser on all of that parser's visitors before it begins
     * parsing.
     *
     * @see #after()
     */
    default void before() { }

    /**
     * Invoked by the parser on all of that parser's visitors after it finishes
     * parsing.
     *
     * @see #after()
     */
    default void after() { }
}
