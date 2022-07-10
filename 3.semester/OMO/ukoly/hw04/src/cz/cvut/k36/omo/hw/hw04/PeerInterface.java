package cz.cvut.k36.omo.hw.hw04;

/**
 * Instance implementujici PeerInterface reprezentuje lokalni uzel nebo uzel
 * pripojeny pres sit.
 */
public interface PeerInterface {
    /**
     * Sdeli vzdalenemu uzlu, ze uzel sender prave obdrzel blok s indexem
     * blockIndex.
     */
    public void have(PeerInterface sender, int blockIndex);

    /**
     * Pozada vzdaleny uzel o blok s indexem blockIndex.
     */
    public void request(PeerInterface sender, int blockIndex);

    /**
     * Zasle vzdalenemu uzlu blok s indexem blockIndex.
     */
    public void piece(PeerInterface sender, int blockIndex, byte[] data);
}
