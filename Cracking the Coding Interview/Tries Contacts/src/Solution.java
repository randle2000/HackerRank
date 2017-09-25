import java.util.*;

interface Trie {

    /**
     * Check if this trie contains the word.
     * @param word The word to check.
     * @return If this trie contains the word.
     */
    boolean contains(String word);

    /**
     * Get the number of occurances of 'word' in this trie.
     * @param word The word to check.
     * @return The number of occurances of 'word' in this trie;
     * 0 if non-existant.
     */
    int frequency(String word);

    /**
     * Inserts 'word' into this trie.
     * @param word The word to insert into the trie.
     * @return The number of occurances of 'word' in the trie
     * after the insertion.
     */
    int insert(String word);

    /**
     * @return The number of unique words in this trie.
     */
    int size();
    
	int substringFrequency(String substr);

}


/**
 * An implementation of a trie that 
 * dynamically grows and shrinks with word insertions 
 * and removals and supports approximate matching of words
 * using edit distance and word frequency.
 * 
 * This trie implementation is not thread-safe because I didn't need it to be,
 * but it would be easy to change.
 */
class TrieImpl implements Trie {

	// dummy node for root of trie
	final TrieNode root;
	
	// current number of unique words in trie
	private int size;
	
	// if this is a case sensitive trie
	protected boolean caseSensitive;
	
	/**
	 * @param caseSensitive If this Trie should be case-insensitive to the words it encounters. 
	 * Case-insensitivity is accomplished by converting String arguments to all functions to 
	 * lower-case before proceeding.
	 */
	public TrieImpl(boolean caseSensitive) {
		root = new TrieNode((char)0);
		size = 0;
		this.caseSensitive = caseSensitive;
	}
	
	public int insert(String word) {
		if (word == null)
			return 0;
		
		int i = root.insert(caseSensitive ? word : word.toLowerCase(), 0);
		
		if (i == 1)
			size++;
		
		return i;
	}
	
	public int frequency(String word) {
		if (word == null)
			return 0;
		
		TrieNode n = root.lookup(caseSensitive ? word : word.toLowerCase(), 0);
		return n == null ? 0 : n.occurances;
	}
	
	public int substringFrequency(String substr) {
		if (substr == null)
			return 0;
		
		TrieNode n = root.lookup(caseSensitive ? substr : substr.toLowerCase(), 0);
		return n == null ? 0 : n.wordCount;
	}
	
	public boolean contains(String word) {
		if (word == null)
			return false;
		
		return root.lookup(caseSensitive ? word : word.toLowerCase(), 0) != null;
	}
	
	public int size() {
		return size;
	}
	
	@Override
	public String toString() {
		return root.toString();
	}
}

/**
 * A node in a trie.
 */
class TrieNode {
	char c;
	int occurances;
	int wordCount;	// total number of words that was inserted 
	Map<Character, TrieNode> children;
	
	TrieNode(char c) {
		this.c = c;
		occurances = 0;
		wordCount = 0;
		children = null;
	}
	
	int insert(String s, int pos) {
		if (s == null || pos >= s.length())
			return 0;
		
		wordCount++;
		
		// allocate on demand
		if (children == null)
			children = new HashMap<Character, TrieNode>();

		char c = s.charAt(pos);
		TrieNode n = children.get(c);

		// make sure we have a child with char c
		if (n == null) {
			n = new TrieNode(c);
			children.put(c, n);
		}
		
		// if we are the last node in the sequence of chars
		// that make up the string
		if (pos == s.length()-1) {
			n.occurances++;
			n.wordCount++;
			return n.occurances;
		} else
			return n.insert(s, pos+1);
	}
	
	TrieNode lookup(String s, int pos) {
		if (s == null)
			return null;
		
		if (pos >= s.length() || children == null)
			return null;
		else if (pos == s.length()-1)
			return children.get(s.charAt(pos));
		else {
			TrieNode n = children.get(s.charAt(pos)); 
			return n == null ? null : n.lookup(s, pos+1);
		}
	}
}



public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Trie trie = new TrieImpl(false);
        for(int a0 = 0; a0 < n; a0++){
            String op = in.next();
            String contact = in.next();
            switch (op) {
            	case "add":
            		trie.insert(contact);
            		break;
            	case "find":
            		System.out.println(trie.substringFrequency(contact));
            		break;
            	default:
            		throw new IllegalArgumentException();
            }
        }
        in.close();
    }
}