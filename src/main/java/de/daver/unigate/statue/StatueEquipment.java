package de.daver.unigate.statue;

import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

public class StatueEquipment {

    private final Statue statue;

    private ItemStack helmet;
    private ItemStack chestPlate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack mainHand;
    private ItemStack offHand;

    public StatueEquipment(Statue statue) {
        this.statue = statue;
        load(statue.getEntity());
    }

    void load(ArmorStand stand) {
        var equip = stand.getEquipment();

        this.helmet = equip.getHelmet();
        this.chestPlate = equip.getChestplate();
        this.leggings = equip.getLeggings();
        this.boots = equip.getBoots();
        this.mainHand = equip.getItemInMainHand();
        this.offHand = equip.getItemInOffHand();
    }



    public ItemStack mainHand() {
        return this.mainHand;
    }

    public ItemStack offHand() {
        return this.offHand;
    }


    public ItemStack helmet() {
        return this.helmet;
    }

    public ItemStack chestPlate() {
        return this.chestPlate;
    }

    public ItemStack leggings() {
        return this.leggings;
    }

    public ItemStack boots() {
        return this.boots;
    }

    public void setMainHand(ItemStack item) {
        this.mainHand = item;
    }

    public void setOffHand(ItemStack item) {
        this.offHand = item;
    }

    public void setHelmet(ItemStack item) {
        this.helmet = item;
    }

    public void setChestPlate(ItemStack item) {
        this.chestPlate = item;
    }

    public void setLeggings(ItemStack item) {
        this.leggings = item;
    }

    public void setBoots(ItemStack item) {
        this.boots = item;
    }

    public void update() {
        ArmorStand stand = statue.getEntity();
        if (stand == null) return;
        update(stand);
    }

    void update(ArmorStand stand) {
        var entityEquip = stand.getEquipment();
        entityEquip.setHelmet(this.helmet, true);
        entityEquip.setChestplate(this.chestPlate, true);
        entityEquip.setLeggings(this.leggings, true);
        entityEquip.setBoots(this.boots, true);
        entityEquip.setItemInMainHand(this.mainHand, true);
        entityEquip.setItemInOffHand(this.offHand, true);
    }
}
