package org.gnk.admin

/**
 * Created by Nico on 22/04/14.
 */
enum right {
    PROFILOPEN(1),
    PROFILMODIFY(2),
    PROFILCLOSE(4),
    USEROPEN(8),
    USERMODIFY(16),
    USERCLOSE(32),
    MINTRIGUEOPEN(64),
    MINTRIGUEMODIFY(128),
    MINTRIGUEDELETE(256),
    INTRIGUEOPEN(512),
    INTRIGUEMODIFY(1024),
    INTRIGUEDELETE(2048),
    MGNOPEN(4096),
    MGNMODIFY(8192),
    MGNDELETE(16384),
    GNOPEN(32768),
    GNMODIFY(65536),
    GNDELETE(131072),
    REFOPEN(262144),
    REFMODIFY(1048576),
    RIGHTSHOW(2097152),
    RIGHTMODIF(4194304),
    RIGHTDELETE(8388608)
    private int value

    public right(int val) {
        this.value = val
    }

    int value() { return value }
}