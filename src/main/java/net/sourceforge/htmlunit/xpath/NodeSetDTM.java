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

import net.sourceforge.htmlunit.xpath.res.XPATHErrorResources;
import net.sourceforge.htmlunit.xpath.res.XSLMessages;
import net.sourceforge.htmlunit.xpath.xml.dtm.DTM;
import net.sourceforge.htmlunit.xpath.xml.dtm.DTMFilter;
import net.sourceforge.htmlunit.xpath.xml.dtm.DTMIterator;
import net.sourceforge.htmlunit.xpath.xml.dtm.DTMManager;
import net.sourceforge.htmlunit.xpath.xml.utils.NodeVector;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

/**
 * The NodeSetDTM class can act as either a NodeVector, NodeList, or NodeIterator. However, in order
 * for it to act as a NodeVector or NodeList, it's required that setShouldCacheNodes(true) be called
 * before the first nextNode() is called, in order that nodes can be added as they are fetched.
 * Derived classes that implement iterators must override runTo(int index), in order that they may
 * run the iteration to the given index.
 *
 * <p>Note that we directly implement the DOM's NodeIterator interface. We do not emulate all the
 * behavior of the standard NodeIterator. In particular, we do not guarantee to present a "live
 * view" of the document ... but in XSLT, the source document should never be mutated, so this
 * should never be an issue.
 *
 * <p>Thought: Should NodeSetDTM really implement NodeList and NodeIterator, or should there be
 * specific subclasses of it which do so? The advantage of doing it all here is that all NodeSetDTMs
 * will respond to the same calls; the disadvantage is that some of them may return
 * less-than-enlightening results when you do so.
 */
