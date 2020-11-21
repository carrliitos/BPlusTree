package BPTree;

/* 
* Stores all of the configuration parameters for our B+ Tree
*/

public class BPlusConfig {
	// Page size
	private int pageSize;
	// Key size
	private int keySize;
	// Entry size
	private int entrySize;
	// Tree degree (internal node degree)
	private int treeDegree;
	// Header size
	private int headerSize;
	// Leaf node header size
	private int leafNodeHeaderSize;
	// Internal node header size
	private int internalNodeHeaderSize;
	// Lookup overflow page header size
	private int lookupOverflowHeaderSize;
	// Lookup overflow page degree
	private int lookupOverflowPageDegree;
	// Leaf node degree
	private int leafNodeDegree;
	// Overflow page degree
	private int overflowPageDegree;
	// Look up page size
	private int lookupPageSize;
	// Iterations to perform conditioning
	private int conditionThreshold;

	/**
	*  Default constructor which initializes all
	*  settings to the predefined defaults.
	*/
	public BPlusConfig() {
		basicParams(1024, 8, 20);
		initializeCommon(pageSize, keySize, entrySize, 1000);
	}

	/**
	* Overloaded constructor allows page size adjustments
	* @param pageSize page size (in bytes)
	*/
	public BPlusConfig(int pageSize) {
		basicParams(pageSize, 8, 20);
		initializeCommon(pageSize, keySize, entrySize, 1000);
	}

	/**
	* Overloaded constructor
	* @param pageSize page size (default is 1024 bytes)
	* @param keySize key size (default is long [8 bytes])
	* @param entrySize satellite data (default is 20 bytes)
	*/
	public BPlusConfig(int pageSize, int keySize,
							  int entrySize) {
		basicParams(pageSize, keySize, entrySize);
		initializeCommon(pageSize, keySize, entrySize, 1000);
	}

	/**
	* Overloaded constructor
	* @param pageSize		   page size (default is 1024 bytes)
	* @param keySize			key size (default is long [8 bytes])
	* @param entrySize		  satellite data (default is 20 bytes)
	* @param conditionThreshold threshold to perform file conditioning
	*/
	@SuppressWarnings("unused")
	public BPlusConfig(int pageSize, int keySize,
							  int entrySize, int conditionThreshold) {
		basicParams(pageSize, keySize, entrySize);
		initializeCommon(pageSize, keySize, entrySize, conditionThreshold);
	}

	/**
	* Set up the basic parameters of the tree
	* @param pageSize  page size (default is 1024 bytes)
	* @param keySize   key size (default is long [8 bytes])
	* @param entrySize satellite data (default is 20 bytes)
	*/
	private void basicParams(int pageSize, int keySize, int entrySize) {
		this.pageSize = pageSize;   // page size (in bytes)
		this.entrySize = entrySize; // entry size (in bytes)
		this.keySize = keySize;	 // key size (in bytes)
	}

	/**
	* Common method to initialize constructor parameters
	* @param pageSize page size (default is 1024 bytes)
	* @param keySize key size (default is long [8 bytes])
	* @param entrySize satellite data (default is 20 bytes)
	* @param conditionThreshold the number of iterations before file conditioning
	*/
	private void initializeCommon(int pageSize, int keySize,
								  int entrySize, int conditionThreshold) {
		// header size in bytes
		this.headerSize = (Integer.SIZE * 4 + 4 * Long.SIZE) / 8;
		// 6 bytes
		this.internalNodeHeaderSize = (Short.SIZE + Integer.SIZE) / 8;
		// 22 bytes
		this.leafNodeHeaderSize = (Short.SIZE + 2 * Long.SIZE + Integer.SIZE) / 8; 
		this.lookupOverflowHeaderSize = 14;
		// lookup page size
		this.lookupPageSize = pageSize - headerSize;
		// iterations for conditioning
		this.conditionThreshold = conditionThreshold;
		// now calculate the tree degree
		this.treeDegree = calculateDegree(2 * keySize, internalNodeHeaderSize);
		// leaf & overflow have the same header size.
		this.leafNodeDegree = calculateDegree((2 * keySize) + entrySize, leafNodeHeaderSize);
		this.overflowPageDegree = calculateDegree(entrySize, leafNodeHeaderSize);
		this.lookupOverflowPageDegree = calculateDegree(keySize, lookupOverflowHeaderSize);
		checkDegreeValidity();
	}

