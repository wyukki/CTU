package cz.cvut.k36.omo.hw.hw04;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Instance tridy Peer reprezentuje lokalni uzel.
 */
public class Peer implements PeerInterface {
    /**
     * Fronta nezpracovanych zprav.
     */
    final Queue<Message> messageQueue = new LinkedBlockingQueue<>();
    /**
     * Mapovani z uzlu na bitovou mapu urcujici ktere bloky ma dany uzel k
     * dispozici.
     */
    final Map<PeerInterface, boolean[]> peers2BlocksMap;
    /**
     * Celkovy pocet bloku ve stahovanem souboru.
     */
    final int totalBlocksCount;
    /**
     * Pole se stazenymi bloky.
     */
    final byte[][] data;
    /**
     * Pocet stazenych bloku.
     */
    int downloadedBlocksCount = 0;

    public Peer(Map<PeerInterface, boolean[]> peers2BlocksMap, int totalBlocksCount) {
        this.peers2BlocksMap = peers2BlocksMap;
        this.totalBlocksCount = totalBlocksCount;
        data = new byte[totalBlocksCount][];
    }

    /**
     * Prijme zpravu "have" a prida ji do fronty zprav.
     */
    public void have(PeerInterface sender, int blockIndex) {
        messageQueue.add(new HaveMessage(blockIndex, sender));
    }

    /**
     * Prijme zpravu "request" a prida ji do fronty zprav.
     */
    public void request(PeerInterface sender, int blockIndex) {
        messageQueue.add(new RequestMessage(blockIndex, sender));
    }

    /**
     * Prijme zpravu "piece" a prida ji do fronty zprav.
     */
    public void piece(PeerInterface sender, int blockIndex, byte[] data) {
        messageQueue.add(new PieceMessage(blockIndex, data, sender));
    }

    /**
     * Vyzvedne nejstarsi zpravu z fronty zprav a zpracuje ji pomoci zadaneho
     * navstevnika. Pokud ve fronte zadna zprava neni, zasle sam sobe a zpracuje
     * zpravu "idle". Vrati true v pripade, ze tento uzel stahnul vsechny bloky,
     * false jinak.
     */
    public boolean processMessage(MessageVisitor visitor) {
        return (messageQueue.isEmpty() ? new IdleMessage(this) : messageQueue.poll()).accept(visitor);
    }
}
