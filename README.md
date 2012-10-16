
mirrorize
==============

keeping things in sync the safe way
--------------

**NOTE**: This project is in pre-alpha stage. All code is experimental and not recommended to adopt on significant data.

*mirrorize* is a Java tool that helps keeping two file system trees in sync. These trees could be, e.g., copies of important data which was backed up twice for redundancy reasons but have been changed independently from each other. *mirrorize* does not synchronize anything, it access the file system in a read-only fashion. 

The main prospects are:
- Identifying differences of any kind between the directories to be compared (a file/folder exists in one tree only, a corresponding file/folder is named differently in both trees, a file/folder with corresponding names has different contents in both trees).
- Revealing redundancies, i.e., identifying files/folders in one tree that also exist somewhere in the other tree but at a different location.
- Verification that files and directory names are compliant with potentially defined naming conventions (to enforce consistent directory structures throughout the trees)