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
 * XMLElement is the standard implementation of IXMLElement.
 *
 * @author Harsh Jain
 * @version 1.0
 */
public class XMLElement implements IXMLElement {
    /** The parent element. */
    private IXMLElement parent;

    /** The attributes of the element. */
    private Vector attributes;

    /** The child elements. */
    private Vector children;

    /** The name of the element. */
    private String name;

    /** The content of the element. */
    private String content;

    /**
     * Creates an empty element.
     */
    public XMLElement() {
        this.attributes = new Vector();
        this.children = new Vector(8);
        this.content = null;
        this.parent = null;
    }

    /**
     * Cleans up the object when it's destroyed.
     *
     * @throws Throwable DOCUMENT ME!
     */
    protected void finalize() throws Throwable {
        this.attributes.clear();
        this.attributes = null;
        this.children = null;
        this.name = null;
        this.content = null;
        this.parent = null;
        super.finalize();
    }

    /**
     * Returns the parent element. This method returns null for the root element.
     *
     * @return Parent element
     */
    public IXMLElement getParent() {
        return this.parent;
    }

    /**
     * Returns the name of the element.
     *
     * @return the name, or null if the element only contains #PCDATA.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name
     *
     * @param name the non-null name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds a child element.
     *
     * @param child the non-null child to add.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addChild(IXMLElement child) {
        if (child == null) {
            throw new IllegalArgumentException("child must not be null");
        }

        if ((child.getName() == null) && (!this.children.isEmpty())) {
            IXMLElement lastChild = (IXMLElement) this.children.lastElement();

            if (lastChild.getName() == null) {
                lastChild.setContent(lastChild.getContent() + child.getContent());

                return;
            }
        }

        ((XMLElement) child).parent = this;
        this.children.addElement(child);
    }

    /**
     * Inserts a child element.
     *
     * @param child the non-null child to add.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    /**
     * Removes a child element.
     *
     * @param child the non-null child to remove.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeChild(IXMLElement child) {
        if (child == null) {
            throw new IllegalArgumentException("child must not be null");
        }

        this.children.removeElement(child);
    }

    /**
     * Removes the child located at a certain index.
     *
     * @param index the index of the child, where the first child has index 0.
     */
    public void removeChildAtIndex(int index) {
        this.children.removeElementAt(index);
    }

    /**
     * Returns an enumeration of all child elements.
     *
     * @return the non-null enumeration
     */
    public Enumeration enumerateChildren() {
        return this.children.elements();
    }

    /**
     * Returns whether the element is a leaf element.
     *
     * @return true if the element has no children.
     */
    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    /**
     * Returns whether the element has children.
     *
     * @return true if the element has children.
     */
    public boolean hasChildren() {
        return (!this.children.isEmpty());
    }

    /**
     * Returns the number of children.
     *
     * @return the count.
     */
    public int getChildrenCount() {
        return this.children.size();
    }

    /**
     * Returns a vector containing all the child elements.
     *
     * @return the vector.
     */
    public Vector getChildren() {
        return this.children;
    }

    /**
     * Returns the child at a specific index.
     *
     * @param index the index of the child
     *
     * @return the non-null child
     *
     * @throws ArrayIndexOutOfBoundsException if the index is out of bounds.
     */
    public IXMLElement getChildAtIndex(int index) throws ArrayIndexOutOfBoundsException {
        return (IXMLElement) this.children.elementAt(index);
    }

    /**
     * Searches a child element.
     *
     * @param name the full name of the child to search for.
     *
     * @return the child element, or null if no such child was found.
     */
    public IXMLElement getFirstChildNamed(String name) {
        Enumeration enu = this.children.elements();

        while (enu.hasMoreElements()) {
            IXMLElement child = (IXMLElement) enu.nextElement();
            String childName = child.getName();

            if ((childName != null) && childName.equals(name)) {
                return child;
            }
        }

        return null;
    }

