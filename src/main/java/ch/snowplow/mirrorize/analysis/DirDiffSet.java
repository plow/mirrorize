package ch.snowplow.mirrorize.analysis;

import java.util.Iterator;
import java.util.TreeSet;

import ch.snowplow.mirrorize.common.HashedFile;
import ch.snowplow.mirrorize.common.HashedFileRelat;
import ch.snowplow.mirrorize.common.HashedFileSet;

public class DirDiffSet<T extends Comparable<T>> extends TreeSet<HashedFile<T>> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DirDiffSet(HashedFileSet<T> set) {
        // TODO maybe its possible to provide a SortedSet here for performance
        // reasons
        super(set);
    }

    @Override
    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        Iterator<HashedFile<T>> it = iterator();
        while (it.hasNext()) {
            HashedFile<T> fileHash = it.next();
            // strBuf.append(Tools.getSpaces(fileHash.getPath().getDepth() * 2))
            // .append(fileHash.getPath().getName()).append("\n");

            strBuf.append(fileHash.getPath().getPath());
            if (fileHash instanceof HashedFileRelat) {
                strBuf.append(" -> ").append(
                        ((HashedFileRelat<T>) fileHash).getRelatedPaths());
            }
            strBuf.append("\n");
        }
        return strBuf.toString();
    }

}
