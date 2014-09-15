package org.granitemc.granite.entity;

/*****************************************************************************************
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ****************************************************************************************/

import org.granitemc.granite.api.entity.Entity;
import org.granitemc.granite.reflect.Composite;

import java.util.UUID;

public abstract class GraniteEntity extends Composite implements Entity {

    public GraniteEntity(Object parent, boolean proxy, Object... args) {
        super(parent, proxy, args);
    }

    @Override
    public int getEntityId() {
        //Obf: F
        return (Integer) invoke("getEntityID");
    }

    @Override
    public void setEntityId(int Id) {
        //Obf: d
        invoke("setEntityId", Id);
    }

    //TODO: this is new in 1.8
    @Override
    public void G() {
        //Obf: G
        invoke("G");
    }

    /*public Entity(aqu var1) {
        //Obf: wv
        invoke("Entity", var1);
    }*/

    @Override
    public void entityInit() {
        //Obf: h
        invoke("entityInit");
    }

    /*public xv getDataWatcher() {
        //Obf: H
        return (xv) invoke("getDataWatcher");
    }*/

    @Override
    public void setDead() {
        //Obf: J
        invoke("setDead");
    }

    @Override
    public void setSize(float width, float height) {
        //Obf: a
        invoke("setSize(float;float)", width, height);
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        //Obf: b
        invoke("setRotation(float;float)", yaw, pitch);
    }

    @Override
    public void setPosition(double x, double y, double z) {
        //Obf: b
        invoke("setPosition(double;double;double)", x, y, z);
    }

    @Override
    public void onUpdate() {
        //Obf: s_
        invoke("onUpdate");
    }

    @Override
    public void onEntityUpdate() {
        //Obf: K
        invoke("onEntityUpdate");
    }

    @Override
    public int getMaxInPortalTime() {
        //Obf: L
        return (Integer) invoke("getMaxInPortalTime");
    }

    @Override
    public void setOnFireFromLava() {
        //Obf: M
        invoke("setOnFireFromLava");
    }

    @Override
    public void setFire(int seconds) {
        //Obf: e
        invoke("setFire(int)", seconds);
    }

    @Override
    public void extinguish() {
        //Obf: N
        invoke("extinguish");
    }

    @Override
    public void kill() {
        //Obf: O
        invoke("kill");
    }

    @Override
    public boolean isOffsetPositionInLiquid(double x, double y, double z) {
        //Obf: c
        return (boolean) invoke("isOffsetPositionInLiquid", x, y, z);
    }

    //TODO: What does this do?
    /*private boolean b(brt var1) {
        //Obf: b
        return (boolean) invoke("b", var1);
    }*/

    @Override
    public void moveEntity(double x, double y, double z) {
        //Obf: d
        invoke("moveEntity(double;double;double)", x, y, z);
    }

    //TODO: What does this do?
    @Override
    public void m() {
        //Obf: m
        invoke("m");
    }

    //TODO: What does this do?
    @Override
    public void Q() {
        //Obf: Q
        invoke("Q");
    }

    //TODO: What does this do?
    /*protected void a(dt var1, atr var2) {
        //Obf: a
        invoke("a", var1, var2);
    }*/

    //TODO: Work out what var1 and var2 do (volume/pitch, maybe inverse?)
    @Override
    public void playSound(String soundName, float var1, float var2) {
        //Obf: a
        invoke("playSound(String;float;float)", soundName, var1, var2);
    }

    @Override
    public boolean canTriggerWalking() {
        //Obf: r_
        return (boolean) invoke("canTriggerWalking");
    }

    //TODO: ???
    /*public void updateFallState(double distanceFallenThisTick, boolean onGround, atr var4, dt var5) {
        //Obf: a
        invoke("updateFallState", distanceFallenThisTick, onGround, var4, var5);
    }*/

    //TODO: API-fy this (brt = AxisAlignedBB?)
    /*public brt getBoundingBox() {
        //Obf: S
        return (brt) invoke("getBoundingBox");
    }*/

