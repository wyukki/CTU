package cz.cvut.fel.pjv;

public class BruteForceAttacker extends Thief {

    private char[] guess;
    private char[] charList;
    private int charListLength;

    @Override
    public void breakPassword(int sizeOfPassword) {
        charList = getCharacters();
        charListLength = charList.length;
        if (charListLength == 0 || sizeOfPassword == 0) {
            tryOpen("".toCharArray());
            return;
        }
        guess = new char[sizeOfPassword];
        for (int i = 0; i < sizeOfPassword && !isOpened(); ++i) {
            for (int j = 0; j < charListLength && !isOpened(); ++j) {
                guess[i] = charList[j]; // set every guess's char to first char in charList
                tryToBreak(i + 1);
            }
        }
    }

    private void tryToBreak(int index) {
        for (int i = 0; i < charListLength && !isOpened(); ++i) {
            guess[index % guess.length] = charList[(i + index) % charListLength]; // set to guess[index] next char from charList
            if (!isOpened() && index < guess.length - 1) {
                tryToBreak(index + 1); // recursive trying guess.length-combinations
            }
            if (isOpened()) return;
            tryOpen(guess);
        }
    }
}
