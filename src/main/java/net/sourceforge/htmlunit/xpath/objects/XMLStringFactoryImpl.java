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
package net.sourceforge.htmlunit.xpath.objects;

import net.sourceforge.htmlunit.xpath.xml.utils.FastStringBuffer;
import net.sourceforge.htmlunit.xpath.xml.utils.XMLString;
import net.sourceforge.htmlunit.xpath.xml.utils.XMLStringFactory;

/**
 * Class XMLStringFactoryImpl creates XString versions of XMLStrings.
 *
 * @xsl.usage internal
 */
public class XMLStringFactoryImpl extends XMLStringFactory {
  /** The XMLStringFactory to pass to DTM construction. */
  private static XMLStringFactory m_xstringfactory = new XMLStringFactoryImpl();

  /**
   * Get the XMLStringFactory to pass to DTM construction.
   *
   * @return A never-null static reference to a String factory.
   */
  public static XMLStringFactory getFactory() {
    return m_xstringfactory;
  }

  /**
   * Create a new XMLString from a Java string.
   *
   * @param string Java String reference, which must be non-null.
   * @return An XMLString object that wraps the String reference.
   */
  @Override
  public XMLString newstr(String string) {
    return new XString(string);
  }

  /**
   * Create a XMLString from a FastStringBuffer.
   *
   * @param fsb FastStringBuffer reference, which must be non-null.
   * @param start The start position in the array.
   * @param length The number of characters to read from the array.
   * @return An XMLString object that wraps the FastStringBuffer reference.
   */
  @Override
  public XMLString newstr(FastStringBuffer fsb, int start, int length) {
    return new XStringForFSB(fsb, start, length);
  }

  /**
   * Create a XMLString from a FastStringBuffer.
   *
   * @param string FastStringBuffer reference, which must be non-null.
   * @param start The start position in the array.
   * @param length The number of characters to read from the array.
   * @return An XMLString object that wraps the FastStringBuffer reference.
   */
  @Override
  public XMLString newstr(char[] string, int start, int length) {
    return new XStringForChars(string, start, length);
  }

  /**
   * Get a cheap representation of an empty string.
   *
   * @return An non-null reference to an XMLString that represents "".
   */
  @Override
  public XMLString emptystr() {
    return XString.EMPTYSTRING;
  }
}
