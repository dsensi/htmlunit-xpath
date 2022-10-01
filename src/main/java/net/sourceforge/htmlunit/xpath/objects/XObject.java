/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the  "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.htmlunit.xpath.objects;

import java.io.Serializable;
import net.sourceforge.htmlunit.xpath.Expression;
import net.sourceforge.htmlunit.xpath.ExpressionOwner;
import net.sourceforge.htmlunit.xpath.NodeSetDTM;
import net.sourceforge.htmlunit.xpath.XPathContext;
import net.sourceforge.htmlunit.xpath.XPathException;
import net.sourceforge.htmlunit.xpath.XPathVisitor;
import net.sourceforge.htmlunit.xpath.res.XPATHErrorResources;
import net.sourceforge.htmlunit.xpath.res.XSLMessages;
import net.sourceforge.htmlunit.xpath.xml.dtm.DTMIterator;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

/**
 * This class represents an XPath object, and is capable of converting the object to various types,
 * such as a string. This class acts as the base class to other XPath type objects, such as XString,
 * and provides polymorphic casting capabilities.
 */
public class XObject extends Expression implements Serializable, Cloneable {
  static final long serialVersionUID = -821887098985662951L;

  /**
   * The java object which this object wraps.
   *
   * @serial
   */
  protected Object m_obj; // This may be NULL!!!

  /** Create an XObject. */
  public XObject() {}

  /**
   * Create an XObject.
   *
   * @param obj Can be any object, should be a specific type for derived classes, or null.
   */
  public XObject(Object obj) {
    setObject(obj);
  }

  protected void setObject(Object obj) {
    m_obj = obj;
  }

  /**
   * For support of literal objects in xpaths.
   *
   * @param xctxt The XPath execution context.
   * @return This object.
   * @throws javax.xml.transform.TransformerException
   */
  @Override
  public XObject execute(XPathContext xctxt) throws javax.xml.transform.TransformerException {
    return this;
  }

  /**
   * Specify if it's OK for detach to release the iterator for reuse. This function should be called
   * with a value of false for objects that are stored in variables. Calling this with a value of
   * false on a XNodeSet will cause the nodeset to be cached.
   *
   * @param allowRelease true if it is OK for detach to release this iterator for pooling.
   */
  public void allowDetachToRelease(boolean allowRelease) {}

  /**
   * Detaches the <code>DTMIterator</code> from the set which it iterated over, releasing any
   * computational resources and placing the iterator in the INVALID state. After <code>detach
   * </code> has been invoked, calls to <code>nextNode</code> or <code>previousNode</code> will
   * raise a runtime exception.
   */
  public void detach() {}

  /** Forces the object to release it's resources. This is more harsh than detach(). */
  public void destruct() {

    if (null != m_obj) {
      allowDetachToRelease(true);
      detach();

      setObject(null);
    }
  }

  /** Reset for fresh reuse. */
  public void reset() {}

  /** Constant for NULL object type */
  public static final int CLASS_NULL = -1;

  /** Constant for UNKNOWN object type */
  public static final int CLASS_UNKNOWN = 0;

  /** Constant for BOOLEAN object type */
  public static final int CLASS_BOOLEAN = 1;

  /** Constant for NUMBER object type */
  public static final int CLASS_NUMBER = 2;

  /** Constant for STRING object type */
  public static final int CLASS_STRING = 3;

  /** Constant for NODESET object type */
  public static final int CLASS_NODESET = 4;

  /** Constant for RESULT TREE FRAGMENT object type */
  public static final int CLASS_RTREEFRAG = 5;

  /** Represents an unresolved variable type as an integer. */
  public static final int CLASS_UNRESOLVEDVARIABLE = 600;

  /**
   * Tell what kind of class this is.
   *
   * @return CLASS_UNKNOWN
   */
  public int getType() {
    return CLASS_UNKNOWN;
  }

  /**
   * Given a request type, return the equivalent string. For diagnostic purposes.
   *
   * @return type string "#UNKNOWN" + object class name
   */
  public String getTypeString() {
    return "#UNKNOWN (" + object().getClass().getName() + ")";
  }

  /**
   * Cast result object to a number. Always issues an error.
   *
   * @return 0.0
   * @throws javax.xml.transform.TransformerException
   */
  public double num() throws javax.xml.transform.TransformerException {

    error(
        XPATHErrorResources.ER_CANT_CONVERT_TO_NUMBER,
        new Object[] {getTypeString()}); // "Can not convert
    // "+getTypeString()+"
    // to a number");

    return 0.0;
  }

  /**
   * Cast result object to a number, but allow side effects, such as the incrementing of an
   * iterator.
   *
   * @return numeric value of the string conversion from the next node in the NodeSetDTM, or NAN if
   *     no node was found
   */
  public double numWithSideEffects() throws javax.xml.transform.TransformerException {
    return num();
  }

