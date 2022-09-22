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

import net.sourceforge.htmlunit.xpath.XPathContext;
import net.sourceforge.htmlunit.xpath.objects.XNumber;
import net.sourceforge.htmlunit.xpath.objects.XObject;
import net.sourceforge.htmlunit.xpath.xml.dtm.DTMIterator;

/**
 * Execute the Count() function.
 *
 * @xsl.usage advanced
 */
public class FuncCount extends FunctionOneArg {
  static final long serialVersionUID = -7116225100474153751L;

  /**
   * Execute the function. The function must return a valid object.
   *
   * @param xctxt The current execution context.
   * @return A valid XObject.
   * @throws javax.xml.transform.TransformerException
   */
  @Override
  public XObject execute(XPathContext xctxt) throws javax.xml.transform.TransformerException {

    //    DTMIterator nl = m_arg0.asIterator(xctxt, xctxt.getCurrentNode());

    //    // We should probably make a function on the iterator for this,
    //    // as a given implementation could optimize.
    //    int i = 0;
    //
    //    while (DTM.NULL != nl.nextNode())
    //    {
    //      i++;
    //    }
    //    nl.detach();
    DTMIterator nl = m_arg0.asIterator(xctxt, xctxt.getCurrentNode());
    int i = nl.getLength();
    nl.detach();

    return new XNumber((double) i);
  }
}
