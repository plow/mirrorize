
mirrorize
==============

keep things in sync the safe way

**NOTE**: This project is in pre-alpha stage. All code is experimental and not recommended to adopt on significant data.

*mirrorize* is a Java tool that helps keeping two file system trees in sync. These trees could be, e.g., copies of very important data which was backed up twice for redundancy reasons. It's assumed that both copies has been changed independently. *mirrorize* does not synchronize anything, it access the file system in a read-only fashion. 

The main prospects are:
- Identifying differences of any kind between the directories to be compared (file/folder exists in one tree only, corresponding file is named differently in both trees, corresponding file/folder has different names in both trees).
- Revealing redundancies, i.e., identifying files/folders in one tree that also exist in the other tree but at a different location.
- Verification that that files and directory names are compliant with potentially defined naming conventions (to enforce consistent directory structures)