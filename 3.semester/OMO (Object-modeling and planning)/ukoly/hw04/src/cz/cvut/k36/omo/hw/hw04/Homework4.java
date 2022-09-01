package cz.cvut.k36.omo.hw.hw04;

public class Homework4 extends MessageVisitor {
    public Homework4(Peer peer) {
        super(peer);
    }

    @Override
    public boolean visitHaveMessage(HaveMessage message) {
        this.peer.peers2BlocksMap.get(message.getSender())[message.getBlockIndex()] = true;
        return false;
    }

    @Override
    public boolean visitRequestMessage(RequestMessage message) {
        if (this.peer.data[message.getBlockIndex()] != null) {
            PeerInterface sender = message.getSender();
            sender.piece(this.peer, message.getBlockIndex(),
                    this.peer.data[message.getBlockIndex()]);
        }
        return false;
    }

    @Override
    public boolean visitPieceMessage(PieceMessage message) {
        this.peer.data[message.getBlockIndex()] = message.getData();
        this.peer.downloadedBlocksCount++;
        for (PeerInterface p : this.peer.peers2BlocksMap.keySet()) {
            p.have(this.peer, message.getBlockIndex());
        }
        return this.peer.downloadedBlocksCount == this.peer.totalBlocksCount;
    }

    @Override
    public boolean visitIdleMessage(IdleMessage message) {
        int rarestPackage = Integer.MAX_VALUE;
        boolean[] absentPackages = new boolean[peer.totalBlocksCount];
        for (int i = 0; i < this.peer.totalBlocksCount; ++i) {
            absentPackages[i] = this.peer.data[i] == null;
        }
        PeerInterface hasData = null;
        int indexOfRarestPackage = 0;
        for (int i = 0; i < this.peer.totalBlocksCount; ++i) {
            int counter = 0;
            PeerInterface tmp = null;
            for (PeerInterface p : this.peer.peers2BlocksMap.keySet()) {
                if (this.peer.peers2BlocksMap.get(p)[i] && absentPackages[i]) {
                    counter++;
                    tmp = p;
                }
            }
            if (rarestPackage > counter) {
                rarestPackage = counter;
                counter = 0;
                indexOfRarestPackage = i;
                hasData = tmp;
            }
        }
        if (hasData != null) {
            hasData.request(this.peer, indexOfRarestPackage);
        }
        return false;
    }
}
