package BPTree;

/* Stores all of the configuration parameters for our B+ Tree */

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

	// Default constructor
	public BPlusConfig() {
		basicParams();
		initializeCommon(pageSize, keySize, entrySize, 1000);
	}

	/**
	* Basic parameters for the tree
	* @param pageSize 	page size, default to 1024 bytes
	* @param keySize 	key size, default to long (8 bytes)
	* @param entrySize 	entry size, default to 20 bytes
	*/
	public void basicParams(int pageSize, int keySize, int entrySize) {
		this.pageSize = pageSize;
		this.keySize = keySize;
		this.entrySize = entrySize;
	}

	/**
	* Common method to initialize the constructor
	* @param pageSize 	page size
	* @param keySize 	key size
	* @param entrySize 	entry size
	* @param conditionThreshold	number of iterations before file conditioning
	*/
	public void initializeCommon(int pageSize, int keySize, 
			int entrySize, int conditionThreshold) {
		continue;
	}
}