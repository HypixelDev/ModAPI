package net.hypixel.modapi.error;

public class UnknownErrorReason implements ErrorReason {
    private final int id;

    UnknownErrorReason(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UnknownErrorReason{" +
                "id=" + id +
                '}';
    }
}
