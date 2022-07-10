package main.roomElements;

public final class Blinds {
    private boolean isOpen;

    public Blinds() {
        this.isOpen = true;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
