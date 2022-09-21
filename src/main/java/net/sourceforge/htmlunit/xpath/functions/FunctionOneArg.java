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
/*
 * $Id$
 */
package net.sourceforge.htmlunit.xpath.functions;

import net.sourceforge.htmlunit.xpath.Expression;
import net.sourceforge.htmlunit.xpath.ExpressionOwner;
import net.sourceforge.htmlunit.xpath.XPathVisitor;
import net.sourceforge.htmlunit.xpath.res.XSLMessages;

/**
 * Base class for functions that accept one argument.
 * @xsl.usage advanced
 */
public class FunctionOneArg extends Function implements ExpressionOwner
{
    static final long serialVersionUID = -5180174180765609758L;

  /** The first argument passed to the function (at index 0).
   *  @serial  */
  Expression m_arg0;

  /**
   * Return the first argument passed to the function (at index 0).
   *
   * @return An expression that represents the first argument passed to the 
   *         function.
   */
  public Expression getArg0()
  {
    return m_arg0;
  }
  
  /**
   * Set an argument expression for a function.  This method is called by the 
   * XPath compiler.
   *
   * @param arg non-null expression that represents the argument.
   * @param argNum The argument number index.
   *
   * @throws WrongNumberArgsException If the argNum parameter is greater than 0.
   */
  @Override
public void setArg(Expression arg, int argNum)
          throws WrongNumberArgsException
  {

    if (0 == argNum)
    {
      m_arg0 = arg;
      arg.exprSetParent(this);
    }
    else
      reportWrongNumberArgs();
  }

  /**
   * Check that the number of arguments passed to this function is correct. 
   *
   *
   * @param argNum The number of arguments that is being passed to the function.
   *
   * @throws WrongNumberArgsException
   */
  @Override
public void checkNumberArgs(int argNum) throws WrongNumberArgsException
  {
    if (argNum != 1)
      reportWrongNumberArgs();
  }

  /**
   * Constructs and throws a WrongNumberArgException with the appropriate
   * message for this function object.
   *
   * @throws WrongNumberArgsException
   */
  @Override
protected void reportWrongNumberArgs() throws WrongNumberArgsException {
      throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("one", null));
  }
  
  /**
   * Tell if this expression or it's subexpressions can traverse outside 
   * the current subtree.
   * 
   * @return true if traversal outside the context node's subtree can occur.
   */
   @Override
public boolean canTraverseOutsideSubtree()
   {
    return m_arg0.canTraverseOutsideSubtree();
   }
   
  /**
   * @see net.sourceforge.htmlunit.xpath.XPathVisitable#callVisitors(ExpressionOwner, XPathVisitor)
   */
  @Override
public void callArgVisitors(XPathVisitor visitor)
  {
    if(null != m_arg0)
      m_arg0.callVisitors(this, visitor);
  }


  /**
   * @see ExpressionOwner#getExpression()
   */
  @Override
public Expression getExpression()
  {
    return m_arg0;
  }

  /**
   * @see ExpressionOwner#setExpression(Expression)
   */
  @Override
public void setExpression(Expression exp)
  {
    exp.exprSetParent(this);
    m_arg0 = exp;
  }
  
  /**
   * @see Expression#deepEquals(Expression)
   */
  @Override
public boolean deepEquals(Expression expr)
  {
    if(!super.deepEquals(expr))
      return false;
      
    if(null != m_arg0)
    {
      if(null == ((FunctionOneArg)expr).m_arg0)
        return false;
        
      if(!m_arg0.deepEquals(((FunctionOneArg)expr).m_arg0))
        return false;
    }
    else if(null != ((FunctionOneArg)expr).m_arg0)
      return false;

    return true;
  }


}
