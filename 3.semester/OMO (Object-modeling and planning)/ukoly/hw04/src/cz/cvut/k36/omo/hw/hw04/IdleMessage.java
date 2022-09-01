package cz.cvut.k36.omo.hw.hw04;

public class IdleMessage extends Message {
    public IdleMessage(PeerInterface sender) {
        super(sender);
    }

    @Override
    public boolean accept(MessageVisitor visitor) {
        return visitor.visitIdleMessage(this);
    }
}
