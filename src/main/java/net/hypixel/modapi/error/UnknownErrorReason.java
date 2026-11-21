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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnknownErrorReason that = (UnknownErrorReason) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "UnknownErrorReason{" +
                "id=" + id +
                '}';
    }
}
