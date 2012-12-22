package ch.snowplow.mirrorize.analysis;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import ch.snowplow.mirrorize.common.HashedFile;
import ch.snowplow.mirrorize.common.HashedFileSet;
import ch.snowplow.mirrorize.testdata.builders.HashedFileBuilder;
import ch.snowplow.mirrorize.testdata.builders.HashedFileSetBuilder;

public class DirDiffSetTest extends TestCase {

    List<HashedFile<String>> hashedFile;
    HashedFileSet<String> hfs;
    DirDiffSet<String> dds;

    @Override
    protected void setUp() {
        hashedFile = new ArrayList<HashedFile<String>>(3);
        hashedFile.add(new HashedFileBuilder<String>("").withPath("a")
                .withHash("abc").build());
        hashedFile.add(new HashedFileBuilder<String>("").withPath("a/pat.h")
                .withHash("def").build());
        hashedFile.add(new HashedFileBuilder<String>("").withPath("b/pat.h")
                .withHash("ghi").build());
        hfs = new HashedFileSetBuilder<String>()
                .addHashedFile(hashedFile.get(2))
                .addHashedFile(hashedFile.get(1))
                .addHashedFile(hashedFile.get(0)).build();
        dds = new DirDiffSet<>(hfs);
    }

    public void testDirDiffSet() {
        assertEquals(hfs.size(), dds.size());
        for (int i = 0; i < 3; i++) {
            assertTrue(dds.contains(hashedFile.get(i)));
        }
    }

    public void testToString() {
        System.out.println(dds.toString());
        assertEquals("a\na/pat.h\nb/pat.h\n", dds.toString());
    }
}
