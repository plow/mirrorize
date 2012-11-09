#!/bin/bash

# retrieves MD5 hashes of all files in the specified directory.
# Usage examples:
#    1) md5_hashes_of_dir.sh  /home/user/test_tree/
#    2) ./md5_hashes_of_dir.sh ../resources/test_tree/

for i in `find $1 -type f`; do md5sum "$i"; done
