import java.util.*;


class Sequence {
	private List<Integer> seq = new LinkedList<>();
	
	public Integer get(int index) {
		return seq.get(index);
	}
	
	public void append(Integer y) {
		seq.add(y);
	}
	
	public int size() {
		return seq.size();
	}
}

class SeqList {
	private int n;
	private List<Sequence> list;
	private int lastAnswer = 0;
	
	public SeqList(int n) {
		this.n = n;
		list = new ArrayList<>(n);
		for (int j = 0; j < n; j++)
			list.add(new Sequence());
	}
	
	public Sequence findSequence(int x) {
		int index = (x ^ lastAnswer) % n;
		return list.get(index);
	}

	public int getLastAnswer() {
		return lastAnswer;
	}

	public void setLastAnswer(int lastAnswer) {
		this.lastAnswer = lastAnswer;
	}
	
}


public class Solution {
	
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int q = in.nextInt();
        
        SeqList seqList = new SeqList(n);
        
        while (q-- > 0) {
        	int queryType = in.nextInt();
        	int x = in.nextInt();
        	int y = in.nextInt();
        	
        	Sequence seq;
        	switch (queryType) {
        		case 1:	seq = seqList.findSequence(x);
        				seq.append(y);
        				break;
        		case 2:	seq = seqList.findSequence(x);
        				seqList.setLastAnswer(seq.get(y % seq.size()));
        				System.out.println(seqList.getLastAnswer());
        				break;
        	}
        }
        in.close();
    }
}