package de.daver.unigate.statue;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

import java.util.function.Consumer;

public record Statue(ArmorStand stand) {

    public Statue(ArmorStand stand) {
        this.stand = stand;
        stand.setGravity(false);
        stand.setArms(true);
        stand.setInvulnerable(true);
    }

    public void move(double x, double y, double z) {
        stand.teleport(stand.getLocation().add(x, y, z));
    }

    public void moveHead(int x, int y, int z) {
        addEulerAngle(stand.getHeadPose(), x, y, z, stand::setHeadPose);
    }

    public void moveBody(int x, int y, int z)  {
        addEulerAngle(stand.getBodyPose(), x, y, z, stand::setBodyPose);
    }

    public void moveLeftArm(int x, int y, int z) {
        addEulerAngle(stand.getLeftArmPose(), x, y, z, stand::setLeftArmPose);
    }

    public void moveRightArm(int x, int y, int z) {
        addEulerAngle(stand.getRightArmPose(), x, y, z, stand::setRightArmPose);
    }

    public void moveLeftLeg(int x, int y, int z) {
        addEulerAngle(stand.getLeftLegPose(), x, y, z, stand::setLeftLegPose);
    }

    public void moveRightLeg(int x, int y, int z) {
        addEulerAngle(stand.getRightLegPose(), x, y, z, stand::setRightLegPose);
    }

    public boolean toggleGravity() {
        if(stand.hasGravity()) stand.setGravity(false);
        else stand.setGravity(true);
        return stand.hasGravity();
    }

    public boolean toggleGlowing() {
        if(stand.isGlowing()) stand.setGlowing(false);
        else stand.setGlowing(true);
        return stand.isGlowing();

    }

    public boolean toggleSmall() {
        if(stand.isSmall()) stand.setSmall(false);
        else stand.setSmall(true);
        return stand.isSmall();
    }

    public boolean toggleVisible() {
        if(stand.isVisible()) stand.setVisible(false);
        else stand.setVisible(true);
        return stand.isVisible();
    }

    public boolean toggleArms() {
        if(stand.hasArms()) stand.setArms(false);
        else stand.setArms(true);
        return stand.hasArms();
    }

    public boolean toggleBasePlate() {
        if(stand.hasBasePlate()) stand.setBasePlate(false);
        else stand.setBasePlate(true);
        return stand.hasBasePlate();
    }

    public void setCustomName(Component customName) {
        stand.customName(customName);
    }

    public void delete() {
        stand.remove();
    }

    private void addEulerAngle(EulerAngle angle, int x, int y, int z, Consumer<EulerAngle> setter) {
        var newAngle = angle.add(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z));
        setter.accept(newAngle);
    }

    public Statue copy(Location location) {
        var newStand = location.getWorld().spawn(location, ArmorStand.class);
        var newStatue = new Statue(newStand);
        this.copyAttributes(newStatue);
        return newStatue;
    }

    public void copyAttributes(Statue other) {
        var target = other.stand;

        target.setGravity(stand.hasGravity());
        target.setArms(stand.hasArms());
        target.setBasePlate(stand.hasBasePlate());
        target.setSmall(stand.isSmall());
        target.setVisible(stand.isVisible());
        target.setGlowing(stand.isGlowing());
        target.setCustomName(stand.getCustomName());
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

    public String displayName() {
        var name = stand.customName();
        if(name == null) return "";
        return MiniMessage.miniMessage().serialize(name);
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
