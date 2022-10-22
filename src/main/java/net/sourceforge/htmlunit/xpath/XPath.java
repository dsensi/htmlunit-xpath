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
package net.sourceforge.htmlunit.xpath;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import net.sourceforge.htmlunit.xpath.compiler.Compiler;
import net.sourceforge.htmlunit.xpath.compiler.FunctionTable;
import net.sourceforge.htmlunit.xpath.compiler.XPathParser;
import net.sourceforge.htmlunit.xpath.objects.XObject;
import net.sourceforge.htmlunit.xpath.res.XPATHErrorResources;
import net.sourceforge.htmlunit.xpath.res.XPATHMessages;
import net.sourceforge.htmlunit.xpath.xml.utils.PrefixResolver;

/**
 * The XPath class wraps an expression object and provides general services for execution of that
 * expression.
 */
public class XPath {

  /**
   * The top of the expression tree.
   *
   * @serial
   */
  private final Expression m_mainExp;

  /** The function table for xpath build-in functions */
  private transient FunctionTable m_funcTable = null;

  /** initial the function table */
  private void initFunctionTable() {
    m_funcTable = new FunctionTable();
  }

  /**
   * Get the SourceLocator on the expression object.
   *
   * @return the SourceLocator on the expression object, which may be null.
   */
  public SourceLocator getLocator() {
    return m_mainExp;
  }

  /** Represents a select type expression. */
  public static final int SELECT = 0;

  /** Represents a match type expression. */
  public static final int MATCH = 1;

  /**
   * Construct an XPath object.
   *
   * <p>(Needs review -sc) This method initializes an XPathParser/ Compiler and compiles the
   * expression.
   *
   * @param exprString The XPath expression.
   * @param prefixResolver A prefix resolver to use to resolve prefixes to namespace URIs.
   * @param type one of {@link #SELECT} or {@link #MATCH}.
   * @param errorListener The error listener, or null if default should be used.
   * @throws javax.xml.transform.TransformerException if syntax or other error.
   */
  public XPath(
      String exprString, PrefixResolver prefixResolver, int type, ErrorListener errorListener)
      throws javax.xml.transform.TransformerException {
    initFunctionTable();
    if (null == errorListener)
      errorListener = new net.sourceforge.htmlunit.xpath.xml.utils.DefaultErrorHandler();

    XPathParser parser = new XPathParser(errorListener);
    Compiler compiler = new Compiler(errorListener, m_funcTable);

    if (SELECT == type) parser.initXPath(compiler, exprString, prefixResolver);
    else if (MATCH == type) parser.initMatchPattern(compiler, exprString, prefixResolver);
    else
      throw new RuntimeException(
          XPATHMessages.createXPATHMessage(
              XPATHErrorResources.ER_CANNOT_DEAL_XPATH_TYPE,
              new Object[] {Integer.toString(type)})); // "Can not deal with XPath type: " + type);

    // System.out.println("----------------");
    m_mainExp = compiler.compile(0);
  }

  /**
   * Construct an XPath object.
   *
   * <p>(Needs review -sc) This method initializes an XPathParser/ Compiler and compiles the
   * expression.
   *
   * @param exprString The XPath expression.
   * @param prefixResolver A prefix resolver to use to resolve prefixes to namespace URIs.
   * @param type one of {@link #SELECT} or {@link #MATCH}.
   * @param errorListener The error listener, or null if default should be used.
   * @param aTable the function table to be used
   * @throws javax.xml.transform.TransformerException if syntax or other error.
   */
  public XPath(
      String exprString,
      PrefixResolver prefixResolver,
      int type,
      ErrorListener errorListener,
      FunctionTable aTable)
      throws javax.xml.transform.TransformerException {
    m_funcTable = aTable;
    if (null == errorListener)
      errorListener = new net.sourceforge.htmlunit.xpath.xml.utils.DefaultErrorHandler();

    XPathParser parser = new XPathParser(errorListener);
    Compiler compiler = new Compiler(errorListener, m_funcTable);

    if (SELECT == type) parser.initXPath(compiler, exprString, prefixResolver);
    else if (MATCH == type) parser.initMatchPattern(compiler, exprString, prefixResolver);
    else
      throw new RuntimeException(
          XPATHMessages.createXPATHMessage(
              XPATHErrorResources.ER_CANNOT_DEAL_XPATH_TYPE,
              new Object[] {Integer.toString(type)}));
    // "Can not deal with XPath type: " + type);

    // System.out.println("----------------");
    m_mainExp = compiler.compile(0);
  }