public class NodeSetDTM extends NodeVector
    implements /* NodeList, NodeIterator, */ DTMIterator, Cloneable {

  /** Create an empty nodelist. */
  public NodeSetDTM(DTMManager dtmManager) {
    super();
    m_manager = dtmManager;
  }

  /**
   * Create an empty, using the given block size.
   *
   * @param blocksize Size of blocks to allocate
   * @param dummy pass zero for right now...
   */
  public NodeSetDTM(int blocksize, int dummy, DTMManager dtmManager) {
    super(blocksize);
    m_manager = dtmManager;
  }

  /**
   * Create a NodeSetDTM, and copy the members of the given NodeSetDTM into it.
   *
   * @param nodelist Set of Nodes to be made members of the new set.
   */
  public NodeSetDTM(NodeSetDTM nodelist) {

    super();
    m_manager = nodelist.getDTMManager();
    m_root = nodelist.getRoot();

    addNodes(nodelist);
  }

  /**
   * Create a NodeSetDTM, and copy the members of the given DTMIterator into it.
   *
   * @param ni Iterator which yields Nodes to be made members of the new set.
   */
  public NodeSetDTM(DTMIterator ni) {

    super();

    m_manager = ni.getDTMManager();
    m_root = ni.getRoot();
    addNodes(ni);
  }

  /**
   * Create a NodeSetDTM, and copy the members of the given DTMIterator into it.
   *
   * @param iterator Iterator which yields Nodes to be made members of the new set.
   */
  public NodeSetDTM(NodeIterator iterator, XPathContext xctxt) {

    super();

    Node node;
    m_manager = xctxt.getDTMManager();

    while (null != (node = iterator.nextNode())) {
      int handle = xctxt.getDTMHandleFromNode(node);
      addNodeInDocOrder(handle, xctxt);
    }
  }

  /** Create a NodeSetDTM, and copy the members of the given DTMIterator into it. */
  public NodeSetDTM(NodeList nodeList, XPathContext xctxt) {

    super();

    m_manager = xctxt.getDTMManager();

    int n = nodeList.getLength();
    for (int i = 0; i < n; i++) {
      Node node = nodeList.item(i);
      int handle = xctxt.getDTMHandleFromNode(node);
      // Do not reorder or strip duplicate nodes from the given DOM nodelist
      addNode(handle); // addNodeInDocOrder(handle, xctxt);
    }
  }

  /**
   * Create a NodeSetDTM which contains the given Node.
   *
   * @param node Single node to be added to the new set.
   */
  public NodeSetDTM(int node, DTMManager dtmManager) {

    super();
    m_manager = dtmManager;

    addNode(node);
  }

  /**
   * @return The root node of the Iterator, as specified when it was created. For non-Iterator
   *     NodeSetDTMs, this will be null.
   */
  @Override
  public int getRoot() {
    if (DTM.NULL == m_root) {
      if (size() > 0) {
        return item(0);
      }
      return DTM.NULL;
    }
    return m_root;
  }

  /**
   * Initialize the context values for this expression after it is cloned.
   *
   * @param context The XPath runtime context for this transformation.
   */
  @Override
  public void setRoot(int context, Object environment) {
    // no-op, I guess... (-sb)
  }

  /**
   * Clone this NodeSetDTM. At this time, we only expect this to be used with LocPathIterators; it
   * may not work with other kinds of NodeSetDTMs.
   *
   * @return a new NodeSetDTM of the same type, having the same state... though unless overridden in
   *     the subclasses, it may not copy all the state information.
   * @throws CloneNotSupportedException if this subclass of NodeSetDTM does not support the clone()
   *     operation.
   */
  @Override
  public Object clone() throws CloneNotSupportedException {

    return super.clone();
  }

  /**
   * Get a cloned Iterator, and reset its state to the beginning of the iteration.
   *
   * @return a new NodeSetDTM of the same type, having the same state... except that the reset()
   *     operation has been called.
   * @throws CloneNotSupportedException if this subclass of NodeSetDTM does not support the clone()
   *     operation.
   */
  @Override
  public DTMIterator cloneWithReset() throws CloneNotSupportedException {

    NodeSetDTM clone = (NodeSetDTM) clone();

    clone.reset();

    return clone;
  }

  /** Reset the iterator. May have no effect on non-iterator Nodesets. */
  @Override
  public void reset() {
    m_next = 0;
  }

  /**
   * This attribute determines which node types are presented via the iterator. The available set of
   * constants is defined in the <code>DTMFilter</code> interface. For NodeSetDTMs, the mask has
   * been hardcoded to show all nodes except EntityReference nodes, which have no equivalent in the
   * XPath data model.
   *
   * @return integer used as a bit-array, containing flags defined in the DOM's DTMFilter class. The
   *     value will be <code>SHOW_ALL &amp; ~SHOW_ENTITY_REFERENCE</code>, meaning that only entity
   *     references are suppressed.
   */
  @Override
  public int getWhatToShow() {
    return DTMFilter.SHOW_ALL & ~DTMFilter.SHOW_ENTITY_REFERENCE;
  }

  /**
   * The filter object used to screen nodes. Filters are applied to further reduce (and restructure)
   * the DTMIterator's view of the document. In our case, we will be using hardcoded filters built
   * into our iterators... but getFilter() is part of the DOM's DTMIterator interface, so we have to
   * support it.
   *
   * @return null, which is slightly misleading. True, there is no user-written filter object, but
   *     in fact we are doing some very sophisticated custom filtering. A DOM purist might suggest
   *     returning a placeholder object just to indicate that this is not going to return all nodes
   *     selected by whatToShow.
   */
  public DTMFilter getFilter() {
    return null;
  }

  /**
   * The value of this flag determines whether the children of entity reference nodes are visible to
   * the iterator. If false, they will be skipped over. <br>
   * To produce a view of the document that has entity references expanded and does not expose the
   * entity reference node itself, use the whatToShow flags to hide the entity reference node and
   * set expandEntityReferences to true when creating the iterator. To produce a view of the
   * document that has entity reference nodes but no entity expansion, use the whatToShow flags to
   * show the entity reference node and set expandEntityReferences to false.
   *
   * @return true for all iterators based on NodeSetDTM, meaning that the contents of EntityRefrence
   *     nodes may be returned (though whatToShow says that the EntityReferences themselves are not
   *     shown.)
   */
  @Override
  public boolean getExpandEntityReferences() {
    return true;
  }

  /**
   * Get an instance of a DTM that "owns" a node handle. Since a node iterator may be passed without
   * a DTMManager, this allows the caller to easily get the DTM using just the iterator.
   *
   * @param nodeHandle the nodeHandle.
   * @return a non-null DTM reference.
   */
  @Override
  public DTM getDTM(int nodeHandle) {

    return m_manager.getDTM(nodeHandle);
  }

  /* An instance of the DTMManager. */
  DTMManager m_manager;

  /**
   * Get an instance of the DTMManager. Since a node iterator may be passed without a DTMManager,
   * this allows the caller to easily get the DTMManager using just the iterator.
   *
   * @return a non-null DTMManager reference.
   */
  @Override
  public DTMManager getDTMManager() {

    return m_manager;
  }

  /**
   * Returns the next node in the set and advances the position of the iterator in the set. After a
   * DTMIterator is created, the first call to nextNode() returns the first node in the set.
   *
   * @return The next <code>Node</code> in the set being iterated over, or <code>DTM.NULL</code> if
   *     there are no more members in that set.
   * @throws DOMException INVALID_STATE_ERR: Raised if this method is called after the <code>
   *     detach</code> method was invoked.
   */
  @Override
  public int nextNode() {

    if (m_next < this.size()) {
      int next = this.elementAt(m_next);

      m_next++;

      return next;
    }
    return DTM.NULL;
  }

  /**
   * Returns the previous node in the set and moves the position of the iterator backwards in the
   * set.
   *
   * @return The previous <code>Node</code> in the set being iterated over, or<code>DTM.NULL
   *     </code> if there are no more members in that set.
   * @throws DOMException INVALID_STATE_ERR: Raised if this method is called after the <code>
   *     detach</code> method was invoked.
   * @throws RuntimeException thrown if this NodeSetDTM is not of a cached type, and hence doesn't
   *     know what the previous node was.
   */
  @Override
  public int previousNode() {

    if (!m_cacheNodes)
      throw new RuntimeException(
          XSLMessages.createXPATHMessage(
              XPATHErrorResources.ER_NODESETDTM_CANNOT_ITERATE, null)); // "This
    // NodeSetDTM
    // can not
    // iterate
    // to a
    // previous
    // node!");

    if ((m_next - 1) > 0) {
      m_next--;
      return this.elementAt(m_next);
    }
    return DTM.NULL;
  }

  /**
   * Detaches the iterator from the set which it iterated over, releasing any computational
   * resources and placing the iterator in the INVALID state. After<code>detach</code> has been
   * invoked, calls to <code>nextNode</code> or<code>previousNode</code> will raise the exception
   * INVALID_STATE_ERR.
   *
   * <p>This operation is a no-op in NodeSetDTM, and will not cause INVALID_STATE_ERR to be raised
   * by later operations.
   */
  @Override
  public void detach() {}

  /**
   * Specify if it's OK for detach to release the iterator for reuse.
   *
   * @param allowRelease true if it is OK for detach to release this iterator for pooling.
   */
  @Override
  public void allowDetachToRelease(boolean allowRelease) {
    // no action for right now.
  }

  /**
   * Tells if this NodeSetDTM is "fresh", in other words, if the first nextNode() that is called
   * will return the first node in the set.
   *
   * @return true if nextNode() would return the first node in the set, false if it would return a
   *     later one.
   */
  @Override
  public boolean isFresh() {
    return m_next == 0;
  }

  /**
   * If an index is requested, NodeSetDTM will call this method to run the iterator to the index. By
   * default this sets m_next to the index. If the index argument is -1, this signals that the
   * iterator should be run to the end.
   *
   * @param index Position to advance (or retreat) to, with 0 requesting the reset ("fresh")
   *     position and -1 (or indeed any out-of-bounds value) requesting the final position.
   * @throws RuntimeException thrown if this NodeSetDTM is not one of the types which supports
   *     indexing/counting.
   */
  @Override
  public void runTo(int index) {

    if (!m_cacheNodes)
      throw new RuntimeException(
          XSLMessages.createXPATHMessage(
              XPATHErrorResources.ER_NODESETDTM_CANNOT_INDEX, null)); // "This
    // NodeSetDTM
    // can not do
    // indexing
    // or
    // counting
    // functions!");

    if ((index >= 0) && (m_next < m_firstFree)) m_next = index;
    else m_next = m_firstFree - 1;
  }

  /**
   * Returns the <code>index</code>th item in the collection. If <code>index</code> is greater than
   * or equal to the number of nodes in the list, this returns <code>null</code>.
   *
   * <p>TODO: What happens if index is out of range?
   *
   * @param index Index into the collection.
   * @return The node at the <code>index</code>th position in the <code>NodeList</code>, or <code>
   *     null</code> if that is not a valid index.
   */
  @Override
  public int item(int index) {

    runTo(index);

    return this.elementAt(index);
  }

  /**
   * The number of nodes in the list. The range of valid child node indices is 0 to <code>length-1
   * </code> inclusive. Note that this operation requires finding all the matching nodes, which may
   * defeat attempts to defer that work.
   *
   * @return integer indicating how many nodes are represented by this list.
   */
  @Override
  public int getLength() {

    runTo(-1);

    return this.size();
  }

  /**
   * Add a node to the NodeSetDTM. Not all types of NodeSetDTMs support this operation
   *
   * @param n Node to be added
   * @throws RuntimeException thrown if this NodeSetDTM is not of a mutable type.
   */
  public void addNode(int n) {
    this.addElement(n);
  }

  // %TBD%
  // /**
  // * Copy NodeList members into this nodelist, adding in
  // * document order. If a node is null, don't add it.
  // *
  // * @param nodelist List of nodes which should now be referenced by
  // * this NodeSetDTM.
  // * @throws RuntimeException thrown if this NodeSetDTM is not of
  // * a mutable type.
  // */
  // public void addNodes(NodeList nodelist)
  // {
  //
  // if (!m_mutable)
  // throw new RuntimeException("This NodeSetDTM is not mutable!");
  //
  // if (null != nodelist) // defensive to fix a bug that Sanjiva reported.
  // {
  // int nChildren = nodelist.getLength();
  //
  // for (int i = 0; i < nChildren; i++)
  // {
  // int obj = nodelist.item(i);
  //
  // if (null != obj)
  // {
  // addElement(obj);
  // }
  // }
  // }
  //
  // // checkDups();
  // }

  // %TBD%
  // /**
  // * <p>Copy NodeList members into this nodelist, adding in
  // * document order. Only genuine node references will be copied;
  // * nulls appearing in the source NodeSetDTM will
  // * not be added to this one. </p>
  // *
  // * <p> In case you're wondering why this function is needed: NodeSetDTM
  // * implements both DTMIterator and NodeList. If this method isn't
  // * provided, Java can't decide which of those to use when addNodes()
  // * is invoked. Providing the more-explicit match avoids that
  // * ambiguity.)</p>
  // *
  // * @param ns NodeSetDTM whose members should be merged into this NodeSetDTM.
  // * @throws RuntimeException thrown if this NodeSetDTM is not of
  // * a mutable type.
  // */
  // public void addNodes(NodeSetDTM ns)
  // {
  //
  // if (!m_mutable)
  // throw new RuntimeException("This NodeSetDTM is not mutable!");
  //
  // addNodes((DTMIterator) ns);
  // }

  /**
   * Copy NodeList members into this nodelist, adding in document order. Null references are not
   * added.
   *
   * @param iterator DTMIterator which yields the nodes to be added.
   * @throws RuntimeException thrown if this NodeSetDTM is not of a mutable type.
   */
  public void addNodes(DTMIterator iterator) {
    if (null != iterator) // defensive to fix a bug that Sanjiva reported.
    {
      int obj;

      while (DTM.NULL != (obj = iterator.nextNode())) {
        addElement(obj);
      }
    }

    // checkDups();
  }

  /**
   * Add the node into a vector of nodes where it should occur in document order.
   *
   * @param node The node to be added.
   * @param test true if we should test for doc order
   * @param support The XPath runtime context.
   * @return insertIndex.
   * @throws RuntimeException thrown if this NodeSetDTM is not of a mutable type.
   */
  public int addNodeInDocOrder(int node, boolean test, XPathContext support) {
    int insertIndex = -1;

    if (test) {

      // This needs to do a binary search, but a binary search
      // is somewhat tough because the sequence test involves
      // two nodes.
      int size = size(), i;

      for (i = size - 1; i >= 0; i--) {
        int child = elementAt(i);

        if (child == node) {
          i = -2; // Duplicate, suppress insert

          break;
        }

        DTM dtm = support.getDTM(node);
        if (!dtm.isNodeAfter(node, child)) {
          break;
        }
      }

      if (i != -2) {
        insertIndex = i + 1;

        insertElementAt(node, insertIndex);
      }
    } else {
      insertIndex = this.size();

      boolean foundit = false;

      for (int i = 0; i < insertIndex; i++) {
        if (i == node) {
          foundit = true;

          break;
        }
      }

      if (!foundit) addElement(node);
    }

    // checkDups();
    return insertIndex;
  } // end addNodeInDocOrder(Vector v, Object obj)

  /**
   * Add the node into a vector of nodes where it should occur in document order.
   *
   * @param node The node to be added.
   * @param support The XPath runtime context.
   * @return The index where it was inserted.
   * @throws RuntimeException thrown if this NodeSetDTM is not of a mutable type.
   */
  public int addNodeInDocOrder(int node, XPathContext support) {
    return addNodeInDocOrder(node, true, support);
  } // end addNodeInDocOrder(Vector v, Object obj)

  /**
   * Same as setElementAt.
   *
   * @param node The node to be set.
   * @param index The index of the node to be replaced.
   * @throws RuntimeException thrown if this NodeSetDTM is not of a mutable type.
   */
  @Override
  public void setItem(int node, int index) {
    super.setElementAt(node, index);
  }

  /**
   * Get the nth element.
   *
   * @param i The index of the requested node.
   * @return Node at specified index.
   */
  @Override
  public int elementAt(int i) {

    runTo(i);

    return super.elementAt(i);
  }

  /**
   * Tell if the table contains the given node.
   *
   * @param s Node to look for
   * @return True if the given node was found.
   */
  @Override
  public boolean contains(int s) {

    runTo(-1);

    return super.contains(s);
  }

  /**
   * Searches for the first occurence of the given argument, beginning the search at index, and
   * testing for equality using the equals method.
   *
   * @param elem Node to look for
   * @param index Index of where to start the search
   * @return the index of the first occurrence of the object argument in this vector at position
   *     index or later in the vector; returns -1 if the object is not found.
   */
  @Override
  public int indexOf(int elem, int index) {

    runTo(-1);

    return super.indexOf(elem, index);
  }

  /**
   * Searches for the first occurence of the given argument, beginning the search at index, and
   * testing for equality using the equals method.
   *
   * @param elem Node to look for
   * @return the index of the first occurrence of the object argument in this vector at position
   *     index or later in the vector; returns -1 if the object is not found.
   */
  @Override
  public int indexOf(int elem) {

    runTo(-1);

    return super.indexOf(elem);
  }

  /** If this node is being used as an iterator, the next index that nextNode() will return. */
  protected transient int m_next = 0;

  /**
   * Get the current position, which is one less than the next nextNode() call will retrieve. i.e.
   * if you call getCurrentPos() and the return is 0, the next fetch will take place at index 1.
   *
   * @return The the current position index.
   */
  @Override
  public int getCurrentPos() {
    return m_next;
  }

  /**
   * Set the current position in the node set.
   *
   * @param i Must be a valid index.
   * @throws RuntimeException thrown if this NodeSetDTM is not of a cached type, and thus doesn't
   *     permit indexed access.
   */
  @Override
  public void setCurrentPos(int i) {

    if (!m_cacheNodes)
      throw new RuntimeException(
          XSLMessages.createXPATHMessage(
              XPATHErrorResources.ER_NODESETDTM_CANNOT_INDEX, null)); // "This
    // NodeSetDTM
    // can not do
    // indexing
    // or
    // counting
    // functions!");

    m_next = i;
  }

  /**
   * Return the last fetched node. Needed to support the UnionPathIterator.
   *
   * @return the last fetched node.
   * @throws RuntimeException thrown if this NodeSetDTM is not of a cached type, and thus doesn't
   *     permit indexed access.
   */
  @Override
  public int getCurrentNode() {

    if (!m_cacheNodes)
      throw new RuntimeException("This NodeSetDTM can not do indexing or counting functions!");

    int saved = m_next;
    // because nextNode always increments
    // But watch out for copy29, where the root iterator didn't
    // have nextNode called on it.
    int current = (m_next > 0) ? m_next - 1 : m_next;
    int n = (current < m_firstFree) ? elementAt(current) : DTM.NULL;
    m_next = saved; // HACK: I think this is a bit of a hack. -sb
    return n;
  }

  /**
   * True if this list is cached.
   *
   * @serial
   */
  protected transient boolean m_cacheNodes = true;

  /** The root of the iteration, if available. */
  protected int m_root = DTM.NULL;

  /**
   * If setShouldCacheNodes(true) is called, then nodes will be cached. They are not cached by
   * default. This switch must be set before the first call to nextNode is made, to ensure that all
   * nodes are cached.
   *
   * @param b true if this node set should be cached.
   * @throws RuntimeException thrown if an attempt is made to request caching after we've already
   *     begun stepping through the nodes in this set.
   */
  @Override
  public void setShouldCacheNodes(boolean b) {

    if (!isFresh())
      throw new RuntimeException(
          XSLMessages.createXPATHMessage(
              XPATHErrorResources.ER_CANNOT_CALL_SETSHOULDCACHENODE, null)); // "Can
    // not
    // call
    // setShouldCacheNodes
    // after
    // nextNode
    // has
    // been
    // called!");

    m_cacheNodes = b;
  }

  private transient int m_last = 0;

  public int getLast() {
    return m_last;
  }

  public void setLast(int last) {
    m_last = last;
  }

  /**
   * Returns true if all the nodes in the iteration well be returned in document order.
   *
   * @return true as a default.
   */
  @Override
  public boolean isDocOrdered() {
    return true;
  }

  /**
   * Returns the axis being iterated, if it is known.
   *
   * @return Axis.CHILD, etc., or -1 if the axis is not known or is of multiple types.
   */
  @Override
  public int getAxis() {
    return -1;
  }
}
