package me.miqhtie.blockswap;

import org.bukkit.Material;

public class PlayerStatus {
    public Material block;
    public boolean collected;

    public PlayerStatus(Material block, boolean collected) {
        this.block = block;
        this.collected = collected;
    }
}
