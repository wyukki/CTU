package cz.cvut.k36.omo.hw.hw04;

public class HaveMessage extends Message {
    private final int blockIndex;

    public HaveMessage(int blockIndex, PeerInterface sender) {
        super(sender);
        this.blockIndex = blockIndex;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public boolean accept(MessageVisitor visitor) {
        return visitor.visitHaveMessage(this);
    }
}
