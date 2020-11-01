package utils;

public class SingleCodeRange {
    private String filePath;
    private int startLine;
    private int endLine;

    public SingleCodeRange(String filePath, int startLine, int length) {
        this.filePath = filePath;
        this.startLine = startLine;
        this.endLine = startLine + length;
    }

    public boolean hasOverlap(SingleCodeRange otherSingleRange) {
        return filePath.equals(otherSingleRange.filePath)
                && this.startLine <= otherSingleRange.endLine
                && otherSingleRange.startLine <= this.endLine;
    }
}

