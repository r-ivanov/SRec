/* 
*
* This file is part of java2XML
* Copyright (C) Harsh Jain, All Rights Reserved.
* Email : harsh@harshjain.com			Website : http://www.harshjain.com/
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



import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * This is the main class which provides simple methods to generate the XML representation
 * of the source code. 
 * 
 * @author Harsh Jain
 * @version 1.0 
 */
public class Java2XML {

    public static void main(String args) {
        /*if (args.length < 1){
            System.out.println("Usage: java -jar java2xml.jar file1.java file2.java file3.java .....");
        }else
        {*/
            try{
                FileReader reader = new FileReader(args);
                
                IXMLElement element = convertToXML(reader, args);

                FileOutputStream fout = new FileOutputStream((args.replace(".java",""))+".xml");

                PrintStream pout = new PrintStream(fout);
                //System.out.println("Starting parsing");
                XMLHelper.dumpXMLElement(element, pout);
                //System.out.println("Written output to output.xml");
                pout.flush();
                fout.flush();
                pout.close();
                fout.close();
            }catch(JXMLException e){
                System.out.println(e.getMessage());
                System.exit(1);
            }catch(FileNotFoundException e){
                System.out.println(e.getMessage());
                System.exit(1);
            }catch(IOException e)
            {
                System.out.println(e.getMessage());
                System.exit(1);
            }                                                
        //}
    }


	public static void j2XML(String ficheroJava) {
		FileReader reader =null;
            try{
                reader = new FileReader(ficheroJava);                
            } catch (Exception e) {}
		try {
                IXMLElement element = convertToXML(reader, ficheroJava);
                FileOutputStream fout = new FileOutputStream( (ficheroJava.replace(".java",""))+"xml"   );
                PrintStream pout = new PrintStream(fout);
                System.out.println("Starting parsing");
                XMLHelper.dumpXMLElement(element, pout);
                System.out.println("Written output to output xml");
                pout.flush();
                fout.flush();
                pout.close();
                fout.close();
            } catch (JXMLException e){
                System.out.println(e.getMessage());
                System.exit(1);
            } catch (FileNotFoundException e){
                System.out.println(e.getMessage());
                System.exit(1);
            } catch (IOException e)
            {
                System.out.println(e.getMessage());
                System.exit(1);
            }   

	}


    
    /**
     * Convert an array of source code files to XML
     * 
     * 
     * @param javaCode
     * @param classNames
     * @return
     * @throws JXMLException
     */
    public static String java2XML (String[] javaCode, String[] classNames)
    throws JXMLException
    {
        if (javaCode.length != classNames.length)
            throw new JXMLException("Arguments should be of equal length");
        Reader[] readers = new Reader[javaCode.length];
        for (int i = 0;i<javaCode.length;i++){
            readers[i] = new StringReader(javaCode[i]);
        }
        IXMLElement xml = convertToXML(readers, classNames);
        StringWriter writer = new StringWriter();
        XMLHelper.dumpXMLElement(xml, writer);
        return writer.toString();

    }
    
    /**
     * Convert one single file to XML
     * 
     * @param javaCode
     * @param className
     * @return
     * @throws JXMLException
     */
    public static String java2XML (String javaCode, String className)
    throws JXMLException
    {

        StringReader reader = new StringReader(javaCode); 

        IXMLElement xml = convertToXML(reader, className);

        StringWriter writer = new StringWriter();

        XMLHelper.dumpXMLElement(xml, writer);

        return writer.toString();
        
    }
    
    
    
    
    /**
     * Convert an array of Readers with there classNames to XMLElement
     * 
     * @param javaCode
     * @param classNames
     * @return
     * @throws JXMLException
     */    
    public static IXMLElement convertToXML(Reader[] javaCode, String[] classNames)
    throws JXMLException
    {
        if (javaCode == null || classNames == null || javaCode.length != classNames.length)
           throw new JXMLException("Illegal Arguments passed"); 	
	 
        IXMLElement element = new XMLElement();		
        element.setName("java-source-program");    	
        for (int i=0;i<javaCode.length;i++){
            IXMLElement elem = convertToXML(javaCode[i], classNames[i]);		
            element.addChild(elem);
        }
        return element;
    }
    
    /**
     * 
     * Reads the data from reader and converts it to IXMLElement
     * 
     * @param code
     * @param className
     * @return
     * @throws JXMLException
     */
    public static IXMLElement convertToXML(Reader code, String className)
    throws JXMLException
    {
        IXMLElement javaCode = JavaParser.convert(code);
        if (className!=null)
            javaCode.setAttribute("name",className);
        return javaCode;
    }
    
}
