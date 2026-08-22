package de.daver.unigate.dimension.gen;

import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

public interface PaperSavedData {

    int DATA_VERSION = LevelData.NBT_VERSION;

    static CompoundTag metadata(UUID uuid) {
        CompoundTag data = new CompoundTag();
        data.putIntArray("uuid", uuidToIntArray(uuid));
        return envelope(data);
    }

    static CompoundTag envelope(CompoundTag data) {
        CompoundTag root = new CompoundTag();
        root.putInt("DataVersion", DATA_VERSION);
        root.put("data", data);
        return root;
    }

    static int[] uuidToIntArray(UUID uuid) {
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();
        return new int[]{(int) (most >> 32), (int) most, (int) (least >> 32), (int) least};
    }

    static void write(Tag<?> tag, Path file) throws IOException {
        Files.createDirectories(file.getParent());
        try (OutputStream out = Files.newOutputStream(file);
             GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            new NBTSerializer(false).toStream(new NamedTag(null, tag), gzip);
        }
    }
}
