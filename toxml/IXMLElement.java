/*
 *
 * This file is part of java2XML
 * Copyright (C) Harsh Jain, All Rights Reserved.
 * Email : harsh@harshjain.com                        Website : http://www.harshjain.com/
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from the
 * use of this software.
 *
 * Permission is granted to anyone to use this software for any non-commercial
 * applications freely, subject to the following restriction.
 *
 *  1. The origin of this software must not be misrepresented; you must not
 *     claim that you wrote the original software. If you use this software in
 *     a product, an acknowledgment in the product documentation would be
 *     appreciated but is not required.
 *
 *  2. Altered source versions must be plainly marked as such, and must not be
 *     misrepresented as being the original software.
 *
 *  3. This notice must not be removed or altered from any source distribution.
 *
 * For using this software for commercial purpose please contact the author.
 *
 *
 */
package toxml;



import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;


/**
 * IXMLElement abstracts the tree structure of XML.  The interface supports only the basic operations. There is no
 * support for namespaces  in elements as well as in attributes, as they are not required.
 *
 * @author Harsh Jain
 */
public interface IXMLElement {
    /**
     * Returns the name of the element.
     *
     * @return the name, or null if the element only contains #PCDATA.
     */
    public String getName();

    /**
     * Sets the full name. This method also sets the short name and clears the namespace URI.
     *
     * @param name the non-null name.
     */
    public void setName(String name);

    /**
     * Adds a child element.
     *
     * @param child the non-null child to add.
     */
    public void addChild(IXMLElement child);

    /**
     * Removes a child element.
     *
     * @param child the non-null child to remove.
     */
    public void removeChild(IXMLElement child);

    /**
     * Removes the child located at a certain index.
     *
     * @param index the index of the child, where the first child has index 0.
     */
    public void removeChildAtIndex(int index);

    /**
     * Returns an enumeration of all child elements.
     *
     * @return the non-null enumeration
     */
    public Enumeration enumerateChildren();

    /**
     * Returns whether the element is a leaf element.
     *
     * @return true if the element has no children.
     */
    public boolean isLeaf();

    /**
     * Returns whether the element has children.
     *
     * @return true if the element has children.
     */
    public boolean hasChildren();

    /**
     * Returns the number of children.
     *
     * @return the count.
     */
    public int getChildrenCount();

    /**
     * Returns a vector containing all the child elements.
     *
     * @return the vector.
     */
    public Vector getChildren();

    /**
     * Returns the child at a specific index.
     *
     * @param index the index of the child
     *
     * @return the non-null child
     *
     * @throws ArrayIndexOutOfBoundsException if the index is out of bounds.
     */
    public IXMLElement getChildAtIndex(int index) throws ArrayIndexOutOfBoundsException;

    /**
     * Searches a child element.
     *
     * @param name the full name of the child to search for.
     *
     * @return the child element, or null if no such child was found.
     */
    public IXMLElement getFirstChildNamed(String name);

    /**
     * Returns a vector of all child elements named <I>name</I>.
     *
     * @param name the full name of the children to search for.
     *
     * @return the non-null vector of child elements.
     */
    public Vector getChildrenNamed(String name);

    /**
     * Returns the number of attributes.
     *
     * @return number of attributes
     */
    public int getAttributeCount();

    /**
     * Returns the value of an attribute.
     *
     * @param name the non-null name of the attribute.
     *
     * @return the value, or null if the attribute does not exist.
     */
    public String getAttribute(String name);

    /**
     * Returns the value of an attribute.
     *
     * @param name the non-null full name of the attribute.
     * @param defaultValue the default value of the attribute.
     *
     * @return the value, or defaultValue if the attribute does not exist.
     */
    public String getAttribute(String name, String defaultValue);

    /**
     * Sets an attribute.
     *
     * @param name the non-null full name of the attribute.
     * @param value the non-null value of the attribute.
     */
    public void setAttribute(String name, String value);

    /**
     * Removes an attribute.
     *
     * @param name the non-null name of the attribute.
     */
    public void removeAttribute(String name);

    /**
     * Returns an enumeration of all attribute names.
     *
     * @return the non-null enumeration.
     */
    public Enumeration enumerateAttributeNames();

    /**
     * Returns whether an attribute exists.
     *
     * @param name the non-null name of the attribute.
     *
     * @return true if the attribute exists.
     */
    public boolean hasAttribute(String name);

    /**
     * Returns all attributes as a Properties object.
     *
     * @return the non-null set.
     */
    public Properties getAttributes();

    /**
     * Return the #PCDATA content of the element.
     *
     * @return the content.
     */
    public String getContent();

    /**
     * Sets the #PCDATA content.
     *
     * @param content the (possibly null) content.
     */
    public void setContent(String content);
}
