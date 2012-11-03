package ch.snowplow.mirrorize.testdata;

import java.io.File;

import ch.snowplow.mirrorize.testdata.builders.FileBuilder;
import ch.snowplow.mirrorize.testdata.builders.PathBuilder;

public enum FileTestData {

    // TODO: make sure that there are two files with the same content, i.e.,
    // with the same hash in one and the same tree.

    // identical file in both trees
    FILE_TR1_A1("tree1/a/file_a1.txt", "content of the file file_a1.txt"),
    FILE_TR2_A1("tree2/a/file_a1.txt", "content of the file file_a1.txt"),
    // file existing only in tree 1
    FILE_TR1_A2("tree1/a/file_a2.txt", "content of the file file_a2.txt"),
    // file existing only in tree 2
    FILE_TR2_A3("tree2/a/file_a3.txt", "content of the file file_a3.txt"),
    // file existing in both trees with different paths but same content
    FILE_TR1_A4("tree1/a/file_a4_1.txt", "content of the file file_a4.txt"),
    FILE_TR2_A4("tree2/a/file_a4_2.txt", "content of the file file_a4.txt"),
    // file existing in both trees with different content but same path
    FILE_TR1_A5("tree1/a/file_a5.txt", "content of the file file_a5_1.txt"),
    FILE_TR2_A5("tree2/a/file_a5.txt", "content of the file file_a5_1.txt"),

    // -----------------------

    // identical file in both trees
    FILE_TR1_B1("tree1/b/file_b1.txt", "content of the file file_b1.txt"),
    FILE_TR2_B1("tree2/b/file_b1.txt", "content of the file file_b1.txt"),
    // identical subtree in both trees
    FILE_TR1_B1_D("tree1/b_dir/file_b1d.txt",
            "content of the file file_b1d.txt"),
    FILE_TR2_B1_D("tree2/b_dir/file_b1d.txt",
            "content of the file file_b1d.txt"),
    FILE_TR1_B2_D("tree1/b_dir/file_b2d.txt",
            "content of the file file_b2d.txt"),
    FILE_TR2_B2_D("tree2/b_dir/file_b2d.txt",
            "content of the file file_b2d.txt"),

    // file existing only in tree 1
    FILE_TR1_C("tree1/c/file_c1.txt", "content of the file file_c1.txt"),
    // subtree existing only in tree1
    FILE_TR1_C1_D("tree1/c_dir/file_c1d.txt",
            "content of the file file_c1d.txt"),
    FILE_TR1_C2_D("tree1/c_dir/file_c2d.txt",
            "content of the file file_c2d.txt"),

    // file existing only in tree 2
    FILE_TR2_D("tree2/d/file_d1.txt", "content of the file file_d1.txt"),
    // subtree existing only in tree 2
    FILE_TR1_D1_D("tree2/d_dir/file_d1d.txt",
            "content of the file file_d1d.txt"),
    FILE_TR1_D2_D("tree2/d_dir/file_d2d.txt",
            "content of the file file_d2d.txt"),

    // file existing in both trees with different paths but same content
    FILE_TR1_E("tree1/e/file_e1.txt", "content of the file file_e.txt"),
    FILE_TR2_E("tree2/e/file_e2.txt", "content of the file file_e.txt"),
    // subtree existing in both trees with different paths but same content
    FILE_TR1_E1_D("tree1/e_dir1/file_e1d.txt",
            "content of the file file_e1d.txt"),
    FILE_TR1_E2_D("tree1/e_dir1/file_e2d.txt",
            "content of the file file_e2d.txt"),
    FILE_TR2_E1_D("tree2/e_dir2/file_e1d.txt",
            "content of the file file_e1d.txt"),
    FILE_TR2_E2_D("tree2/e_dir2/file_e2d.txt",
            "content of the file file_e2d.txt"),

    // file existing in both trees with different content but same path
    FILE_TR1_F("tree1/f/file_f.txt", "content of the file file_f1.txt"),
    FILE_TR2_F("tree2/f/file_f.txt", "content of the file file_f2.txt"),
    // subtree existing in both trees with different content but same path
    FILE_TR1_F1_D("tree1/f_dir/file_f1d.txt",
            "content of the file file_e1d_1.txt"),
    FILE_TR1_F2_D("tree1/f_dir/file_f2d.txt",
            "content of the file file_e2d_1.txt"),
    FILE_TR2_F1_D("tree1/f_dir/file_f1d.txt",
            "content of the file file_e1d_2.txt"),
    FILE_TR2_F2_D("tree1/f_dir/file_f2d.txt",
            "content of the file file_e2d_2.txt");

    String path, content;

    FileTestData(String path, String content) {
        this.path = path;
        this.content = content;
    }

    public File build() {
        return new FileBuilder()
                .withPath(new PathBuilder().withPath(path).build())
                .withContent(content).build();
    }

    public File build(String treeRoot) {
        return new FileBuilder()
                .withPath(new PathBuilder().withPath(path).build())
                .withContent(content).withTreeRoot(treeRoot).build();
    }

}