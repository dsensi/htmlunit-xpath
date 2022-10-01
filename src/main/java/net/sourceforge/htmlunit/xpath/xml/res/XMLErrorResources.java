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
package net.sourceforge.htmlunit.xpath.xml.res;

import java.util.ListResourceBundle;

/**
 * Set up error messages. We build a two dimensional array of message keys and message strings. In
 * order to add a new message here, you need to first add a String constant. And you need to enter
 * key, value pair as part of the contents array. You also need to update MAX_CODE for error strings
 * and MAX_WARNING for warnings ( Needed for only information purpose )
 */
public class XMLErrorResources extends ListResourceBundle {

  /*
   * This file contains error and warning messages related to Xalan Error
   * Handling.
   *
   * General notes to translators:
   *
   * 1) Xalan (or more properly, Xalan-interpretive) and XSLTC are names of
   * components. XSLT is an acronym for
   * "XML Stylesheet Language: Transformations". XSLTC is an acronym for XSLT
   * Compiler.
   *
   * 2) A stylesheet is a description of how to transform an input XML document
   * into a resultant XML document (or HTML document or text). The stylesheet
   * itself is described in the form of an XML document.
   *
   * 3) A template is a component of a stylesheet that is used to match a
   * particular portion of an input document and specifies the form of the
   * corresponding portion of the output document.
   *
   * 4) An element is a mark-up tag in an XML document; an attribute is a modifier
   * on the tag. For example, in <elem attr='val' attr2='val2'> "elem" is an
   * element name, "attr" and "attr2" are attribute names with the values "val"
   * and "val2", respectively.
   *
   * 5) A namespace declaration is a special attribute that is used to associate a
   * prefix with a URI (the namespace). The meanings of element names and
   * attribute names that use that prefix are defined with respect to that
   * namespace.
   *
   * 6) "Translet" is an invented term that describes the class file that results
   * from compiling an XML stylesheet into a Java class.
   *
   * 7) XPath is a specification that describes a notation for identifying nodes
   * in a tree-structured representation of an XML document. An instance of that
   * notation is referred to as an XPath expression.
   *
   */

