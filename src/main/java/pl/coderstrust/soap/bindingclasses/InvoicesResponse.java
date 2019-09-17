//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.09.17 at 08:50:11 PM BST 
//


package pl.coderstrust.soap.bindingclasses;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://project-13-zuzanna-radek-mateusz-radek-przemek}responseBase"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="invoices" type="{http://project-13-zuzanna-radek-mateusz-radek-przemek}invoiceSoap" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "invoices"
})
@XmlRootElement(name = "invoicesResponse")
public class InvoicesResponse
    extends ResponseBase
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    protected List<InvoiceSoap> invoices;

    /**
     * Gets the value of the invoices property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the invoices property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInvoices().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InvoiceSoap }
     * 
     * 
     */
    public List<InvoiceSoap> getInvoices() {
        if (invoices == null) {
            invoices = new ArrayList<InvoiceSoap>();
        }
        return this.invoices;
    }

    public String toString() {
        final ToStringStrategy strategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        super.appendFields(locator, buffer, strategy);
        {
            List<InvoiceSoap> theInvoices;
            theInvoices = (((this.invoices!= null)&&(!this.invoices.isEmpty()))?this.getInvoices():null);
            strategy.appendField(locator, this, "invoices", buffer, theInvoices);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof InvoicesResponse)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!super.equals(thisLocator, thatLocator, object, strategy)) {
            return false;
        }
        final InvoicesResponse that = ((InvoicesResponse) object);
        {
            List<InvoiceSoap> lhsInvoices;
            lhsInvoices = (((this.invoices!= null)&&(!this.invoices.isEmpty()))?this.getInvoices():null);
            List<InvoiceSoap> rhsInvoices;
            rhsInvoices = (((that.invoices!= null)&&(!that.invoices.isEmpty()))?that.getInvoices():null);
            if (!strategy.equals(LocatorUtils.property(thisLocator, "invoices", lhsInvoices), LocatorUtils.property(thatLocator, "invoices", rhsInvoices), lhsInvoices, rhsInvoices)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = super.hashCode(locator, strategy);
        {
            List<InvoiceSoap> theInvoices;
            theInvoices = (((this.invoices!= null)&&(!this.invoices.isEmpty()))?this.getInvoices():null);
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "invoices", theInvoices), currentHashCode, theInvoices);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

    public Object clone() {
        return copyTo(createNewInstance());
    }

    public Object copyTo(Object target) {
        final CopyStrategy strategy = JAXBCopyStrategy.INSTANCE;
        return copyTo(null, target, strategy);
    }

    public Object copyTo(ObjectLocator locator, Object target, CopyStrategy strategy) {
        final Object draftCopy = ((target == null)?createNewInstance():target);
        super.copyTo(locator, draftCopy, strategy);
        if (draftCopy instanceof InvoicesResponse) {
            final InvoicesResponse copy = ((InvoicesResponse) draftCopy);
            if ((this.invoices!= null)&&(!this.invoices.isEmpty())) {
                List<InvoiceSoap> sourceInvoices;
                sourceInvoices = (((this.invoices!= null)&&(!this.invoices.isEmpty()))?this.getInvoices():null);
                @SuppressWarnings("unchecked")
                List<InvoiceSoap> copyInvoices = ((List<InvoiceSoap> ) strategy.copy(LocatorUtils.property(locator, "invoices", sourceInvoices), sourceInvoices));
                copy.invoices = null;
                if (copyInvoices!= null) {
                    List<InvoiceSoap> uniqueInvoicesl = copy.getInvoices();
                    uniqueInvoicesl.addAll(copyInvoices);
                }
            } else {
                copy.invoices = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new InvoicesResponse();
    }

}
