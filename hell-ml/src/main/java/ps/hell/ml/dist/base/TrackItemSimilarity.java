package ps.hell.ml.dist.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class TrackItemSimilarity {

	public class TrackData {

		private String NO_VALUE = "None";
		private HashSet<Long> NO_GENRES = new HashSet<Long>();

		private long trackID;
		private long albumID;
		private long artistID;
		private HashSet<Long> genreIDs;

		public TrackData(long trackID, long albumID, long artistID,
				long... tokens) {
			trackID = trackID;
			albumID = albumID;
			artistID = artistID;
			for (long token : tokens) {
				genreIDs.add(token);
			}
		}

		public long getTrackID() {
			return trackID;
		}

		public long getAlbumID() {
			return albumID;
		}

		public long getArtistID() {
			return artistID;
		}

		public HashSet<Long> getGenreIDs() {
			return genreIDs;
		}

	}
	public HashMap<Long,TrackData> trackData=new HashMap<Long,TrackData>();
	public TrackItemSimilarity(TrackData[] data) {
	    for (TrackData d:data) {
	      trackData.put(d.getTrackID(), d);
	    }
	  }

	  public double itemSimilarity(long itemID1, long itemID2) {
	    if (itemID1 == itemID2) {
	      return 1.0;
	    }
	    TrackData data1 = trackData.get(itemID1);
	    TrackData data2 = trackData.get(itemID2);
	    if (data1 == null || data2 == null) {
	      return 0.0;
	    }

	    // Arbitrarily decide that same album means "very similar"
	    if (data1.getAlbumID()>0L  && data1.getAlbumID() == data2.getAlbumID()) {
	      return 0.9;
	    }
	    // ... and same artist means "fairly similar"
	    if (data1.getArtistID()>0L  && data1.getArtistID() == data2.getArtistID()) {
	      return 0.7;
	    }

	    // Tanimoto coefficient similarity based on genre, but maximum value of 0.25
	    HashSet<Long> genres1 = data1.getGenreIDs();
	    HashSet<Long> genres2 = data2.getGenreIDs();
	    if (genres1 == null || genres2 == null) {
	      return 0.0;
	    }
	    int intersectionSize = intersectionSize(genres1,genres2);
	    if (intersectionSize == 0) {
	      return 0.0;
	    }
	    int unionSize = genres1.size() + genres2.size() - intersectionSize;
	    return intersectionSize / (4.0 * unionSize);
	  }
	  
	  public int intersectionSize(HashSet<Long> set1,HashSet<Long> set2){
		  int sum=0;
		  for(Long val:set1){
			  if(set2.contains(val)){
				  sum++;
			  }
		  }
		  return sum;
	  }

	  public double[] itemSimilarities(long itemID1, long[] itemID2s) {
	    int length = itemID2s.length;
	    double[] result = new double[length];
	    for (int i = 0; i < length; i++) {
	      result[i] = itemSimilarity(itemID1, itemID2s[i]);
	    }
	    return result;
	  }

	  public Long[] allSimilarItemIDs(long itemID) {
	    HashSet<Long> allSimilarItemIDs = new HashSet<Long>();
	    for (Entry<Long,TrackData> possiblySimilarItemID:trackData.entrySet()) {
	      if (!Double.isNaN(itemSimilarity(itemID, possiblySimilarItemID.getKey()))) {
	        allSimilarItemIDs.add(possiblySimilarItemID.getKey());
	      }
	    }
	    return (Long[])allSimilarItemIDs.toArray();
	  }
	  
}
