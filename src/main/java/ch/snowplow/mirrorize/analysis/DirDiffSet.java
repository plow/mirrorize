package ch.snowplow.mirrorize.analysis;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import ch.snowplow.mirrorize.common.Path;
import ch.snowplow.mirrorize.common.Tools;

public class DirDiffSet extends TreeSet<Path> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DirDiffSet(Set<Path> set) {
        // TODO maybe its possible to provide a SortedSet here for performance
        // reasons
        super(set);
    }

    @Override
    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        Iterator<Path> it = iterator();
        while (it.hasNext()) {
            Path path = it.next();
            strBuf.append(Tools.getSpaces(path.getDepth() * 2))
                    .append(path.getName()).append("\n");
        }
        return strBuf.toString();
    }

}
