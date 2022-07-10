package main.roomElements;

public final class Window {
    private boolean isOpen;

    public Window() {
        this.isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