    @Override
    public void dealFireDamage(int amountDamage) {
        //Obf: f
        invoke("dealFireDamage(int)", amountDamage);
    }

    @Override
    public boolean isImmuneToFire() {
        //Obf: K
        return (boolean) invoke("isImmuneToFire");
    }

    @Override
    public void fall(float var1, float var2) {
        //Obf: e
        invoke("fall", var1, var2);
    }

    @Override
    public boolean isWet() {
        //Obf: U
        return (boolean) invoke("isWet");
    }

    @Override
    public boolean isInWater() {
        //Obf: V
        return (boolean) invoke("isInWater");
    }

    @Override
    public boolean handleWaterMovement() {
        //Obf: W
        return (boolean) invoke("handleWaterMovement");
    }

    //TODO: What does this do?
    @Override
    public void X() {
        //Obf: X
        invoke("X");
    }

    //TODO: What does this do?
    @Override
    public void Y() {
        //Obj: Y
        invoke("Y");
    }

    @Override
    public String getSplashSound() {
        //Obf: aa
        return (String) invoke("getSplashSound");
    }

    //TODO: Work out what var1 (class bof) is
    /*public boolean isInsideOfMaterial(bof var1) {
        //Obf: a
        return (boolean) invoke("isInsideOfMaterial");
    }*/

    @Override
    public boolean handleLavaMovement() {
        //Obf: ab
        return (boolean) invoke("handleLavaMovement");
    }

    //TODO: Work out what var1,2 and 3 do?
    @Override
    public void moveFlying(float var1, float var2, float var3) {
        //Obf: a
        invoke("moveFlying", var1, var2, var3);
    }

    //TODO: Work out what var1 does
    @Override
    public float getBrightness(float var1) {
        //Obf: c
        return (float) invoke("getBrightness", var1);
    }

    //TODO: Work out what class aqu is
    /*public void setWorld(aqu var1) {
        //Obf: a
        invoke("setWorld", var1);
    }*/

    //TODO: Work out what class dt is
    /*public void a(dt var1, float var2, float var3) {
        //Obf: a
        invoke("a", var1, var2, var3);
    }*/

