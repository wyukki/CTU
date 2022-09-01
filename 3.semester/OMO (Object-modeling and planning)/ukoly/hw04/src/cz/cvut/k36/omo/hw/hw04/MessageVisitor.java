package cz.cvut.k36.omo.hw.hw04;

/**
 * Instance tridy MessageVisitor reprezentuji navstevniky zpracovavajici zpravy.
 */
public abstract class MessageVisitor {
    protected final Peer peer;

    public MessageVisitor(Peer peer) {
        this.peer = peer;
    }

    /*
     * Zpracuje zpravu "have": v lokalnim uzlu vyznaci, ze dany vzdaleny uzel ma
     * k dispozici blok s danym indexem.
     *
     * Vzdy vrati false.
     */
    public abstract boolean visitHaveMessage(HaveMessage message);

    /*
     * Zpracuje zpravu "request": pokud ma lokalni uzel pozadovany blok k
     * dispozici, obratem ho posle vzdalenemu uzlu pomoci zpravy "piece". Pokud
     * ne, pozadavek ignoruje.
     *
     * Vzdy vrati false.
     */
    public abstract boolean visitRequestMessage(RequestMessage message);

    /*
     * Zpracuje zpravu "piece": ulozi obdrzena data do lokalniho uzlu, zvysi
     * pocet stazenych bloku a vsem vzdalenym uzlum (vcetne toho, od ktereho
     * data obdrzel) rozesle zpravu "have".
     *
     * Vrati true, pokud lokalni uzel stahl vsechny bloky, false jinak.
     */
    public abstract boolean visitPieceMessage(PieceMessage message);

    /*
     * Zpracuje zpravu "idle": vybere nejvzacnejsi jeste nestazeny blok a zazada
     * o nej u nektereho z jeho vlastniku. Nejvzacnejsi blok je takovy, ktery
     * vlastni nejmene vzdalenych uzlu. Pokud je nejvzacnejsich bloku nekolik,
     * vybere jeden z nich.
     *
     * Vzdy vrati false.
     */
    public abstract boolean visitIdleMessage(IdleMessage message);
}

