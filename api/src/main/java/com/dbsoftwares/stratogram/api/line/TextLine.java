package com.dbsoftwares.stratogram.api.line;

public interface TextLine extends HologramLine
{

    /**
     * Gets the current text content of this line.
     *
     * @return the current text content.
     */
    String getText();

    /**
     * Updates the text content of this line.
     *
     * @param text the new content for this line.
     */
    void setText( final String text );
}