	/**
	* Calculates the degree of a node
	* @param elementSize  		the node element size
	* @param elementHeaderSize 	the node
	* @return 					the node degree
	*/
	private int calculateDegree(int elementSize, int elementHeaderSize) {
		return((int) (((pageSize - elementHeaderSize) / (2.0 * elementSize)) + 0.5));
	}

	/**
	* Checks if we have any degree < 2 (which is not allowed)
	*/
	private void checkDegreeValidity() {
		if(treeDegree < 2 || 
			leafNodeDegree < 2 || 
			overflowPageDegree < 2 || 
			lookupOverflowPageDegree < 2) {
			throw new IllegalArgumentException("Cant have a degree < 2");
		}
	}

	public int getPageSize() { return pageSize; }
	public int getEntrySize() {	return entrySize; }
	public int getFirstLookupPageElements() { return lookupPageSize / keySize; }
	public int getTreeDegree() { return treeDegree; }
	public int getOverflowPageDegree() { return (overflowPageDegree); }
	public int getMaxInternalNodeCapacity() { return ((2 * treeDegree) - 1); }
	public int getMaxLeafNodeCapacity() { return ((2 * leafNodeDegree) - 1); }
	public int getMaxOverflowNodeCapacity() { return ((2 * overflowPageDegree) - 1); }
	public int getMaxLookupPageOverflowCapacity() { return ((2 * lookupOverflowPageDegree) - 1); }
	public int getMinLeafNodeCapacity() { return (leafNodeDegree - 1); }
	public int getMinInternalNodeCapacity()	{ return (treeDegree - 1); }
	public int getKeySize()	{ return keySize; }
	public int getLeafNodeDegree() { return leafNodeDegree; }
	public int getLookupPageDegree() { return (pageSize / keySize); }
	public int getLookupPageSize() { return (lookupPageSize); }
	public long getLookupPageOffset() { return (pageSize - lookupPageSize); }
	public int getConditionThreshold() { return (conditionThreshold); }
	public int getHeaderSize() { return (headerSize); }
	public int getPageCountOffset() { return (headerSize - 16);	}
	public int getLookupOverflowHeaderSize() { return (lookupOverflowHeaderSize); }

	public void setConditionThreshold(int conditionThreshold) {
		this.conditionThreshold = conditionThreshold; 
	}

	public void printConfiguration() {
		System.out.println("\n\nPrinting B+ Tree configuration\n");
		System.out.println("Page size: " + pageSize + " (in bytes)");
		System.out.println("Key size: " + keySize + " (in bytes)");
		System.out.println("Entry size: " + entrySize + " (in bytes)");
		System.out.println("File header size: " + headerSize + " (in bytes)");
		System.out.println("Lookup space size: " + getLookupPageSize() +
				" (in bytes)");
		System.out.println("\nInternal Node Degree: " +
				getTreeDegree() +
				"\n\t Min cap: " + getMinInternalNodeCapacity() +
				"\n\t Max cap: " + getMaxInternalNodeCapacity() +
				"\n\t Total header bytes: " + internalNodeHeaderSize);

		System.out.println("\nLeaf Node Degree: " +
				getLeafNodeDegree() +
				"\n\t Min cap: " + getMinLeafNodeCapacity() +
				"\n\t Max cap: " + getMaxLeafNodeCapacity() +
				"\n\t Total header bytes: " + leafNodeHeaderSize);

		System.out.println("\nOverflow page Degree: " +
				getOverflowPageDegree() +
				"\n\tExpected cap: " + getMaxOverflowNodeCapacity());

		System.out.println("\nLookup page overflow Degree" +
				getOverflowPageDegree() +
				"\n\tExpected cap: " + getMaxInternalNodeCapacity());
	}
}