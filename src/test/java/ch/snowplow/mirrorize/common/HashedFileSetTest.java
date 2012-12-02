package ch.snowplow.mirrorize.common;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import ch.snowplow.mirrorize.testdata.builders.HashedFileBuilder;
import ch.snowplow.mirrorize.testdata.builders.HashedFileSetBuilder;
import ch.snowplow.mirrorize.testdata.builders.PathBuilder;

public class HashedFileSetTest extends TestCase {

    public void testRemoveAllByPath() {
        HashedFileSet<String> fhs = new HashedFileSetBuilder<String>()
                .addFileHash(
                        new HashedFileBuilder<String>("").withHash("ahr")
                                .withPath("a/pat.h").build())
                .addFileHash(
                        new HashedFileBuilder<String>("").withHash("cer")
                                .withPath("g/pat.e").build())
                .addFileHash(
                        new HashedFileBuilder<String>("").withHash("era")
                                .withPath("k/pat.q").build()).build();

        assertEquals(3, fhs.size());
        List<Path> paths;
        paths = Arrays.asList(new PathBuilder().withPath("no/pat.h").build(),
                new PathBuilder().withPath("neither/a/pat.h").build());
        fhs.removeAllByPath(paths);
        assertEquals(3, fhs.size());

        paths = Arrays.asList(new PathBuilder().withPath("no/pat.h").build(),
                new PathBuilder().withPath("neither/a/pat.h").build(),
                new PathBuilder().withPath("a/pat.h").build());
        fhs.removeAllByPath(paths);
        assertEquals(2, fhs.size());
        for (HashedFile<String> fh : fhs) {
            assertFalse("a/pat.h".equals(fh.getPath().getPath()));
        }

        paths = Arrays.asList(new PathBuilder().withPath("g/pat.e").build(),
                new PathBuilder().withPath("k/pat.q").build(),
                new PathBuilder().withPath("a/pat.h").build());
        fhs.removeAllByPath(paths);
        assertEquals(0, fhs.size());
    }
}