  /*
   * Message keys
   */
  public static final String ER_FUNCTION_NOT_SUPPORTED = "ER_FUNCTION_NOT_SUPPORTED";
  public static final String ER_CANNOT_OVERWRITE_CAUSE = "ER_CANNOT_OVERWRITE_CAUSE";
  public static final String ER_NO_DEFAULT_IMPL = "ER_NO_DEFAULT_IMPL";
  public static final String ER_CHUNKEDINTARRAY_NOT_SUPPORTED = "ER_CHUNKEDINTARRAY_NOT_SUPPORTED";
  public static final String ER_OFFSET_BIGGER_THAN_SLOT = "ER_OFFSET_BIGGER_THAN_SLOT";
  public static final String ER_COROUTINE_NOT_AVAIL = "ER_COROUTINE_NOT_AVAIL";
  public static final String ER_COROUTINE_CO_EXIT = "ER_COROUTINE_CO_EXIT";
  public static final String ER_COJOINROUTINESET_FAILED = "ER_COJOINROUTINESET_FAILED";
  public static final String ER_COROUTINE_PARAM = "ER_COROUTINE_PARAM";
  public static final String ER_PARSER_DOTERMINATE_ANSWERS = "ER_PARSER_DOTERMINATE_ANSWERS";
  public static final String ER_NO_PARSE_CALL_WHILE_PARSING = "ER_NO_PARSE_CALL_WHILE_PARSING";
  public static final String ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED =
      "ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED";
  public static final String ER_ITERATOR_AXIS_NOT_IMPLEMENTED = "ER_ITERATOR_AXIS_NOT_IMPLEMENTED";
  public static final String ER_ITERATOR_CLONE_NOT_SUPPORTED = "ER_ITERATOR_CLONE_NOT_SUPPORTED";
  public static final String ER_UNKNOWN_AXIS_TYPE = "ER_UNKNOWN_AXIS_TYPE";
  public static final String ER_AXIS_NOT_SUPPORTED = "ER_AXIS_NOT_SUPPORTED";
  public static final String ER_NO_DTMIDS_AVAIL = "ER_NO_DTMIDS_AVAIL";
  public static final String ER_NOT_SUPPORTED = "ER_NOT_SUPPORTED";
  public static final String ER_NODE_NON_NULL = "ER_NODE_NON_NULL";
  public static final String ER_COULD_NOT_RESOLVE_NODE = "ER_COULD_NOT_RESOLVE_NODE";
  public static final String ER_STARTPARSE_WHILE_PARSING = "ER_STARTPARSE_WHILE_PARSING";
  public static final String ER_STARTPARSE_NEEDS_SAXPARSER = "ER_STARTPARSE_NEEDS_SAXPARSER";
  public static final String ER_COULD_NOT_INIT_PARSER = "ER_COULD_NOT_INIT_PARSER";
  public static final String ER_EXCEPTION_CREATING_POOL = "ER_EXCEPTION_CREATING_POOL";
  public static final String ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE =
      "ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE";
  public static final String ER_SCHEME_REQUIRED = "ER_SCHEME_REQUIRED";
  public static final String ER_NO_SCHEME_IN_URI = "ER_NO_SCHEME_IN_URI";
  public static final String ER_NO_SCHEME_INURI = "ER_NO_SCHEME_INURI";
  public static final String ER_PATH_INVALID_CHAR = "ER_PATH_INVALID_CHAR";
  public static final String ER_SCHEME_FROM_NULL_STRING = "ER_SCHEME_FROM_NULL_STRING";
  public static final String ER_SCHEME_NOT_CONFORMANT = "ER_SCHEME_NOT_CONFORMANT";
  public static final String ER_HOST_ADDRESS_NOT_WELLFORMED = "ER_HOST_ADDRESS_NOT_WELLFORMED";
  public static final String ER_PORT_WHEN_HOST_NULL = "ER_PORT_WHEN_HOST_NULL";
  public static final String ER_INVALID_PORT = "ER_INVALID_PORT";
  public static final String ER_FRAG_FOR_GENERIC_URI = "ER_FRAG_FOR_GENERIC_URI";
  public static final String ER_FRAG_WHEN_PATH_NULL = "ER_FRAG_WHEN_PATH_NULL";
  public static final String ER_FRAG_INVALID_CHAR = "ER_FRAG_INVALID_CHAR";
  public static final String ER_PARSER_IN_USE = "ER_PARSER_IN_USE";
  public static final String ER_CANNOT_CHANGE_WHILE_PARSING = "ER_CANNOT_CHANGE_WHILE_PARSING";
  public static final String ER_SELF_CAUSATION_NOT_PERMITTED = "ER_SELF_CAUSATION_NOT_PERMITTED";
  public static final String ER_NO_USERINFO_IF_NO_HOST = "ER_NO_USERINFO_IF_NO_HOST";
  public static final String ER_NO_PORT_IF_NO_HOST = "ER_NO_PORT_IF_NO_HOST";
  public static final String ER_NO_QUERY_STRING_IN_PATH = "ER_NO_QUERY_STRING_IN_PATH";
  public static final String ER_NO_FRAGMENT_STRING_IN_PATH = "ER_NO_FRAGMENT_STRING_IN_PATH";
  public static final String ER_CANNOT_INIT_URI_EMPTY_PARMS = "ER_CANNOT_INIT_URI_EMPTY_PARMS";
  public static final String ER_METHOD_NOT_SUPPORTED = "ER_METHOD_NOT_SUPPORTED";
  public static final String ER_INCRSAXSRCFILTER_NOT_RESTARTABLE =
      "ER_INCRSAXSRCFILTER_NOT_RESTARTABLE";
  public static final String ER_XMLRDR_NOT_BEFORE_STARTPARSE = "ER_XMLRDR_NOT_BEFORE_STARTPARSE";
  public static final String ER_AXIS_TRAVERSER_NOT_SUPPORTED = "ER_AXIS_TRAVERSER_NOT_SUPPORTED";
  public static final String ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER =
      "ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER";
  public static final String ER_SYSTEMID_UNKNOWN = "ER_SYSTEMID_UNKNOWN";
  public static final String ER_LOCATION_UNKNOWN = "ER_LOCATION_UNKNOWN";
  public static final String ER_PREFIX_MUST_RESOLVE = "ER_PREFIX_MUST_RESOLVE";
  public static final String ER_CREATEDOCUMENT_NOT_SUPPORTED = "ER_CREATEDOCUMENT_NOT_SUPPORTED";
  public static final String ER_CHILD_HAS_NO_OWNER_DOCUMENT = "ER_CHILD_HAS_NO_OWNER_DOCUMENT";
  public static final String ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT =
      "ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT";
  public static final String ER_CANT_OUTPUT_TEXT_BEFORE_DOC = "ER_CANT_OUTPUT_TEXT_BEFORE_DOC";
  public static final String ER_CANT_HAVE_MORE_THAN_ONE_ROOT = "ER_CANT_HAVE_MORE_THAN_ONE_ROOT";
  public static final String ER_ARG_LOCALNAME_NULL = "ER_ARG_LOCALNAME_NULL";
  public static final String ER_ARG_LOCALNAME_INVALID = "ER_ARG_LOCALNAME_INVALID";
  public static final String ER_ARG_PREFIX_INVALID = "ER_ARG_PREFIX_INVALID";
  public static final String ER_NAME_CANT_START_WITH_COLON = "ER_NAME_CANT_START_WITH_COLON";

