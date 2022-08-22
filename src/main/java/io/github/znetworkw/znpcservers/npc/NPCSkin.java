package io.github.znetworkw.znpcservers.npc;

import io.github.znetworkw.znpcservers.skin.SkinFetcherBuilder;
import io.github.znetworkw.znpcservers.skin.SkinFetcherResult;
import io.github.znetworkw.znpcservers.utility.Utils;

public class NPCSkin {
    private static final String EMPTY_STRING = "";
    private static final String[] EMPTY_ARRAY = new String[]{"", ""};
    private static final int LAYER_INDEX = NPCSkin.SkinLayerValues.findLayerByVersion();
    private final String texture;
    private final String signature;

    protected NPCSkin(String... values) {
        if (values.length < 1) {
            throw new IllegalArgumentException("Length cannot be zero or negative.");
        } else {
            this.texture = values[0];
            this.signature = values[1];
        }
    }

    public String getTexture() {
        return this.texture;
    }

    public String getSignature() {
        return this.signature;
    }

    public int getLayerIndex() {
        return LAYER_INDEX;
    }

    public static NPCSkin forValues(String... values) {
        return new NPCSkin(values.length > 0 ? values : EMPTY_ARRAY);
    }

    public static void forName(String skin, SkinFetcherResult skinFetcherResult) {
        SkinFetcherBuilder.withName(skin).toSkinFetcher().doReadSkin(skinFetcherResult);
    }

    static enum SkinLayerValues {
        V8(8, 12),
        V9(10, 13),
        V14(14, 15),
        V16(16, 16),
        V17(17, 17),
        V18(18, 17);

        final int minVersion;
        final int layerValue;

        private SkinLayerValues(int minVersion, int layerValue) {
            this.minVersion = minVersion;
            this.layerValue = layerValue;
        }

        static int findLayerByVersion() {
            int value = V8.layerValue;
            NPCSkin.SkinLayerValues[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                NPCSkin.SkinLayerValues skinLayerValue = var1[var3];
                if (Utils.BUKKIT_VERSION >= skinLayerValue.minVersion) {
                    value = skinLayerValue.layerValue;
                }
            }

            return value;
        }
    }
}
