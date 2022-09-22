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
package net.sourceforge.htmlunit.xpath.operations;

import net.sourceforge.htmlunit.xpath.Expression;
import net.sourceforge.htmlunit.xpath.ExpressionOwner;
import net.sourceforge.htmlunit.xpath.XPathContext;
import net.sourceforge.htmlunit.xpath.XPathVisitor;
import net.sourceforge.htmlunit.xpath.objects.XObject;

/** The baseclass for a binary operation. */
public class Operation extends Expression implements ExpressionOwner {
  static final long serialVersionUID = -3037139537171050430L;

  /**
   * The left operand expression.
   *
   * @serial
   */
  protected Expression m_left;

  /**
   * The right operand expression.
   *
   * @serial
   */
  protected Expression m_right;

  /**
   * Tell if this expression or it's subexpressions can traverse outside the current subtree.
   *
   * @return true if traversal outside the context node's subtree can occur.
   */
  @Override
  public boolean canTraverseOutsideSubtree() {

    if (null != m_left && m_left.canTraverseOutsideSubtree()) return true;

    if (null != m_right && m_right.canTraverseOutsideSubtree()) return true;

    return false;
  }

  /**
   * Set the left and right operand expressions for this operation.
   *
   * @param l The left expression operand.
   * @param r The right expression operand.
   */
  public void setLeftRight(Expression l, Expression r) {
    m_left = l;
    m_right = r;
    l.exprSetParent(this);
    r.exprSetParent(this);
  }

  /**
   * Execute a binary operation by calling execute on each of the operands, and then calling the
   * operate method on the derived class.
   *
   * @param xctxt The runtime execution context.
   * @return The XObject result of the operation.
   * @throws javax.xml.transform.TransformerException
   */
  @Override
  public XObject execute(XPathContext xctxt) throws javax.xml.transform.TransformerException {

    XObject left = m_left.execute(xctxt, true);
    XObject right = m_right.execute(xctxt, true);

    XObject result = operate(left, right);
    left.detach();
    right.detach();
    return result;
  }

  /**
   * Apply the operation to two operands, and return the result.
   *
   * @param left non-null reference to the evaluated left operand.
   * @param right non-null reference to the evaluated right operand.
   * @return non-null reference to the XObject that represents the result of the operation.
   * @throws javax.xml.transform.TransformerException
   */
  public XObject operate(XObject left, XObject right)
      throws javax.xml.transform.TransformerException {
    return null; // no-op
  }

  /** @return the left operand of binary operation, as an Expression. */
  public Expression getLeftOperand() {
    return m_left;
  }

  /** @return the right operand of binary operation, as an Expression. */
  public Expression getRightOperand() {
    return m_right;
  }

  class LeftExprOwner implements ExpressionOwner {
    /** @see ExpressionOwner#getExpression() */
    @Override
    public Expression getExpression() {
      return m_left;
    }

    /** @see ExpressionOwner#setExpression(Expression) */
    @Override
    public void setExpression(Expression exp) {
      exp.exprSetParent(Operation.this);
      m_left = exp;
    }
  }

  /**
   * @see net.sourceforge.htmlunit.xpath.XPathVisitable#callVisitors(ExpressionOwner, XPathVisitor)
   */
  @Override
  public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
    if (visitor.visitBinaryOperation(owner, this)) {
      m_left.callVisitors(new LeftExprOwner(), visitor);
      m_right.callVisitors(this, visitor);
    }
  }

  /** @see ExpressionOwner#getExpression() */
  @Override
  public Expression getExpression() {
    return m_right;
  }

  /** @see ExpressionOwner#setExpression(Expression) */
  @Override
  public void setExpression(Expression exp) {
    exp.exprSetParent(this);
    m_right = exp;
  }

  /** @see Expression#deepEquals(Expression) */
  @Override
  public boolean deepEquals(Expression expr) {
    if (!isSameClass(expr)) return false;

    if (!m_left.deepEquals(((Operation) expr).m_left)) return false;

    if (!m_right.deepEquals(((Operation) expr).m_right)) return false;

    return true;
  }
}
