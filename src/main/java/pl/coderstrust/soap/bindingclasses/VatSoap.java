//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.09.17 at 08:50:11 PM BST 
//


package pl.coderstrust.soap.bindingclasses;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vatSoap.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="vatSoap"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="VAT_23"/&gt;
 *     &lt;enumeration value="VAT_8"/&gt;
 *     &lt;enumeration value="VAT_5"/&gt;
 *     &lt;enumeration value="VAT_0"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "vatSoap")
@XmlEnum
public enum VatSoap {

    VAT_23,
    VAT_8,
    VAT_5,
    VAT_0;

    public String value() {
        return name();
    }

    public static VatSoap fromValue(String v) {
        return valueOf(v);
    }

}