  /**
   * Construct an XPath object.
   *
   * <p>(Needs review -sc) This method initializes an XPathParser/ Compiler and compiles the
   * expression.
   *
   * @param exprString The XPath expression.
   * @param prefixResolver A prefix resolver to use to resolve prefixes to namespace URIs.
   * @param type one of {@link #SELECT} or {@link #MATCH}.
   * @throws javax.xml.transform.TransformerException if syntax or other error.
   */
  public XPath(String exprString, PrefixResolver prefixResolver, int type)
      throws javax.xml.transform.TransformerException {
    this(exprString, prefixResolver, type, null);
  }

  /**
   * Construct an XPath object.
   *
   * @param expr The Expression object.
   */
  public XPath(Expression expr) {
    m_mainExp = expr;
    initFunctionTable();
  }

  /**
   * Given an expression and a context, evaluate the XPath and return the result.
   *
   * @param xctxt The execution context.
   * @param contextNode The node that "." expresses.
   * @param namespaceContext The context in which namespaces in the XPath are supposed to be
   *     expanded.
   * @return The result of the XPath or null if callbacks are used.
   * @throws TransformerException thrown if the error condition is severe enough to halt processing.
   * @throws javax.xml.transform.TransformerException in case of error
   */
  public XObject execute(
      XPathContext xctxt, org.w3c.dom.Node contextNode, PrefixResolver namespaceContext)
      throws javax.xml.transform.TransformerException {
    return execute(xctxt, xctxt.getDTMHandleFromNode(contextNode), namespaceContext);
  }

  /**
   * Given an expression and a context, evaluate the XPath and return the result.
   *
   * @param xctxt The execution context.
   * @param contextNode The node that "." expresses.
   * @param namespaceContext The context in which namespaces in the XPath are supposed to be
   *     expanded.
   * @throws TransformerException thrown if the active ProblemListener decides the error condition
   *     is severe enough to halt processing.
   * @throws javax.xml.transform.TransformerException in case of error
   */
  public XObject execute(XPathContext xctxt, int contextNode, PrefixResolver namespaceContext)
      throws javax.xml.transform.TransformerException {

    xctxt.pushNamespaceContext(namespaceContext);

    xctxt.pushCurrentNodeAndExpression(contextNode);

    XObject xobj = null;

    try {
      xobj = m_mainExp.execute(xctxt);
    } catch (TransformerException te) {
      te.setLocator(this.getLocator());
      ErrorListener el = xctxt.getErrorListener();
      if (null != el) // defensive, should never happen.
      {
        el.error(te);
      } else throw te;
    } catch (Exception e) {
      while (e instanceof net.sourceforge.htmlunit.xpath.xml.utils.WrappedRuntimeException) {
        e = ((net.sourceforge.htmlunit.xpath.xml.utils.WrappedRuntimeException) e).getException();
      }
      // e.printStackTrace();

      String msg = e.getMessage();

      if (msg == null || msg.length() == 0) {
        msg = XPATHMessages.createXPATHMessage(XPATHErrorResources.ER_XPATH_ERROR, null);
      }
      TransformerException te = new TransformerException(msg, getLocator(), e);
      ErrorListener el = xctxt.getErrorListener();
      // te.printStackTrace();
      if (null != el) // defensive, should never happen.
      {
        el.fatalError(te);
      } else throw te;
    } finally {
      xctxt.popNamespaceContext();

      xctxt.popCurrentNodeAndExpression();
    }

    return xobj;
  }

