package edu.brown.cs.cs32friends.maps;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import edu.brown.cs.cs32friends.graph.EdgeStorable;
import edu.brown.cs.cs32friends.graph.GraphSourceParser;
import edu.brown.cs.cs32friends.graph.ValuedEdge;
import edu.brown.cs.cs32friends.graph.ValuedVertex;
import edu.brown.cs.cs32friends.graph.VertexStorable;

import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * A cached {@link GraphSourceParser} that uses {@link com.google.common.cache.Cache}.
 *
 * @param <T> The type of Object that {@link ValuedVertex} stores
 * @param <W> The type of Object that {@link ValuedEdge} stores
 */
public class CachedMapsGraphSrcParser
    <T extends VertexStorable, W extends EdgeStorable>
    implements GraphSourceParser {
  private GraphSourceParser parser;
  private LoadingCache<T, Set<W>> edgeValueCache;
  private LoadingCache<String, T> vertValueCache;

  /**
   * Constructs a new CachingGraphSourceParser.
   *
   * @param parser the {@link GraphSourceParser}
   * @param maxVertSize maximum number of vertices to be stored in the cache
   * @param maxEdgeSize maximum number of edges to be stored in the cache
   */
  public CachedMapsGraphSrcParser(GraphSourceParser parser, int maxVertSize, int maxEdgeSize) {
    this.parser = parser;
    edgeValueCache = CacheBuilder.newBuilder()
        .maximumSize(maxEdgeSize)
        .build(
            new CacheLoader<>() {
              public Set<W> load(T key) {
                return parser.getEdgeValues(key);
              }
            });

    vertValueCache = CacheBuilder.newBuilder()
        .maximumSize(maxVertSize)
        .build(
            new CacheLoader<>() {
              public T load(String key) {
                return parser.getVertexValue(key);
              }
            });

  }

  /**
   * Clears the cache.
   */
  public void clearCache() {
    vertValueCache.invalidateAll();
    edgeValueCache.invalidateAll();
  }

  /**
   * Outputs an {@link Object} parsed from the data source that matches the query given.
   *
   * @param name An identifier for the requested Object.
   * @return An {@link Object} parsed from the data source
   */
  @Override
  public T getVertexValue(String name) {
    try {
      return vertValueCache.get(name);
    } catch (ExecutionException e) {
      throw new IllegalStateException("broke edge cash.");
    }
  }

  /**
   * Gets the values associated with a given {@link VertexStorable}.
   *
   * @param v a value stored in a {@link ValuedVertex}
   * @return A set of values associated with this {@link VertexStorable}
   */
  @Override
  public Set<W> getEdgeValues(VertexStorable v) {
    T key;
    try {
      key = (T) v;
    } catch (ClassCastException e) {
      throw new IllegalArgumentException(
          "must pass a VertexStorable of same type of CachingGraphSourceParser generic.");
    }

    try {
      return edgeValueCache.get(key);
    } catch (ExecutionException e) {
      throw new IllegalStateException("broke edge cash.");
    }
  }

  /**
   * Gets the values associated with a given {@link EdgeStorable}.
   *
   * @param e a value stored in a {@link ValuedEdge}
   * @return A set of values associated with this {@link EdgeStorable}
   */
  @Override
  public Set<T> getVertexValues(EdgeStorable e) {
    return parser.getVertexValues(e);
  }
}