  /*
   * Now fill in the message text. Then fill in the message text for that message
   * code in the array. Use the new error code as the index into the array.
   */

  // Error messages...

  /**
   * Get the lookup table for error messages
   *
   * @return The association list.
   */
  @Override
  public Object[][] getContents() {
    return new Object[][] {

            /* Error message ID that has a null message, but takes in a single object. */
      {"ER0000", "{0}"},
      {ER_FUNCTION_NOT_SUPPORTED, "Function not supported!"},
      {ER_CANNOT_OVERWRITE_CAUSE, "Cannot overwrite cause"},
      {ER_NO_DEFAULT_IMPL, "No default implementation found "},
      {ER_CHUNKEDINTARRAY_NOT_SUPPORTED, "ChunkedIntArray({0}) not currently supported"},
      {ER_OFFSET_BIGGER_THAN_SLOT, "Offset bigger than slot"},
      {ER_COROUTINE_NOT_AVAIL, "Coroutine not available, id={0}"},
      {ER_COROUTINE_CO_EXIT, "CoroutineManager received co_exit() request"},
      {ER_COJOINROUTINESET_FAILED, "co_joinCoroutineSet() failed"},
      {ER_COROUTINE_PARAM, "Coroutine parameter error ({0})"},
      {ER_PARSER_DOTERMINATE_ANSWERS, "\nUNEXPECTED: Parser doTerminate answers {0}"},
      {ER_NO_PARSE_CALL_WHILE_PARSING, "parse may not be called while parsing"},
      {
        ER_TYPED_ITERATOR_AXIS_NOT_IMPLEMENTED,
        "Error: typed iterator for axis  {0} not implemented"
      },
      {ER_ITERATOR_AXIS_NOT_IMPLEMENTED, "Error: iterator for axis {0} not implemented "},
      {ER_ITERATOR_CLONE_NOT_SUPPORTED, "Iterator clone not supported"},
      {ER_UNKNOWN_AXIS_TYPE, "Unknown axis traversal type: {0}"},
      {ER_AXIS_NOT_SUPPORTED, "Axis traverser not supported: {0}"},
      {ER_NO_DTMIDS_AVAIL, "No more DTM IDs are available"},
      {ER_NOT_SUPPORTED, "Not supported: {0}"},
      {ER_NODE_NON_NULL, "Node must be non-null for getDTMHandleFromNode"},
      {ER_COULD_NOT_RESOLVE_NODE, "Could not resolve the node to a handle"},
      {ER_STARTPARSE_WHILE_PARSING, "startParse may not be called while parsing"},
      {ER_STARTPARSE_NEEDS_SAXPARSER, "startParse needs a non-null SAXParser"},
      {ER_COULD_NOT_INIT_PARSER, "could not initialize parser with"},
      {ER_EXCEPTION_CREATING_POOL, "exception creating new instance for pool"},
      {ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE, "Path contains invalid escape sequence"},
      {ER_SCHEME_REQUIRED, "Scheme is required!"},
      {ER_NO_SCHEME_IN_URI, "No scheme found in URI: {0}"},
      {ER_NO_SCHEME_INURI, "No scheme found in URI"},
      {ER_PATH_INVALID_CHAR, "Path contains invalid character: {0}"},
      {ER_SCHEME_FROM_NULL_STRING, "Cannot set scheme from null string"},
      {ER_SCHEME_NOT_CONFORMANT, "The scheme is not conformant."},
      {ER_HOST_ADDRESS_NOT_WELLFORMED, "Host is not a well formed address"},
      {ER_PORT_WHEN_HOST_NULL, "Port cannot be set when host is null"},
      {ER_INVALID_PORT, "Invalid port number"},
      {ER_FRAG_FOR_GENERIC_URI, "Fragment can only be set for a generic URI"},
      {ER_FRAG_WHEN_PATH_NULL, "Fragment cannot be set when path is null"},
      {ER_FRAG_INVALID_CHAR, "Fragment contains invalid character"},
      {ER_PARSER_IN_USE, "Parser is already in use"},
      {ER_CANNOT_CHANGE_WHILE_PARSING, "Cannot change {0} {1} while parsing"},
      {ER_SELF_CAUSATION_NOT_PERMITTED, "Self-causation not permitted"},
      {ER_NO_USERINFO_IF_NO_HOST, "Userinfo may not be specified if host is not specified"},
      {ER_NO_PORT_IF_NO_HOST, "Port may not be specified if host is not specified"},
      {ER_NO_QUERY_STRING_IN_PATH, "Query string cannot be specified in path and query string"},
      {ER_NO_FRAGMENT_STRING_IN_PATH, "Fragment cannot be specified in both the path and fragment"},
      {ER_CANNOT_INIT_URI_EMPTY_PARMS, "Cannot initialize URI with empty parameters"},
      {ER_METHOD_NOT_SUPPORTED, "Method not yet supported "},
      {
        ER_INCRSAXSRCFILTER_NOT_RESTARTABLE, "IncrementalSAXSource_Filter not currently restartable"
      },
      {ER_XMLRDR_NOT_BEFORE_STARTPARSE, "XMLReader not before startParse request"},
      {ER_AXIS_TRAVERSER_NOT_SUPPORTED, "Axis traverser not supported: {0}"},
      {
        ER_ERRORHANDLER_CREATED_WITH_NULL_PRINTWRITER,
        "ListingErrorHandler created with null PrintWriter!"
      },
      {ER_SYSTEMID_UNKNOWN, "SystemId Unknown"},
      {ER_LOCATION_UNKNOWN, "Location of error unknown"},
      {ER_PREFIX_MUST_RESOLVE, "Prefix must resolve to a namespace: {0}"},
      {ER_CREATEDOCUMENT_NOT_SUPPORTED, "createDocument() not supported in XPathContext!"},
      {ER_CHILD_HAS_NO_OWNER_DOCUMENT, "Attribute child does not have an owner document!"},
      {
        ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT,
        "Attribute child does not have an owner document element!"
      },
      {
        ER_CANT_OUTPUT_TEXT_BEFORE_DOC,
        "Warning: can't output text before document element!  Ignoring..."
      },
      {ER_CANT_HAVE_MORE_THAN_ONE_ROOT, "Can't have more than one root on a DOM!"},
      {ER_ARG_LOCALNAME_NULL, "Argument 'localName' is null"},

      // Note to translators: A QNAME has the syntactic form [NCName:]NCName
      // The localname is the portion after the optional colon; the message indicates
      // that there is a problem with that part of the QNAME.
      {ER_ARG_LOCALNAME_INVALID, "Localname in QNAME should be a valid NCName"},

      // Note to translators: A QNAME has the syntactic form [NCName:]NCName
      // The prefix is the portion before the optional colon; the message indicates
      // that there is a problem with that part of the QNAME.
      {ER_ARG_PREFIX_INVALID, "Prefix in QNAME should be a valid NCName"},
      {ER_NAME_CANT_START_WITH_COLON, "Name cannot start with a colon"},
      {"BAD_CODE", "Parameter to createMessage was out of bounds"},
      {"FORMAT_FAILED", "Exception thrown during messageFormat call"},
      {"line", "Line #"},
      {"column", "Column #"}
    };
  }
}
