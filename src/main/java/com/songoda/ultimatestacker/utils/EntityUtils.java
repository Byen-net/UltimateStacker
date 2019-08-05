package com.songoda.ultimatestacker.utils;

import com.songoda.ultimatestacker.UltimateStacker;
import com.songoda.ultimatestacker.entity.Check;
import com.songoda.ultimatestacker.entity.EntityStack;
import com.songoda.ultimatestacker.utils.settings.Setting;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;

import java.util.*;
import java.util.stream.Collectors;

public class EntityUtils {

    private List<String> checks = Setting.STACK_CHECKS.getStringList();
    private boolean stackFlyingDown = Setting.ONLY_STACK_FLYING_DOWN.getBoolean();
    private boolean keepFire = Setting.KEEP_FIRE.getBoolean();
    private boolean keepPotion = Setting.KEEP_POTION.getBoolean();
    private int searchRadius = Setting.SEARCH_RADIUS.getInt();

    private final Map<CachedChunk, Entity[]> cachedChunks = new HashMap<>();

    public void clearChunkCache() {
        this.cachedChunks.clear();
    }


    private Set<CachedChunk> getNearbyChunks(Location location, double radius) {
        World world = location.getWorld();
        Set<CachedChunk> chunks = new HashSet<>();
        if (world == null) return chunks;

        Chunk firstChunk = location.getChunk();
        chunks.add(new CachedChunk(firstChunk));
        int minX = (int) Math.floor(((location.getX() - radius) - 2.0D) / 16.0D);
        int maxX = (int) Math.floor(((location.getX() + radius) + 2.0D) / 16.0D);
        int minZ = (int) Math.floor(((location.getZ() - radius) - 2.0D) / 16.0D);
        int maxZ = (int) Math.floor(((location.getZ() + radius) + 2.0D) / 16.0D);

        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                if (firstChunk.getX() == x && firstChunk.getZ() == z) continue;
                chunks.add(new CachedChunk(world.getName(), x, z));
            }
        }
        return chunks;
    }

    private List<LivingEntity> getNearbyEntities(Location location, double radius) {
        List<LivingEntity> entities = new ArrayList<>();
        for (CachedChunk chunk : getNearbyChunks(location, radius)) {
            Entity[] entityArray;
            if (cachedChunks.containsKey(chunk)) {
                entityArray = cachedChunks.get(chunk);
            } else {
                entityArray = chunk.getChunk().getEntities();
                cachedChunks.put(chunk, entityArray);
            }
            for (Entity e : entityArray) {
                if (e.getWorld() != location.getWorld()
                        || !(e instanceof LivingEntity)
                        || location.distanceSquared(e.getLocation()) >= radius * radius) continue;
                entities.add((LivingEntity) e);
            }
        }
        return entities;
    }

    public LivingEntity newEntity(LivingEntity toClone) {
        LivingEntity newEntity = (LivingEntity) toClone.getWorld().spawnEntity(toClone.getLocation(), toClone.getType());
        newEntity.setVelocity(toClone.getVelocity());

        for (String checkStr : checks) {
            Check check = Check.valueOf(checkStr);
            switch (check) {
                case AGE: {
                    if (!(toClone instanceof Ageable) || ((Ageable) toClone).isAdult()) break;
                    ((Ageable) newEntity).setBaby();
                    break;
                }
                case NERFED: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_9)) break;
                    if (!toClone.hasAI()) newEntity.setAI(false);
                }
                case IS_TAMED: {
                    if (!(toClone instanceof Tameable)) break;
                    ((Tameable) newEntity).setTamed(((Tameable) toClone).isTamed());
                }
                case ANIMAL_OWNER: {
                    if (!(toClone instanceof Tameable)) break;
                    ((Tameable) newEntity).setOwner(((Tameable) toClone).getOwner());
                }
                case SKELETON_TYPE: {
                    if (!(toClone instanceof Skeleton)
                            || UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_12)) break;
                    ((Skeleton) newEntity).setSkeletonType(((Skeleton) toClone).getSkeletonType());
                    break;
                }
                case SHEEP_COLOR: {
                    if (!(toClone instanceof Sheep)) break;
                    ((Sheep) newEntity).setColor(((Sheep) toClone).getColor());
                    break;
                }
                case SHEEP_SHEERED: {
                    if (!(toClone instanceof Sheep)) break;
                    ((Sheep) newEntity).setSheared(((Sheep) toClone).isSheared());
                    break;
                }
                case LLAMA_COLOR: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_11)
                            || !(toClone instanceof Llama)) break;
                    ((Llama) newEntity).setColor(((Llama) toClone).getColor());
                    break;
                }
                case LLAMA_STRENGTH: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_11)
                            || !(toClone instanceof Llama)) break;
                    ((Llama) newEntity).setStrength(((Llama) toClone).getStrength());
                    break;
                }
                case VILLAGER_PROFESSION: {
                    if (!(toClone instanceof Villager)) break;
                    ((Villager) newEntity).setProfession(((Villager) toClone).getProfession());
                    break;
                }
                case SLIME_SIZE: {
                    if (!(toClone instanceof Slime)) break;
                    ((Slime) newEntity).setSize(((Slime) toClone).getSize());
                    break;
                }
                case HORSE_JUMP: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_11)
                            || !(toClone instanceof AbstractHorse)) break;
                    ((AbstractHorse) newEntity).setJumpStrength(((AbstractHorse) toClone).getJumpStrength());
                    break;
                }
                case HORSE_COLOR: {
                    if (!(toClone instanceof Horse)) break;
                    ((Horse) newEntity).setColor(((Horse) toClone).getColor());
                    break;
                }
                case HORSE_STYLE: {
                    if (!(toClone instanceof Horse)) break;
                    ((Horse) newEntity).setStyle(((Horse) toClone).getStyle());
                    break;
                }
                case ZOMBIE_BABY: {
                    if (!(toClone instanceof Zombie)) break;
                    ((Zombie) newEntity).setBaby(((Zombie) toClone).isBaby());
                    break;
                }
                case WOLF_COLLAR_COLOR: {
                    if (!(toClone instanceof Wolf)) break;
                    ((Wolf) newEntity).setCollarColor(((Wolf) toClone).getCollarColor());
                    break;
                }
                case OCELOT_TYPE: {
                    if (!(toClone instanceof Ocelot)
                            || UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_14)) break;
                    ((Ocelot) newEntity).setCatType(((Ocelot) toClone).getCatType());
                }
                case CAT_TYPE: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_14)
                            || !(toClone instanceof Cat)) break;
                    ((Cat) newEntity).setCatType(((Cat) toClone).getCatType());
                    break;
                }
                case RABBIT_TYPE: {
                    if (!(toClone instanceof Rabbit)) break;
                    ((Rabbit) newEntity).setRabbitType(((Rabbit) toClone).getRabbitType());
                    break;
                }
                case PARROT_TYPE: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_12)
                            || !(toClone instanceof Parrot)) break;
                    ((Parrot) newEntity).setVariant(((Parrot) toClone).getVariant());
                    break;
                }
                case PUFFERFISH_STATE: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_13)
                            || !(toClone instanceof PufferFish)) break;
                    ((PufferFish) newEntity).setPuffState(((PufferFish) toClone).getPuffState());
                    break;
                }
                case TROPICALFISH_PATTERN: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_13)
                            || !(toClone instanceof TropicalFish)) break;
                    ((TropicalFish) newEntity).setPattern(((TropicalFish) toClone).getPattern());
                    break;
                }
                case TROPICALFISH_PATTERN_COLOR: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_13)
                            || !(toClone instanceof TropicalFish)) break;
                    ((TropicalFish) newEntity).setPatternColor(((TropicalFish) toClone).getPatternColor());
                    break;
                }
                case TROPICALFISH_BODY_COLOR: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_13)
                            || !(toClone instanceof TropicalFish)) break;
                    ((TropicalFish) newEntity).setBodyColor(((TropicalFish) toClone).getBodyColor());
                    break;
                }
                case PHANTOM_SIZE: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_13)
                            || !(toClone instanceof Phantom)) break;
                    ((Phantom) newEntity).setSize(((Phantom) toClone).getSize());
                    break;
                }
            }
        }

        if (keepFire)
            newEntity.setFireTicks(toClone.getFireTicks());
        if (keepPotion)
            newEntity.addPotionEffects(toClone.getActivePotionEffects());

        return newEntity;
    }

    public List<LivingEntity> getSimilarEntitiesAroundEntity(LivingEntity initalEntity, Location location) {
        // Create a list of all entities around the initial entity of the same type.
        List<LivingEntity> entityList = getNearbyEntities(location, searchRadius)
                .stream().filter(entity -> entity.getType() == initalEntity.getType() && entity != initalEntity)
                .collect(Collectors.toCollection(LinkedList::new));

        if (stackFlyingDown && Methods.canFly(initalEntity))
            entityList.removeIf(entity -> entity.getLocation().getY() > initalEntity.getLocation().getY());

        for (String checkStr : checks) {
            Check check = Check.valueOf(checkStr);
            switch (check) {
                case SPAWN_REASON: {
                    if (initalEntity.hasMetadata("US_REASON"))
                        entityList.removeIf(entity -> entity.hasMetadata("US_REASON") && !entity.getMetadata("US_REASON").get(0).asString().equals("US_REASON"));
                }
                case AGE: {
                    if (!(initalEntity instanceof Ageable)) break;

                    if (((Ageable) initalEntity).isAdult()) {
                        entityList.removeIf(entity -> !((Ageable) entity).isAdult());
                    } else {
                        entityList.removeIf(entity -> ((Ageable) entity).isAdult());
                    }
                    break;
                }
                case NERFED: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_9)) break;
                    entityList.removeIf(entity -> entity.hasAI() != initalEntity.hasAI());
                }
                case IS_TAMED: {
                    if (!(initalEntity instanceof Tameable)) break;
                    entityList.removeIf(entity -> ((Tameable) entity).isTamed());
                }
                case ANIMAL_OWNER: {
                    if (!(initalEntity instanceof Tameable)) break;

                    Tameable tameable = ((Tameable) initalEntity);
                    entityList.removeIf(entity -> ((Tameable) entity).getOwner() != tameable.getOwner());
                }
                case PIG_SADDLE: {
                    if (!(initalEntity instanceof Pig)) break;
                    entityList.removeIf(entity -> ((Pig) entity).hasSaddle());
                    break;
                }
                case SKELETON_TYPE: {
                    if (!(initalEntity instanceof Skeleton)) break;

                    Skeleton skeleton = (Skeleton) initalEntity;
                    entityList.removeIf(entity -> ((Skeleton) entity).getSkeletonType() != skeleton.getSkeletonType());
                    break;
                }
                case SHEEP_COLOR: {
                    if (!(initalEntity instanceof Sheep)) break;

                    Sheep sheep = ((Sheep) initalEntity);
                    entityList.removeIf(entity -> ((Sheep) entity).getColor() != sheep.getColor());
                    break;
                }
                case SHEEP_SHEERED: {
                    if (!(initalEntity instanceof Sheep)) break;

                    Sheep sheep = ((Sheep) initalEntity);
                    if (sheep.isSheared()) {
                        entityList.removeIf(entity -> !((Sheep) entity).isSheared());
                    } else {
                        entityList.removeIf(entity -> ((Sheep) entity).isSheared());
                    }
                    break;
                }
                case LLAMA_COLOR: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_11)
                            || !(initalEntity instanceof Llama)) break;
                    Llama llama = ((Llama) initalEntity);
                    entityList.removeIf(entity -> ((Llama) entity).getColor() != llama.getColor());
                    break;
                }
                case LLAMA_STRENGTH: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_11)
                            || !(initalEntity instanceof Llama)) break;
                    Llama llama = ((Llama) initalEntity);
                    entityList.removeIf(entity -> ((Llama) entity).getStrength() != llama.getStrength());
                    break;
                }
                case VILLAGER_PROFESSION: {
                    if (!(initalEntity instanceof Villager)) break;
                    Villager villager = ((Villager) initalEntity);
                    entityList.removeIf(entity -> ((Villager) entity).getProfession() != villager.getProfession());
                    break;
                }
                case SLIME_SIZE: {
                    if (!(initalEntity instanceof Slime)) break;
                    Slime slime = ((Slime) initalEntity);
                    entityList.removeIf(entity -> ((Slime) entity).getSize() != slime.getSize());
                    break;
                }
                case HORSE_CARRYING_CHEST: {
                    if (UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_11)) {
                        if (!(initalEntity instanceof ChestedHorse)) break;
                        entityList.removeIf(entity -> ((ChestedHorse) entity).isCarryingChest());
                    } else {
                        if (!(initalEntity instanceof Horse)) break;
                        entityList.removeIf(entity -> ((Horse) entity).isCarryingChest());
                    }
                    break;
                }
                case HORSE_HAS_ARMOR: {
                    if (!(initalEntity instanceof Horse)) break;
                    entityList.removeIf(entity -> ((Horse) entity).getInventory().getArmor() != null);
                    break;
                }
                case HORSE_HAS_SADDLE: {
                    if (UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_13)
                            && initalEntity instanceof AbstractHorse) {
                        entityList.removeIf(entity -> ((AbstractHorse) entity).getInventory().getSaddle() != null);
                        break;
                    }
                    if (!(initalEntity instanceof Horse)) break;
                    entityList.removeIf(entity -> ((Horse) entity).getInventory().getSaddle() != null);
                    break;
                }
                case HORSE_JUMP: {
                    if (UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_11)) {
                        if (!(initalEntity instanceof AbstractHorse)) break;
                        AbstractHorse horse = ((AbstractHorse) initalEntity);
                        entityList.removeIf(entity -> ((AbstractHorse) entity).getJumpStrength() != horse.getJumpStrength());
                    } else {
                        if (!(initalEntity instanceof Horse)) break;
                        Horse horse = ((Horse) initalEntity);
                        entityList.removeIf(entity -> ((Horse) entity).getJumpStrength() != horse.getJumpStrength());

                    }
                    break;
                }
                case HORSE_COLOR: {
                    if (!(initalEntity instanceof Horse)) break;
                    Horse horse = ((Horse) initalEntity);
                    entityList.removeIf(entity -> ((Horse) entity).getColor() != horse.getColor());
                    break;
                }
                case HORSE_STYLE: {
                    if (!(initalEntity instanceof Horse)) break;
                    Horse horse = ((Horse) initalEntity);
                    entityList.removeIf(entity -> ((Horse) entity).getStyle() != horse.getStyle());
                    break;
                }
                case ZOMBIE_BABY: {
                    if (!(initalEntity instanceof Zombie)) break;
                    Zombie zombie = (Zombie) initalEntity;
                    entityList.removeIf(entity -> ((Zombie) entity).isBaby() != zombie.isBaby());
                    break;
                }
                case WOLF_COLLAR_COLOR: {
                    if (!(initalEntity instanceof Wolf)) break;
                    Wolf wolf = (Wolf) initalEntity;
                    entityList.removeIf(entity -> ((Wolf) entity).getCollarColor() != wolf.getCollarColor());
                    break;
                }
                case OCELOT_TYPE: {
                    if (!(initalEntity instanceof Ocelot)) break;
                    Ocelot ocelot = (Ocelot) initalEntity;
                    entityList.removeIf(entity -> ((Ocelot) entity).getCatType() != ocelot.getCatType());
                }
                case CAT_TYPE: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_14)
                            || !(initalEntity instanceof Cat)) break;
                    Cat cat = (Cat) initalEntity;
                    entityList.removeIf(entity -> ((Cat) entity).getCatType() != cat.getCatType());
                    break;
                }
                case RABBIT_TYPE: {
                    if (!(initalEntity instanceof Rabbit)) break;
                    Rabbit rabbit = (Rabbit) initalEntity;
                    entityList.removeIf(entity -> ((Rabbit) entity).getRabbitType() != rabbit.getRabbitType());
                    break;
                }
                case PARROT_TYPE: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_12)
                            || !(initalEntity instanceof Parrot)) break;
                    Parrot parrot = (Parrot) initalEntity;
                    entityList.removeIf(entity -> ((Parrot) entity).getVariant() != parrot.getVariant());
                    break;
                }
                case PUFFERFISH_STATE: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_13)
                            || !(initalEntity instanceof PufferFish)) break;
                    PufferFish pufferFish = (PufferFish) initalEntity;
                    entityList.removeIf(entity -> ((PufferFish) entity).getPuffState() != pufferFish.getPuffState());
                    break;
                }
                case TROPICALFISH_PATTERN: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_13)
                            || !(initalEntity instanceof TropicalFish)) break;
                    TropicalFish tropicalFish = (TropicalFish) initalEntity;
                    entityList.removeIf(entity -> ((TropicalFish) entity).getPattern() != tropicalFish.getPattern());
                    break;
                }
                case TROPICALFISH_PATTERN_COLOR: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_13)
                            || !(initalEntity instanceof TropicalFish)) break;
                    TropicalFish tropicalFish = (TropicalFish) initalEntity;
                    entityList.removeIf(entity -> ((TropicalFish) entity).getPatternColor() != tropicalFish.getPatternColor());
                    break;
                }
                case TROPICALFISH_BODY_COLOR: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_13)
                            || !(initalEntity instanceof TropicalFish)) break;
                    TropicalFish tropicalFish = (TropicalFish) initalEntity;
                    entityList.removeIf(entity -> ((TropicalFish) entity).getBodyColor() != tropicalFish.getBodyColor());
                    break;
                }
                case PHANTOM_SIZE: {
                    if (!UltimateStacker.getInstance().isServerVersionAtLeast(ServerVersion.V1_13)
                            || !(initalEntity instanceof Phantom)) break;
                    Phantom phantom = (Phantom) initalEntity;
                    entityList.removeIf(entity -> ((Phantom) entity).getSize() != phantom.getSize());
                    break;
                }
            }
        }

        if (initalEntity.hasMetadata("breedCooldown")) {
            entityList.removeIf(entity -> !entity.hasMetadata("breedCooldown"));
        }

        return entityList;
    }

    public void splitFromStack(LivingEntity entity) {
        UltimateStacker instance = UltimateStacker.getInstance();
        EntityStack stack = instance.getEntityStackManager().getStack(entity);

        if (stack.getAmount() <= 1) return;

        LivingEntity newEntity = newEntity(entity);

        int newAmount = stack.getAmount() - 1;
        if (newAmount != 1)
            instance.getEntityStackManager().addStack(new EntityStack(newEntity, newAmount));
        stack.setAmount(1);
        instance.getEntityStackManager().removeStack(entity);
        entity.setVelocity(Methods.getRandomVector());
    }
}
