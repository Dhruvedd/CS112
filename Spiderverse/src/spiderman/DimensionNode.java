package spiderman;

public class DimensionNode {
    private int data;
    private DimensionNode next;

    // Constructor
    public DimensionNode(int data) {
        this.data = data;
        this.next = null;
    }

    // Getter and Setter methods
    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public DimensionNode getNext() {
        return next;
    }

    public void setNext(DimensionNode next) {
        this.next = next;
    }
}
