package summarizer.simgle;

public class RelevanceScore implements Comparable<RelevanceScore>{
	public int score;
	public int index;
	@Override
	public int compareTo(RelevanceScore o) {
		// TODO Auto-generated method stub
		return o.score-score;
	}
}
