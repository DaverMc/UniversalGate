package de.daver.unigate.statue;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.EulerAngle;

import java.util.function.Consumer;

public class Statue {

    private final ArmorStand stand;
    private final StatuePose headPose;
    private final StatuePose bodyPose;
    private final StatuePose leftArmPose;
    private final StatuePose rightArmPose;
    private final StatuePose leftLegPose;
    private final StatuePose rightLegPose;
    private final StatuePosition position;
    private final StatueEquipment equipment;
    private final StatueAttributes attributes;

    public Statue(ArmorStand stand) {
        this.stand = stand;
        stand.setGravity(false);
        stand.setInvulnerable(true);
        stand.setDisabledSlots(EquipmentSlot.values());

        this.headPose = new StatuePose(this, new PoseBridges.Head());
        this.bodyPose = new StatuePose(this, new PoseBridges.Body());
        this.leftArmPose = new StatuePose(this, new PoseBridges.LeftArm());
        this.rightArmPose = new StatuePose(this, new PoseBridges.RightArm());
        this.leftLegPose = new StatuePose(this, new PoseBridges.LeftLeg());
        this.rightLegPose = new StatuePose(this, new PoseBridges.RightLeg());
        this.position = new StatuePosition(stand);
        this.equipment = new StatueEquipment(this);
        this.attributes = new StatueAttributes(this);


    }

    public StatuePose head() {
        return this.headPose;
    }

    public StatuePose body() {
        return this.bodyPose;
    }

    public StatuePose leftArm() {
        return this.leftArmPose;
    }

    public StatuePose rightArm() {
        return this.rightArmPose;
    }

    public StatuePose leftLeg() {
        return this.leftLegPose;
    }

    public StatuePose rightLeg() {
        return this.rightLegPose;
    }

    public StatuePosition position() {
        return this.position;
    }

    public StatueEquipment equipment() {
        return this.equipment;
    }

    public StatueAttributes attributes() {
        return this.attributes;
    }

    public ArmorStand getEntity() {
        var stand = this.stand;
        if(!stand.isValid()) return null;
        return this.stand;
    }

    public void delete() {
        stand.remove();
    }

    public void copyAttributes(Statue other) {
        var target = other.stand;

        target.setGravity(stand.hasGravity());
        target.setArms(stand.hasArms());
        target.setBasePlate(stand.hasBasePlate());
        target.setSmall(stand.isSmall());
        target.setVisible(stand.isVisible());
        target.setGlowing(stand.isGlowing());
        target.customName(stand.customName());
        target.setCustomNameVisible(stand.isCustomNameVisible());

        target.setHeadPose(stand.getHeadPose());
        target.setBodyPose(stand.getBodyPose());
        target.setLeftArmPose(stand.getLeftArmPose());
        target.setRightArmPose(stand.getRightArmPose());
        target.setLeftLegPose(stand.getLeftLegPose());
        target.setRightLegPose(stand.getRightLegPose());

        target.getEquipment().setHelmet(stand.getEquipment().getHelmet());
        target.getEquipment().setChestplate(stand.getEquipment().getChestplate());
        target.getEquipment().setLeggings(stand.getEquipment().getLeggings());
        target.getEquipment().setBoots(stand.getEquipment().getBoots());
        target.getEquipment().setItemInMainHand(stand.getEquipment().getItemInMainHand());
        target.getEquipment().setItemInOffHand(stand.getEquipment().getItemInOffHand());
    }

    public boolean small() {
        return stand.isSmall();
    }

    public boolean visible() {
        return stand.isVisible();
    }

    public boolean glowing() {
        return stand.isGlowing();
    }

    public boolean arms() {
        return stand.hasArms();
    }

    public boolean basePlate() {
        return stand.hasBasePlate();
    }

    public boolean gravity() {
        return stand.hasGravity();
    }

    public boolean nameVisible() {
        return stand.isCustomNameVisible();
    }

    public void small(boolean small) {
        stand.setSmall(small);
    }

    public void visible(boolean visible) {
        stand.setVisible(visible);
    }

    public void glowing(boolean glowing) {
        stand.setGlowing(glowing);
    }

    public void arms(boolean arms) {
        stand.setArms(arms);
    }

    public void basePlate(boolean basePlate) {
        stand.setBasePlate(basePlate);
    }

    public void gravity(boolean gravity) {
        stand.setGravity(gravity);
    }

    public void nameVisible(boolean visible) {
        stand.setCustomNameVisible(visible);
    }
}