  /**
   * Given an expression and a context, evaluate the XPath and return the result.
   *
   * @param xctxt The execution context.
   * @param contextNode The node that "." expresses.
   * @param namespaceContext The context in which namespaces in the XPath are supposed to be
   *     expanded.
   * @return the result
   * @throws TransformerException thrown if the active ProblemListener decides the error condition
   *     is severe enough to halt processing.
   * @throws javax.xml.transform.TransformerException in case of error
   */
  public boolean bool(XPathContext xctxt, int contextNode, PrefixResolver namespaceContext)
      throws javax.xml.transform.TransformerException {

    xctxt.pushNamespaceContext(namespaceContext);

    xctxt.pushCurrentNodeAndExpression(contextNode);

    try {
      return m_mainExp.bool(xctxt);
    } catch (TransformerException te) {
      te.setLocator(this.getLocator());
      ErrorListener el = xctxt.getErrorListener();
      if (null != el) // defensive, should never happen.
      {
        el.error(te);
      } else throw te;
    } catch (Exception e) {
      while (e instanceof net.sourceforge.htmlunit.xpath.xml.utils.WrappedRuntimeException) {
        e = ((net.sourceforge.htmlunit.xpath.xml.utils.WrappedRuntimeException) e).getException();
      }
      // e.printStackTrace();

      String msg = e.getMessage();

      if (msg == null || msg.length() == 0) {
        msg = XPATHMessages.createXPATHMessage(XPATHErrorResources.ER_XPATH_ERROR, null);
      }

      TransformerException te = new TransformerException(msg, getLocator(), e);
      ErrorListener el = xctxt.getErrorListener();
      // te.printStackTrace();
      if (null != el) // defensive, should never happen.
      {
        el.fatalError(te);
      } else throw te;
    } finally {
      xctxt.popNamespaceContext();

      xctxt.popCurrentNodeAndExpression();
    }

    return false;
  }

  /**
   * Warn the user of an problem.
   *
   * @param xctxt The XPath runtime context.
   * @param msg An error msgkey that corresponds to one of the constants found in {@link
   *     net.sourceforge.htmlunit.xpath.res.XPATHErrorResources}, which is a key for a format
   *     string.
   * @param args An array of arguments represented in the format string, which may be null.
   * @throws TransformerException if the current ErrorListoner determines to throw an exception.
   */
  public void warn(XPathContext xctxt, String msg, Object[] args)
      throws javax.xml.transform.TransformerException {

    String fmsg = XPATHMessages.createXPATHWarning(msg, args);
    ErrorListener ehandler = xctxt.getErrorListener();

    if (null != ehandler) {
      ehandler.warning(new TransformerException(fmsg));
    }
  }

  /**
   * Tell the user of an error, and probably throw an exception.
   *
   * @param xctxt The XPath runtime context.
   * @param msg An error msgkey that corresponds to one of the constants found in {@link
   *     net.sourceforge.htmlunit.xpath.res.XPATHErrorResources}, which is a key for a format
   *     string.
   * @param args An array of arguments represented in the format string, which may be null.
   * @throws TransformerException if the current ErrorListoner determines to throw an exception.
   */
  public void error(XPathContext xctxt, String msg, Object[] args)
      throws javax.xml.transform.TransformerException {

    String fmsg = XPATHMessages.createXPATHMessage(msg, args);
    ErrorListener ehandler = xctxt.getErrorListener();

    if (null != ehandler) {
      ehandler.fatalError(new TransformerException(fmsg));
    }
  }

  /**
   * This will traverse the heararchy, calling the visitor for each member. If the called visitor
   * method returns false, the subtree should not be called.
   *
   * @param visitor The visitor whose appropriate method will be called.
   */
  public void callVisitors(XPathVisitor visitor) {
    m_mainExp.callVisitors(visitor);
  }

  /** The match score if no match is made. */
  public static final double MATCH_SCORE_NONE = Double.NEGATIVE_INFINITY;

  /**
   * The match score if the pattern has the form of a QName optionally preceded by an @ character.
   */
  public static final double MATCH_SCORE_QNAME = 0.0;

  /** The match score if the pattern pattern has the form NCName:*. */
  public static final double MATCH_SCORE_NSWILD = -0.25;

  /** The match score if the pattern consists of just a NodeTest. */
  public static final double MATCH_SCORE_NODETEST = -0.5;

  /**
   * The match score if the pattern consists of something other than just a NodeTest or just a
   * qname.
   */
  public static final double MATCH_SCORE_OTHER = 0.5;
}