  /**
   * Cast result object to a boolean. Always issues an error.
   *
   * @return false
   * @throws javax.xml.transform.TransformerException
   */
  public boolean bool() throws javax.xml.transform.TransformerException {

    error(
        XPATHErrorResources.ER_CANT_CONVERT_TO_NUMBER,
        new Object[] {getTypeString()}); // "Can not convert
    // "+getTypeString()+"
    // to a number");

    return false;
  }

  /**
   * Cast result object to a boolean, but allow side effects, such as the incrementing of an
   * iterator.
   *
   * @return True if there is a next node in the nodeset
   */
  public boolean boolWithSideEffects() throws javax.xml.transform.TransformerException {
    return bool();
  }

  /**
   * Cast result object to a string.
   *
   * @return The string this wraps or the empty string if null
   */
  public XString xstr() {
    return new XString(str());
  }

  /**
   * Cast result object to a string.
   *
   * @return The object as a string
   */
  public String str() {
    return (m_obj != null) ? m_obj.toString() : "";
  }

  /**
   * Return the string representation of the object
   *
   * @return the string representation of the object
   */
  @Override
  public String toString() {
    return str();
  }

  /**
   * Return a java object that's closest to the representation that should be handed to an
   * extension.
   *
   * @return The object that this class wraps
   */
  public Object object() {
    return m_obj;
  }

  /**
   * Cast result object to a nodelist. Always issues an error.
   *
   * @return null
   * @throws javax.xml.transform.TransformerException
   */
  public DTMIterator iter() throws javax.xml.transform.TransformerException {

    error(
        XPATHErrorResources.ER_CANT_CONVERT_TO_NODELIST,
        new Object[] {getTypeString()}); // "Can not convert
    // "+getTypeString()+"
    // to a NodeList!");

    return null;
  }

  /**
   * Get a fresh copy of the object. For use with variables.
   *
   * @return This object, unless overridden by subclass.
   */
  public XObject getFresh() {
    return this;
  }

  /**
   * Cast result object to a nodelist. Always issues an error.
   *
   * @return null
   * @throws javax.xml.transform.TransformerException
   */
  public NodeIterator nodeset() throws javax.xml.transform.TransformerException {

    error(
        XPATHErrorResources.ER_CANT_CONVERT_TO_NODELIST,
        new Object[] {getTypeString()}); // "Can not convert
    // "+getTypeString()+"
    // to a NodeList!");

    return null;
  }

  /**
   * Cast result object to a nodelist. Always issues an error.
   *
   * @return null
   * @throws javax.xml.transform.TransformerException
   */
  public NodeList nodelist() throws javax.xml.transform.TransformerException {

    error(
        XPATHErrorResources.ER_CANT_CONVERT_TO_NODELIST,
        new Object[] {getTypeString()}); // "Can not convert
    // "+getTypeString()+"
    // to a NodeList!");

    return null;
  }

  /**
   * Cast result object to a nodelist. Always issues an error.
   *
   * @return The object as a NodeSetDTM.
   * @throws javax.xml.transform.TransformerException
   */
  public NodeSetDTM mutableNodeset() throws javax.xml.transform.TransformerException {

    error(
        XPATHErrorResources.ER_CANT_CONVERT_TO_MUTABLENODELIST,
        new Object[] {getTypeString()}); // "Can not
    // convert
    // "+getTypeString()+"
    // to a
    // NodeSetDTM!");

    return (NodeSetDTM) m_obj;
  }

  /**
   * Cast object to type t.
   *
   * @param t Type of object to cast this to
   * @param support XPath context to use for the conversion
   * @return This object as the given type t
   * @throws javax.xml.transform.TransformerException
   */
  public Object castToType(int t, XPathContext support)
      throws javax.xml.transform.TransformerException {

    Object result;

    switch (t) {
      case CLASS_STRING:
        result = str();
        break;
      case CLASS_NUMBER:
        result = new Double(num());
        break;
      case CLASS_NODESET:
        result = iter();
        break;
      case CLASS_BOOLEAN:
        result = bool() ? Boolean.TRUE : Boolean.FALSE;
        break;
      case CLASS_UNKNOWN:
        result = m_obj;
        break;

      default:
        error(
            XPATHErrorResources.ER_CANT_CONVERT_TO_TYPE,
            new Object[] {getTypeString(), Integer.toString(t)}); // "Can
        // not
        // convert
        // "+getTypeString()+"
        // to
        // a
        // type#"+t);

        result = null;
    }

    return result;
  }

