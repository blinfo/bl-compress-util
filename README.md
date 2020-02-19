# bl-compress-util
Utility Project for compressed files.

### Decompressor
Extracts content of a compressed input stream to a Map<String, byte[]> where the keys are the entry names and the values the content of each entry.

### Compressor
Takes a Map<String, byte[]> and creates a compressed archive file where the keys are used as entry names and the values as entry content.

### Suffix
An enum of file-types handled.

