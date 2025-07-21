package me.braydon.sync.common;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.braydon.sync.server.MinecraftServer;

/**
 * @author Braydon
 * @see <a href="https://minecraft.fandom.com/wiki/Protocol_version">Protocol Version Numbers</a>
 * @see <a href="https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-1-21">Spigot NMS (1.21+)</a>
 * @see <a href="https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-1-16">Spigot NMS (1.16 - 1.20)</a>
 * @see <a href="https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-1-10-1-15">Spigot NMS (1.10 - 1.15)</a>
 * @see <a href="https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-legacy">Spigot NMS (1.8 - 1.9)</a>
 */
@RequiredArgsConstructor @Getter @ToString
public enum MinecraftVersion {
    V1_21_7(772, "1_21_R3"), // 1.21.7
    V1_21_5(770, "1_21_R3"), // 1.21.5
    V1_21_4(769, "1_21_R3"), // 1.21.4
    V1_21_2(768, "1_21_R2"), // 1.21.2 & 1.21.3
    V1_21(767, "1_21_R1"), // 1.21 & 1.21.1

    V1_20_5(766, "v1_20_R2"), // 1.20.5 & 1.20.6
    V1_20_3(765, "v1_20_R2"), // 1.20.3 & 1.20.4
    V1_20_2(764, "v1_20_R2"), // 1.20.2
    V1_20(763, "v1_20_R1"), // 1.20 & 1.20.1

    V1_19_4(762, "v1_19_R3"), // 1.19.4
    V1_19_3(761, "v1_19_R2"), // 1.19.3
    V1_19_1(760, "v1_19_R1"), // 1.19.1 & 1.19.2
    V1_19(759, "v1_19_R1"), // 1.19

    V1_18_2(758, "v1_18_R2"), // 1.18.2
    V1_18(757, "v1_18_R1"), // 1.18 & 1.18.1

    V1_17_1(756, "v1_17_R1"), // 1.17.1
    V1_17(755, "v1_17_R1"), // 1.17

    V1_16_4(754, "v1_16_R3"), // 1.16.4 & 1.16.5
    V1_16_3(753, "v1_16_R2"), // 1.16.3
    V1_16_2(751, "v1_16_R2"), // 1.16.2
    V1_16_1(736, "v1_16_R1"), // 1.16.1
    V1_16(735, "v1_16_R1"), // 1.16

    V1_15_2(578, "v1_15_R1"), // 1.15.2
    V1_15_1(575, "v1_15_R1"), // 1.15.1
    V1_15(573, "v1_15_R1"), // 1.15

    V1_14_4(498, "v1_14_R1"), // 1.14.4
    V1_14_3(490, "v1_14_R1"), // 1.14.3
    V1_14_2(485, "v1_14_R1"), // 1.14.2
    V1_14_1(480, "v1_14_R1"), // 1.14.1
    V1_14(477, "v1_14_R1"), // 1.14

    V1_13_2(404, "v1_13_R2"), // 1.13.2
    V1_13_1(401, "v1_13_R2"), // 1.13.1
    V1_13(393, "v1_13_R1"), // 1.13

    V1_12_2(340, "v1_12_R1"), // 1.12.2
    V1_12_1(338, "v1_12_R1"), // 1.12.1
    V1_12(335, "v1_12_R1"), // 1.12

    V1_11_1(316, "v1_11_R1"), // 1.11.1 & 1.11.2
    V1_11(315, "v1_11_R1"), // 1.11

    V1_10(210, "v1_10_R1"), // 1.10.x

    V1_9_3(110, "v1_9_R2"), // 1.9.3 & 1.9.4
    V1_9_2(109, "v1_9_R1"), // 1.9.2
    V1_9_1(108, "v1_9_R1"), // 1.9.1
    V1_9(107, "v1_9_R1"), // 1.9

    V1_8(47, "v1_8_R3"), // 1.8.x

    V1_7_6(5, "v1_7_R4"), // 1.7.6 - 1.7.10

    UNKNOWN(-1, "Unknown");

    public static final MinecraftVersion[] VERSIONS = values();

    /**
     * The protocol number of this version.
     */
    private final int protocol;

    /**
     * The server version for this version.
     */
    @NonNull private final String nmsVersion;

    /**
     * The cached name of this version.
     */
    private String name;

    /**
     * Get the name of this protocol version.
     *
     * @return the name
     */
    @NonNull
    public String getName() {
        // We have a name
        if (name != null) {
            return name;
        }
        if (this == UNKNOWN) {
            name = nmsVersion;
        } else { // Parse the name
            name = name().substring(1);
            name = name.replace("_", ".");
        }
        return name;
    }

    /**
     * Check if this version is
     * above the one given.
     *
     * @param other the other version
     * @return true if above, otherwise false
     */
    public boolean isAbove(@NonNull MinecraftVersion other) {
        return protocol > other.getProtocol();
    }

    /**
     * Check if this version is
     * or above the one given.
     *
     * @param other the other version
     * @return true if is or above, otherwise false
     */
    public boolean isOrAbove(@NonNull MinecraftVersion other) {
        return protocol >= other.getProtocol();
    }

    /**
     * Check if this version is
     * below the one given.
     *
     * @param other the other version
     * @return true if below, otherwise false
     */
    public boolean isBelow(@NonNull MinecraftVersion other) {
        return protocol < other.getProtocol();
    }

    /**
     * Check if this version is
     * or below the one given.
     *
     * @param other the other version
     * @return true if is or below, otherwise false
     */
    public boolean isOrBelow(@NonNull MinecraftVersion other) {
        return protocol <= other.getProtocol();
    }

    /**
     * Get the version from the given decimal version.
     * Example: 1.21.7 -> V1_21_7
     *
     * @param version the version to get the version for
     * @return the version
     */
    public static MinecraftVersion byDecimalVersion(@NonNull String version) {
        version = "V" + version.replace(".", "_");
        for (MinecraftVersion versionEnum : VERSIONS) {
            if (versionEnum.name().equals(version)) {
                return versionEnum;
            }
        }
        return UNKNOWN;
    }

    /**
     * Get the version from the given protocol.
     *
     * @param protocol the protocol to get the version for
     * @return the version
     */
    @NonNull
    public static MinecraftVersion byProtocol(int protocol) {
        for (MinecraftVersion version : VERSIONS) {
            if (version.getProtocol() == protocol) {
                return version;
            }
        }
        return UNKNOWN;
    }
}