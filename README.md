# MultiValueDictionary

MultiValueDictionary is a java command line application for creating and storing a multi-level dictionary in memory. It allows you to store multiple values under the same key.

## Installation
Running this program requires Java Runtime Environment, can download for your operating system [here](https://www.java.com/en/download/).
The jar file included in this project was compiled with jdk-1.8, so this should be able to run on any java versions later than 1.8.

### Command Line
1) Download the project
2) Navigate to the `WorkSample/out` directory of the project in your command line.
3) Enter `java -jar WorkSample.jar` in the command line to run the program.

## Usage
You can use the commands listed below to manage your multi-value dictionary.

### ADD
- Ex. `ADD <key> <value>`
- Adds the value under the specified key into the dictionary.

### REMOVE
- Ex. `REMOVE <key> <value>`
- Removes the value under the specified key in the dictionary.
- Removes the key if value is the last member under the key.

### REMOVEALL
- Ex. `REMOVEALL <key>`
- Removes all values under a key and removes the key itself from the dictionary.

### KEYS
- Ex. `KEYS`
- Returns a list of keys stored in the dictionary.

### MEMBERS
- Ex. `MEMBERS`
- Returns a list of values stored in the dictionary.

### CLEAR
- Ex. `CLEAR`
- Clears the entire dictionary, removing all keys and their corresponding values.

### KEYEXISTS
- Ex. `KEYEXISTS <key>`
- Returns true or false for if the key exists in the dictionary.

### MEMBEREXISTS
- Ex. `MEMBEREXISTS <key> <value>`
- Returns true or false for if the value exists under the specified key in the dictionary.

### ALLMEMBERS
- `ALLMEMBERS`
- Returns a list of all of the values stored in the dictionary.

### ITEMS
- `ITEMS`
- Prints a list of all keys and their corresponding values.

### HELP
- `HELP`
- Prints the list of all supported commands of the MultiValueDictionary.

### EXIT
- `EXIT`
 - This will exit the program.