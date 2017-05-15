Demolisher [![Build Status](https://travis-ci.org/MasoodFallahpoor/Demolisher.svg?branch=master)](https://travis-ci.org/MasoodFallahpoor/Demolisher)
=========
Demolisher is a tiny command-line Java program that receives a directory and a list of file names and recursively deletes files with given file names from given directory and all its subdirectories.

## Usage

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
java -jar demolisher.jar FULL_PATH_OF_project_DIRECTORY a.txt b.txt
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

## Notes

- The general form of running Demolisher is as follows:
```
java -jar demolisher.jar [OPTION]... DIRECTORY FILE-NAME...
```
- You need JDK/JRE 8 to run Demolisher.
- The path to *java* command should be in your PATH environment variable or you should provide the full path of java command in order to run Demolisher.
- If the path of directory or file names contain space then they should be surrounded with double quotes like: 
```
java -jar demolisher.jar "C:\Program Files\project" "file 1.txt" "file 2.txt"
```
- File names could be entered with or without extension. When file name has extension then only files with that exact name and extension are deleted. When no extension is entered then all files with specified name are deleted regardless of their extension.
- One can use *find* command in GNU/Linux shell to implement the functionality of Demolisher. For example consider the following invocation of Demolisher:
```
java -jar demolisher.jar /home/foo/dummyDir/ a.txt b.txt
```
The equivalent functionality is achieved with find command as follows:
```
find /home/foo/dummyDir -type f \( -name "a.txt" -o -name "b.txt" \)
```

## License

Copyright 2017 Masood Fallahpoor.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.