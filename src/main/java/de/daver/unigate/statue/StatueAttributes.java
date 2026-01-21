package de.daver.unigate.statue;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.ArmorStand;

public class StatueAttributes {

    private final Statue statue;

    private boolean small;
    private boolean basePlate;
    private boolean visible;
    private boolean arms;
    private boolean glowing;

    private Component name;

    StatueAttributes(Statue statue) {
        this.statue = statue;
        load(statue.getEntity());
    }

    void load(ArmorStand stand) {
        this.small = stand.isSmall();
        this.basePlate = stand.hasBasePlate();
        this.visible = stand.isVisible();
        this.arms = stand.hasArms();
        this.glowing = stand.isGlowing();
        if(stand.isCustomNameVisible()) this.name = stand.customName();
    }

    void apply(ArmorStand stand) {
        stand.setSmall(this.small);
        stand.setBasePlate(this.basePlate);
        stand.setVisible(this.visible);
        stand.setArms(this.arms);
        stand.setGlowing(this.glowing);
        if(name == null) return;
        stand.setCustomNameVisible(true);
        stand.customName(this.name);
    }

    public boolean isSmall() {
        return this.small;
    }

    public boolean hasBasePlate() {
        return this.basePlate;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public boolean hasArms() {
        return this.arms;
    }

    public boolean isGlowing() {
        return this.glowing;
    }

    public Component name() {
        return this.name;
    }

    public String nameString() {
        if(name == null) return "";
        return MiniMessage.miniMessage().serialize(name);
    }

    public void setSmall(boolean small) {
        this.small = small;
    }

    public void setBasePlate(boolean visible) {
        this.basePlate = visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setArms(boolean visible) {
        this.arms = visible;
    }

    public void setGlowing(boolean glow) {
        this.glowing = glow;
    }

    public void setName(Component component) {
        this.name = component;
    }

    public void update() {
        var stand = this.statue.getEntity();
        if(stand == null) return;
        apply(stand);
    }
}
