package spiderman;

public class DimensionNode {
    private Dimension data;
    private DimensionNode next;

    // Constructor
    public DimensionNode(Dimension data) {
        this.data = data;
        this.next = null;
    }

    // Getter and Setter methods
    public Dimension getData() {
        return data;
    }

    public void setData(Dimension data) {
        this.data = data;
    }

    public DimensionNode getNext() {
        return next;
    }

    public void setNext(DimensionNode next) {
        this.next = next;
    }
}