  /**
   * Tell if one object is less than the other.
   *
   * @param obj2 Object to compare this to
   * @return True if this object is less than the given object
   * @throws javax.xml.transform.TransformerException
   */
  public boolean lessThan(XObject obj2) throws javax.xml.transform.TransformerException {

    // In order to handle the 'all' semantics of
    // nodeset comparisons, we always call the
    // nodeset function. Because the arguments
    // are backwards, we call the opposite comparison
    // function.
    if (obj2.getType() == XObject.CLASS_NODESET) return obj2.greaterThan(this);

    return this.num() < obj2.num();
  }

  /**
   * Tell if one object is less than or equal to the other.
   *
   * @param obj2 Object to compare this to
   * @return True if this object is less than or equal to the given object
   * @throws javax.xml.transform.TransformerException
   */
  public boolean lessThanOrEqual(XObject obj2) throws javax.xml.transform.TransformerException {

    // In order to handle the 'all' semantics of
    // nodeset comparisons, we always call the
    // nodeset function. Because the arguments
    // are backwards, we call the opposite comparison
    // function.
    if (obj2.getType() == XObject.CLASS_NODESET) return obj2.greaterThanOrEqual(this);

    return this.num() <= obj2.num();
  }

  /**
   * Tell if one object is greater than the other.
   *
   * @param obj2 Object to compare this to
   * @return True if this object is greater than the given object
   * @throws javax.xml.transform.TransformerException
   */
  public boolean greaterThan(XObject obj2) throws javax.xml.transform.TransformerException {

    // In order to handle the 'all' semantics of
    // nodeset comparisons, we always call the
    // nodeset function. Because the arguments
    // are backwards, we call the opposite comparison
    // function.
    if (obj2.getType() == XObject.CLASS_NODESET) return obj2.lessThan(this);

    return this.num() > obj2.num();
  }

  /**
   * Tell if one object is greater than or equal to the other.
   *
   * @param obj2 Object to compare this to
   * @return True if this object is greater than or equal to the given object
   * @throws javax.xml.transform.TransformerException
   */
  public boolean greaterThanOrEqual(XObject obj2) throws javax.xml.transform.TransformerException {

    // In order to handle the 'all' semantics of
    // nodeset comparisons, we always call the
    // nodeset function. Because the arguments
    // are backwards, we call the opposite comparison
    // function.
    if (obj2.getType() == XObject.CLASS_NODESET) return obj2.lessThanOrEqual(this);

    return this.num() >= obj2.num();
  }

  /**
   * Tell if two objects are functionally equal.
   *
   * @param obj2 Object to compare this to
   * @return True if this object is equal to the given object
   * @throws javax.xml.transform.TransformerException
   */
  public boolean equals(XObject obj2) {

    // In order to handle the 'all' semantics of
    // nodeset comparisons, we always call the
    // nodeset function.
    if (obj2.getType() == XObject.CLASS_NODESET) return obj2.equals(this);

    if (null != m_obj) {
      return m_obj.equals(obj2.m_obj);
    } else {
      return obj2.m_obj == null;
    }
  }

  /**
   * Tell if two objects are functionally not equal.
   *
   * @param obj2 Object to compare this to
   * @return True if this object is not equal to the given object
   * @throws javax.xml.transform.TransformerException
   */
  public boolean notEquals(XObject obj2) throws javax.xml.transform.TransformerException {

    // In order to handle the 'all' semantics of
    // nodeset comparisons, we always call the
    // nodeset function.
    if (obj2.getType() == XObject.CLASS_NODESET) return obj2.notEquals(this);

    return !equals(obj2);
  }

  /**
   * Tell the user of an error, and probably throw an exception.
   *
   * @param msg Error message to issue
   * @throws javax.xml.transform.TransformerException
   */
  protected void error(String msg) throws javax.xml.transform.TransformerException {
    error(msg, null);
  }

  /**
   * Tell the user of an error, and probably throw an exception.
   *
   * @param msg Error message to issue
   * @param args Arguments to use in the message
   * @throws javax.xml.transform.TransformerException
   */
  protected void error(String msg, Object[] args) throws javax.xml.transform.TransformerException {

    String fmsg = XSLMessages.createXPATHMessage(msg, args);

    // boolean shouldThrow = support.problem(m_support.XPATHPROCESSOR,
    // m_support.ERROR,
    // null,
    // null, fmsg, 0, 0);
    // if(shouldThrow)
    {
      throw new XPathException(fmsg, this);
    }
  }

  /**
   * @see net.sourceforge.htmlunit.xpath.XPathVisitable#callVisitors(ExpressionOwner, XPathVisitor)
   */
  @Override
  public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
    assertion(false, "callVisitors should not be called for this object!!!");
  }

  /** @see Expression#deepEquals(Expression) */
  @Override
  public boolean deepEquals(Expression expr) {
    if (!isSameClass(expr)) return false;

    // If equals at the expression level calls deepEquals, I think we're
    // still safe from infinite recursion since this object overrides
    // equals. I hope.
    if (!this.equals((XObject) expr)) return false;

    return true;
  }
}
