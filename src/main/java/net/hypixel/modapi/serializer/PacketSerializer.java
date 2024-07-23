package net.hypixel.modapi.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import io.netty.util.CharsetUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

@ApiStatus.Internal
public class PacketSerializer {
    private static final int MAX_BYTES_PER_CHAR_UTF8 = (int) CharsetUtil.getEncoder(CharsetUtil.UTF_8).maxBytesPerChar();
    private static final int MAX_STRING_LENGTH = 32767;

    private final ByteBuf buf;

    public PacketSerializer(ByteBuf buf) {
        this.buf = buf;
    }

    public boolean readBoolean() {
        return this.buf.readBoolean();
    }

    public PacketSerializer writeBoolean(boolean value) {
        this.buf.writeBoolean(value);
        return this;
    }

    public int readVarInt() {
        int i = 0;
        int j = 0;

        byte b;
        do {
            b = this.buf.readByte();
            i |= (b & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b & 128) == 128);

        return i;
    }

    public PacketSerializer writeVarInt(int value) {
        if ((value & 0xFFFFFF80) == 0) {
            buf.writeByte(value);
        } else if ((value & 0xFFFFC000) == 0) {
            buf.writeShort((value & 0x7F | 0x80) << 8 | (value >>> 7 & 0x7F));
        } else if ((value & 0xFFE00000) == 0) {
            buf.writeMedium((value & 0x7F | 0x80) << 16 | (value >>> 7 & 0x7F | 0x80) << 8 | (value >>> 14 & 0x7F));
        } else if ((value & 0xF0000000) == 0) {
            buf.writeInt((value & 0x7F | 0x80) << 24 | (value >>> 7 & 0x7F | 0x80) << 16 | (value >>> 14 & 0x7F | 0x80) << 8 | (value >>> 21 & 0x7F));
        } else {
            buf.writeInt((value & 0x7F | 0x80) << 24 | (value >>> 7 & 0x7F | 0x80) << 16 | (value >>> 14 & 0x7F | 0x80) << 8 | (value >>> 21 & 0x7F | 0x80));
            buf.writeByte(value >>> 28);
        }
        return this;
    }

    public long readLong() {
        return this.buf.readLong();
    }

    public PacketSerializer writeLong(long value) {
        this.buf.writeLong(value);
        return this;
    }

    public double readDouble() {
        return this.buf.readDouble();
    }

    public PacketSerializer writeDouble(double value) {
        this.buf.writeDouble(value);
        return this;
    }

    public String readString() {
        return this.readString(MAX_STRING_LENGTH);
    }

    public String readString(int maxLength) {
        int maxBytes = maxLength * MAX_BYTES_PER_CHAR_UTF8;
        int length = this.readVarInt();
        if (length > maxBytes) {
            throw new DecoderException("String too long (length " + length + ", max " + maxBytes + ")");
        }

        if (length < 0) {
            throw new DecoderException("String length is less than zero!");
        }

        int readableBytes = this.buf.readableBytes();
        if (length > readableBytes) {
            throw new DecoderException("Not enough readable bytes - bytes: " + readableBytes + " length: " + length);
        }

        String string = this.buf.toString(this.buf.readerIndex(), length, CharsetUtil.UTF_8);
        this.buf.readerIndex(this.buf.readerIndex() + length);
        if (string.length() > maxLength) {
            throw new DecoderException("String length " + string.length() + " is longer than maximum allowed " + maxLength);
        }

        return string;
    }

    public PacketSerializer writeString(String string) {
        return this.writeString(string, MAX_STRING_LENGTH);
    }

    public PacketSerializer writeString(String string, int maxLength) {
        if (string.length() > maxLength) {
            throw new EncoderException("String too long (length " + string.length() + ", max " + maxLength + ")");
        }

        int maxBytes = maxLength * MAX_BYTES_PER_CHAR_UTF8;
        byte[] bytes = string.getBytes(CharsetUtil.UTF_8);
        if (bytes.length > maxBytes) {
            throw new EncoderException("String too long (bytes " + bytes.length + ", max " + maxBytes + ")");
        }

        this.writeVarInt(bytes.length);
        this.buf.writeBytes(bytes);
        return this;
    }

    public UUID readUuid() {
        return new UUID(this.buf.readLong(), this.buf.readLong());
    }

    public PacketSerializer writeUuid(UUID uuid) {
        this.buf.writeLong(uuid.getMostSignificantBits());
        this.buf.writeLong(uuid.getLeastSignificantBits());
        return this;
    }

    public <T> Optional<T> readOptional(Function<PacketSerializer, T> function) {
        return this.readBoolean() ? Optional.of(function.apply(this)) : Optional.empty();
    }

    @Nullable
    public <T> T readOptionally(Function<PacketSerializer, T> function) {
        return this.readBoolean() ? function.apply(this) : null;
    }

    public <T> PacketSerializer writeOptional(Optional<T> optional, BiConsumer<PacketSerializer, T> consumer) {
        return writeOptionally(optional.orElse(null), consumer);
    }

    public <T> PacketSerializer writeOptionally(@Nullable T value, BiConsumer<PacketSerializer, T> consumer) {
        this.writeBoolean(value != null);
        if (value != null) {
            consumer.accept(this, value);
        }
        return this;
    }

    public void discardRemaining() {
        this.buf.readerIndex(this.buf.writerIndex());
    }
}
