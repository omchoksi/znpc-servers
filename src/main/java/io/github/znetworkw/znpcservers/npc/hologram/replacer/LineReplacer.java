package io.github.znetworkw.znpcservers.npc.hologram.replacer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utils.Utils;

public interface LineReplacer {
    ImmutableList<LineReplacer> LINE_REPLACERS = ImmutableList.of(new RGBLine());

    String make(String var1);

    boolean isSupported();

    static String makeAll(ZUser user, String string) {
        UnmodifiableIterator var2 = LINE_REPLACERS.iterator();

        while(var2.hasNext()) {
            LineReplacer lineReplacer = (LineReplacer)var2.next();
            if (lineReplacer.isSupported()) {
                string = lineReplacer.make(string);
            }
        }

        return Utils.toColor(Utils.PLACEHOLDER_SUPPORT && user != null ? Utils.getWithPlaceholders(string, user.toPlayer()) : string);
    }
}
