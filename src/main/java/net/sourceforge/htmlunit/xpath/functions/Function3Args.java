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
package net.sourceforge.htmlunit.xpath.functions;

import net.sourceforge.htmlunit.xpath.Expression;
import net.sourceforge.htmlunit.xpath.ExpressionOwner;
import net.sourceforge.htmlunit.xpath.XPathVisitor;
import net.sourceforge.htmlunit.xpath.res.XSLMessages;

/**
 * Base class for functions that accept three arguments.
 *
 * @xsl.usage advanced
 */
public class Function3Args extends Function2Args {
  static final long serialVersionUID = 7915240747161506646L;

  /**
   * The third argument passed to the function (at index 2).
   *
   * @serial
   */
  Expression m_arg2;

  /**
   * Return the third argument passed to the function (at index 2).
   *
   * @return An expression that represents the third argument passed to the function.
   */
  public Expression getArg2() {
    return m_arg2;
  }

  /**
   * Set an argument expression for a function. This method is called by the XPath compiler.
   *
   * @param arg non-null expression that represents the argument.
   * @param argNum The argument number index.
   * @throws WrongNumberArgsException If the argNum parameter is greater than 2.
   */
  @Override
  public void setArg(Expression arg, int argNum) throws WrongNumberArgsException {

    if (argNum < 2) super.setArg(arg, argNum);
    else if (2 == argNum) {
      m_arg2 = arg;
      arg.exprSetParent(this);
    } else reportWrongNumberArgs();
  }

  /**
   * Check that the number of arguments passed to this function is correct.
   *
   * @param argNum The number of arguments that is being passed to the function.
   * @throws WrongNumberArgsException
   */
  @Override
  public void checkNumberArgs(int argNum) throws WrongNumberArgsException {
    if (argNum != 3) reportWrongNumberArgs();
  }

  /**
   * Constructs and throws a WrongNumberArgException with the appropriate message for this function
   * object.
   *
   * @throws WrongNumberArgsException
   */
  @Override
  protected void reportWrongNumberArgs() throws WrongNumberArgsException {
    throw new WrongNumberArgsException(XSLMessages.createXPATHMessage("three", null));
  }

  /**
   * Tell if this expression or it's subexpressions can traverse outside the current subtree.
   *
   * @return true if traversal outside the context node's subtree can occur.
   */
  @Override
  public boolean canTraverseOutsideSubtree() {
    return super.canTraverseOutsideSubtree() ? true : m_arg2.canTraverseOutsideSubtree();
  }

  class Arg2Owner implements ExpressionOwner {
    /** @see ExpressionOwner#getExpression() */
    @Override
    public Expression getExpression() {
      return m_arg2;
    }

    /** @see ExpressionOwner#setExpression(Expression) */
    @Override
    public void setExpression(Expression exp) {
      exp.exprSetParent(Function3Args.this);
      m_arg2 = exp;
    }
  }

  /**
   * @see net.sourceforge.htmlunit.xpath.XPathVisitable#callVisitors(ExpressionOwner, XPathVisitor)
   */
  @Override
  public void callArgVisitors(XPathVisitor visitor) {
    super.callArgVisitors(visitor);
    if (null != m_arg2) m_arg2.callVisitors(new Arg2Owner(), visitor);
  }

  /** @see Expression#deepEquals(Expression) */
  @Override
  public boolean deepEquals(Expression expr) {
    if (!super.deepEquals(expr)) return false;

    if (null != m_arg2) {
      if (null == ((Function3Args) expr).m_arg2) return false;

      if (!m_arg2.deepEquals(((Function3Args) expr).m_arg2)) return false;
    } else if (null != ((Function3Args) expr).m_arg2) return false;

    return true;
  }
}
