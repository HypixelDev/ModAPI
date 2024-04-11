package net.hypixel.modapi.serializer;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import io.netty.util.CharsetUtil;

import java.util.UUID;

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

    public void writeVarInt(int i) {
        while ((i & -128) != 0) {
            this.buf.writeByte(i & 127 | 128);
            i >>>= 7;
        }

        this.buf.writeByte(i);
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

}
