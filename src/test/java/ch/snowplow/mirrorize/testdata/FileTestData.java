package ch.snowplow.mirrorize.testdata;

import java.io.File;

import ch.snowplow.mirrorize.testdata.builders.FileBuilder;
import ch.snowplow.mirrorize.testdata.builders.PathBuilder;

public enum FileTestData {

    // TODO: make sure that there are two files with the same content, i.e.,
    // with the same hash in one and the same tree.

    /*
     * 0789b4aa776b949e5c90c69dc8b0eb98 tree1/c_dir/file_c1d.txt
     * 0b85fda69267ba2933a468f891e812d5 tree1/c_dir/file_c2d.txt
     * d47e535b32ac49e17cd1fe8de2669598 tree1/e_dir1/file_e1d.txt
     * 1d26753ad87127981110f7176646de37 tree1/e_dir1/file_e2d.txt
     * be701c4b62bf36cae8f6919f400377d7 tree1/e/file_e1.txt
     * 88ce097fd926d442f5c004da42defc58 tree1/b_dir/file_b2d.txt
     * 5c286bd756dec2babb0c33a16a346635 tree1/b_dir/file_b1d.txt
     * 6628110ad88b31b4a7d56e0918f48822 tree1/f_dir/file_f1d.txt
     * 8f650af5567b0347ef13e97387f0b3f9 tree1/f_dir/file_f2d.txt
     * 86c0c10a8ec7a4d654c8b332007688e3 tree1/b/file_b1.txt
     * 4212c6a5cb4c0562bd872844d8415d31 tree1/f/file_f.txt
     * 2e16e3cd9eb722587cc54a359dfacb8a tree1/c/file_c1.txt
     * 15ccac37073d521c1945eae79a00781b tree1/a/file_a2.txt
     * 1a374a46776039e0abc2199e54145c36 tree1/a/file_a1.txt
     * 459ce31f6f4c7b0340a1b70693240621 tree1/a/file_a5.txt
     * 8285ccd933c08d95d0ac718b4cd9091f tree1/a/file_a4_1.txt
     * 84d575ac39fecd85920945e67faee53b tree2/d_dir/file_d1d.txt
     * 12343a5fcec4cbd4400cb6ad4b632e79 tree2/d_dir/file_d2d.txt
     * be701c4b62bf36cae8f6919f400377d7 tree2/e/file_e2.txt
     * 88ce097fd926d442f5c004da42defc58 tree2/b_dir/file_b2d.txt
     * 5c286bd756dec2babb0c33a16a346635 tree2/b_dir/file_b1d.txt
     * d47e535b32ac49e17cd1fe8de2669598 tree2/e_dir2/file_e1d.txt
     * 1d26753ad87127981110f7176646de37 tree2/e_dir2/file_e2d.txt
     * a6492bee1efcfad1b52397603b033d34 tree2/d/file_d1.txt
     * f8d98bbe7cb166e362cd01e82cc0dcec tree2/f_dir/file_f1d.txt
     * e3bae493cee9c0ba62d1d32bbeca9642 tree2/f_dir/file_f2d.txt
     * 86c0c10a8ec7a4d654c8b332007688e3 tree2/b/file_b1.txt
     * 21bdd4e7a3da5269a15612ebba077089 tree2/f/file_f.txt
     * b84a67299f7d5bc780a6e9f208c0d827 tree2/a/file_a3.txt
     * 8285ccd933c08d95d0ac718b4cd9091f tree2/a/file_a4_2.txt
     * 1a374a46776039e0abc2199e54145c36 tree2/a/file_a1.txt
     * 90ac6efa8e57f3898d86c260e47a219d tree2/a/file_a5.txt
     */

    // identical file in both trees
    FILE_TR1_A1(
            "tree1/a/file_a1.txt",
            "content of the file file_a1.txt",
            "1a374a46776039e0abc2199e54145c36"),
    FILE_TR2_A1(
            "tree2/a/file_a1.txt",
            "content of the file file_a1.txt",
            "1a374a46776039e0abc2199e54145c36"),
    // file existing only in tree 1
    FILE_TR1_A2(
            "tree1/a/file_a2.txt",
            "content of the file file_a2.txt",
            "15ccac37073d521c1945eae79a00781b"),
    // file existing only in tree 2
    FILE_TR2_A3(
            "tree2/a/file_a3.txt",
            "content of the file file_a3.txt",
            "b84a67299f7d5bc780a6e9f208c0d827"),
    // file existing in both trees with different paths but same content
    FILE_TR1_A4(
            "tree1/a/file_a4_1.txt",
            "content of the file file_a4.txt",
            "8285ccd933c08d95d0ac718b4cd9091f"),
    FILE_TR2_A4(
            "tree2/a/file_a4_2.txt",
            "content of the file file_a4.txt",
            "8285ccd933c08d95d0ac718b4cd9091f"),
    // file existing in both trees with different content but same path
    FILE_TR1_A5(
            "tree1/a/file_a5.txt",
            "content of the file file_a5_1.txt",
            "459ce31f6f4c7b0340a1b70693240621"),
    FILE_TR2_A5(
            "tree2/a/file_a5.txt",
            "content of the file file_a5_2.txt",
            "90ac6efa8e57f3898d86c260e47a219d"),

