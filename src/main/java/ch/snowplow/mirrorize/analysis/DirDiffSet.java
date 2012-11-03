package ch.snowplow.mirrorize.analysis;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import ch.snowplow.mirrorize.common.FileHash;
import ch.snowplow.mirrorize.common.Tools;

public class DirDiffSet<T> extends TreeSet<FileHash<T>> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DirDiffSet(Set<FileHash<T>> set) {
        // TODO maybe its possible to provide a SortedSet here for performance
        // reasons
        super(set);
    }

    @Override
    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        Iterator<FileHash<T>> it = iterator();
        while (it.hasNext()) {
            FileHash<T> fileHash = it.next();
            strBuf.append(Tools.getSpaces(fileHash.getPath().getDepth() * 2))
                    .append(fileHash.getPath().getName()).append("\n");
        }
        return strBuf.toString();
    }

}