    /**
     * Returns a vector of all child elements named <I>name</I>.
     *
     * @param name the full name of the children to search for.
     *
     * @return the non-null vector of child elements.
     */
    public Vector getChildrenNamed(String name) {
        Vector result = new Vector(this.children.size());
        Enumeration enu = this.children.elements();

        while (enu.hasMoreElements()) {
            IXMLElement child = (IXMLElement) enu.nextElement();
            String childName = child.getName();

            if ((childName != null) && childName.equals(name)) {
                result.addElement(child);
            }
        }

        return result;
    }

    /**
     * Returns the number of attributes.
     *
     * @return DOCUMENT ME!
     */
    public int getAttributeCount() {
        return this.attributes.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name the non-null name of the attribute.
     *
     * @return the value, or null if the attribute does not exist.
     *
     * @deprecated As of NanoXML/Java 2.1, replaced by {@link #getAttribute(java.lang.String,java.lang.String)} Returns
     *             the value of an attribute.
     */
    public String getAttribute(String name) {
        return this.getAttribute(name, null);
    }

    /**
     * Returns the value of an attribute.
     *
     * @param name the non-null full name of the attribute.
     * @param defaultValue the default value of the attribute.
     *
     * @return the value, or defaultValue if the attribute does not exist.
     */
    public String getAttribute(String name, String defaultValue) {
        Attribute attr = searchAttribute(name);

        if (attr == null) {
            return defaultValue;
        } else {
            return attr.value;
        }
    }

    private Attribute searchAttribute(String name) {
        for (int i = 0; i < attributes.size(); i++) {
            Attribute at = (Attribute) attributes.elementAt(i);

            if (at.key.equals(name)) {
                return at;
            }
        }

        return null;
    }

    /**
     * Sets an attribute.
     *
     * @param name the non-null full name of the attribute.
     * @param value the non-null value of the attribute.
     */
    public void setAttribute(String name, String value) {
        Attribute attr = searchAttribute(name);

        if (attr == null) {
            attr = new Attribute();
            attr.key = name;
            attr.value = value;
            attributes.addElement(attr);
        } else {
            attr.value = value;
        }
    }

    /**
     * Removes an attribute.
     *
     * @param name the non-null name of the attribute.
     */
    public void removeAttribute(String name) {
        for (int i = 0; i < this.attributes.size(); i++) {
            Attribute attr = (Attribute) this.attributes.elementAt(i);

            if (attr.key.equals(name)) {
                this.attributes.removeElementAt(i);

                return;
            }
        }
    }

    /**
     * Returns an enumeration of all attribute names.
     *
     * @return the non-null enumeration.
     */
    public Enumeration enumerateAttributeNames() {
        Vector result = new Vector();
        Enumeration enu = this.attributes.elements();

        while (enu.hasMoreElements()) {
            Attribute attr = (Attribute) enu.nextElement();
            result.addElement(attr.key);
        }

        return result.elements();
    }

    /**
     * Returns whether an attribute exists.
     *
     * @param name DOCUMENT ME!
     *
     * @return true if the attribute exists.
     */
    public boolean hasAttribute(String name) {
        return searchAttribute(name) != null;
    }

    /**
     * Returns all attributes as a Properties object.
     *
     * @return the non-null set.
     */
    public Properties getAttributes() {
        Properties result = new Properties();
        Enumeration enu = this.attributes.elements();

        while (enu.hasMoreElements()) {
            Attribute attr = (Attribute) enu.nextElement();
            result.put(attr.key, attr.value);
        }

        return result;
    }

    /**
     * Returns all attributes in a specific namespace as a Properties object.
     *
     * @return the non-null set.
     */
    /**
     * Return the #PCDATA content of the element. If the element has a combination of #PCDATA content and child
     * elements, the #PCDATA sections can be retrieved as unnamed child objects. In this case, this method returns
     * null.
     *
     * @return the content.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Sets the #PCDATA content. It is an error to call this method with a non-null value if there are child objects.
     *
     * @param content the (possibly null) content.
     */
    public void setContent(String content) {
        this.content = content;
    }

    private class Attribute {
        public String key;
        public String value;
    }
}
