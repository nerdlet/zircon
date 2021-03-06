package org.hexworks.zircon.examples.custom;

import org.hexworks.zircon.api.Blocks;
import org.hexworks.zircon.api.Maybes;
import org.hexworks.zircon.api.Tiles;
import org.hexworks.zircon.api.data.Block;
import org.hexworks.zircon.api.data.impl.Position3D;
import org.hexworks.zircon.api.data.impl.Size3D;
import org.hexworks.zircon.api.game.base.BaseGameArea;
import org.hexworks.zircon.api.util.Maybe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.TreeMap;

public class CustomGameArea extends BaseGameArea {

    private Block defaultBlock = Blocks.newBuilder()
            .addLayer(Tiles.empty())
            .build();
    private int layersPerBlock;
    private TreeMap<Position3D, Block> blocks = new TreeMap<>();

    public CustomGameArea(Size3D visibleSize, Size3D actualSize, int layersPerBlock) {
        super(visibleSize, actualSize);
        this.layersPerBlock = layersPerBlock;
    }

    @NotNull
    @Override
    public Block getDefaultBlock() {
        return defaultBlock;
    }

    @Override
    public int layersPerBlock() {
        return layersPerBlock;
    }

    @Override
    public boolean hasBlockAt(@NotNull Position3D position) {
        return blocks.containsKey(position);
    }

    @NotNull
    @Override
    public Maybe<Block> fetchBlockAt(@NotNull Position3D position) {
        return Maybes.ofNullable(blocks.get(position));
    }

    @NotNull
    @Override
    public Block fetchBlockOrDefault(@NotNull Position3D position) {
        return blocks.getOrDefault(position, defaultBlock);
    }

    @NotNull
    @Override
    public Iterable<Block> fetchBlocks() {
        return new ArrayList<>(blocks.values());
    }

    @Override
    public void setBlockAt(@NotNull Position3D position, @NotNull Block block) {
        if (!actualSize().containsPosition(position)) {
            throw new IllegalArgumentException("The supplied position ($position) is not within the size ($size) of this game area.");
        }
        int layerCount = block.getLayers().size();
        if (layerCount != layersPerBlock()) {
            throw new IllegalArgumentException("The number of layers per block for this game area is ${getLayersPerBlock()}." +
                    " The supplied layers have a size of $layerCount.");
        }
        blocks.put(position, block);
    }
}
