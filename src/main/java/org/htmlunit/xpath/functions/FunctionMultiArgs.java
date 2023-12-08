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
package org.htmlunit.xpath.functions;

import org.htmlunit.xpath.Expression;
import org.htmlunit.xpath.XPathVisitor;
import org.htmlunit.xpath.res.XPATHErrorResources;
import org.htmlunit.xpath.res.XPATHMessages;

/** Base class for functions that accept an undetermined number of multiple arguments. */
public class FunctionMultiArgs extends Function3Args {

  /**
   * Argument expressions that are at index 3 or greater.
   *
   * @serial
   */
  Expression[] m_args;

  /** {@inheritDoc} */
  @Override
  public void setArg(final Expression arg, final int argNum) throws WrongNumberArgsException {

    if (argNum < 3) super.setArg(arg, argNum);
    else {
      if (null == m_args) {
        m_args = new Expression[1];
        m_args[0] = arg;
      } else {

        // Slow but space conservative.
        final Expression[] args = new Expression[m_args.length + 1];

        System.arraycopy(m_args, 0, args, 0, m_args.length);

        args[m_args.length] = arg;
        m_args = args;
      }
      arg.exprSetParent(this);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void checkNumberArgs(final int argNum) throws WrongNumberArgsException {}

  /** {@inheritDoc} */
  @Override
  protected void reportWrongNumberArgs() throws WrongNumberArgsException {
    final String fMsg =
        XPATHMessages.createXPATHMessage(
            XPATHErrorResources.ER_INCORRECT_PROGRAMMER_ASSERTION,
            new Object[] {
              "Programmer's assertion:  the method FunctionMultiArgs.reportWrongNumberArgs() should never be called."
            });

    throw new RuntimeException(fMsg);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canTraverseOutsideSubtree() {

    if (super.canTraverseOutsideSubtree()) {
      return true;
    }

    for (final Expression m_arg : m_args) {
      if (m_arg.canTraverseOutsideSubtree()) {
        return true;
      }
    }

    return false;
  }

  /** {@inheritDoc} */
  @Override
  public void callArgVisitors(final XPathVisitor visitor) {
    super.callArgVisitors(visitor);
    if (null != m_args) {
      for (final Expression m_arg : m_args) {
        m_arg.callVisitors(visitor);
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean deepEquals(final Expression expr) {
    if (!super.deepEquals(expr)) return false;

    final FunctionMultiArgs fma = (FunctionMultiArgs) expr;
    if (null != m_args) {
      final int n = m_args.length;
      if ((null == fma) || (fma.m_args.length != n)) return false;

      for (int i = 0; i < n; i++) {
        if (!m_args[i].deepEquals(fma.m_args[i])) return false;
      }

    } else if (null != fma.m_args) {
      return false;
    }

    return true;
  }
}