    // -----------------------

    // identical file in both trees
    FILE_TR1_B1(
            "tree1/b/file_b1.txt",
            "content of the file file_b1.txt",
            "86c0c10a8ec7a4d654c8b332007688e3"),
    FILE_TR2_B1(
            "tree2/b/file_b1.txt",
            "content of the file file_b1.txt",
            "86c0c10a8ec7a4d654c8b332007688e3"),
    // identical subtree in both trees
    FILE_TR1_B1_D(
            "tree1/b_dir/file_b1d.txt",
            "content of the file file_b1d.txt",
            "5c286bd756dec2babb0c33a16a346635"),
    FILE_TR2_B1_D(
            "tree2/b_dir/file_b1d.txt",
            "content of the file file_b1d.txt",
            "5c286bd756dec2babb0c33a16a346635"),
    FILE_TR1_B2_D(
            "tree1/b_dir/file_b2d.txt",
            "content of the file file_b2d.txt",
            "88ce097fd926d442f5c004da42defc58"),
    FILE_TR2_B2_D(
            "tree2/b_dir/file_b2d.txt",
            "content of the file file_b2d.txt",
            "88ce097fd926d442f5c004da42defc58"),

    // file existing only in tree 1
    FILE_TR1_C(
            "tree1/c/file_c1.txt",
            "content of the file file_c1.txt",
            "2e16e3cd9eb722587cc54a359dfacb8a"),
    // subtree existing only in tree1
    FILE_TR1_C1_D(
            "tree1/c_dir/file_c1d.txt",
            "content of the file file_c1d.txt",
            "0789b4aa776b949e5c90c69dc8b0eb98"),
    FILE_TR1_C2_D(
            "tree1/c_dir/file_c2d.txt",
            "content of the file file_c2d.txt",
            "0b85fda69267ba2933a468f891e812d5"),

    // file existing only in tree 2
    FILE_TR2_D(
            "tree2/d/file_d1.txt",
            "content of the file file_d1.txt",
            "a6492bee1efcfad1b52397603b033d34"),
    // subtree existing only in tree 2
    FILE_TR1_D1_D(
            "tree2/d_dir/file_d1d.txt",
            "content of the file file_d1d.txt",
            "84d575ac39fecd85920945e67faee53b"),
    FILE_TR1_D2_D(
            "tree2/d_dir/file_d2d.txt",
            "content of the file file_d2d.txt",
            "12343a5fcec4cbd4400cb6ad4b632e79"),

    // file existing in both trees with different paths but same content
    FILE_TR1_E(
            "tree1/e/file_e1.txt",
            "content of the file file_e.txt",
            "be701c4b62bf36cae8f6919f400377d7"),
    FILE_TR2_E(
            "tree2/e/file_e2.txt",
            "content of the file file_e.txt",
            "be701c4b62bf36cae8f6919f400377d7"),
    // subtree existing in both trees with different paths but same content
    FILE_TR1_E1_D(
            "tree1/e_dir1/file_e1d.txt",
            "content of the file file_e1d.txt",
            "d47e535b32ac49e17cd1fe8de2669598"),
    FILE_TR1_E2_D(
            "tree1/e_dir1/file_e2d.txt",
            "content of the file file_e2d.txt",
            "1d26753ad87127981110f7176646de37"),
    FILE_TR2_E1_D(
            "tree2/e_dir2/file_e1d.txt",
            "content of the file file_e1d.txt",
            "d47e535b32ac49e17cd1fe8de2669598"),
    FILE_TR2_E2_D(
            "tree2/e_dir2/file_e2d.txt",
            "content of the file file_e2d.txt",
            "1d26753ad87127981110f7176646de37"),

    // file existing in both trees with different content but same path
    FILE_TR1_F(
            "tree1/f/file_f.txt",
            "content of the file file_f1.txt",
            "4212c6a5cb4c0562bd872844d8415d31"),
    FILE_TR2_F(
            "tree2/f/file_f.txt",
            "content of the file file_f2.txt",
            "21bdd4e7a3da5269a15612ebba077089"),
    // subtree existing in both trees with different content but same path
    FILE_TR1_F1_D(
            "tree1/f_dir/file_f1d.txt",
            "content of the file file_e1d_1.txt",
            "6628110ad88b31b4a7d56e0918f48822"),
    FILE_TR1_F2_D(
            "tree1/f_dir/file_f2d.txt",
            "content of the file file_e2d_1.txt",
            "8f650af5567b0347ef13e97387f0b3f9"),
    FILE_TR2_F1_D(
            "tree2/f_dir/file_f1d.txt",
            "content of the file file_e1d_2.txt",
            "f8d98bbe7cb166e362cd01e82cc0dcec"),
    FILE_TR2_F2_D(
            "tree2/f_dir/file_f2d.txt",
            "content of the file file_e2d_2.txt",
            "e3bae493cee9c0ba62d1d32bbeca9642");

    String path, content, hash;

    FileTestData(String path, String content, String hash) {
        this.path = path;
        this.content = content;
        this.hash = hash;
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

    public String getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }

    public String getHash() {
        return hash;
    }

}