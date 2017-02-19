Demolisher
=========
Demolisher is a tiny command-line Java program that receives a directory and a list of file names and recursively deletes files with given file names from given directory and all its subdirectories.

Usage
--------
Suppose there is a directory named *project* with the following structure:
```
project
|   README.md
|   file001.txt
|   a.txt
|   b.txt
|
+---dir1
|   |   file011.txt
|   |   file012.txt
|   |   b.txt
|   |
|   +---subdir1
|       |   file111.txt
|       |   file112.txt
|       |   a.txt
|       |   b.txt
|   
+---dir2
    |   file021.txt
    |   file022.txt
    |   a.txt
    |   b.txt
```
In order to delete files with name either *a.txt* or *b.txt* one could use Demolisher as follows:
```
java -jar demolisher FULL_PATH_OF_project_DIRECTORY a.txt b.txt
```

After executing the above command the structure of *project* directory will be:
```
project
|   README.md
|   file001.txt
|
+---dir1
|   |   file011.txt
|   |   file012.txt
|   |
|   +---subdir1
|       |   file111.txt
|       |   file112.txt
|   
+---dir2
    |   file021.txt
    |   file022.txt
```

Notes
--------
- The general form of running Demolisher is as follows:
```
java -jar demolisher [OPTION]... DIRECTORY FILE-NAME...
```
- You need JDK/JRE 8 to run Demolisher.
- The path to *java* command should be in your PATH environment variable or you should provide the full path of java command in order to run Demolisher.
- If the path of directory or file names contain space then they should be surrounded with double quotes like: 
```
java -jar demolisher "C:\Program Files\project" "file 1.txt" "file 2.txt"
```
- File names could be entered with or without extension. When file name has extension then only files with that exact name and extension are deleted. When no extension is entered then all files with specified name are deleted regardless of their extension.

License
--------
Copyright (C) 2017 masood@fallahpoor.ir

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program. If not, see http://www.gnu.org/licenses/.