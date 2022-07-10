package cz.cvut.k36.omo.hw.hw04;

public class PieceMessage extends Message {
    private final int blockIndex;
    private final byte[] data;

    public PieceMessage(int blockIndex, byte[] data, PeerInterface sender) {
        super(sender);
        this.blockIndex = blockIndex;
        this.data = data;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public byte[] getData() {
        return data;
    }

    public boolean accept(MessageVisitor visitor) {
        return visitor.visitPieceMessage(this);
    }
}