    @Override
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
        //Obf: b
        invoke("setLocationAndAngles(double;double;double;float;float)", x, y, z, yaw, pitch);
    }

    @Override
    public float getDistanceToEntity(GraniteEntity entity) {
        //Obf: g
        return (float) invoke("getDistanceToEntity", entity);
    }

    @Override
    public double getDistanceSq(double x, double y, double z) {
        //Obf: e
        return (double) invoke("getDistanceSq", x, y, z);
    }

    //TODO: Find out what class dt is
    /*public double b(dt var1) {
        //Obf: b
        return (double) invoke("b", var1);
    }*/

    //TODO: Find out what class dt is
    /*public double c(dt var1) {
        //Obf: c
        return (double) invoke("b", var1);
    }*/

    @Override
    public double getDistance(double x, double y, double z) {
        //Obf: f
        return (double) invoke("getDistance", x, y, z);
    }

    @Override
    public double getDistanceSqToEntity(GraniteEntity entity) {
        //Obf: h
        return (double) invoke("getDistanceSqToEntity", entity);
    }

    //TODO: Something is up with this override?
    //@Override
    public void onCollideWithPlayer(GraniteEntity entityPlayer) {
        //Obf: d
        invoke("onCollideWithPlayer", entityPlayer);
    }

    @Override
    public void applyEntityCollision(GraniteEntity entity) {
        //Obf: i
        invoke("applyEntityCollision", entity);
    }

    @Override
    public void addVelocity(double x, double y, double z) {
        //Obf: g
        invoke("addVelocity", x, y, z);
    }

    @Override
    public void setBeenAttacked() {
        //Obf: ac
        invoke("setBeenAttacked");
    }

    //TODO: Work out what class ro is and what var2 does
    /*public void attackEntityFrom(ro var1, float var2) {
        //Obf: a
        invoke("attackEntityFrom", var1, var2);
    }*/

    //TODO: Work out what class brw is and what var1 does
    /*public brw d(float var1) {
        //Obf: d
        return (brw) invoke("d", var1);
    }*/

    @Override
    public boolean canBeCollidedWith() {
        //Obf: ad
        return (boolean) invoke("canBeCollidedWith");
    }

    @Override
    public boolean canBePushed() {
        //Obf: ae
        return (boolean) invoke("canBePushed");
    }

    //TODO: Currently does nothing
    @Override
    public void addToPlayerScore(GraniteEntity entity, int amount) {
        //Obf: b
        invoke("addToPlayerScore", entity, amount);
    }

    @Override
    public String getEntityString() {
        //Obf: ag
        return (String) invoke("getEntityString");
    }

    //TODO: Currently not used
    @Override
    public void onChunkLoad() {
        //Obf: ah
        invoke("onChunkLoad");
    }

    //TODO: find out what adw and alq class is
    //TODO: find a good name for functions
    //TODO: what do the vars do?
    /*public adw s(alq var1, int var2) {
        //Obf: s
        return (adw) invoke("s", var1, var2);
    }*/

    //TODO: find out what adw and alq class is
    //TODO: find a good name for functions
    //TODO: what do the vars do?
    /*public adw a(alq var1, int var2, float var3) {
        //Obf: a
        return (adw) invoke("a", var1, var2, var3);
    }*/

    //TODO: find out what adw and amj class is
    //TODO: what do the vars do?
    /*public adw entityDropItem(amj var1, float var2) {
        //Obf: a
        return (adw) invoke("entityDropItem", var1, var2);
    }*/

    @Override
    public boolean isEntityAlive() {
        //Obf: ai
        return (boolean) invoke("isEntityAlive");
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        //Obf: aj
        return (boolean) invoke("isEntityInsideOpaqueBlock");
    }

    //TODO: find out what class ahd is
    /*public boolean interactFirst(ahd var1) {
        //Obf: e
        return (boolean) invoke("interactFirst", var1);
    }*/

    //TODO: work out what class brt is
    /*public brt getCollisionBox() {
        //Obf: j
        return (brt) invoke("getCollisionBox");
    }*/

    @Override
    public void updateRidden() {
        //Obf: ak
        invoke("updateRidden");
    }

    @Override
    public void updateRiderPosition() {
        //Obf: al
        invoke("updateRiderPosition");
    }

    @Override
    public double getYOffset() {
        //Obf: am
        return (double) invoke("getYOffset");
    }

    @Override
    public double getMountedYOffset() {
        //Obf: an
        return (double) invoke("getMountedYOffset");
    }

    @Override
    public void mountEntity(GraniteEntity entity) {
        //Obf: a
        invoke("mountEntity(n.m.entity.Entity)", entity);
    }

    //TODO: wat?
    /*public float getCollisionBorderSize() {
        //Obf: ao
        return (float) invoke("getCollisionBorderSize");
    }*/

    //TODO: work out what class brt is
    /*public brw getLookVector() {
        //Obf: ap
        return (brw) invoke("getLookVector");
    }*/

    @Override
    public boolean isEating() {
        //Obf: aq
        return (boolean) invoke("isEating");
    }

    @Override
    public void setEating(boolean var1) {
        //Obf: f
        invoke("setEating(boolean)", var1);
    }

    //TODO: work out what class amj is
    /*public amj[] getInventory() {
        //Obf: at
        return (amj[]) invoke("getInventory");
    }*/

    //TODO: work out what class amj is
    /*public void setCurrentItemOrArmor(int var1, amj var2) {
        //Obf: c
        invoke("setCurrentItemOrArmor", var1, var2);
    }*/

    @Override
    public int getPortalCooldown() {
        //Obf: ar
        return (Integer) invoke("getPortalCooldown");
    }

    @Override
    public boolean isBurning() {
        //Obf: au
        return (boolean) invoke("isBurning");
    }

    @Override
    public boolean isRiding() {
        //Obf: av
        return (boolean) invoke("isRiding");
    }

    @Override
    public boolean isSneaking() {
        //Obf: aw
        return (boolean) invoke("isSneaking");
    }

    @Override
    public void setSneaking(boolean var1) {
        //Obf: c
        invoke("setSneaking(boolean)", var1);
    }

    @Override
    public boolean isSprinting() {
        //Obf: ax
        return (boolean) invoke("isSprinting");
    }

    @Override
    public void setSprinting(boolean var1) {
        //Obf: d
        invoke("setSprinting(boolean)", var1);
    }

    @Override
    public boolean isInvisible() {
        //Obf: ay
        return (boolean) invoke("isInvisible");
    }

    @Override
    public void setInvisible(boolean var1) {
        //Obf: e
        invoke("setInvisible(boolean)", var1);
    }

    @Override
    public boolean getFlag(int flag) {
        //Obf: g
        return (boolean) invoke("getFlag", flag);
    }

    @Override
    public void setFlag(int flag, boolean var2) {
        //Obf: b
        invoke("setFlag", flag, var2);
    }

    @Override
    public int getAir() {
        //Obf: aA
        return (Integer) invoke("getAir");
    }

    @Override
    public void setAir(int var1) {
        //Obf: h
        invoke("setAir(int)", var1);
    }

    //TODO: Find out what class ads is
    /*public void onStruckByLightning(ads var1) {
        //Obf: a
        invoke("onStruckByLightning", var1);
    }*/

    //TODO: Find out what class xm is
    /*public void onKillEntity(xm var1) {
        //Obf: a
        invoke("onKillEntity", var1);
    }*/

    //TODO: Work out a suitable name and what the vars do
    @Override
    public boolean j(double var1, double var2, double var3) {
        //Obf: j
        return (boolean) invoke("j", var1, var2, var3);
    }

    @Override
    public void setInWeb() {
        //Obf: aB
        invoke("setInWeb");
    }

    @Override
    public String getCommandSenderName() {
        //Obf: d_
        return (String) invoke("getCommandSenderName");
    }

    @Override
    public GraniteEntity[] getParts() {
        //Obf: aC
        return (GraniteEntity[]) invoke("getParts");
    }

    @Override
    public boolean isEntityEqual(GraniteEntity entity) {
        //Obf: k
        return (boolean) invoke("isEntityEqual(n.m.entity.Entity)", entity);
    }

    @Override
    public float getRotationYawHead() {
        //Obf: aD
        return (float) invoke("getRotationYawHead");
    }

    //TODO: Find a suitable name (currently not used)
    @Override
    public void f(float var1) {
        //Obf: f
        invoke("f");
    }

    @Override
    public boolean canAttackWithItem() {
        //Obf: aE
        return (boolean) invoke("canAttackWithItem");
    }

    @Override
    public boolean hitByEntity(GraniteEntity entity) {
        //Obf: l
        return (boolean) invoke("hitByEntity", entity);
    }

    @Override
    public void copyDataFrom(GraniteEntity entity) {
        //Obf: n
        invoke("copyDataFrom", entity);
    }

    @Override
    public void travelToDimension(int dimension) {
        //Obf: c
        invoke("travelToDimension(int)", dimension);
    }

    //TODO: Find suitable name and work out what the vars do and their classes
    /*public float a(aqo var1, aqu var2, dt var3, bec var4) {
        //Obf: a
        return (float) invoke("a", aqo var1, aqu var2, dt var3, bec var4);
    }*/

    //TODO: Find suitable name and work out what the vars do and their classes
    /*public boolean a(aqo var1, aqu var2, dt var3, bec var4, float var5) {
        //Obf: a
        return (boolean) invoke("a", aqo var1, aqu var2, dt var3, bec var4, float var5);
    }*/

    @Override
    public int getMaxSafePointTries() {
        //Obf: aF
        return (Integer) invoke("getMaxSafePointTries");
    }

    @Override
    public int getTeleportDirection() {
        //Obf: aG
        return (Integer) invoke("getTeleportDirection");
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        //Obf: aH
        return (boolean) invoke("doesEntityNotTriggerPressurePlate");
    }

    //TODO: find out what class j is
    /*public void addEntityCrashInfo(j var1) {
        //Obf: a
        invoke("addEntityCrashInfo", var1);
    }*/

    @Override
    public UUID getUniqueID() {
        //Obf: aJ
        return (UUID) invoke("getUniqueID");
    }

    @Override
    public boolean isPushedByWater() {
        //Obf: aK
        return (boolean) invoke("isPushedByWater");
    }

    //TODO: Find Suitable name and get ho class
    /*public ho e_() {
        //Obf: e_
        return (ho) invoke("e_");
    }*/

    //TODO: These are new in 1.8?

    /**
     * public void a(String var1) {
     * //Obf: a
     * invoke("a", var1);
     * }
     * <p/>
     * public String aL() {
     * //Obf: aL
     * return (String) invoke("aL")
     * }
     * <p/>
     * public boolean k_() {
     * //Obf: k_
     * return (boolean) invoke("k_");
     * }
     * <p/>
     * public void g(boolean var1) {
     * //Obf: g
     * invoke("g", var1);
     * }
     * <p/>
     * public boolean aM() {
     * //Obf: aM
     * return (boolean) invoke("aM");
     * }
     * <p/>
     * public void a(double var1, double var3, double var5) {
     * //Obf: a
     * invoke("a", var1, var3, var5);
     * }
     */

    //TODO: Find Suitable name (not currently used)
    @Override
    public void i(int var1) {
        //Obf: i
        invoke("i", var1);
    }

    //TODO: These are new in 1.8?

    /**
     * public ej aO() {
     * //Obf: aO
     * return (ej) invoke("aO");
     * }
     *
     * protected hr aP() {
     * //Obf: aP
     * return (hr) invoke("aO");
     * }
     *
     * public boolean a(qw var1) {
     * //Obf: a
     * return (boolean) invoke("a");
     * }
     *
     * public brt aQ() {
     * //Obf: aQ
     * return (brt) invoke("aQ");
     * }
     *
     * public void a(brt var1) {
     * //Obf: a
     * invoke("a", var1);
     * }
     *
     * public float aR() {
     * //Obf: aR
     * return (float) invoke("aR");
     * }
     *
     * public boolean aS() {
     * //Obf: aS
     * return (boolean) invoke("aS");
     * }
     *
     * public void h(boolean var1) {
     * //Obf: h
     * invoke("h", var1);
     * }
     *
     * public boolean d(int var1, amj var2) {
     * //Obf: d
     * return (boolean) invoke("d", var1, var2);
     * }
     *
     * //TODO: ? (not currently used)
     * public void a(ho var1) {
     * //Obf: a
     * invoke("a", var1);
     * }
     *
     * public boolean a(int var1, String var2) {
     * //Obf: a
     * return (boolean) invoke("a", var1, var2);
     * }
     *
     * public dt c() {
     * //Obf: c
     * return (dt) invoke("c");
     * }
     *
     * public brw d() {
     * //Obf: d
     * return (brw) invoke("d");
     * }
     *
     * public aqu e() {
     * //Obf: e
     * return (aqu) invoke("e")
     * }
     *
     * public Entity f() {
     * //Obf: f
     * return (Entity) invoke("f");
     * }
     *
     * public boolean t_() {
     * //Obf: t_
     * return (boolean) invoke("t_");
     * }
     *
     * public void a(ag var1, int var2) {
     * //Obf: a
     * invoke("a", var1, var2);
     * }
     *
     * public af aT() {
     * //Obf: aT
     * return (af) invoke("aT");
     * }
     *
     * public void o(Entity var1) {
     * //Obf: o
     * invoke("o", var1);
     * }
     *
     * public fn aU() {
     * //Obf: aU
     * return (fn) invoke("aU");
     * }
     *
     * public boolean a(ahd var1, brw var2) {
     * //Obf: a
     * return (boolean) invoke("a", var1, var2);
     * }
     *
     * public boolean aV() {
     * //Obf: aV
     * return (boolean) invoke("aV");
     * }
     *
     * protected void a(xm var1, Entity var2) {
     * //Obf: a
     * invoke("a", var1, var2);
     * }
     */
}
