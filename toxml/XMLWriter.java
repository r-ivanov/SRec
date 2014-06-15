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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import java.util.Enumeration;
import java.util.Vector;


/**
 * An XMLWriter writes XML data to a stream.
 *
 * @author Harsh Jain
 */
public class XMLWriter {
    private PrintWriter writer;

    /**
     * Creates a new XMLWriter object.
     *
     * @param writer DOCUMENT ME!
     */
    public XMLWriter(Writer writer) {
        if (writer instanceof PrintWriter) {
            this.writer = (PrintWriter) writer;
        } else {
            this.writer = new PrintWriter(writer);
        }
    }

    /**
     * Creates a new XMLWriter object.
     *
     * @param stream DOCUMENT ME!
     */
    public XMLWriter(OutputStream stream) {
        this.writer = new PrintWriter(stream);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Throwable DOCUMENT ME!
     */
    protected void finalize() throws Throwable {
        this.writer = null;
        super.finalize();
    }

    /**
     * DOCUMENT ME!
     *
     * @param xml DOCUMENT ME!
     *
     * @throws JXMLException DOCUMENT ME!
     */
    public void write(IXMLElement xml) throws JXMLException {
       try{
        this.write(xml, false, 0, true);
       }catch(IOException e){
           throw new JXMLException(e.getMessage());
       }
    }

    /**
     * DOCUMENT ME!
     *
     * @param xml DOCUMENT ME!
     * @param prettyPrint DOCUMENT ME!
     *
     * @throws JXMLException DOCUMENT ME!
     */
    public void write(IXMLElement xml, boolean prettyPrint)
        throws IOException {

            this.write(xml, prettyPrint, 0, true);

    }

    /**
     * DOCUMENT ME!
     *
     * @param xml DOCUMENT ME!
     * @param prettyPrint DOCUMENT ME!
     * @param indent DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void write(IXMLElement xml, boolean prettyPrint, int indent)
        throws IOException {
        this.write(xml, prettyPrint, indent, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param xml DOCUMENT ME!
     * @param prettyPrint DOCUMENT ME!
     * @param indent DOCUMENT ME!
     * @param collapseEmptyElements DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void write(IXMLElement xml, boolean prettyPrint, int indent, boolean collapseEmptyElements)
        throws IOException {
        if (prettyPrint) {
            for (int i = 0; i < indent; i++) {
                this.writer.print(' ');
            }
        }

        if (xml.getName() == null) {
            if (xml.getContent() != null) {
                if (prettyPrint) {
                    this.writeEncoded(xml.getContent().trim());
                    writer.println();
                } else {
                    this.writeEncoded(xml.getContent());
                }
            }
        } else {
            this.writer.print('<');
            this.writer.print(xml.getName());

            Vector nsprefixes = new Vector();

            Enumeration enu = xml.enumerateAttributeNames();

            while (enu.hasMoreElements()) {
                String key = (String) enu.nextElement();
                String value = xml.getAttribute(key, null);
                this.writer.print(" " + key + "=\"");
                this.writeEncoded(value);
                this.writer.print('"');
            }

            if ((xml.getContent() != null) && (xml.getContent().length() > 0)) {
                writer.print('>');
                this.writeEncoded(xml.getContent());
                writer.print("</" + xml.getName() + '>');

                if (prettyPrint) {
                    writer.println();
                }
            } else if (xml.hasChildren() || (!collapseEmptyElements)) {
                writer.print('>');

                if (prettyPrint) {
                    writer.println();
                }

                enu = xml.enumerateChildren();

                while (enu.hasMoreElements()) {
                    IXMLElement child = (IXMLElement) enu.nextElement();
                    this.write(child, prettyPrint, indent + 4, collapseEmptyElements);
                }

                if (prettyPrint) {
                    for (int i = 0; i < indent; i++) {
                        this.writer.print(' ');
                    }
                }

                this.writer.print("</" + xml.getName() + ">");

                if (prettyPrint) {
                    writer.println();
                }
            } else {
                this.writer.print("/>");

                if (prettyPrint) {
                    writer.println();
                }
            }
        }

        this.writer.flush();
    }

    private void writeEncoded(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            this.writer.print(c);
        }
    }
}
