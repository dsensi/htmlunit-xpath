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
package net.sourceforge.htmlunit.xpath.axes;

import net.sourceforge.htmlunit.xpath.compiler.Compiler;

/** This class iterates over set of nodes that needs to be sorted. */
public class WalkingIteratorSorted extends WalkingIterator {

  // /** True if the nodes will be found in document order */
  // protected boolean m_inNaturalOrder = false;

  /** True if the nodes will be found in document order, and this can be determined statically. */
  protected boolean m_inNaturalOrderStatic = false;

  /**
   * Create a WalkingIterator iterator, including creation of step walkers from the opcode list, and
   * call back into the Compiler to create predicate expressions.
   *
   * @param compiler The Compiler which is creating this expression.
   * @param opPos The position of this iterator in the opcode list from the compiler.
   * @param shouldLoadWalkers True if walkers should be loaded, or false if this is a derived
   *     iterator and it doesn't wish to load child walkers.
   * @throws javax.xml.transform.TransformerException
   */
  WalkingIteratorSorted(Compiler compiler, int opPos, int analysis, boolean shouldLoadWalkers)
      throws javax.xml.transform.TransformerException {
    super(compiler, opPos, analysis, shouldLoadWalkers);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isDocOrdered() {
    return m_inNaturalOrderStatic;
  }
}
