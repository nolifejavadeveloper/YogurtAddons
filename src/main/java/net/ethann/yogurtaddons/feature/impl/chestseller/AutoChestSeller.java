package net.ethann.yogurtaddons.feature.impl.chestseller;

import net.ethann.yogurtaddons.macro.impl.potion.AutoSellerHUD;
import net.ethann.yogurtaddons.notification.Notification;
import net.ethann.yogurtaddons.notification.NotificationLevel;
import net.ethann.yogurtaddons.notification.NotificationService;
import net.ethann.yogurtaddons.notification.impl.desktop.DesktopNotificationService;
import net.ethann.yogurtaddons.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandHandler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
public class AutoChestSeller extends ManagedTask {
    private static DecimalFormat df = new DecimalFormat("#.##");
    private static final String AWKWARD_POTION_NAME = "Awkward Potion x1";
    private static final String JUMP_1_POTION_NAME = "Jump Boost I Potion x1";
    private static final String JUMP_4_POTION_NAME = "Jump Boost IV Potion x1";
    private static final int TARGET_POTION_SELL_PRICE = 22833;

    private static final int REPORT_INTERVAL_MINUTES = 5;

    private final Scheduler scheduler;
    private final Scheduler reportScheduler;
    private final AutoSellerHUD hud;
    private SellingStage stage;

    private Instant start = null;

    private int sessionTotalMoney = 0;
    private int sessionTotalPotion = 0;
    private int sessionTargetPotionCount = 0;
    private int sessionAwkwardCount = 0;
    private int sessionJump1Count = 0;
    private int sessionUnknownCount = 0;

    private boolean isInRetry = false;

    private final ArrayList<Pair<Integer, Integer>> order = new ArrayList<>(Arrays.asList(
            p(9, 54),
            p(18, 63),
            p(27, 72),
            p(0, 81),
            p(1, 82),
            p(28, 73),
            p(19, 64),
            p(10, 55),
            p(11, 56),
            p(20, 65),
            p(29, 74),
            p(2, 83),
            p(3, 84),
            p(30, 75),
            p(21, 66),
            p(12, 57),
            p(13, 58),
            p(22, 67),
            p(32, 76),
            p(4, 85),
            p(5, 86),
            p(33, 77),
            p(23, 68),
            p(14, 59),
            p(15, 60),
            p(24, 69),
            p(33, 78),
            p(6, 87),
            p(7, 88),
            p(34, 79),
            p(25, 70),
            p(16, 61),
            p(17, 62),
            p(26, 71),
            p(35, 80)
    ));

    private int index;

    private Pair<Integer, Integer> p(int a, int b) {
        return new Pair<>(a, b);
    }

