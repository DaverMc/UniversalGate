package de.daver.unigate.statue;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EquipmentSlot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class Statue {

    private final UUID entityId;
    private final String worldName;

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
        this.entityId = stand.getUniqueId();
        this.worldName = stand.getWorld().getName();
        stand.setGravity(false);
        stand.setInvulnerable(true);
        stand.setDisabledSlots(EquipmentSlot.values());

        this.headPose = new StatuePose(this, new PoseBridges.Head());
        this.bodyPose = new StatuePose(this, new PoseBridges.Body());
        this.leftArmPose = new StatuePose(this, new PoseBridges.LeftArm());
        this.rightArmPose = new StatuePose(this, new PoseBridges.RightArm());
        this.leftLegPose = new StatuePose(this, new PoseBridges.LeftLeg());
        this.rightLegPose = new StatuePose(this, new PoseBridges.RightLeg());
        this.position = new StatuePosition(this);
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
        var world = Bukkit.getWorld(this.worldName);
        if(world == null) return null;
        var entity = world.getEntity(this.entityId);
        if(!(entity instanceof ArmorStand stand)) return null;
        if(!stand.isValid()) return null;
        return stand;
    }

    public void delete() {
        var stand = getEntity();
        if(stand == null) return;
        stand.remove();
    }

    public void copyAttributes(Statue other) {
        var otherAttributes = other.attributes();
        otherAttributes.setArms(attributes.hasArms());
        otherAttributes.setBasePlate(attributes.hasBasePlate());
        otherAttributes.setGlowing(attributes.isGlowing());
        otherAttributes.setSmall(attributes.isSmall());
        otherAttributes.setVisible(attributes.isVisible());
        otherAttributes.setName(attributes.name());

        var otherEquipment = other.equipment();
        otherEquipment.setBoots(equipment.boots());
        otherEquipment.setChestPlate(equipment.chestPlate());
        otherEquipment.setHelmet(equipment.helmet());
        otherEquipment.setLeggings(equipment.leggings());
        otherEquipment.setMainHand(equipment.mainHand());
        otherEquipment.setOffHand(equipment.offHand());

        other.position().set(position());
        other.head().set(head());
        other.body().set(body());
        other.leftArm().set(leftArm());
        other.rightArm().set(rightArm());
        other.leftLeg().set(leftLeg());
        other.rightLeg().set(rightLeg());

        other.update();
    }

    public void update() {
        var entity = getEntity();
        if(entity == null) return;
        position.update(entity);
        equipment.update(entity);
        attributes.update(entity);

        headPose.update(entity);
        bodyPose.update(entity);
        leftArmPose.update(entity);
        rightArmPose.update(entity);
        leftLegPose.update(entity);
        rightLegPose.update(entity);
    }

    public void parseToFile(Path path) throws IOException {
        if (path.getParent() != null) Files.createDirectories(path.getParent());
        Files.writeString(path, parsePose());
    }

    public void loadFromFile(Path path) throws IOException {
        var pose = Files.readString(path);
        unparsePose(pose);
    }

    public void unparsePose(String parsed) {
        var values = parsed.split(";");

        int i = 0;
        i = unparsePose(headPose, i, values);
        i = unparsePose(bodyPose, i, values);
        i = unparsePose(leftArmPose, i, values);
        i = unparsePose(rightArmPose, i, values);
        i = unparsePose(leftLegPose, i, values);
        i = unparsePose(rightLegPose, i, values);

        update();
    }

    private int unparsePose(StatuePose pose, int startIndex, String[] values) {
        double x = Double.parseDouble(values[startIndex]);
        double y = Double.parseDouble(values[startIndex + 1]);
        double z = Double.parseDouble(values[startIndex + 2]);
        pose.set(new StatuePose(this, x, y, z));
        return startIndex + 3;
    }

    public String parsePose() {
        return String.join("",
                parsePose(headPose),
                parsePose(bodyPose),
                parsePose(leftArmPose),
                parsePose(rightArmPose),
                parsePose(leftLegPose),
                parsePose(rightLegPose));
    }

    private String parsePose(StatuePose pose) {
        return pose.x() + ";" + pose.y() + ";" + pose.z() + ";";
    }
}
