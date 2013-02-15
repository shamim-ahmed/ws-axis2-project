package comp.mq.edu.itec833.content;

/**
 * The interface that all search managers implement
 * @author Shamim Ahmed
 *
 */
public interface ContentManager {
  /**
   * this method initiates a search for content
   * @param tag the keyword
   * @param maxPerTag max number of items in result
   * @return an XML string representing the search result
   */
  String search(String tag, int maxPerTag);
}