    public AutoChestSeller() {
        this.scheduler = new Scheduler();
        this.reportScheduler = new Scheduler();
        this.hud = new AutoSellerHUD(scheduler);

        ClientCommandHandler.instance.registerCommand(new AutoChestSellerCommand(this));
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (reportScheduler.isOver()) {
            reportProgress();
            reportScheduler.schedule(REPORT_INTERVAL_MINUTES * 60 * 1000);
        }
        if (!scheduler.isOver()) return;
        switch (stage) {
            case OPEN_CHEST:
                ChatUtil.log("opening chest...");

                ControlUtil.rightClick();

                scheduler.schedule(DelayUtil.getDelay(400, 300));
                stage = SellingStage.SELECT_ITEM;
                break;
            case SELECT_ITEM: {
                if (!InventoryUtil.getCurrentContainerName().equals("Large Chest")) {
                    if (isInRetry) {
                        ChatUtil.warn("already in retry! disabling");
                        disable();
                    }
                    ChatUtil.warn("container name does not match \"Large Chest\"");
                    scheduler.schedule(1500);
                    ChatUtil.log("starting retry");
                    isInRetry = true;
                    return;
                }

                isInRetry = false;

                EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
                Container chest = p.openContainer;

                if (!InventoryUtil.isContainerFull(chest)) {
                    ChatUtil.log("close tick continuing...");
                    scheduler.schedule(2000);
                    return;
                }

                ChatUtil.log("selecting first item...");

                Slot s = chest.getSlot(49);

                if (!s.getHasStack() || !shouldSell(s.getStack())) {
                   ChatUtil.warn("invalid slot for first click: " + s.getSlotIndex());
                   disable();
                   return;
                }

                ChatUtil.log("valid item for first click: " + s.getSlotIndex() + s.getStack().getDisplayName());
                InventoryUtil.leftClickItem(s.getSlotIndex());

                scheduler.schedule(DelayUtil.getDelay(400, 250));
                stage = SellingStage.FIRST_SHIFT_CLICK;
                break;
            }
            case FIRST_SHIFT_CLICK: {
                if (!InventoryUtil.getCurrentContainerName().equals("Large Chest")) {
                    if (isInRetry) {
                        ChatUtil.warn("already in retry! disabling");
                        disable();
                    }
                    ChatUtil.warn("container name does not match \"Large Chest\"");
                    scheduler.schedule(1500);
                    ChatUtil.log("starting retry");
                    isInRetry = true;
                    return;
                }

                isInRetry = false;

                ChatUtil.log("performing first shift click...");

                EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
                Container chest = p.openContainer;

                Slot s = chest.getSlot(50);

                if (!s.getHasStack() || !shouldSell(s.getStack())) {
                    ChatUtil.warn("invalid slot for second click: " + s.getSlotIndex());
                    disable();
                    return;
                }

                ChatUtil.log("valid item for second click: " + s.getSlotIndex() + s.getStack().getDisplayName());
                InventoryUtil.leftClickItem(s.getSlotIndex());

                scheduler.schedule(DelayUtil.getDelay(100, 100));
                stage = SellingStage.ALL_TAKE;
                break;
            }
            case ALL_TAKE: {
                if (!InventoryUtil.getCurrentContainerName().equals("Large Chest")) {
                    if (isInRetry) {
                        ChatUtil.warn("already in retry! disabling");
                        disable();
                    }
                    ChatUtil.warn("container name does not match \"Large Chest\"");
                    scheduler.schedule(1500);
                    ChatUtil.log("starting retry");
                    isInRetry = true;
                    return;
                }

                isInRetry = false;

                EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
                Container chest = p.openContainer;

                ChatUtil.log("performing second shift click...");

                for (int i = 0; i < chest.getInventory().size() - 36; i++) {
                    Slot s = chest.getSlot(i);
                    if (s.getHasStack() && shouldSell(s.getStack())) {
                        InventoryUtil.leftShiftClickItem(s.getSlotIndex());
                    }
                }

                scheduler.schedule(DelayUtil.getDelay(300, 200));
                stage = SellingStage.CLOSE_CHEST;
                break;
            }
            case CLOSE_CHEST: {
                ChatUtil.log("done taking out, closing chest...");

                InventoryUtil.closeInventory();

                stage = SellingStage.OPEN_TRADE_MENU;
                scheduler.schedule(DelayUtil.getDelay(1200, 300));
                break;
            }
            case OPEN_TRADE_MENU: {
                ChatUtil.sendCommand("trades");

                scheduler.schedule(DelayUtil.getDelay(900, 400));
                stage = SellingStage.SELL;
                break;
            }
            case SELL: {
                if (order.isEmpty()) {
                    scheduler.schedule(DelayUtil.getDelay(400, 300));
                    stage = SellingStage.CLOSE_TRADES;
                    return;
                }
                if (!InventoryUtil.getCurrentContainerName().equals("Trades")) {
                    ChatUtil.warn("container name does not match \"Trades\"");
                    if (isInRetry) {
                        ChatUtil.warn("retry used, disabling...");
                        disable();
                        return;
                    }

                    isInRetry = true;

                    scheduler.schedule(2000);
                    return;
                }

                isInRetry = false;
                EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
                InventoryPlayer inv = p.inventory;
                ItemStack item;

                Pair<Integer,Integer> slot;
                while (true) {
                    if (index >= order.size()) {
                        ChatUtil.log("unable to find a slot, ending...");
                        scheduler.schedule(DelayUtil.getDelay(400, 300));
                        stage = SellingStage.CLOSE_TRADES;
                        return;
                    }
                    slot = order.get(index);
                    index++;
                    item = inv.getStackInSlot(slot.a);
                    if (item == null) {
                        ChatUtil.log("skipping slot " + slot.a + " since it is empty");
                    }else if (!shouldSell(item)) {
                        ChatUtil.log("skipping slot " + slot.a + " since it is not the target item");
                    }else {
                        break;
                    }

                }

                String displayName = StringUtils.stripControlCodes(item.getDisplayName());

                if (displayName.equals(AWKWARD_POTION_NAME)) {
                    sessionAwkwardCount++;
                }else if (displayName.equals(JUMP_1_POTION_NAME)) {
                    sessionJump1Count++;
                }else if (displayName.equals(JUMP_4_POTION_NAME)){
                    sessionTargetPotionCount++;
                    addMoney();
                }else {
                    sessionUnknownCount++;
                    ChatUtil.warn("unknown item: " + displayName);
                    NotificationService.getInstance().send(Notification.create().withLevel(NotificationLevel.WARNING).withTitle("Chest Seller Issue Detected").withDetails("Chest Seller has detected an unknown item in inventory and was about to sell it. &nItem name: **" + displayName + "**&n&n*This is not a fatal error and therefore the script has not been stopped.*"), NotificationService.DISCORD);
                }

                sessionTotalPotion++;

                InventoryUtil.middleClickItem(slot.b);

                ChatUtil.log("clicked " + slot.b);

                scheduler.schedule(DelayUtil.getDelay(300, 100));
                break;
            }
            case CLOSE_TRADES: {
                InventoryUtil.closeInventory();

                stage = SellingStage.IDLE;
                ChatUtil.log("all done, closing trade menu...");

                scheduler.schedule(DelayUtil.getDelay(300, 200));
                ChatUtil.log("starting new cycle...");

                reset();
            }

        }
    }

