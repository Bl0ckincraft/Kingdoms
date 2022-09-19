package fr.blockincraft.kingdoms.data;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * For performances and time this can't hold all materials
 */
public class ConstructionsData {
    public static final Map<Long, Map<Material, Long>> constructionsMaterials = new HashMap<>();
}
