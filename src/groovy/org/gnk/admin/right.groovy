package org.gnk.admin

/**
 * Created by Nico on 22/04/14.
 */
enum right {
    PROFILOPEN(1),
    PROFILMODIFY(64),
    PROFILCLOSE(4096),
    USEROPEN(2),
    USERMODIFY(128),
    USERCLOSE(8192),
    MINTRIGUEOPEN(4),
    MINTRIGUEMODIFY(256),
    MINTRIGUEDELETE(16384),
    INTRIGUEOPEN(8),
    INTRIGUEMODIFY(512),
    INTRIGUEDELETE(32768),
    MGNOPEN(16),
    MGNMODIFY(1024),
    MGNDELETE(65536),
    GNOPEN(32),
    GNMODIFY(2048),
    GNDELETE(131072),
    REFOPEN(262144),
    REFMODIFY(524288),
    REFDELETE(1048576),
    RIGHTSHOW(2097152),
    RIGHTMODIF(4194304),
    RIGHTDELETE(8388608)
    private int value

    public right(int val) {
        this.value = val
    }

    int value() { return value }
}