    private void addMoney() {
        hud.addMoney(TARGET_POTION_SELL_PRICE);
        sessionTotalMoney += TARGET_POTION_SELL_PRICE;
    }

    private void reportProgress() {
        int badCount = sessionUnknownCount + sessionAwkwardCount + sessionJump1Count;
        ChatUtil.log("sending progress...");
        Instant now = Instant.now();
        Duration elapsed = Duration.between(start, now);
        NotificationService.getInstance().send(Notification.create().withLevel(NotificationLevel.INFO).withTitle("Chest Seller Progress Report").withDetails(
                String.format(
                        "Session running for: **%d minutes**&n" +
                        "Money earned (this is NOT profit): **%sM**&n" +
                        "Total potion sold: **%d**&n" +
                        "Total target potion sold: **%d**&n" +
                        "Total awkward potion sold: **%d**&n" +
                        "Total jump 1 potion sold: **%d**&n" +
                        "Total unknown item (potion) sold: **%d**&n" +
                        "Total bad potions (includes unknown, awkward, and jump 1): **%d**&n" +
                        "Potion success rate: **%s**",

                        elapsed.toMinutes(),
                        df.format(sessionTotalMoney / 1000000.0),
                        sessionTotalPotion,
                        sessionTargetPotionCount,
                        sessionAwkwardCount,
                        sessionJump1Count,
                        sessionUnknownCount,
                        badCount,
                        badCount == 0 ? "NaN" : df.format(100 - (badCount / ((double) sessionTotalPotion) * 100)) + "%"
                        )
        ), NotificationService.DISCORD);
    }

    private void reset() {
        index = 0;
        stage = SellingStage.OPEN_CHEST;
    }

    private void resetStatsTracker() {
        sessionTotalMoney = 0;
        sessionTotalPotion = 0;
        sessionTargetPotionCount = 0;
        sessionAwkwardCount = 0;
        sessionJump1Count = 0;
        sessionUnknownCount = 0;
    }

    private boolean shouldSell(ItemStack itemStack) {
        return itemStack.getItem() == Items.potionitem;
    }

    protected void onEnable() {
        resetStatsTracker();
        start = Instant.now();
        isInRetry = false;
        hud.resetMoney();

        reportScheduler.schedule(1000);

        index = 0;
        stage = SellingStage.OPEN_CHEST;

        scheduler.schedule(1000);
        MinecraftForge.EVENT_BUS.register(this);

        hud.enable();
    }

    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);

        hud.disable();
    }

    @Override
    protected String getName() {
        return "Chest Seller";
    }

    private enum SellingStage {
        IDLE,
        OPEN_CHEST,
        SELECT_ITEM,
        FIRST_SHIFT_CLICK,
        ALL_TAKE,
        CLOSE_CHEST,
        OPEN_TRADE_MENU,
        SELL,
        CLOSE_TRADES,
    }
}
